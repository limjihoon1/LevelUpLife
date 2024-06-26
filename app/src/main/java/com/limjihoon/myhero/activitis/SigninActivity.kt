package com.limjihoon.myhero.activitis

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.data.Member
import com.limjihoon.myhero.databinding.ActivitySigninBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {
    lateinit var binding: ActivitySigninBinding
    private val auth = Firebase.auth
    private var hero = 0
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

        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvBack.setOnClickListener { onBackPressed() }

        setContentView(binding.root)
        binding.btn.setOnClickListener { signup() }

        binding.char1.setOnClickListener {
            if (hero != 1) {
                binding.char1.setImageResource(R.drawable.level_up_life_char1_re)
                hero = 1
                binding.char2.setImageResource(R.drawable.level_up_char4_bl)
            }
        }

        binding.char2.setOnClickListener {
            if (hero != 4) {
                binding.char2.setImageResource(R.drawable.level_up_char4)
                hero = 4
                binding.char1.setImageResource(R.drawable.level_up_life_char1_re_bl)
            }
        }
    }

    private fun signup() {
        val email = binding.inputLayoutLoginEmail.editText!!.text.toString()
        val password = binding.inputLayoutLoginPw.editText!!.text.toString()
        val passwordConfirm = binding.inputLayoutLoginPwCin.editText!!.text.toString()
        val nickname = binding.layoutInputNickname.editText!!.text.toString()

        var checkEmail = ""
        var checkNickname = ""

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getEmail(email).enqueue(object : Callback<Member> {
            override fun onResponse(p0: Call<Member>, p1: Response<Member>) {

                val data = p1.body()

                if (data != null) {
                    checkEmail = data.email
                }

                val retrofit2 = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                val retrofitService2 = retrofit2.create(RetrofitService::class.java)

                retrofitService2.getNickname(nickname).enqueue(object : Callback<Member> {
                    override fun onResponse(p0: Call<Member>, p1: Response<Member>) {

                        val data2 = p1.body()

                        if (data2 != null) {
                            checkNickname = data2.nickname
                        }

                        if (email.isEmpty()) {
                            Toast.makeText(this@SigninActivity, "이메일을 입력해주세요!!", Toast.LENGTH_SHORT).show()
                        } else if (email == checkEmail) {
                            Toast.makeText(this@SigninActivity, "중복된 이메일이 있습니다!!", Toast.LENGTH_SHORT).show()
                        } else if (password.isEmpty() && passwordConfirm.isEmpty()) {
                            Toast.makeText(this@SigninActivity,"비밀번호 또는 비밀번호 확인을 입력해주세요!!",Toast.LENGTH_SHORT).show()
                        } else if(password != passwordConfirm) {
                            Toast.makeText(this@SigninActivity, "비밀번호가 일치하지않습니다!!", Toast.LENGTH_SHORT).show()
                        } else if(password.length <6) {
                            Toast.makeText(this@SigninActivity, "비밀번호 7자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                        } else if (nickname.isEmpty()) {
                            Toast.makeText(this@SigninActivity, "닉네임을 입력해주세요!!", Toast.LENGTH_SHORT).show()
                        } else if (nickname == checkNickname) {
                            Toast.makeText(this@SigninActivity, "중복된 닉네임이 있습니다!!", Toast.LENGTH_SHORT).show()
                        } else if (nickname.length > 15) {
                            Toast.makeText(this@SigninActivity, "15자 이내로 닉네임을 설정해 주세요", Toast.LENGTH_SHORT).show()
                        } else if (hero == 0) {
                            Toast.makeText(this@SigninActivity, "캐릭터를 선택해주세요!!", Toast.LENGTH_SHORT).show()
                        }else{

                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                                if (it.isSuccessful) {
                                    val uid = auth.currentUser?.uid.toString()
                                    val member = Member(email, uid, nickname, hero)

                                    val retrofit3 =RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                                    val retrofitService3 = retrofit3.create(RetrofitService::class.java)

                                    retrofitService3.insertMember(member)
                                        .enqueue(object : Callback<String> {
                                            override fun onResponse(
                                                p0: Call<String>,
                                                p1: Response<String>
                                            ) {
                                                val data3 = p1.body()

                                                auth.signInWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener {

                                                        if (it.isSuccessful) {

                                                            spfEdit.putBoolean("isLogin", true)
                                                            spfEdit2.putString("uid", uid)
                                                            spfEdit2.putString("nickname", nickname)
                                                            spfEdit.apply()
                                                            spfEdit2.apply()
                                                            G.uid = uid
                                                            G.nickname = nickname

                                                            startActivity(Intent(this@SigninActivity,MainActivity::class.java))
                                                            finish()
                                                            Toast.makeText(this@SigninActivity,"$data3",Toast.LENGTH_SHORT).show()
                                                            Log.d("good", "로그인 성공")
                                                        } else {Toast.makeText(this@SigninActivity,"로그인 실패",Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                            }

                                            override fun onFailure(p0: Call<String>, p1: Throwable) {
                                                Log.d("error", "${p1.message}")
                                            }

                                        })

                                } else {
                                    Toast.makeText(this@SigninActivity, "회원가입 실패 : 이메일 형식이 맞는지 확인해 주세요", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                        }
                    }

                    override fun onFailure(p0: Call<Member>, p1: Throwable) {

                    }

                })

            }

            override fun onFailure(p0: Call<Member>, p1: Throwable) {
                Log.d("err", "${p1.message}")
            }

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_bottom_enter_start, R.anim.from_top_enter_exit)
    }

}