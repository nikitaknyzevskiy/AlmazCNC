/*
 * Copyright (c) 2020.
 * Nkita Knyazevkiy
 * UA
 */

package com.rokobit.almaz.unit

import com.rokobit.almaz.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiFactory {

    private var builder: OkHttpClient.Builder? = null

    private fun getClient(): OkHttpClient {
        if (builder == null) {
            builder = OkHttpClient.Builder()
            builder?.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            builder?.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            builder?.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)

        }

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder?.addInterceptor(logging)
        }

        return builder!!.build()
    }

    fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        var TAG: String = ApiFactory::class.java.simpleName

        private const val DEFAULT_TIMEOUT = 15
        const val CONNECT_TIMEOUT = DEFAULT_TIMEOUT
        const val WRITE_TIMEOUT = DEFAULT_TIMEOUT
        const val READ_TIMEOUT = DEFAULT_TIMEOUT
    }
}