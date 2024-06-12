package com.limjihoon.myhero.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
import com.limjihoon.myhero.activitis.MapActivity
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.databinding.FragmentHomeBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fabtn.setOnClickListener { startActivity(Intent(requireContext(),ChatBotActivity::class.java)) }
        binding.creatTodo.setOnClickListener { listCreate() }
        binding.createMap.setOnClickListener { startActivity(Intent(requireContext(), MapActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getMember("asdfghjklzzxcvbnmqwer1").enqueue(object : Callback<Member2> {
            override fun onResponse(p0: Call<Member2>, p1: Response<Member2>) {
                val data = p1.body()

//                AlertDialog.Builder(requireContext()).setMessage("$data").create().show()

                binding.nickname.text = data!!.nickname
                binding.level.text = "Lv : ${data.level}"
                binding.tvExp.text = "${data.exp}/50"
                binding.coin.text = "${data.coin} COIN"

                if (data.hero == 1) {
                    binding.hero.setImageResource(R.drawable.level_up_char1)
                }

            }

            override fun onFailure(p0: Call<Member2>, p1: Throwable) {
                Log.d("error", "${p1.message}")
            }

        })

        val retrofit2 = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService2 = retrofit2.create(RetrofitService::class.java)
    }
    private fun listCreate(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_input_todo, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

}