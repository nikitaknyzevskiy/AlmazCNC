package com.rokobit.almaz.screen

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.rokobit.almaz.AlmazViewModel
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.body.model.LayerPresetModel
import com.rokobit.almaz.rest.repository.FileRepository
import com.rokobit.almaz.unit.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class PrintViewModel : AlmazViewModel() {

    private val fileRepository = FileRepository

    val layerLiveData = MutableLiveData<LayerPresetModel>()

    fun addLayer(context: Context, bmp: Bitmap, speed: Int, black: Int, white: Int) = liveData {
        val outputFile = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")

        BitmapUtils.save(bmp, outputFile)

        MediaScannerConnection.scanFile(
            context,
            arrayOf(outputFile.toString()),
            null,
            null
        )

        val layer = LayerPresetModel(
            speed = speed.toLong(),
            black = black.toLong(),
            white = white.toLong(),
            layerFile = outputFile.toString(),
            projectName = "Layer1Preset"
        )

        try {
            fileRepository.uploadImage(outputFile)
        } catch (e: Exception) {
            Log.e("Nik", "layer error", e)
        }

        try {
            fileRepository.uploadLayers(arrayListOf(layer))
        } catch (e: Exception) {
            Log.e("Nik", "layer error", e)
        }

        layerLiveData.postValue(layer)

        emit(true)
    }

    fun uploadImage(file: File) : LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        GlobalScope.launch(Dispatchers.IO + drawError) {
            try {
                fileRepository.uploadImage(file)
                data.postValue(true)
            }
            catch (e: com.google.gson.stream.MalformedJsonException) {
                Log.e("Nik", "upload image error", e)
                data.postValue(true)
            }
            catch (e: Exception) {
                Log.e("Nik", "upload image error", e)
                data.postValue(false)
            }
        }

        return data
    }

    fun uploadLayers(files: List<File>) : LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        if (files.isEmpty()) {
            data.postValue(false)
            return data
        }

        val pName = System.currentTimeMillis().toString()

        GlobalScope.launch(Dispatchers.IO + drawError) {
            val layers = arrayListOf<LayerPresetModel>()

            for (file in files) {
                try {
                    fileRepository.uploadImage(file)
                } catch (e: com.google.gson.stream.MalformedJsonException) {
                    Log.e("Nik", "upload image error", e)
                } catch (e: Exception) {
                    Log.e("Nik", "upload image error", e)
                    data.postValue(false)
                    return@launch
                } finally {
                    layers.add(
                        LayerPresetModel(
                            projectName = pName,
                            layerFile = file.name
                        )
                    )
                }
            }

            try {
                fileRepository.uploadLayers(layers)
            } catch (e: Exception) {
            }

            data.postValue(true)
        }

        return data
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