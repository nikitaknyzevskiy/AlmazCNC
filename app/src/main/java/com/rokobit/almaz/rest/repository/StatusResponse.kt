package com.rokobit.almaz.rest.repository

import com.rokobit.almaz.rest.StatusRequest
import com.rokobit.almaz.unit.AppRest

object StatusResponse {

    private val statusRest = AppRest("http://192.168.4.1/", StatusRequest::class.java).api()

    suspend fun motor() = statusRest.motor()

}