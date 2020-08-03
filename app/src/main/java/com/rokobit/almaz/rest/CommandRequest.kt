package com.rokobit.almaz.rest

import com.rokobit.almaz.body.CommandBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT

interface CommandRequest {

    @Headers("Content-Type: application/json")
    @PUT("/printer/command")
    suspend fun sendCommand(@Body command: CommandBody): String

}