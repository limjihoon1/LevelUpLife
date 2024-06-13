package com.limjihoon.myhero.activitis

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
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
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String
    private val auth = Firebase.auth
    private var hero = 0

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
        binding.btnNext.setOnClickListener { next() }
        setContentView(binding.root)
        binding.btn.setOnClickListener { clickBtn() }

        binding.char1.setOnClickListener {
            if (hero != 1) {
                binding.char1.setImageResource(R.drawable.level_up_char1)
                hero = 1
                binding.char2.setImageResource(R.drawable.level_up_char4_bl)
            }
        }

        binding.char2.setOnClickListener {
            if (hero != 4) {
                binding.char2.setImageResource(R.drawable.level_up_char4)
                hero = 4
                binding.char1.setImageResource(R.drawable.level_up_char1_bl)
            }
        }
    }


    private fun clickBtn() {
        val email = intent.getStringExtra("email").toString()
        val password = intent.getStringExtra("password").toString()
        val nickname = binding.layoutInputNickname.editText!!.text.toString()

        if (nickname.isEmpty()) {
            Toast.makeText(this, "닉네임을 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
        } else if (hero == 0) {
            Toast.makeText(this, "캐릭터를 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
        } else {

            val uid = "asdfghjklzzxcvbnmqwer1"
            val member = Member(email, uid, nickname, hero)

            val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
            val retrofitService = retrofit.create(RetrofitService::class.java)

            retrofitService.insertMember(member).enqueue(object : Callback<String> {
                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                    val data = p1.body()

                    AlertDialog.Builder(this@SigninActivity).setMessage("$data").create()
                        .show()

//                    startActivity(Intent(this@SigninActivity2, MainActivity::class.java))
//                    finish()
//                    Toast.makeText(this@SigninActivity2, "$data", Toast.LENGTH_SHORT).show()
                    Log.d("good", "로그인 성공")

                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {
                    Log.d("error", "${p1.message}")
                }

            })

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_bottom_enter_start, R.anim.from_top_enter_exit)
    }

    private fun click() {
        var intent = Intent(this, SigninActivity2::class.java)
        email = binding.inputLayoutLoginId.editText!!.text.toString()
        password = binding.inputLayoutLoginPw.editText!!.text.toString()
        passwordConfirm = binding.inputLayoutLoginPwCin.editText!!.text.toString()
        val id = Firebase.firestore.collection("users").whereEqualTo("email", email).get()
        if (id.toString() == email) {
            Toast.makeText(this, "중복된 이메일이 있습니다", Toast.LENGTH_SHORT).show()
        } else {
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

    private fun next() {
        var intent = Intent(this, SigninActivity2::class.java)
        email = binding.inputLayoutLoginId.editText!!.text.toString()
        password = binding.inputLayoutLoginPw.editText!!.text.toString()
        passwordConfirm = binding.inputLayoutLoginPwCin.editText!!.text.toString()
        var checkEmail = ""

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getEmail(email).enqueue(object : Callback<Member> {
            override fun onResponse(p0: Call<Member>, p1: Response<Member>) {
                val data = p1.body()

                if (data != null) {
                    checkEmail = data.email
                }

                if (email == checkEmail) {
                    Toast.makeText(this@SigninActivity, "중복된 이메일이 있습니다", Toast.LENGTH_SHORT)
                        .show()
                } else if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    Toast.makeText(
                        this@SigninActivity,
                        "이메일 또는 비밀번호를 입력하지 않으셨습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != passwordConfirm) {
                    AlertDialog.Builder(this@SigninActivity)
                        .setMessage("비밀번호가 같지 않습니다 다시 입력해주세요.").create().show()
                    binding.inputLayoutLoginPwCin.requestFocus()
                    return
                } else {
                    intent.putExtra("email", email)
                    intent.putExtra("password", password)
                    startActivity(intent)

                }

//                AlertDialog.Builder(this@SigninActivity).setMessage("$data").create().show()
            }

            override fun onFailure(p0: Call<Member>, p1: Throwable) {
                Log.d("err", "${p1.message}")
            }

        })

//        retrofitService.getEmail(email).enqueue(object : Callback<String> {
//            override fun onResponse(p0: Call<String>, p1: Response<String>) {
//                val data = p1.body()
//
//                AlertDialog.Builder(this@SigninActivity).setMessage("$data").create().show()
//            }
//
//            override fun onFailure(p0: Call<String>, p1: Throwable) {
//                Log.d("err", "${p1}")
//            }
//
//        })


    }
}