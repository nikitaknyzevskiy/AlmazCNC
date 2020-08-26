package com.rokobit.almaz.screen

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.rokobit.almaz.R
import com.rokobit.almaz.body.model.LayerPresetModel
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_image_setup.*
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream

class ImageSetupFragment : Fragment() {

    private val PICK_PHOTO_FOR_AVATAR = 1

    private val mViewModel: PrintViewModel by lazy {
        ViewModelProviders.of(requireActivity())
            .get(PrintViewModel::class.java)
    }

    private var bitmatMap: Bitmap? = null
        set(value) {
            field = value
            imagestup_imageview.setImageBitmap(field)
        }

    private val layerIndex by lazy {
        arguments?.getInt("layer_index")?:0
    }

    private var pixel = 20
        set(value) {
            if (value > 60 || value < 20)
                return
            field = value
            imagesetup_pixel.text = field.toString()
        }
    private var speed = 1
        @SuppressLint("SetTextI18n")
        set(value) {
            field = value
            imagestup_speed.text = getString(R.string.speed) + ": " + field.toString()
        }
    private var black = 0
        @SuppressLint("SetTextI18n")
        set(value) {
            field = value
            imagestup_black.text = getString(R.string.black) + ": " + (field * (255 / 10)).toString()
        }
    private var white = 0
        @SuppressLint("SetTextI18n")
        set(value) {
            field = value
            imagestup_white.text = getString(R.string.white) + ": " + (field * (255 / 10)).toString()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_setup, container, false)
    }

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

            mViewModel.addLayer(requireContext(), layerIndex, bitmatMap?:return@setOnClickListener, pixel, speed, black, white).observe(
                this.viewLifecycleOwner,
                Observer {

                    if (it) {
                        mViewModel.layerLiveData.removeObserver(observer)
                        progressBar2.visibility = View.INVISIBLE

                        findNavController().popBackStack()
                    }

                }
            )
        }

        imagesetup_pixel_minus_btn.setOnClickListener {
            pixel--
        }
        imagesetup_pixel_plus_btn.setOnClickListener {
            pixel++
        }

        imagestup_speed_seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speed = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        imagestup_black_seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                black = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        imagestup_white_seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                white = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        speed = imagestup_speed_seekBar.progress
        black = imagestup_black_seekBar.progress
        white = imagestup_white_seekBar.progress

        mViewModel.layerLiveData.observe(this.viewLifecycleOwner, observer)
    }

    private val observer = Observer<LinkedHashMap<Int, LayerPresetModel>> {
        val layer = it[layerIndex]?:return@Observer

        pixel = layer.pictureDensity.toInt()

        imagestup_speed_seekBar.progress = layer.speed.toInt()

        imagestup_black_seekBar.progress = layer.black.toInt()

        imagestup_white_seekBar.progress = layer.white.toInt()

        imagesetup_next_btn.isEnabled = true

        crateBitmap(File(requireContext().cacheDir, layer.layerFile).inputStream())
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
            //.withAspectRatio(3f, 2f)
            //.withMaxResultSize(416, 270)
            .start(requireContext(), this)
    }

    private fun crateBitmap(inputStream: InputStream) {
        val bufferedInputStream = BufferedInputStream(inputStream)
        val bmp = BitmapFactory.decodeStream(bufferedInputStream)

        imagesetup_next_btn.isEnabled = true

        bitmatMap = bmp
    }

}