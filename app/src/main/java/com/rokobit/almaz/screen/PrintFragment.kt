package com.rokobit.almaz.screen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_print.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class PrintFragment : Fragment() {

    private val mViewModel: PrintViewModel by lazy {
        ViewModelProviders.of(this)
            .get(PrintViewModel::class.java)
    }

    private val file by lazy {
        File(requireContext().cacheDir, "example_print.jpg")
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

        progressBar.visibility = View.INVISIBLE

        loadExampleImage()

        print_start_btn.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            mViewModel.uploadImage(file).observe(this.viewLifecycleOwner, uploadImageObs)
        }

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

            print_start_btn.isEnabled = true
            print_pause_btn.isEnabled = false
            print_cancel_btn.isEnabled = false
        }

    }

    private val uploadImageObs = Observer<Boolean> {
        progressBar.visibility = View.INVISIBLE
        print_start_btn.isEnabled = !it
        print_pause_btn.isEnabled = it
        print_cancel_btn.isEnabled = it


        mViewModel.startPrint()
    }

    private fun loadExampleImage() {
        print_start_btn.isEnabled = false

        val openRawResource = resources.openRawResource(R.raw.example_print)

        copyStreamToFile(openRawResource, file)

        print_imageview.setImageURI(Uri.fromFile(file))

        print_start_btn.isEnabled = true
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

}