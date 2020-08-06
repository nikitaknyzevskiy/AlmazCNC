package com.rokobit.almaz.screen

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.rokobit.almaz.local_data.ImageDatabase
import com.rokobit.almaz.local_data.ImageEntity
import com.rokobit.almaz.local_data.ImageRepository
import com.rokobit.almaz.psd.model.Psd
import com.rokobit.almaz.psd.parser.PsdFileParser
import com.rokobit.almaz.psd.parser.layer.LayerParser
import com.rokobit.almaz.psd.parser.layer.LayersSectionParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class OpenImageViewModel(application: Application) : AndroidViewModel(application) {

    private val MAXIMUM_NUMBER_OF_PSD_LAYERS = 11
    private var mLayerParserList: List<LayerParser>? = null

    private val database = ImageDatabase.getDatabase(application)
    private val repository = ImageRepository(database.imageDao())

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _previouslyOpenedImageList = repository.getAllImages().asLiveData()
    val previouslyOpenedImageList: LiveData<List<ImageEntity>>
        get() = _previouslyOpenedImageList

    fun saveImage(path: String) {
        coroutineScope.launch {
            repository.saveImage(ImageEntity(path, 0))
        }
    }

    @Throws(IOException::class)
    fun decodePSDtoBitmap(path: String): Bitmap? {
        val parser = PsdFileParser()
        val file = File(path)
        parser.parse(FileInputStream(file.absolutePath))
        val layers: LayersSectionParser = parser.layersSectionParser
        mLayerParserList = layers.layers
        if (mLayerParserList!!.size > MAXIMUM_NUMBER_OF_PSD_LAYERS) {
            Log.e("Error", "Too many layers in psd")
            return null
        }
        for (layer in mLayerParserList!!) {
            println(layer.name.toString() + " left = " + layer.getleft() + " top = " + layer.gettop())
        }
        return Psd(File(file.absolutePath)).image
    }



}