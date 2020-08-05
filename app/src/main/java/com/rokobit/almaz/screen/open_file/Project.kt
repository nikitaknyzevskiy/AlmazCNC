package com.rokobit.almaz.screen.open_file

import java.io.File
import java.io.Serializable
import java.util.*

class Project : Serializable {
    var projectName: String? = null
    var projectPath: String? = null
    var projectLayerCount = 0
    private val projectLayerFiles: MutableList<File>? =
        ArrayList()
    private val printingPassDataList: MutableList<OnePrintingPassData>? =
        ArrayList<OnePrintingPassData>()
    private var mPrinterLayers: Array<PrinterLayer?> = arrayOfNulls<PrinterLayer>(5)
    var layerPresetHeaderNames = arrayListOf<String>()
    var isAllLayersAreUploaded = false

    constructor() {}
    constructor(layerPresetHeaderNames: Array<String?>) {
        for (i in 0..4) {
            mPrinterLayers[i] = PrinterLayer()
            mPrinterLayers[i]?.layerPresetName = layerPresetHeaderNames[i]
        }
    }

    fun getProjectLayerFiles(): List<File>? {
        return projectLayerFiles
    }

    fun addProjectLayerFile(layerFile: File) {
        if (projectLayerFiles != null) {
            projectLayerFiles.add(layerFile)
            projectLayerCount += 1
        }
    }

    fun getPrintingPassDataList(): List<OnePrintingPassData>? {
        return printingPassDataList
    }

    fun addPrintingPassData(printingPassData: OnePrintingPassData?) {
        if (printingPassData != null && printingPassDataList != null) {
            printingPassData.printerTaskNumber = printingPassDataList.size
            printingPassDataList.add(printingPassData)
        }
    }

    var printerLayers: Array<PrinterLayer?>
        get() = mPrinterLayers
        set(printerLayers) {
            mPrinterLayers = printerLayers
        }

}
