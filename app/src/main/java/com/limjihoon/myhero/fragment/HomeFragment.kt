package com.limjihoon.myhero.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.activitis.MapActivity
import com.limjihoon.myhero.adapter.TodoRecyclerAdapter
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.databinding.FragmentHomeBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding
    private var uid = ""
//    lateinit var todos: Todo
    var itmes = mutableListOf<Todo>()
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
        var intent = Intent(requireContext(), MapActivity::class.java)

        binding.createMap.setOnClickListener {startActivity(intent) }

        binding.recy.adapter = TodoRecyclerAdapter(requireContext(), itmes)
    }

    override fun onResume() {
        super.onResume()

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getMember("ZevqYU2WU3aMWsN61LWcXxSBlA52").enqueue(object : Callback<Member2> {
            override fun onResponse(p0: Call<Member2>, p1: Response<Member2>) {
                val data = p1.body()

//                AlertDialog.Builder(requireContext()).setMessage("$data").create().show()

                binding.nickname.text = data!!.nickname
                binding.level.text = "Lv : ${data.level}"
                binding.tvExp.text = "${data.exp}/50"
                binding.coin.text = "${data.coin} COIN"

                uid = data.uid

                if (data.hero == 1) {
                    binding.hero.setImageResource(R.drawable.level_up_char1)
                } else if(data.hero == 12) {
                    binding.hero.setImageResource(R.drawable.level_up_char_hiden2)
                }

                val retrofit2 = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                val retrofitService2 = retrofit2.create(RetrofitService::class.java)

                retrofitService2.getTodo(uid).enqueue(object : Callback<List<Todo>> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(p0: Call<List<Todo>>, p1: Response<List<Todo>>) {
                        val data = p1.body()

                        itmes.clear()
                        binding.recy.adapter!!.notifyDataSetChanged()

                        data?.forEach {
                            itmes.add(0, it)
                            binding.recy.adapter!!.notifyDataSetChanged()
                        }

//                        AlertDialog.Builder(requireContext()).setMessage("$data").create().show()
//                        Log.d("todo", "aaa: ${data!!.workTodo}")
                    }

                    override fun onFailure(p0: Call<List<Todo>>, p1: Throwable) {
                        Log.d("etodo", "${p1.message}")
                    }

                })

            }

            override fun onFailure(p0: Call<Member2>, p1: Throwable) {
                Log.d("error", "${p1.message}")
            }

        })

    }
    private fun listCreate(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_input_todo, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        val dialogButton2: Button = dialogView.findViewById(R.id.cancelButton)
        val dialogTv = dialogView.findViewById<EditText>(R.id.scheduleEditText)

        dialogButton.setOnClickListener {

            val todo = Todo(uid, dialogTv.text.toString(), 0)

//            AlertDialog.Builder(requireContext()).setMessage("$uid : ${dialogTv.text.toString()}").create().show()

            val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
            val retrofitService = retrofit.create(RetrofitService::class.java)

            retrofitService.insertTodo(todo).enqueue(object : Callback<String> {
                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                    val data = p1.body()

                    Toast.makeText(requireContext(), "$data", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {
                    Log.d("error", "${p1.message}")
                }

            })

        }

        dialogButton2.setOnClickListener {
            dialog.dismiss()
        }

    }

}