package com.rokobit.almaz.screen.open_file

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import com.rokobit.almaz.R
import java.io.*
import java.nio.channels.FileChannel
import java.util.*

class AppFileAndDirectoryHelper() {
    lateinit var path: File

    fun createFilesList(): List<File>? {
        if (originalImagesDirectory.isFile) return null
        val filesList: MutableList<File> =
            ArrayList()
        val files: Array<File>? = originalImagesDirectory.listFiles()
        if (files != null) {
            for (file: File in files) {
                if (file.isFile) {
                    filesList.add(file)
                }
                filesList.sortWith(Comparator { o1, o2 ->
                    o2.name.substring(o2.name.indexOf("."))
                        .compareTo(
                            o1.name.substring(o1.name.indexOf(".")),
                            ignoreCase = true
                        )
                })
            }
        }
        return filesList
    }

    fun getFileSize(file: File?): String {
        var size: Float = 0f
        if ((file != null) && file.exists() && file.isFile) {
            size = file.length().toFloat() / 1000000f
        }
        return AppTextAndKeyboardUtils().formatFloatInLocaleString(size, 2).toString() + " Mb"
    }

    fun createAppDirectories(context: Context) {
        createOriginalImagesDirectory(context)
        createTemporaryFilesDirectory()
        createProjectDirectory()
        createSavedFilesDirectory()
    }

    private fun createSavedFilesDirectory(): Boolean {
        val directory: File =
            File(path.toString() + File.separator + ORIGINAL_IMAGE_FOLDER + File.separator + SAVED_FILES_FOLDER)
        var result: Boolean = false
        if (directory.exists()) {
            result = true
        } else {
            result = directory.mkdirs()
        }
        return result
    }

    private val savedFilesDirectory: File
        get() = File(path.toString() + File.separator + ORIGINAL_IMAGE_FOLDER + File.separator + SAVED_FILES_FOLDER + File.separator)

    private fun createOriginalImagesDirectory(context: Context): Boolean {
        val directory: File =
            File(path.toString() + File.separator + ORIGINAL_IMAGE_FOLDER)
        var result: Boolean = false
        if (directory.exists()) {
            result = true
        } else {
            result = directory.mkdirs()
            if (directory.exists()) {
                result = result && directory.setExecutable(true)
                result = result && directory.setReadable(true)
                result = result && directory.setWritable(true)
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(directory.toString()),
                    null,
                    null
                )
            }
        }
        return result
    }

    private fun createTemporaryFilesDirectory(): Boolean {
        val directory: File =
            File(appPath.toString() + File.separator + ORIGINAL_IMAGE_FOLDER + File.separator + TEMPORARY_FILES_FOLDER)
        return directory.mkdir()
    }

    private fun createProjectDirectory(): Boolean {
        val directory: File =
            File(path.toString() + File.separator + ORIGINAL_IMAGE_FOLDER + File.separator + PROJECTS_FOLDER)
        return directory.mkdir()
    }

    private val originalImagesDirectory: File
        get() = File(path.toString() + File.separator + ORIGINAL_IMAGE_FOLDER + File.separator)

    val temporaryFilesDirectory: File
        get() = File(
            (appPath.toString() + File.separator + ORIGINAL_IMAGE_FOLDER
                    + File.separator + TEMPORARY_FILES_FOLDER + File.separator)
        )

    val projectDirectory: File
        get() {
            return File(
                (path.toString() + File.separator + ORIGINAL_IMAGE_FOLDER
                        + File.separator + PROJECTS_FOLDER + File.separator)
            )
        }

    val temporaryFileName: File
        get() {
            return File(
                temporaryFilesDirectory,
                TEMPORARY_FILE_NAME
            )
        }

    fun createNewOrGetExistingProjectOutputDirectory(
        directoryName: String?,
        context: Context?
    ): File? {
        val file: File = File(projectDirectory, directoryName)
        var result: Boolean = false
        if (directoryExists(file)) {
            result = true
        } else {
            result = file.mkdir()
            if (result) {
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.toString()),
                    null,
                    null
                )
            }
        }
        return if (result) file else null
    }

    fun createNewOrGetExistingFileOutputDirectory(
        directoryName: String?,
        context: Context?
    ): File? {
        val file = File(savedFilesDirectory, directoryName)
        val result: Boolean
        result = if (directoryExists(file)) {
            true
        } else {
            file.mkdir()
        }
        return if (result) file else null
    }

    private fun directoryExists(file: File?): Boolean {
        if (file == null) return false
        val dir: File = File(file.toString())
        return dir.exists() && dir.isDirectory
    }

    fun deleteTemporaryFiles() {
        val files: List<File> =
            AppFileAndDirectoryHelper().temporaryDirectoryFilesList
                ?: return
        for (file: File in files) {
            if (file.exists() && file.isFile) {
                file.delete()
            }
        }
    }

    fun deleteFile(file: File) {
        if (file.exists() && file.isFile) {
            file.delete()
        }
    }

    private val temporaryDirectoryFilesList: List<File>?
        get() {
            val filesList: MutableList<File> =
                ArrayList()
            val files: Array<File> = temporaryFilesDirectory.listFiles() ?: return null
            for (file: File in files) {
                if (file.isFile) {
                    filesList.add(file)
                }
            }
            return filesList
        }

    fun copyFileToAppDirectory(
        source: File,
        destination: File,
        context: Context
    ): Array<String?> {
        val results: Array<String?> = arrayOfNulls(2)
        if ((source == destination)) {
            results[0] = "false"
            results[1] = context.resources
                .getString(R.string.error_message_an_attempt_to_copy_the_file_itself)
            Log.d("file_copy_log", "copy error 0")
            return results
        }
        var `in`: FileChannel? = null
        try {
            `in` = FileInputStream(source).channel
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            results[0] = "false"
            results[1] =
                context.resources.getString(R.string.error_message_source_file_not_found)
            Log.d("file_copy_log", "copy error 1")
        }
        var out: FileChannel? = null
        try {
            out = FileOutputStream(destination).channel
        } catch (e: FileNotFoundException) {
            results[0] = "false"
            results[1] =
                context.resources.getString(R.string.error_message_destination_file_not_found)
            e.printStackTrace()
            Log.d("file_copy_log", "copy error 2")
        }
        try {
            try {
                `in`!!.transferTo(0, `in`.size(), out)
                results[0] = "true"
                results[1] =
                    context.resources.getString(R.string.file_copy_success_message)
            } catch (e: IOException) {
                e.printStackTrace()
                results[0] = "false"
                results[1] =
                    context.resources.getString(R.string.error_message_copying_interrupted)
                Log.d("file_copy_log", "copy error 3")
            }
        } catch (e: Exception) {
            results[0] = "false"
            results[1] = context.resources.getString(R.string.not_copied_message)
            Log.d("file_copy_log", "copy error 4")
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return results
    }

    fun createFileCopy(
        source: File,
        destination: File
    ): Array<String?> {
        val results: Array<String?> = arrayOfNulls(2)
        if ((source == destination)) {
            results[0] = "false"
            Log.d("file_copy_log", "copy error 0")
            return results
        }
        var `in`: FileChannel? = null
        try {
            `in` = FileInputStream(source).channel
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            results[0] = "false"
            Log.d("file_copy_log", "copy error 1")
        }
        var out: FileChannel? = null
        try {
            out = FileOutputStream(destination).channel
        } catch (e: FileNotFoundException) {
            results[0] = "false"
            e.printStackTrace()
            Log.d("file_copy_log", "copy error 2")
        }
        try {
            try {
                `in`!!.transferTo(0, `in`.size(), out)
                results[0] = "true"
                } catch (e: IOException) {
                e.printStackTrace()
                results[0] = "false"
                Log.d("file_copy_log", "copy error 3")
            }
        } catch (e: Exception) {
            results[0] = "false"
            Log.d("file_copy_log", "copy error 4")
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return results
    }

    fun deleteProjectFiles(projectName: String) {
        val files: List<File> = getProjectFilesList(projectName) ?: return
        for (file: File in files) {
            if (file.exists() && file.isFile) {
                file.delete()
            }
        }
    }

    private fun getProjectFilesList(projectName: String): List<File>? {
        val filesList: MutableList<File> =
            ArrayList()
        val projectDirectory = File(projectDirectory, projectName)
        if (projectDirectory.exists()) {
            val files: Array<File> = projectDirectory.listFiles() ?: return null
            for (file: File in files) {
                if (file.isFile) {
                    filesList.add(file)
                }
            }
        }
        return filesList
    }

    companion object {
        private val path: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM
        )
        private val appPath: File = Environment.getExternalStorageDirectory()
        private const val ORIGINAL_IMAGE_FOLDER: String = "Almaz"
        private const val TEMPORARY_FILES_FOLDER: String = "temp"
        private const val SAVED_FILES_FOLDER: String = "saved"
        private const val PROJECTS_FOLDER: String = "project"
        private const val TEMPORARY_FILE_NAME: String = "preview.jpg"
    }
}

