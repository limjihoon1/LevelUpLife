package com.limjihoon.myhero.activitis

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnLogIn.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
            overridePendingTransition(R.anim.from_bottom_enter_xml, R.anim.from_top_enter_xml)

        }

        binding.btnSignIn.setOnClickListener {

            startActivity(Intent(this, RLoginActivity::class.java))
            overridePendingTransition(R.anim.from_bottom_enter_xml, R.anim.from_top_enter_xml)
        }
        binding.btn.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
    }
}