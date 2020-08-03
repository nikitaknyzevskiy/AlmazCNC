package com.rokobit.almaz.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menu_print.isEnabled = false

        menu_scan.isEnabled = false

        menu_settings.isEnabled = false

        menu_active.isEnabled = false

        menu_motion.setOnClickListener {
            findNavController().navigate(
                R.id.action_menuFragment_to_motionFragment
            )
        }
    }

}