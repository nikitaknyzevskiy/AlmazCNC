package com.rokobit.almaz.screen.open_file

import java.io.File
import java.io.Serializable

class OnePrintingPassData : Serializable {
    var layerId //до якого шару відноситься
            = 0
    var dataId // номер цього елемента в списку проходів (локальнй список малюнка)
            = 0
    var printingSpeed = 5f //0.5f; // швидкість друку
    var printerHeadClearance = 10 // зазор Z
    var blackLevel = 0 // гістограма
    var whiteLevel = 255 // гістограма
    var pictureBrightness = 0 // яскравість малюнка
    var layerFile = charArrayOf() // повний шлях файлу на диску (андроід)
    private var printerTaskIsFinished =
        0 // показує що друк цього проходу виконано (0 - не виконано, 1 - виконано)
    var printerTaskNumber // номер черги друку в принтері (принтер визначає по цьому значенню послідовність друку)
            = 0
    var projectName // назва проекту (береться з назви відкритого файлу і присвоюється папці де лежать файли підготовлені на друк)
            : String? = null
    var startupPrintPosition_X // стартова позиція проходу
            = 0
    var startupPrintPosition_Y // стартова позиція проходу
            = 0
    var layerPosition_X // координата шару
            = 0
    var layerPosition_Y // координата шару
            = 0
    var pictureDensity // роздільна здатність малюнку
            = 0

    fun printerTaskIsFinished(): Int {
        return printerTaskIsFinished
    }

    fun setPrinterTaskIsFinished(printerTaskIsFinished: Int) {
        this.printerTaskIsFinished = printerTaskIsFinished
    }

    fun getLayerFile(): File? {
        return File(String(layerFile))
    }

    fun setLayerFile(layerFile: File) {
        this.layerFile = layerFile.toString().toCharArray()
    }

}
