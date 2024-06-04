package com.limjihoon.myhero.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(){
    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)
        binding.btn.setOnClickListener { rendum() }
        return binding.root
    }
    private fun rendum(){


    }

}