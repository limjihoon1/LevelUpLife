package com.limjihoon.myhero.activitis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.limjihoon.myhero.R
import com.limjihoon.myhero.data.Member
import com.limjihoon.myhero.databinding.ActivitySignin2Binding
import com.limjihoon.myhero.databinding.ActivitySigninBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity2 : AppCompatActivity() {
    lateinit var binding: ActivitySignin2Binding
    private val auth = Firebase.auth
    private var hero = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignin2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

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

                    AlertDialog.Builder(this@SigninActivity2).setMessage("$data").create().show()

//                    startActivity(Intent(this@SigninActivity2, MainActivity::class.java))
//                    finish()
//                    Toast.makeText(this@SigninActivity2, "$data", Toast.LENGTH_SHORT).show()
                    Log.d("good", "로그인 성공")

                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {
                    Log.d("error", "${p1.message}")
                }

            })

//            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
//
//                if (it.isSuccessful) {
//                    val uid = auth.currentUser?.uid.toString()
//                    val member = Member(email, uid, nickname, hero)
//
//                    val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
//                    val retrofitService = retrofit.create(RetrofitService::class.java)
//
//                    retrofitService.insertMember(member).enqueue(object : Callback<String> {
//                        override fun onResponse(p0: Call<String>, p1: Response<String>) {
//                            val data = p1.body()
//
//                            AlertDialog.Builder(this@SigninActivity2).setMessage("$data").create().show()
//
//                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
//
//                                if (it.isSuccessful) {
//                                    startActivity(Intent(this@SigninActivity2, MainActivity::class.java))
//                                    finish()
//                                    Toast.makeText(this@SigninActivity2, "$data", Toast.LENGTH_SHORT).show()
//                                    Log.d("good", "로그인 성공")
//                                } else {
//                                    Toast.makeText(this@SigninActivity2, "로그인 실패", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        }
//
//                        override fun onFailure(p0: Call<String>, p1: Throwable) {
//                            Log.d("error", "${p1.message}")
//                        }
//
//                    })
//
//                } else {
//                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
        }

    }

    private fun btnnext() {
        var email = intent.getStringExtra("email")
        var password = intent.getStringExtra("password")
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
        startActivity(Intent(this, MainActivity::class.java))
    }
}