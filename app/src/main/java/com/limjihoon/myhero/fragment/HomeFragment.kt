package com.limjihoon.myhero.fragment

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
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
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

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var uid = ""
    var items = mutableListOf<Todo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fabtn.setOnClickListener { startActivity(Intent(requireContext(), ChatBotActivity::class.java)) }
        binding.creatTodo.setOnClickListener { listCreate() }
        binding.createMap.setOnClickListener { startActivity(Intent(requireContext(), MapActivity::class.java)) }

        // items를 어댑터에 전달하여 초기화합니다.
        binding.recy.adapter = TodoRecyclerAdapter(requireContext(), items)

    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getMember(G.uid).enqueue(object : Callback<Member2> {
            override fun onResponse(call: Call<Member2>, response: Response<Member2>) {
                val data = response.body()
                data?.let {
                    binding.nickname.text = it.nickname
                    binding.level.text = "Lv : ${it.level}"
                    binding.tvExp2.text = "${it.exp}/50"
                    binding.coin.text = "${it.coin} COIN"
                    uid = it.uid

                    when(it.hero) {
                        1 -> { binding.hero.setImageResource(R.drawable.level_up_char1) }
                        2 -> { binding.hero.setImageResource(R.drawable.level_up_char2) }
                        3 -> { binding.hero.setImageResource(R.drawable.level_up_char3) }
                        4 -> { binding.hero.setImageResource(R.drawable.level_up_char4) }
                        5 -> { binding.hero.setImageResource(R.drawable.level_up_char5) }
                        6 -> { binding.hero.setImageResource(R.drawable.level_up_char6) }
                        7 -> { binding.hero.setImageResource(R.drawable.level_up_char7) }
                        8 -> { binding.hero.setImageResource(R.drawable.level_up_char8) }
                        9 -> { binding.hero.setImageResource(R.drawable.level_up_char9) }
                        10 -> { binding.hero.setImageResource(R.drawable.level_up_char10) }
                        11 -> { binding.hero.setImageResource(R.drawable.level_up_char11) }
                        12 -> { binding.hero.setImageResource(R.drawable.level_up_char_hiden2) }
                    }

                    fetchTodos()
                }
            }

            override fun onFailure(call: Call<Member2>, t: Throwable) {
                Log.d("error", "${t.message}")
            }
        })
    }

    private fun fetchTodos() {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getTodo(uid).enqueue(object : Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                val data = response.body()
                data?.let {
                    items.clear()
                    items.addAll(it)
                    binding.recy.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                Log.d("etodo", "${t.message}")
            }
        })
    }

    private fun listCreate() {
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
            val todo = Todo(uid, dialogTv.text.toString(), 0, 0)

            val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
            val retrofitService = retrofit.create(RetrofitService::class.java)

            retrofitService.insertTodo(todo).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val data = response.body()
                    Toast.makeText(requireContext(), "$data", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    fetchTodos() // 새 할 일을 추가한 후 목록을 갱신합니다.
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("error", "${t.message}")
                }
            })
        }

        dialogButton2.setOnClickListener {
            dialog.dismiss()
        }
    }
}
