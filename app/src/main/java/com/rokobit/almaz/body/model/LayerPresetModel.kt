package com.rokobit.almaz.body.model

import com.google.gson.annotations.SerializedName

data class LayerPresetModel(
    val pictureDensity: Long = 255L,
    @SerializedName("Brightness")
    val brightness: Long = 0L,
    @SerializedName("White")
    val white: Long = 0L,
    val clearence: Long = 255,
    val startupPrintPosition_X: Long = 0L,
    val startupPrintPosition_Y: Long = 0L,
    val layerPosition_X: Long = 0L,
    val layerPosition_Y: Long = 0L,
    val layerId: Long = System.currentTimeMillis(),
    val dataId: Long = System.currentTimeMillis(),
    val printerTaskNumber: Long = System.currentTimeMillis(),
    val printerTaskIsFinished: Long = System.currentTimeMillis(),
    val projectName: String,
    val layerFile: String
)