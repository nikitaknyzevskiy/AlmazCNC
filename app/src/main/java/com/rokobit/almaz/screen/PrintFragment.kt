package com.rokobit.almaz.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rokobit.almaz.R
import com.rokobit.almaz.unit.BitmapUtils
import kotlinx.android.synthetic.main.fragment_print.*
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream


class PrintFragment : Fragment() {

    private val mViewModel: PrintViewModel by lazy {
        ViewModelProviders.of(requireActivity())
            .get(PrintViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_print, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        print_pause_btn.setOnClickListener {
            if (print_pause_btn.text.toString() == getString(R.string.pause)) {
                mViewModel.pausePrint()
                print_pause_btn.setText(R.string.continu)
            }
            else {
                mViewModel.continuePrint()
                print_pause_btn.setText(R.string.pause)
            }
        }

        print_cancel_btn.setOnClickListener {
            mViewModel.stopPrint()
            print_pause_btn.isEnabled = false
            print_cancel_btn.isEnabled = false
            print_print_btn.isEnabled = true
        }

        print_print_btn.setOnClickListener {
            print_pause_btn.isEnabled = true
            print_cancel_btn.isEnabled = true
            mViewModel.startPrint()
            print_print_btn.isEnabled = false
        }

        mViewModel.layerLiveData.observe(this.viewLifecycleOwner, Observer {
            val file = File(requireContext().cacheDir, it.layerFile)
            print_print_btn.isEnabled = true

            print_imageview.setImageURI(Uri.fromFile(file))

        })

    }



}