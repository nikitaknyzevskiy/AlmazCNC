package com.rokobit.almaz.screen.open_file

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.rokobit.almaz.R
import java.io.File
import java.util.*

class OpenFileAdapter : RecyclerView.Adapter<OpenFileAdapter.ItemViewHolder>() {
    private var ADD_FILE // = "додати файл";
            : String? = null

    interface OpenFileAdapterListener {
        fun onAdapterItemClick(file: File?, fileName: String?)
        fun onAdapterItemLongClick(file: File?, fileName: String?)
    }

    private var mAdapterViewClickListener: OpenFileAdapterListener? = null
    private val filesList =
        arrayListOf<File?>()
    private val fileNames = arrayListOf<String?>()
    private var fileName: String? = null

    fun setClickListener(listener: OpenFileAdapterListener?) {
        mAdapterViewClickListener = listener
    }

    fun setData(
        items: List<File?>,
        names: List<String?>,
        fileName: String?
    ) {
        filesList.clear()
        fileNames.clear()
        filesList.addAll(items)
        fileNames.addAll(names)
        ADD_FILE = fileName
        notifyDataSetChanged()
    }

    fun setFileNameToHighlight(fileName: String?) {
        this.fileName = fileName
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_open_file, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        @NonNull holder: ItemViewHolder,
        position: Int
    ) {
        holder.bindItem(position)
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, OnLongClickListener {
        private val textViewFileName: TextView = itemView.findViewById(R.id.item_open_file_text_view)
        private val textViewFileSize: TextView = itemView.findViewById(R.id.item_open_file_file_size_text_view)
        private val itemOpenFileView: View = itemView.findViewById(R.id.item_open_file_background_view)
        private fun setViewVisibility() {
            textViewFileSize.visibility =
                if (adapterPosition < fileNames.size - 1) View.VISIBLE else View.GONE
        }

        init {
            itemOpenFileView.setOnClickListener(this)
            itemOpenFileView.setOnLongClickListener(this)
        }

        fun bindItem(position: Int) {
            textViewFileName.text = filesList[position]?.name
            setViewVisibility()
            textViewFileSize.text = fileNames[position]
            itemOpenFileView.isSelected = filesList[position]?.name == fileName
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            mAdapterViewClickListener!!.onAdapterItemClick(filesList[position], fileName)
        }

        override fun onLongClick(v: View): Boolean {
            val position = adapterPosition
            if (filesList[position]?.name?.toLowerCase(Locale.ROOT) == ADD_FILE!!.toLowerCase(Locale.ROOT)) {
                return true
            }
            mAdapterViewClickListener!!.onAdapterItemLongClick(filesList[position], fileName)
            return true
        }

    }
}
