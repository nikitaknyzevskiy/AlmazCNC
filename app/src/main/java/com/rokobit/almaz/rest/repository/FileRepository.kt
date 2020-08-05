package com.rokobit.almaz.rest.repository

import com.rokobit.almaz.rest.FileRequest
import com.rokobit.almaz.unit.AppRest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

object FileRepository {

    private val fileRest = AppRest("http://192.168.4.1/", FileRequest::class.java).api()

    suspend fun uploadImage(file: File) {
        val requestFile = RequestBody.create("text/plain".toMediaTypeOrNull(), file)

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("name", file.name, requestFile)

        val headerData = HashMap<String,String>()

        headerData["project_name"] = file.name
        headerData["file_name"] = file.name

        fileRest.uploadFile(headerData.toMap(), body)
    }

}