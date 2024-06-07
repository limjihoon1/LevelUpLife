package com.limjihoon.myhero.activitis

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    lateinit var binding: ActivitySigninBinding
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        supportActionBar?.hide()

        binding= ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvBack.setOnClickListener { onBackPressed() }
        binding.btnNext.setOnClickListener { click() }


    }

     override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition( R.anim.from_bottom_enter_start,R.anim.from_top_enter_exit
        )
    }
    private fun click(){
        var intent=Intent(this,SigninActivity2::class.java)
        email = binding.inputLayoutLoginId.editText!!.text.toString()
        password = binding.inputLayoutLoginPw.editText!!.text.toString()
        passwordConfirm = binding.inputLayoutLoginPwCin.editText!!.text.toString()
        val id =Firebase.firestore.collection("users").whereEqualTo("email",email).get()
        if (id.toString()==email){
            Toast.makeText(this, "중복된 이메일이 있습니다", Toast.LENGTH_SHORT).show()
        }else {
            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "이메일 또는 비밀번호를 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
            } else if (password != passwordConfirm) {
                AlertDialog.Builder(this).setMessage("비밀번호가 같지 않습니다 다시 입력해주세요.").create().show()
                binding.inputLayoutLoginPwCin.requestFocus()
                return
            } else {
                intent.putExtra("email", email)
                intent.putExtra("password", password)

                startActivity(intent)

            }
        }

    }
}