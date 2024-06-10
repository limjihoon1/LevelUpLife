package com.limjihoon.myhero.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
import com.limjihoon.myhero.activitis.IntroActivity
import com.limjihoon.myhero.activitis.MapActivity
import com.limjihoon.myhero.databinding.ActivityIntroBinding
import com.limjihoon.myhero.databinding.FragmentHomeBinding
import com.limjihoon.myhero.databinding.FragmentSearchBinding

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        binding.fabtn.setOnClickListener { startActivity(Intent(requireContext(),ChatBotActivity::class.java)) }
        binding.creatTodo.setOnClickListener { listCreate() }
        binding.createMap.setOnClickListener { startActivity(Intent(requireContext(),MapActivity::class.java)) }

        return binding.root

    }
    private fun listCreate(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_input_todo, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

}