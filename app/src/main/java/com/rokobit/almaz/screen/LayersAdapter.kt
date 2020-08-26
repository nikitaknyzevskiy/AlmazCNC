package com.rokobit.almaz.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rokobit.almaz.R
import com.rokobit.almaz.body.model.LayerPresetModel
import java.io.File

class LayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        val layoutID = R.layout.viewholder_layer
    }

    private val pixelTxt: TextView = itemView.findViewById(R.id.layer_pixel)
    private val speedTxt: TextView = itemView.findViewById(R.id.layer_speed)
    private val blackTxt: TextView = itemView.findViewById(R.id.layer_black)
    private val whiteTxt: TextView = itemView.findViewById(R.id.layer_white)
    private val deleteBtn: ImageView = itemView.findViewById(R.id.layer_delete)
    private val imageView: ImageView = itemView.findViewById(R.id.layer_image)

    @SuppressLint("SetTextI18n")
    fun bind(layerPresetModel: LayerPresetModel, onDelete: () -> Unit) {
        pixelTxt.text = itemView.context.getString(R.string.pixel) + ": " + layerPresetModel.pictureDensity.toString()
        speedTxt.text = itemView.context.getString(R.string.speed) + ": " + layerPresetModel.speed.toString()
        blackTxt.text = itemView.context.getString(R.string.black) + ": " + layerPresetModel.black.toString()
        whiteTxt.text = itemView.context.getString(R.string.white) + ": " + layerPresetModel.white.toString()
        deleteBtn.setOnClickListener {
            onDelete.invoke()
        }
        imageView.setImageURI(Uri.fromFile(File(itemView.context.cacheDir, layerPresetModel.layerFile)))
    }

}

class LayersAdapter : RecyclerView.Adapter<LayerViewHolder>() {

    var onClick: (Int) -> Unit = {}
    var onDelete: (Int) -> Unit = {}

    private var layers: Array<LayerPresetModel> = arrayOf()

    fun submit(layers: Array<LayerPresetModel>) {
        this.layers = layers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(LayerViewHolder.layoutID, parent, false)
        return LayerViewHolder(itemView)
    }

    override fun getItemCount(): Int = layers.size

    override fun onBindViewHolder(holder: LayerViewHolder, position: Int) {
        holder.bind(layers[position]) {
            onDelete.invoke(position)
        }
        holder.itemView.setOnClickListener {
            onClick.invoke(position)
        }
    }

}