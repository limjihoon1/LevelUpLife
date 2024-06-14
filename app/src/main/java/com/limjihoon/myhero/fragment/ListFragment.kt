package com.limjihoon.myhero.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.databinding.FragmentNotificationsBinding

class ListFragment : Fragment(){
    lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.wv.loadUrl("http://myhero.dothome.co.kr/levelUpLife")

        binding.wv.settings.javaScriptEnabled = true
        binding.wv.settings.builtInZoomControls = true
        binding.wv.settings.displayZoomControls = false
        binding.wv.settings.allowFileAccess = true

        binding.wv.webViewClient = WebViewClient()

        val ma = activity as MainActivity
        ma.member ?: return
        
    }

}