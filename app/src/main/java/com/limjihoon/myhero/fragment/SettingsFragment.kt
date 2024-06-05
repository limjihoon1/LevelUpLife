package com.limjihoon.myhero.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.FragmentSettingBinding

class SettingsFragment : Fragment(){
    lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
        binding.changeImage.setOnClickListener { rendum() }
        return binding.root
    }
    private fun rendum(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_select_char, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }
}