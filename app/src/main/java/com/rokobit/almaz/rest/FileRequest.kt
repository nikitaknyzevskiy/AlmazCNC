package com.rokobit.almaz.rest

import com.rokobit.almaz.body.model.LayerPresetModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FileRequest {

    @Multipart // вивантажити файл
    @POST("/printer/file")
    suspend fun uploadFile(
        @HeaderMap headerData: Map<String?, String?>?,
        @Part file: MultipartBody.Part?
    ): String

    // відправити на принтенр масив пресетів шару
    @PUT("/printer/structure/layerpreset")
    suspend fun sendLayers(@HeaderMap headerData: Map<String, String>, @Body layerPresets: Array<LayerPresetModel>): String

}