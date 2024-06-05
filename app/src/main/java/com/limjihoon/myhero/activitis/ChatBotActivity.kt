package com.limjihoon.myhero.activitis

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.ActivityChatBotBinding

class ChatBotActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Toast.makeText(this, "안농 나는 쳇봇 영승리 라고해 ", Toast.LENGTH_SHORT).show()

//

    }
}