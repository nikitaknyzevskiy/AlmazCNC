package com.rokobit.almaz.screen.open_file

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LayerPreset : Serializable {
    @SerializedName("pictureDensity")
    @Expose
    var pictureDensity = 0L

    @SerializedName("Brightness")
    @Expose
    var brightness = 0L

    @SerializedName("White")
    @Expose
    var white = 0L

    @SerializedName("Black")
    @Expose
    var black = 0L

    @SerializedName("clearence")
    @Expose
    var clearence = 0L

    @SerializedName("speed")
    @Expose
    var speed = 0L

    @SerializedName("startupPrintPosition_X")
    @Expose
    var startupPrintPositionX = 0L

    @SerializedName("startupPrintPosition_Y")
    @Expose
    var startupPrintPositionY = 0L

    @SerializedName("layerPosition_X")
    @Expose
    var layerPositionX = 0L

    @SerializedName("layerPosition_Y")
    @Expose
    var layerPositionY = 0L

    @SerializedName("layerId")
    @Expose
    var layerId = 0L

    @SerializedName("dataId")
    @Expose
    var dataId = 0L

    @SerializedName("printerTaskNumber")
    @Expose
    var printerTaskNumber = 0L

    @SerializedName("printerTaskIsFinished")
    @Expose
    var printerTaskIsFinished = 1L

    @SerializedName("projectName")
    @Expose
    var projectName = ""

    @SerializedName("layerFile")
    @Expose
    var layerFile = ""

}
