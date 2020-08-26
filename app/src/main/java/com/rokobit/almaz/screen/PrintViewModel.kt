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
            black = black.toLong() * (10 / 255),
            white = white.toLong() * (10 / 255),
            layerFile = outputFile.name,
            projectName = "Layer1Preset"
        )

        try {
            fileRepository.uploadImage(outputFile)
        } catch (e: Exception) {
            Log.e("Nik", "layer error", e)
        }

        /*try {
            fileRepository.uploadLayers(arrayListOf(layer))
        } catch (e: Exception) {
            Log.e("Nik", "layer error", e)
        }*/

        val value = layerLiveData.value
        value?.put(index, layer)
        layerLiveData.postValue(value)

        emit(true)
    }

    fun decodePsdToLayers(context: Context, file: File) = liveData(Dispatchers.IO) {
        val parser = PsdFileParser()
        parser.parse(file.inputStream())

        val psd = Psd(file)

        for (i in 0..psd.layersCount) {
            addLayer(context, layerLiveData.value?.size ?: 0, psd.getLayer(i).image, 20, 1, 0, 0)
        }

        emit(true)
    }

    fun uploadLayers() = liveData(Dispatchers.IO) {
        try {
            fileRepository.uploadLayers(layerLiveData.value!!.values.toList())
        } catch (e: Exception) {
            Log.e("Nik", "layer error", e)
        }

        emit(true)
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