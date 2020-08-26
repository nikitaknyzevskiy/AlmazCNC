package com.rokobit.almaz.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_layers_list.*
import moe.xing.rxfilepicker.RxGetFile

class LayersListFragment : Fragment() {

    private val mViewModel: PrintViewModel by lazy {
        ViewModelProviders.of(requireActivity())
            .get(PrintViewModel::class.java)
    }

    private val adapter = LayersAdapter().apply {
        onClick = {position ->
            findNavController().navigate(R.id.action_layersListFragment_to_imageSetupFragment, Bundle().apply {
                putInt("layer_index", position)
            })
        }
        onDelete = {position ->
            val value = mViewModel.layerLiveData.value
            value?.remove(position)
            mViewModel.layerLiveData.postValue(value)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layers_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layerslist_list.apply {
            layoutManager =
                LinearLayoutManager(requireContext()).apply { orientation = RecyclerView.VERTICAL }
            adapter = this@LayersListFragment.adapter
        }

        layerslist_add_btn.setOnClickListener {
            findNavController().navigate(R.id.action_layersListFragment_to_imageSetupFragment, Bundle().apply {
                putInt("layer_index", mViewModel.layerLiveData.value?.size?:0)
            })
        }

        layerslist_next_btn.setOnClickListener {
            mViewModel.uploadLayers().observe(this.viewLifecycleOwner, Observer {
                findNavController().navigate(R.id.action_layersListFragment_to_printFragment)
            })
        }

        layerslist_add_psd_btn.setOnClickListener {
            RxGetFile.newBuilder().isSingle(true).build()
                .subscribe {file ->
                    if (!file.name.contains(".psd"))
                        return@subscribe

                    it.visibility = View.INVISIBLE
                    layerslist_psd_progressBar.visibility = View.VISIBLE

                    mViewModel.decodePsdToLayers(requireContext(), file)
                }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.layerLiveData.observe(this.viewLifecycleOwner, Observer {
            adapter.submit(it.values.toTypedArray())
            layerslist_next_btn.isEnabled = it.isNotEmpty()
            layerslist_add_btn.isEnabled = it.size < 5
        })
    }

}