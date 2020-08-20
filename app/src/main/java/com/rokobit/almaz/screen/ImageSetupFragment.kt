package com.rokobit.almaz.screen

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.rokobit.almaz.R
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_image_setup.*
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.net.URI


class ImageSetupFragment : Fragment() {

    private val PICK_PHOTO_FOR_AVATAR = 1

    private val mViewModel: PrintViewModel by lazy {
        ViewModelProviders.of(requireActivity())
            .get(PrintViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_setup, container, false)
    }

    private var bitmatMap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagesetup_chnage_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR)
        }

        imagesetup_next_btn.setOnClickListener {
            progressBar2.visibility = View.VISIBLE
            imagesetup_next_btn.isEnabled = false

            mViewModel.addLayer(requireContext(), bitmatMap?:return@setOnClickListener, 5, 114, 50).observe(
                this.viewLifecycleOwner,
                Observer {

                    if (it) {
                        progressBar2.visibility = View.INVISIBLE

                        findNavController().navigate(R.id.action_imageSetupFragment_to_printFragment)
                    }

                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO_FOR_AVATAR) {
            createImage(data?.data?:return)
        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data?:return)?:return
            crateBitmap(requireContext().contentResolver.openInputStream(resultUri)?:return)
        }
    }

    private fun createImage(imageURI: Uri) {
        val file = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")

        UCrop.of(imageURI, Uri.fromFile(file))
            .withAspectRatio(3f, 2f)
            .withMaxResultSize(416, 270)
            .start(requireContext(), this)
    }

    private fun crateBitmap(inputStream: InputStream) {
        val bufferedInputStream = BufferedInputStream(inputStream)
        val bmp = BitmapFactory.decodeStream(bufferedInputStream)

        imagestup_imageview.setImageBitmap(bmp)
        imagesetup_chnage_image.visibility = View.INVISIBLE

        imagesetup_next_btn.isEnabled = true

        bitmatMap = bmp
    }

}