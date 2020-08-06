package com.rokobit.almaz.screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rokobit.almaz.R
import com.rokobit.almaz.local_data.ImageEntity

class PreviouslyOpenedImageAdapter(private val context: Context) : RecyclerView.Adapter<PreviouslyOpenedImageAdapter.OpenFileViewHolder>() {

    private var imageList = arrayListOf<ImageEntity>()

    private lateinit var itemClickListener: (Int) -> Unit

    fun setItemClickListener(clickListener: (Int) -> Unit) {
        this.itemClickListener = clickListener
    }

    inner class OpenFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvImageName: TextView = itemView.findViewById(R.id.tv_file_name)
        val tvImageSize: TextView = itemView.findViewById(R.id.tv_file_size)
        val ivOpenArrow: ImageView = itemView.findViewById(R.id.iv_open_arrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenFileViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_open_image, parent, false)
        return OpenFileViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: OpenFileViewHolder, position: Int) {

        holder.ivOpenArrow.setOnClickListener {
            if (::itemClickListener.isInitialized) {
                itemClickListener(position)
            }
        }

        holder.tvImageName.text = imageList[position].path
        holder.tvImageSize.text = imageList[position].imageId.toString()

    }

    fun updateData(newImageList: List<ImageEntity>) {
        imageList.clear()
        imageList.addAll(newImageList)
        notifyDataSetChanged()
    }

}