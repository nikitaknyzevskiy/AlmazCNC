package com.rokobit.almaz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import moe.xing.baseutils.Init

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Almaz version " + BuildConfig.VERSION_CODE


        Init.getInstance(application, true, BuildConfig.VERSION_NAME, "Almaz")
    }
}