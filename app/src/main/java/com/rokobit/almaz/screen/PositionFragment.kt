package com.rokobit.almaz.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_motion.*

class PositionFragment: Fragment(), Gamepad.OnButtonTouchListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_position, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamepad.setOnButtonTouchListener(this)
    }

    override fun onActionDown(motionType: MotionType) {
    }

    override fun onActionUp(motionType: MotionType) {
    }
}