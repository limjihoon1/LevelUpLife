package com.limjihoon.myhero.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
import com.limjihoon.myhero.activitis.IntroActivity
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
        return binding.root

    }

}