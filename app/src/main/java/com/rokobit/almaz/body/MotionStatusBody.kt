package com.rokobit.almaz.body

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MotionStatusBody(
    @SerializedName("tvalHold")
    @Expose
    val tvalHold: Int,
    @SerializedName("tvalRun")
    @Expose
    val tvalRun: Int
)