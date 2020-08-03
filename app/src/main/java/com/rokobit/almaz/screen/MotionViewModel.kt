package com.rokobit.almaz.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.body.MotionStatusBody
import com.rokobit.almaz.rest.repository.CommandRepository
import com.rokobit.almaz.rest.repository.StatusResponse
import kotlinx.coroutines.*

class MotionViewModel : ViewModel() {

    private val commandResponse = CommandRepository

    private val statusResponse = StatusResponse

    val motorStatus = MutableLiveData<Int>()

    private val drawError = CoroutineExceptionHandler { _, error ->
        Log.e("Nik", "error", error)
    }

    private val commandList = arrayListOf<CommandBody>()

    var timeOutRest = 10L

    fun sendCommand(commandBody: CommandBody) {

        commandList.add(commandBody)

        if (commandList.size > 1)
            return

        GlobalScope.launch(Dispatchers.IO + drawError) {

            while (commandList.isNotEmpty()) {
                val command = commandList[0]

                if (command.action != "move_stop") {
                    while (getTvRun() > 0) {
                        motorStatus.postValue(1)
                        delay(timeOutRest)
                    }
                }
                else {
                    delay(timeOutRest)
                }

                motorStatus.postValue(0)

                commandResponse.hardSendCommand(
                    command
                )


                commandList.removeAt(0)

            }

        }

        /*launch(Dispatchers.IO) {
            commandResponse.hardSendCommand(commandBody)
        }*/

    }

    suspend fun getTvRun(): Int {
        return try {
            statusResponse.motor().tvalRun
        } catch (e: Exception) {
            Log.e("Nik", "motor status error", e)
            -1
        }
    }

    fun loadMotorStatus() {
        GlobalScope.launch(Dispatchers.IO) {
            motorStatus.postValue(
                getTvRun()
            )
        }
    }

}