package com.rokobit.almaz.rest

import com.rokobit.almaz.body.MotionStatusBody
import retrofit2.http.GET

interface StatusRequest {

    @GET("printer/structure/motor")
    suspend fun motor(): MotionStatusBody

}