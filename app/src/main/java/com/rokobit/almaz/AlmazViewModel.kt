package com.rokobit.almaz

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rokobit.almaz.AlmazData.needMotorStatus
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.rest.repository.CommandRepository
import com.rokobit.almaz.rest.repository.StatusResponse
import kotlinx.coroutines.*

object AlmazData {
    var needMotorStatus = false
}

abstract class AlmazViewModel : ViewModel() {

    val motorStatus = MutableLiveData<Int>()

    private val statusResponse = StatusResponse

    val commandResponse = CommandRepository

    open val drawError = CoroutineExceptionHandler { _, error ->
        Log.e("Nik", "error", error)
    }

    private val commandList = arrayListOf<CommandBody>()

    var timeOutRest = 0L

    fun sendCommand(commandBody: CommandBody) {

        commandList.add(commandBody)

        if (commandList.size > 1)
            return

        GlobalScope.launch(Dispatchers.IO + drawError) {
            while (commandList.isNotEmpty()) {
                val command = commandList[0]

                if (needMotorStatus) {
                    while (getTvRun() > 0) {
                        motorStatus.postValue(1)
                        delay(10L)
                    }
                }

                motorStatus.postValue(0)

                commandResponse.hardSendCommand(
                    command
                )

                commandList.removeAt(0)

                needMotorStatus = false

                if (command.action == "move_stop" || command.action == "stop" /*|| command.action == "pause"*/) {
                    needMotorStatus = true
                }
            }

        }

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