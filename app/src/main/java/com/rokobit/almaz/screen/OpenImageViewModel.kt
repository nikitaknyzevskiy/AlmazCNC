package com.rokobit.almaz.screen

import android.app.Application
import androidx.lifecycle.*
import com.rokobit.almaz.local_data.ImageDatabase
import com.rokobit.almaz.local_data.ImageEntity
import com.rokobit.almaz.local_data.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OpenImageViewModel(application: Application) : AndroidViewModel(application) {

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

}