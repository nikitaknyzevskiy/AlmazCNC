package com.rokobit.almaz.rest.repository

import android.util.Log
import com.rokobit.almaz.body.CommandBody
import com.rokobit.almaz.rest.CommandRequest
import com.rokobit.almaz.unit.AppRest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object CommandRepository {

    private val commandRest = AppRest("http://192.168.4.1/", CommandRequest::class.java).api()

    private val commandsList = arrayListOf<CommandBody>()

    suspend fun sendCommand(command: CommandBody) {

        commandsList.add(command)

        if (commandsList.size == 1) {
            while (commandsList.isNotEmpty()) {
                val commandN = commandsList[0]

                try {
                    hardSendCommand(commandN)
                } catch (e: Exception) {
                    Log.e("Nik", "error rest command", e)
                }

                commandsList.removeAt(0)
            }
        }

    }

    suspend fun hardSendCommand(command: CommandBody) {
        try {
            commandRest.sendCommand(command)
        } catch (e: Exception) {
            Log.e("Nik", "error rest command", e)
        }
    }

}