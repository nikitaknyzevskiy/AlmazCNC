package com.rokobit.almaz.screen

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: Fragment() {

    private val viewModel: OpenImageViewModel by viewModels()
    private val args: MainFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagePath = args.path

        if (imagePath.contains("psd")) {
            iv.setImageBitmap(viewModel.decodePSDtoBitmap(imagePath))
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(args.path))
        }

    }

}