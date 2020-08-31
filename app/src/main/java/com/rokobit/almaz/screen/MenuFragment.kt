package com.rokobit.almaz.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.rokobit.almaz.R
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment() {

    private val mViewModel: MenuViewModel by lazy {
        ViewModelProviders.of(requireActivity())
            .get(MenuViewModel::class.java)
    }

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

        menu_active.isEnabled = true

        menu_motion.isEnabled = false

        menu_open_psd_to_layouts.isEnabled = false

        menu_motion.setOnClickListener {
            findNavController().navigate(
                R.id.action_menuFragment_to_motionFragment
            )
        }

        menu_open_psd_to_layouts.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_fragmentOpenFile)
        }

        menu_print.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_layersListFragment)
        }

        menu_active.setOnClickListener {
            mViewModel.connectPrinter(requireContext())
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.printerConnection(requireContext()).observe(this.viewLifecycleOwner, Observer {

            menu_print.isEnabled = it
            menu_motion.isEnabled = it
            menu_active.isEnabled = !it

        })
    }

}