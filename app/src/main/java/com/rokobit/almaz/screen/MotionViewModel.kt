package com.rokobit.almaz.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rokobit.almaz.AlmazViewModel
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.body.MotionStatusBody
import com.rokobit.almaz.rest.repository.CommandRepository
import com.rokobit.almaz.rest.repository.StatusResponse
import kotlinx.coroutines.*

class MotionViewModel : AlmazViewModel() {

    fun loadMotorStatus() {
        GlobalScope.launch(Dispatchers.IO) {
            motorStatus.postValue(
                getTvRun()
            )
        }
    }

}