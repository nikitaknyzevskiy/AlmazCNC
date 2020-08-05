package com.rokobit.almaz.screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rokobit.almaz.R
import com.rokobit.almaz.body.CommandBody
import kotlinx.android.synthetic.main.fragment_motion.*
import kotlin.math.min

class MotionFragment : Fragment(), Gamepad.OnButtonTouchListener {

    private val mViewModel: MotionViewModel by lazy {
        ViewModelProviders.of(this)
            .get(MotionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_motion, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*motion_plus_y.setOnTouchListener { _, event ->
            sendCommand(MotionType.PLUS_Y, event)
            return@setOnTouchListener true
        }

        motion_plus_x.setOnTouchListener { _, event ->
            sendCommand(MotionType.PLUS_X, event)
            return@setOnTouchListener true
        }

        motion_minus_y.setOnTouchListener { _, event ->
            sendCommand(MotionType.MINUS_Y, event)
            return@setOnTouchListener true
        }

        motion_minus_x.setOnTouchListener { _, event ->
            sendCommand(MotionType.MINUS_X, event)
            return@setOnTouchListener true
        }*/

        gamepad.setOnButtonTouchListener(this)

        motion_load_status.setOnClickListener {
            mViewModel.loadMotorStatus()
        }

        mViewModel.motorStatus.observe(this.viewLifecycleOwner, Observer {
            motion_tvrun_txt.text =
                "TvRun = " + if (it == 0) "Free" else if (it > 0) "Busy" else "Error"
        })
    }

    private fun getDelay(): Long {
        return editTextNumber.text.toString().toLong()
    }

    private fun sendCommand(action: String) {
        mViewModel.sendCommand(CommandBody(action))
    }

    override fun onActionDown(motionType: MotionType) {
        when (motionType) {
            MotionType.PLUS_Y -> sendCommand("move_y_forward")
            MotionType.PLUS_X -> sendCommand("move_x_forward")
            MotionType.MINUS_Y -> sendCommand("move_y_back")
            MotionType.MINUS_X -> sendCommand("move_x_back")
        }
    }

    override fun onActionUp(motionType: MotionType) {
        sendCommand("move_stop")
    }

}
