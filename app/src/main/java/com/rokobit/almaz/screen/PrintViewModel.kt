package com.rokobit.almaz.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rokobit.almaz.AlmazViewModel
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.rest.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class PrintViewModel : AlmazViewModel() {

    private val fileRepository = FileRepository

    fun uploadImage(file: File) : LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        GlobalScope.launch(Dispatchers.IO + drawError) {
            try {
                fileRepository.uploadImage(file)
                data.postValue(true)
            } catch (e: Exception) {
                Log.e("Nik", "upload image error", e)
                data.postValue(false)
            }
        }

        return data
    }

    fun startPrint() {
        sendCommand(CommandBody("start"))
    }

    fun pausePrint() {
        sendCommand(CommandBody("pause"))
    }

    fun continuePrint() {
        sendCommand(CommandBody("continue"))
    }

    fun stopPrint() {
        sendCommand(CommandBody("stop"))
    }

}