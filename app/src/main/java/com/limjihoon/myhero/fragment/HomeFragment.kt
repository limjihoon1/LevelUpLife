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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.activitis.MapActivity
import com.limjihoon.myhero.adapter.TodoRecyclerAdapter
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.data.Todo2
import com.limjihoon.myhero.databinding.FragmentHomeBinding
import com.limjihoon.myhero.model.DataManager
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dataManager: DataManager
    private var uid = ""
    var items = mutableListOf<Todo>()
    private val retrofitService: RetrofitService by lazy {
        RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr").create(RetrofitService::class.java)
    }

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

        binding.createMap.setOnClickListener {
            val mapIntent = Intent(requireContext(), MapActivity::class.java).apply {
                putExtra("lat", (activity as MainActivity).myLocation?.latitude ?: 37.555)
                putExtra("lng", (activity as MainActivity).myLocation?.longitude ?: 126.9746)
            }
            startActivity(mapIntent)
        }

        // items를 어댑터에 전달하여 초기화합니다.
        binding.recy.adapter = TodoRecyclerAdapter(requireContext(), items)

        val ma = activity as MainActivity
        ma.dataManager.memberFlow.value ?: return

        dataManager = ma.dataManager

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataManager.memberFlow.collect { member ->
                    updateInfo(member)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        fetchTodos()
    }
    private fun updateInfo(member: Member2?) {
        member?.let {
            binding.nickname.text = it.nickname
            binding.level.text = "Lv : ${it.level}"
            binding.tvExp2.text = "${it.exp}/50"
            binding.coin.text = "${it.coin} COIN"
            uid = it.uid
            var progress = 0

            when (it.exp) {
                0 -> progress = 0
                10 -> progress = 20
                20 -> progress = 40
                30 -> progress = 60
                40 -> progress = 80
            }
            binding.bar.progress = progress

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
        } ?: run {
            binding.nickname.text = "정보 없음"
            binding.level.text = "Lv : -"
            binding.tvExp2.text = "-/50"
            binding.coin.text = "- COIN"
        }
    }

    private fun fetchTodos() {
        retrofitService.getTodo(uid).enqueue(object : Callback<List<Todo>> {
            @SuppressLint("NotifyDataSetChanged")
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
                Toast.makeText(requireContext(), "할 일 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
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
            val todoText = dialogTv.text.toString().trim()
            if (todoText.isEmpty()) {
                Toast.makeText(requireContext(), "할 일 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val todo = Todo2(uid, todoText, 0, "normal")

            retrofitService.insertTodo(todo).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val data = response.body()
                    Toast.makeText(requireContext(), "$data", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    fetchTodos()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("error", "${t.message}")
                    Toast.makeText(requireContext(), "할 일 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        dialogButton2.setOnClickListener {
            dialog.dismiss()
        }
    }

}
