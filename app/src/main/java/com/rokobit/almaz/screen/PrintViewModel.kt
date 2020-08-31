package com.rokobit.almaz.screen

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.rokobit.almaz.AlmazViewModel
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.body.model.LayerPresetModel
import com.rokobit.almaz.rest.repository.FileRepository
import com.rokobit.almaz.unit.BitmapUtils
import kotlinx.coroutines.Dispatchers
import psd.model.Psd
import psd.parser.PsdFileParser
import java.io.File

class PrintViewModel : AlmazViewModel() {

    private val fileRepository = FileRepository

    val layerLiveData = MutableLiveData<LinkedHashMap<Int, LayerPresetModel>>(linkedMapOf())

    fun addLayer(
        context: Context,
        index: Int,
        bmp: Bitmap,
        pixel: Int,
        speed: Int,
        black: Int,
        white: Int
    ) = liveData {

        addLayerHard(context, index, bmp, pixel, speed, black, white)

        emit(true)
    }

    private suspend fun addLayerHard(context: Context,
                                     index: Int,
                                     bmp: Bitmap,
                                     pixel: Int,
                                     speed: Int,
                                     black: Int,
                                     white: Int) {
        val outputFile = File(context.cacheDir, "${System.currentTimeMillis()}.bmp")

        BitmapUtils.save(bmp, outputFile)

        MediaScannerConnection.scanFile(
            context,
            arrayOf(outputFile.toString()),
            null,
            null
        )

        val layer = LayerPresetModel(
            pictureDensity = pixel.toLong(),
            speed = speed.toLong(),
            black = black.toLong() * (255 / 10),
            white = white.toLong() * (255 / 10),
            layerFile = outputFile.name,
            projectName = "Layer1Preset"
        )

        val value = layerLiveData.value
        value?.put(index, layer)
        layerLiveData.postValue(value)
    }

    fun decodePsdToLayers(context: Context, file: File) = liveData(Dispatchers.IO) {
        val parser = PsdFileParser()
        parser.parse(file.inputStream())

        val psd = Psd(file)

        for (i in 0 until psd.layersCount) {
            addLayerHard(context, layerLiveData.value?.size?:0, psd.getLayer(i).image, 20, 1, 0, 0)
        }

        emit(true)
    }

    fun uploadLayers() = liveData(Dispatchers.IO) {
        try {
            fileRepository.uploadLayers(layerLiveData.value!!.values.toList())
        }
        catch (e: Exception) {
            Log.e("Nik", "layer error", e)
        }
        emit(true)
    }

    fun uploadImages(context: Context) = liveData {
        for ((i, entry) in layerLiveData.value!!.values.withIndex()) {
            val outputFile = File(context.cacheDir, entry.layerFile)
            try {
                fileRepository.uploadImage(outputFile)
            } catch (e: Exception) {
                Log.e("Nik", "layer error", e)
            } finally {
                emit(i+1)
            }
        }
    }

    fun startPrint() {
        sendCommand(
            CommandBody("start")
        )
    }

    fun pausePrint() {
        sendCommand(
            CommandBody("pause")
        )
    }

    fun continuePrint() {
        sendCommand(
            CommandBody("continue")
        )
    }

    fun stopPrint() {
        sendCommand(
            CommandBody("stop")
        )
    }

}