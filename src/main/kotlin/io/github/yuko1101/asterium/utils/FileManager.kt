package io.github.yuko1101.asterium.utils

import java.io.*

object FileManager {
    val clientDirectory = File("asterium")
    val modsDirectory = File(clientDirectory, "config")
    val addonsDirectory = File(clientDirectory, "addons")
    val cosmeticsDirectory = File(clientDirectory, "cosmetics")
    fun init() {
        if (!clientDirectory.exists()) {
            clientDirectory.mkdirs()
        }
        if (!modsDirectory.exists()) {
            modsDirectory.mkdirs()
        }
        if (!addonsDirectory.exists()) {
            addonsDirectory.mkdirs()
        }
        if (!cosmeticsDirectory.exists()) {
            cosmeticsDirectory.mkdirs()
        }
    }

    fun writeToFile(file: File, str: String): Boolean {
        return try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val outputStream = FileOutputStream(file)
            outputStream.write(str.toByteArray())
            outputStream.flush()
            outputStream.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun readFromJson(file: File, str: String): String? {
        return try {
            if (!file.exists()) {
                file.createNewFile()
                val outputStream = FileOutputStream(file)
                outputStream.write(str.toByteArray())
                outputStream.flush()
                outputStream.close()
            }
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val builder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            bufferedReader.close()
            inputStreamReader.close()
            fileInputStream.close()
            builder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}