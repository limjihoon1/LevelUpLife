package com.limjihoon.myhero.activitis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.ActivitySignin2Binding
import com.limjihoon.myhero.databinding.ActivitySigninBinding

class SigninActivity2 : AppCompatActivity() {
    lateinit var binding:ActivitySignin2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignin2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.btn.setOnClickListener { btnnext() }

    }

    private fun btnnext() {
        var email =intent.getStringExtra("email")
        var password =intent.getStringExtra("password")
        var nickname = binding.layoutInputNickname.editText!!.text.toString()
        val user = hashMapOf(
            "email" to email,
            "password" to password,
            "nickname" to nickname,
            "char1" to "0",
            "char2" to "0",
            "char3" to "0",
            "char4" to "0",
            "char5" to "0",
            "char6" to "0",
            "char7" to "0",
            "char8" to "0",
            "char9" to "0",
            "char10" to "0",
            "char11" to "0",
            "charhiden" to "0"

        )

        Firebase.firestore.collection("users").document(nickname).set(user)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "회원가입 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        startActivity(Intent(this,MainActivity::class.java))
    }
}