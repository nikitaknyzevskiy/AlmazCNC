package com.rokobit.almaz.screen.open_file

import java.io.Serializable

class PrinterLayer : Serializable {
    var layerPresetName: String? = null
    private var mLayerPresets: Array<LayerPreset?> = arrayOfNulls<LayerPreset>(5)
    var isLayerUploadedToPrinter = false

    var layerPresets: Array<LayerPreset?>
        get() = mLayerPresets
        set(layerPresets) {
            mLayerPresets = layerPresets
        }

    fun getLayerPreset(index: Int): LayerPreset? {
        return if (index > 4 || index < 0) {
            null
        } else mLayerPresets[index]
    }

    fun setLayerPreset(index: Int, layerPreset: LayerPreset?) {
        if (index > 4 || index < 0) {
            return
        }
        mLayerPresets[index] = layerPreset
    }

    init {
        for (i in 0..4) {
            mLayerPresets[i] = LayerPreset()
        }
    }
}
