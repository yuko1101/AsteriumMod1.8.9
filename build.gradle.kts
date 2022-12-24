import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.architectury.pack200.java.Pack200Adapter
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.security.MessageDigest

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.serialization") version "1.7.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("io.github.juuxel.loom-quiltflower") version "1.8.0"
    java
    idea
    signing
}

version = "0.2.0"
group = "io.github.yuko1101.asterium"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
}

quiltflower {
    quiltflowerVersion.set("1.9.0")
}

loom {
    silentMojangMappingsLicense()
    launchConfigs {
        getByName("client") {
            property("fml.coreMods.load", "io.github.yuko1101.asterium.tweaker.AsteriumLoadingPlugin")
            property("elementa.dev", "true")
            property("elementa.debug", "true")
            property("elementa.invalid_usage", "warn")
            property("asmhelper.verbose", "true")
            property("mixin.debug.verbose", "true")
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")
            property("legacy.debugClassLoading", "true")
            property("legacy.debugClassLoadingSave", "true")
            property("legacy.debugClassLoadingFiner", "true")
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "mixins.asterium.json")
        }
    }
    runConfigs {
        getByName("client") {
            isIdeConfigGenerated = true
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("mixins.asterium.json")
    }
    mixin {
        defaultRefmapName.set("mixins.asterium.refmap.json")
    }
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val shadeMod: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    shade("gg.essential:loader-launchwrapper:1.1.3")
    implementation("gg.essential:essential-1.8.9-forge:11092+gecb85a783") {
        exclude(module = "asm")
        exclude(module = "asm-commons")
        exclude(module = "asm-tree")
        exclude(module = "gson")
    }

    shadeMod("com.github.Skytils:AsmHelper:91ecc2bd9c") {
        exclude(module = "kotlin-reflect")
        exclude(module = "kotlin-stdlib-jdk8")
        exclude(module = "kotlin-stdlib-jdk7")
        exclude(module = "kotlin-stdlib")
        exclude(module = "kotlinx-coroutines-core")
    }

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("org.spongepowered:mixin:0.8.5")

}

sourceSets {
    main {
        output.setResourcesDir(file("${buildDir}/classes/kotlin/main"))
    }
}

kotlin {
    jvmToolchain(8)
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", "1.8.9")

        filesMatching("mcmod.info") {
            expand(mapOf("version" to project.version, "mcversion" to "1.8.9"))
        }
        dependsOn(compileJava)
    }
    named<Jar>("jar") {
        manifest {
            attributes(
                mapOf(
                    "ModSide" to "CLIENT",
                    "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
                    "TweakOrder" to "0",
                    "MixinConfigs" to "mixins.asterium.json",
                    "FMLCorePlugin" to "io.github.yuko1101.asterium.tweaker.AsteriumLoadingPlugin",
                    "FMLCorePluginContainsFMLMod" to true,
                    "ForceLoadAsMod" to true,
                    "ModType" to "FML",
                )
            )
        }
        dependsOn(shadowJar)
        enabled = false
    }
    named<RemapJarTask>("remapJar") {
        archiveBaseName.set("Asterium")
        input.set(shadowJar.get().archiveFile)
        doLast {
            MessageDigest.getInstance("SHA-256").digest(archiveFile.get().asFile.readBytes())
                .let { bytes ->
                    println("SHA-256: " + bytes.joinToString(separator = "") { "%02x".format(it) }.toUpperCase())
                }
        }
    }
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("Asterium")
        archiveClassifier.set("dev")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(shade, shadeMod)

        exclude(
            "**/LICENSE.md",
            "**/LICENSE.txt",
            "**/LICENSE",
            "**/NOTICE",
            "**/NOTICE.txt",
            "pack.mcmeta",
            "dummyThing",
            "**/module-info.class",
            "META-INF/proguard/**",
            "META-INF/maven/**",
            "META-INF/versions/**",
            "META-INF/com.android.tools/**",
            "fabric.mod.json"
        )
        mergeServiceFiles()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs =
                listOf(
                    /*"-opt-in=kotlin.RequiresOptIn", */
                    "-Xjvm-default=all",
                    //"-Xjdk-release=1.8",
                    "-Xbackend-threads=0",
                    /*"-Xuse-k2"*/
                )
            languageVersion = "1.7"
        }
        kotlinDaemonJvmArguments.set(
            listOf(
                "-Xmx2G",
                "-Dkotlin.enableCacheBuilding=true",
                "-Dkotlin.useParallelTasks=true",
                "-Dkotlin.enableFastIncremental=true",
                //"-Xbackend-threads=0"
            )
        )
    }
    register<Delete>("deleteClassloader") {
        delete(
            "${project.projectDir}/run/CLASSLOADER_TEMP",
            "${project.projectDir}/run/CLASSLOADER_TEMP1",
            "${project.projectDir}/run/CLASSLOADER_TEMP2",
            "${project.projectDir}/run/CLASSLOADER_TEMP3",
            "${project.projectDir}/run/CLASSLOADER_TEMP4",
            "${project.projectDir}/run/CLASSLOADER_TEMP5",
            "${project.projectDir}/run/CLASSLOADER_TEMP6",
            "${project.projectDir}/run/CLASSLOADER_TEMP7",
            "${project.projectDir}/run/CLASSLOADER_TEMP8",
            "${project.projectDir}/run/CLASSLOADER_TEMP9",
            "${project.projectDir}/run/CLASSLOADER_TEMP10"
        )
    }
}