package com.limjihoon.myhero.activitis

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.databinding.ActivityRloginBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RLoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityRloginBinding
    private val auth = Firebase.auth
    private val spf by lazy { getSharedPreferences("loginSave", MODE_PRIVATE) }
    private val spf2 by lazy { getSharedPreferences("userInfo", MODE_PRIVATE) }
    private val spfEdit by lazy { spf.edit() }
    private val spfEdit2 by lazy { spf2.edit() }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        supportActionBar?.hide()

        binding = ActivityRloginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvBack.setOnClickListener { onBackPressed() }
        binding.btnNext.setOnClickListener { login() }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_bottom_enter_start, R.anim.from_top_enter_exit)
    }

    private fun login() {

        val email = binding.inputLayoutLoginId.editText!!.text.toString()
        val password = binding.inputLayoutLoginPw.editText!!.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요!!", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요!!", Toast.LENGTH_SHORT).show()
        } else {

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

                if (it.isSuccessful) {

                    val uid = auth.currentUser?.uid.toString()

                    val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                    val retrofitService = retrofit.create(RetrofitService::class.java)

                    retrofitService.getMember(uid).enqueue(object :
                        Callback<Member2> {
                        override fun onResponse(call: Call<Member2>, response: Response<Member2>) {
                            val data = response.body()

                            spfEdit.putBoolean("isLogin", true)
                            spfEdit2.putString("uid", data?.uid)
                            spfEdit2.putString("nickname", data?.nickname)
                            spfEdit.apply()
                            spfEdit2.apply()
                            G.uid = data?.uid.toString()
                            G.nickname = data?.nickname.toString()

                            startActivity(Intent(this@RLoginActivity, MainActivity::class.java))
                            finish()
                            Toast.makeText(this@RLoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                        }

                        override fun onFailure(call: Call<Member2>, t: Throwable) {
                            Log.d("error", "${t.message}")
                        }
                    })

                } else {
                    Toast.makeText(this, "이메일 또는 비밀번호가 일치하지않습니다", Toast.LENGTH_SHORT).show()
                    Log.d("loginErr", "로그인 오류")
                }
            }
            
        }


    }

}