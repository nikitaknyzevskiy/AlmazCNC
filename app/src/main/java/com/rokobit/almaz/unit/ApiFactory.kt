/*
 * Copyright (c) 2020.
 * Nkita Knyazevkiy
 * UA
 */

package com.rokobit.almaz.unit

import okhttp3.OkHttpClient
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

            /*builder?.addInterceptor { chain ->
                Log.d(TAG, "addInterceptor")

                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .header("platform", "android")
                    *//*.header("Authorization",
                        "${PreferenceTools.accessToken().tokenType} " +
                                PreferenceTools.accessToken().accessToken
                    )*//*
                    .method(original.method, original.body)

                val request = requestBuilder
                    .cacheControl(CacheControl.Builder().noCache().build())
                    .build()

                chain.proceed(request)
            }*/
        }

        /*if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder?.addInterceptor(logging)
        }*/

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