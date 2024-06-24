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
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.activitis.MapActivity
import com.limjihoon.myhero.adapter.TodoRecyclerAdapter
import com.limjihoon.myhero.data.ChatGPTRequest
import com.limjihoon.myhero.data.ChatGPTResponse
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.Message
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.data.Todo2
import com.limjihoon.myhero.databinding.FragmentHome2Binding
import com.limjihoon.myhero.databinding.FragmentHomeBinding
import com.limjihoon.myhero.model.DataManager
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHome2Binding
    private lateinit var dataManager: DataManager
    private var uid = ""
    var items = mutableListOf<Todo>()

    private val retrofitService: RetrofitService by lazy {
        RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
            .create(RetrofitService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHome2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fabtn.setOnClickListener { todayTodo() }
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

//        getMessage()
    }

    private fun todayTodo() {
        val todoList = "아침 9시 까지 왕십리 교육원 가기,점심 돈까스 먹기,낮잠 1시간 자기,저녁 간단한 안주와 맥주 먹기, 밤 10시 잠자기".trimIndent()
        val todoList2 = "아침 9시 까지 왕십리 교육원 가기,점심 라멘 먹기,저녁 6시 집가기, 밤에 잠자기".trimIndent()

        val request = ChatGPTRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(
                    role = "system",
                    content = "You are an assistant that helps analyze and improve Todo Lists."
                ),
                Message(
                    role = "user",
                    content = """행동 지침 : 지금부터 내 todo list에 대해서 분석을 시작할거야 분석한 결과는
백분율로 계산해서 퍼센트값을 돌려주고 첫번째는 내 todo list에 대한 분석 결과값
두번째는 내 todo list에 너가 생각할때 이런 일정도 있었으면 좋겠다고 생각한 일정을 추가해서
새롭게 만든 todo list를 분석한 결과값 이렇게 두개의 결과값을 나한테줘""".trimIndent()
                ),
                Message(
                    role = "user",
                    content = """예시 응답 : 홍길동님의 todo 분석결과
1.왕십리 교육원 가기
퍼센트: 25%
2.점심 주먹밥, 라면 먹기
퍼센트: 12.5%
3.낮잠 1시간 자기
퍼센트: 12.5%
4.저녁 간단한 안주와 맥주 먹기
퍼센트: 25%
5.밤에 피시방가기
퍼센트: 25%

AI 추천 todo 분석결과
1.왕십리 교육원 가기 (25%)
2.점심 주먹밥, 라면 먹기 (12.5%)
3.낮잠 1시간 자기 (12.5%)
4.운동하기 (6.25%)
5.독서 또는 자기개발 시간 (12.5%)
6.저녁 간단한 안주와 맥주 먹기 (18.75%)
7.밤에 피시방가기 (31.25%)""".trimIndent()
                ),
                Message(role = "user", content = todoList2)
            )
        )

        val retrofit = RetrofitHelper.getRetrofitInstance("https://api.openai.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getChatCompletion2(request).enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, p1: Response<String>) {
                AlertDialog.Builder(requireContext()).setMessage("${p1.body()}").create().show()
                Log.d("gptG", "${p1.body()}")
            }

            override fun onFailure(p0: Call<String>, p1: Throwable) {
                Log.d("gptErr", "${p1.message}")
            }

        })

//        retrofitService.getChatCompletion(request).enqueue(object : Callback<ChatGPTResponse> {
//            override fun onResponse(p0: Call<ChatGPTResponse>, p1: Response<ChatGPTResponse>) {
//                if (p1.isSuccessful) {
//                    val chatResponse = p1.body()
//                    chatResponse?.choices?.forEach {
//                        AlertDialog.Builder(requireContext()).setMessage("${it.message.content}").create().show()
//                    }
//                }
//            }
//
//            override fun onFailure(p0: Call<ChatGPTResponse>, p1: Throwable) {
//                Log.d("gptErr", "${p1.message}")
//            }
//
//        })
    }

    override fun onResume() {
        super.onResume()
        fetchTodos()
    }

    private fun updateInfo(member: Member2?) {
        member?.let {
            binding.nickname.text = it.nickname
            binding.level.text = "${it.level}"
            binding.tvExp2.text = "${it.exp}/50"
            binding.coin.text = "${it.coin}"
            uid = it.uid
            var progress = 0
            when (it.qcc) {
                0 -> binding.tvTodayQuest.text = "오늘의 보상 횟수 0/5 "
                1 -> binding.tvTodayQuest.text = "오늘의 보상 횟수 1/5 "
                2 -> binding.tvTodayQuest.text = "오늘의 보상 횟수 2/5 "
                3 -> binding.tvTodayQuest.text = "오늘의 보상 횟수 3/5 "
                4 -> binding.tvTodayQuest.text = "오늘의 보상 횟수 4/5 "
                5 -> binding.tvTodayQuest.text = "오늘의 보상 횟수 5/5 "

            }
            when (it.exp) {
                0 -> progress = 0
                10 -> progress = 20
                20 -> progress = 40
                30 -> progress = 60
                40 -> progress = 80
            }
            binding.bar.progress = progress

            when (it.hero) {
                1 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char1)
                }

                2 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char2)
                }

                3 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char3)
                }

                4 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char4)
                }

                5 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char5)
                }

                6 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char6)
                }

                7 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char7)
                }

                8 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char8)
                }

                9 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char9)
                }

                10 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char10)
                }

                11 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char11)
                }

                12 -> {
                    binding.hero.setImageResource(R.drawable.level_up_char_hiden2)
                }
            }
        } ?: run {
            binding.nickname.text = "정보 없음"
            binding.level.text = "-"
            binding.tvExp2.text = "-/50"
            binding.coin.text = "-"
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
                }
            })
        }

        dialogButton2.setOnClickListener {
            dialog.dismiss()
        }
    }

}
