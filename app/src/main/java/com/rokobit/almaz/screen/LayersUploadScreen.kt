package com.rokobit.almaz.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_layers_upload.*

class LayersUploadScreen : Fragment() {

    private val mViewModel: PrintViewModel by lazy {
        ViewModelProviders.of(requireActivity())
            .get(PrintViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layers_upload, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startUpload()
    }

    private fun startUpload() {
        layersupload_pb_desc.text = getString(R.string.uploading_image) + " #1"
        mViewModel.uploadImages(requireContext()).observe(this.viewLifecycleOwner, Observer {
            layersupload_pb.progress = it
            layersupload_pb_desc.text = getString(R.string.uploading_image) + " #${it+1}"
            if (it == mViewModel.layerLiveData.value?.size?:0) {
                sendLayer()
            }
        })
    }

    private fun sendLayer() {
        layersupload_pb_desc.text = getString(R.string.uploading_layers)
        mViewModel.uploadLayers().observe(this.viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_layersUploadScreen_to_printFragment)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.layerLiveData.observe(this.viewLifecycleOwner, Observer {
            layersupload_pb.max = it.size
        })
    }

}