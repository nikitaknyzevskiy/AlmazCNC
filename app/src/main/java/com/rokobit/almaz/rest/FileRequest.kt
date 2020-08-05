package com.rokobit.almaz.rest

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileRequest {

    @Multipart // вивантажити файл
    @POST("/printer/file")
    suspend fun uploadFile(
        @HeaderMap headerData: Map<String?, String?>?,
        @Part file: MultipartBody.Part?
    ): Call<ResponseBody>

}