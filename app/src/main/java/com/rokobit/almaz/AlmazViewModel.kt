package com.rokobit.almaz

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.rest.repository.CommandRepository
import com.rokobit.almaz.rest.repository.StatusResponse
import kotlinx.coroutines.*

abstract class AlmazViewModel : ViewModel() {

    val motorStatus = MutableLiveData<Int>()

    private val statusResponse = StatusResponse

    private val commandResponse = CommandRepository

    open val drawError = CoroutineExceptionHandler { _, error ->
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

}