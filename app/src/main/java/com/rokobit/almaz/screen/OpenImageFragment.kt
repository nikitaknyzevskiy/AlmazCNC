package com.rokobit.almaz.screen

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_open_image.*

class OpenImageFragment : Fragment() {

    private val viewModel: OpenImageViewModel by viewModels()
    private lateinit var rvAdapter: PreviouslyOpenedImageAdapter

    companion object {
        private const val GALLERY_REQUEST = 888
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_open_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_arrow_back.setOnClickListener { findNavController().popBackStack() }

        setPreviouslyOpenedImageList()

        button_select_new_image.setOnClickListener {
            openGallery()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onImageSelected(data)
    }

    private fun setPreviouslyOpenedImageList() {
        rvAdapter = PreviouslyOpenedImageAdapter(requireContext())
        rvAdapter.setItemClickListener { position ->
            findNavController().navigate(
                OpenImageFragmentDirections.actionOpenFileFragmentToMainFragment(
                    viewModel.previouslyOpenedImageList.value?.get(position)?.path ?: ""
                )
            )
        }

        rv_previously_opened.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = rvAdapter
        }

        viewModel.previouslyOpenedImageList.observe(viewLifecycleOwner, Observer {
            rvAdapter.updateData(it)
        })

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    private fun onImageSelected(data: Intent?) {
        if (data != null) {
            val selectedImage = data.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = requireContext().contentResolver.query(
                selectedImage!!,
                filePathColumn, null, null, null
            )
            cursor?.moveToFirst()
            val columnIndex: Int = cursor?.getColumnIndex(filePathColumn[0]) ?: 0
            val picturePath: String = cursor?.getString(columnIndex) ?: ""
            cursor?.close()
            viewModel.saveImage(picturePath)
            findNavController().navigate(
                OpenImageFragmentDirections.actionOpenFileFragmentToMainFragment(
                    picturePath
                )
            )
        } else {
            Log.e("OpenFileFragment", "Gallery request error")
        }
    }

}