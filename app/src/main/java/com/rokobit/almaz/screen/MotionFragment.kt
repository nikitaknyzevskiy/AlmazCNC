package com.rokobit.almaz.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rokobit.almaz.R
import com.rokobit.almaz.body.CommandBody
import kotlinx.android.synthetic.main.fragment_motion.*
import java.lang.Exception

enum class MotionType {
    PLUS_Y,
    PLUS_X,
    MINUS_Y,
    MINUS_X
}

class MotionFragment : Fragment() {

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

        motion_plus_y.setOnClickListener {
            sendCommand("move_y_forward")
        }

        motion_plus_x.setOnClickListener {
            sendCommand("move_x_forward")
        }

        motion_minus_y.setOnClickListener {
            sendCommand("move_y_back")
        }

        motion_minus_x.setOnClickListener {
            sendCommand("move_x_back")
        }

        motion_stop.setOnClickListener {
            sendCommand("move_stop")
        }

        motion_load_status.setOnClickListener {
            mViewModel.loadMotorStatus()
        }

        mViewModel.motorStatus.observe(this.viewLifecycleOwner, Observer {
            motion_tvrun_txt.text = "TvRun = " + if (it == 0) "Free" else if (it > 0) "Busy" else "Error"
        })
    }

    private fun getDelay() : Long {
        return editTextNumber.text.toString().toLong()
    }

    private fun sendCommand(type: MotionType, event: MotionEvent) {

        mViewModel.timeOutRest = try {getDelay()} catch (e: Exception) {
            10L
        }

        val command = if (event.action == MotionEvent.ACTION_DOWN)
            CommandBody(
                when (type) {
                    MotionType.PLUS_Y -> "move_y_forward"
                    MotionType.PLUS_X -> "move_x_forward"
                    MotionType.MINUS_Y -> "move_y_back"
                    MotionType.MINUS_X -> "move_x_back"
                }
            )
        else if (event.action == MotionEvent.ACTION_UP)
            CommandBody("move_stop")
        else
            return

        mViewModel.sendCommand(command)
    }

    private fun sendCommand(action: String) {
        mViewModel.sendCommand(CommandBody(action))
    }

}