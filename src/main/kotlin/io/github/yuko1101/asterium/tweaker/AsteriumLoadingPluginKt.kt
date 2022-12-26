package io.github.yuko1101.asterium.tweaker

import io.github.yuko1101.asterium.asm.AsteriumTransformer
import io.github.yuko1101.asterium.utils.StringUtils.startsWithAny
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import java.awt.Desktop
import java.awt.Image
import java.awt.Toolkit
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.net.URI
import java.net.URL
import javax.swing.*


class AsteriumLoadingPluginKt : IFMLLoadingPlugin {
    init {
        // Must use reflection otherwise the "constant" value will be inlined by compiler
        val mixinVersion = runCatching {
            MixinBootstrap::class.java.getDeclaredField("VERSION").also { it.isAccessible = true }
                .get(null) as String
        }.getOrDefault("unknown")
        if (!mixinVersion.startsWithAny("0.8")) {
            try {
                Class.forName("com.mumfrey.liteloader.launch.LiteLoaderTweaker")
                showMessage(AsteriumLoadingPlugin.liteloaderUserMessage)
                AsteriumLoadingPlugin.exit()
            } catch (ignored: ClassNotFoundException) {
                showMessage(
                    AsteriumLoadingPlugin.badMixinVersionMessage + "<br>The culprit seems to be " + File(
                        MixinEnvironment::class.java.protectionDomain.codeSource.location.toURI()
                    ).name + "</p></html>"
                )
                AsteriumLoadingPlugin.exit()
            }
        }

        val forgeVersion = ForgeVersion.getBuildVersion()
        // Asbyth's forge fork uses version 0
        if (!(forgeVersion >= 2318 || forgeVersion == 0)) {
            val forgeUrl =
                URL("https://maven.minecraftforge.net/net/minecraftforge/forge/1.8.9-11.15.1.2318-1.8.9/forge-1.8.9-11.15.1.2318-1.8.9-installer.jar").toURI()
            val forgeButton = createButton("Get Forge") {
                Desktop.getDesktop().browse(forgeUrl)
            }
            showMessage(
                """
                #Asterium has detected that you are using an old version of Forge (build ${forgeVersion}).
                #In order to resolve this issue and launch the game,
                #please install Minecraft Forge build 2318 for Minecraft 1.8.9.
                #If you have already done this and are still getting this error,
                #ask for support in the Discord.
                """.trimMargin("#"),
                forgeButton
            )
        }
        if (this::class.java.classLoader.getResource("patcher.mixins.json") == null && Package.getPackages()
                .any { it.name.startsWith("club.sk1er.patcher") }
        ) {
            val sk1erClubButton = createButton("Go to Sk1er.Club") {
                Desktop.getDesktop().browse(URL("https://sk1er.club/mods/patcher").toURI())
            }
            showMessage(
                """
                #Asterium has detected that you are using an old version of Patcher.
                #You must update Patcher in order for your game to launch.
                #You can do so at https://sk1er.club/mods/patcher
                #If you have already done this and are still getting this error,
                #ask for support in the Discord.
                """.trimMargin("#"), sk1erClubButton
            )
            AsteriumLoadingPlugin.exit()
        }
    }

    override fun getASMTransformerClass(): Array<String> {
        return arrayOf(AsteriumTransformer::class.java.name)
    }

    override fun getModContainerClass(): String? {
        return null
    }

    override fun getSetupClass(): String? {
        return null
    }

    override fun injectData(map: MutableMap<String, Any>?) {
    }

    override fun getAccessTransformerClass(): String? {
        return null
    }

    private fun showMessage(errorMessage: String, vararg options: Any) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // This makes the JOptionPane show on taskbar and stay on top
        val frame = JFrame()
        frame.isUndecorated = true
        frame.isAlwaysOnTop = true
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        var icon: Icon? = null
        try {
            val url = URL("https://cdn.discordapp.com/attachments/630347921400791063/630360330240786452/fairy_soul.png")
            icon = ImageIcon(
                Toolkit.getDefaultToolkit().createImage(url).getScaledInstance(50, 50, Image.SCALE_DEFAULT)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val discordLink = createButton("Open Github") {
            Desktop.getDesktop().browse(URI("https://github.com/yuko1101/AsteriumMod"))
        }
        val close = createButton("Close") {
            exit()
        }
        val totalOptions = arrayOf(discordLink, close, *options)
        JOptionPane.showOptionDialog(
            frame,
            errorMessage,
            "Asterium Error",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.ERROR_MESSAGE,
            icon,
            totalOptions,
            totalOptions[0]
        )
        exit()
    }

    private fun exit() {
        AsteriumLoadingPlugin.exit()
    }

    private fun createButton(text: String, onClick: JButton.() -> Unit): JButton {
        return JButton(text).apply {
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    onClick(this@apply)
                }
            })
        }
    }
}

