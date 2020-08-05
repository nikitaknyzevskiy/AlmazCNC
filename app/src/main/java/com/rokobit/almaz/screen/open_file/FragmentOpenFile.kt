package com.rokobit.almaz.screen.open_file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_open_file.*
import java.io.File

class FragmentOpenFile: Fragment(),
    OpenFileAdapter.OpenFileAdapterListener, FragmentOpenFileInterface, View.OnClickListener {

    private lateinit var openFileAdapter: OpenFileAdapter
    private var sImageData: ImageData? = null
    private var mFileName // це ім'я файлу яке ми виділяємо якщо файл вже було відкрито і ми повернулися на екран вибору файлу
            : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_open_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_open_file_arrow_back_icon.setOnClickListener(this)
        sImageData = ImageData.getInstance()
        mFileName = sImageData?.selectedFileNameToOpen
        openFileAdapter = OpenFileAdapter()
        openFileAdapter.setFileNameToHighlight(mFileName)
        openFileAdapter.setClickListener(this)
    }

    override fun onAdapterItemClick(file: File?, fileName: String?) {
    }

    override fun onAdapterItemLongClick(file: File?, fileName: String?) {
    }

    override fun setFileNameToBeHighlightedInAdapter(fileName: String) {
    }

    override fun updateAdapter() {
    }

    override fun onClick(v: View?) {
    }


}