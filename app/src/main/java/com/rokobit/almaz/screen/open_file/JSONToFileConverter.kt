package com.rokobit.almaz.screen.open_file

import android.os.Environment
import java.io.*
import java.util.*

object JSONToFileConverter {
    const val PATH = "/Almaz"

    @Throws(IOException::class)
    fun saveJsonToFile(`object`: Any?, fileName: String?): String? {
        var path: String? = Environment.getExternalStorageDirectory()
            .toString() + File.separator + PATH + File.separator
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        path += fileName
        val data = File(path)
        if (!data.createNewFile()) {
            data.delete()
            data.createNewFile()
        }
        val objectOutputStream =
            ObjectOutputStream(FileOutputStream(data))
        objectOutputStream.writeObject(`object`)
        objectOutputStream.close()
        return path
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun getJsonFromFile(fileName: String?): Any? {
        var `object`: Any? = null
        var path: String? = Environment.getExternalStorageDirectory()
            .toString() + File.separator +
                PATH + File.separator
        path += fileName
        val data = File(path)
        if (data.exists()) {
            val objectInputStream =
                ObjectInputStream(FileInputStream(data))
            `object` = objectInputStream.readObject()
            objectInputStream.close()
        }
        return `object`
    }

    @Throws(IOException::class)
    fun saveProjectJsonToFile(
        `object`: Any?,
        projectFolderName: String
    ): String {
        val appFileAndDirectoryHelper = AppFileAndDirectoryHelper()
        var path: String = appFileAndDirectoryHelper.projectDirectory.path
            .toString() + File.separator + projectFolderName + File.separator
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        path += Project::class.java.simpleName.toLowerCase()
        val data = File(path)
        if (!data.createNewFile()) {
            data.delete()
            data.createNewFile()
        }
        val objectOutputStream =
            ObjectOutputStream(FileOutputStream(data))
        objectOutputStream.writeObject(`object`)
        objectOutputStream.close()
        return path
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun getProjectFromFile(projectFolderName: String): Any? {
        var `object`: Any? = null
        val appFileAndDirectoryHelper = AppFileAndDirectoryHelper()
        var path: String = appFileAndDirectoryHelper.projectDirectory.path
            .toString() + File.separator + projectFolderName + File.separator
        path += Project::class.java.simpleName.toLowerCase()
        val data = File(path)
        if (data.exists()) {
            val objectInputStream =
                ObjectInputStream(FileInputStream(data))
            `object` = objectInputStream.readObject()
            objectInputStream.close()
        }
        return `object`
    }

    val projects: List<String>?
        get() {
            val directoryList: MutableList<String> =
                ArrayList()
            val appFileAndDirectoryHelper = AppFileAndDirectoryHelper()
            val path: String = appFileAndDirectoryHelper.projectDirectory.path
                .toString() + File.separator
            val f = File(path)
            val files = f.listFiles() ?: return null
            for (file in files) {
                if (file.isDirectory) {
                    directoryList.add(file.name)
                }
            }
            return directoryList
        }
}
