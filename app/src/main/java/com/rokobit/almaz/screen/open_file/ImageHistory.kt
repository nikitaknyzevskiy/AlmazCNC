package com.rokobit.almaz.screen.open_file

import android.graphics.Bitmap
import java.io.*
import java.nio.ByteBuffer

class ImageHistory {
    var bitmapCount = 0
    private var bitmapWidth = 0
    private var bitmapHeight = 0
    fun getBitmap(index: Int): Bitmap? {
        return if (index in 0 until bitmapCount) {
            getBitmapFromDisk(index)
        } else {
            null
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        saveBitmapBytesToDisk(bitmap, bitmapCount)
        bitmapCount += 1
        bitmapWidth = bitmap.width
        bitmapHeight = bitmap.height
    }

    fun updateBitmap(bitmap: Bitmap?, fileIndex: Int) {
        saveBitmapBytesToDisk(bitmap, fileIndex)
    }

    private fun saveBitmapBytesToDisk(bitmap: Bitmap?, fileIndex: Int) {
        val appFileAndDirectoryHelper = AppFileAndDirectoryHelper()
        val directory: File = appFileAndDirectoryHelper.temporaryFilesDirectory
        if (bitmap == null) {
            return
        }
        val size = bitmap.rowBytes * bitmap.height
        val byteBuffer = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuffer)
        val byteArray: ByteArray
        byteArray = byteBuffer.array()
        var fos: FileOutputStream? = null
        val outFile =
            File(directory, FILE_NAME + fileIndex.toString())
        try {
            fos = FileOutputStream(outFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(byteArray)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getBitmapFromDisk(fileIndex: Int): Bitmap? {
        val appFileAndDirectoryHelper = AppFileAndDirectoryHelper()
        val directory: File = appFileAndDirectoryHelper.temporaryFilesDirectory
        val file =
            File(directory, FILE_NAME + fileIndex.toString())
        if (!file.exists()) {
            return null
        }
        val size = file.length().toInt()
        val bytes: ByteArray
        bytes = try {
            ByteArray(size)
        } catch (e: OutOfMemoryError) {
            BaseActivity.showMemoryErrorDialog(BaseActivity())
            return null
        }
        try {
            val buf =
                BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val config = Bitmap.Config.ARGB_8888
        if (bitmapWidth == 0 || bitmapHeight == 0) {
            return null
        }
        val bitmap: Bitmap =
            BaseActivity.createBitmap(bitmapWidth, bitmapHeight, config) ?: return null
        val buffer = ByteBuffer.wrap(bytes)
        buffer.rewind()
        //        try { // розкоментувати щоб додаток не падав якщо розмір бітмапа, який читаємо, не відповідає ширині і висоті малюнка
        bitmap.copyPixelsFromBuffer(buffer)
        //        }catch (Exception e){
//            //
//        }
        return bitmap
    }

    fun setBitmapWidth(bitmapWidth: Int) {
        this.bitmapWidth = bitmapWidth
    }

    fun setBitmapHeight(bitmapHeight: Int) {
        this.bitmapHeight = bitmapHeight
    }

    companion object {
        const val FILE_NAME = "history_file_"
    }
}
