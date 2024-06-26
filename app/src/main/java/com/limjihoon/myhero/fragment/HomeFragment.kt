package com.limjihoon.myhero.fragment

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
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
import com.airbnb.lottie.LottieDrawable
import com.google.gson.Gson
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.ChatBotActivity
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.activitis.MapActivity
import com.limjihoon.myhero.adapter.TodoRecyclerAdapter
import com.limjihoon.myhero.data.AnalysisResult
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
        binding.fabtn.setOnClickListener {
//            AlertDialog.Builder(requireContext()).setMessage("${dataManager.todoFlow.value?.size}").create().show()
            if (dataManager.todoFlow.value!!.isNotEmpty() && dataManager.todoFlow.value?.size!! >= 3) {
                todayTodo()
            } else if (dataManager.todoFlow.value?.size!! <  3) {
                Toast.makeText(
                    requireContext(),
                    "일정이 최소 3개 정도는 있어야 합니다!! ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "분석할 일정이 없습니다 일정 추가 후 다시 눌러주세요!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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

        val todoList = StringBuilder()

        if (dataManager.todoFlow.value!!.isNotEmpty()) {
            for (i in 0 until dataManager.todoFlow.value!!.size) {
                todoList.append("${dataManager.todoFlow.value!![i].workTodo},")
            }
        } else {
            Log.d("todoErr", "일정 목록이 없습니다")
        }

        binding.animationView.apply {
            visibility = View.VISIBLE
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
            binding.backgroundDim.visibility = View.VISIBLE
            binding.tvAiLoading.visibility = View.VISIBLE

            setOnTouchListener { v, event -> true }
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    setOnTouchListener(null)
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}

            })
        }
        val request = ChatGPTRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(
                    role = "system",
                    content = "You are an assistant that helps analyze and improve Todo Lists."
                ),
                Message(
                    role = "user",
                    content = """행동 지침 : 지금부터 내가 너에게 주는 todo list를 기반으로 데이터를 json형태로 줄때에는 사용자(나) 그리고 너(GPT)가 주는대답을 json형식으로 만들어줘
                         데이터의 구분을위해 사용자(나)는 myTodoList 너(GPT)는 recommendTodo라고 구분지어줘
                          그리고 todo list에 대해서는 사용자(나)의 todo list를 받아서 너(GPT)가 여러가지 추천사항을 적용해서 새롭게 만든 데이터를 recommendTodo에 적용시켜준뒤에
                          기존 사용자(나)의 todo list에 너(GPT)가 추천해주는걸 포함해서 백분율표의 퍼센트로도 함께 적용시켜줘 그후에 todo list에 전체 
                          분위기에 맞춰서 총평을 180자이내로 해줄래? 총평은 msg로 구분지어줄래? 총평을 작성할떄 60자이내에 한번씩 줄을바꿔줘""".trimIndent()
                ),
                Message(
                    role = "user",
                    content = """예시 응답 : {
    "myTodoList": [
        {"todo": "영등포역가기", "percentage": 20},
        {"todo": "근처 카페에서", "percentage": 20},
        {"todo": "영화보기", "percentage": 20},
        {"todo": "늦은 점심먹고 산책하기", "percentage": 20},
        {"todo": "친구랑 헤어지고 게임하기", "percentage": 20}
    ],
    "recommendTodo": [
        {"todo": "영등포역가기", "percentage": 14.3},
        {"todo": "근처 카페에서", "percentage": 14.3},
        {"todo": "영화보기", "percentage": 14.3},
        {"todo": "늦은 점심먹고 산책하기", "percentage": 14.3},
        {"todo": "친구랑 헤어지고 게임하기", "percentage": 14.3},
        {"todo": "저녁 운동하기", "percentage": 14.3},
        {"todo": "자기 전 독서하기", "percentage": 14.3}
    ],
    "msg": "오늘의 일정은 일과 운동, 휴식이 잘 균형 잡혀 있습니다.\n아침 스트레칭과 저녁 명상을 추가하여\n하루를 더욱 활기차고 평온하게 마무리하세요."
}""".trimIndent()
                ),
                Message(role = "user", content = todoList.toString())
            )
        )

        val retrofit = RetrofitHelper.getRetrofitInstance("https://api.openai.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)

//        retrofitService.getChatCompletion2(request).enqueue(object : Callback<String> {
//            override fun onResponse(p0: Call<String>, p1: Response<String>) {
//                AlertDialog.Builder(requireContext()).setMessage("${p1.body()}").create().show()
//                Log.d("gptG", "${p1.body()}")
//            }
//
//            override fun onFailure(p0: Call<String>, p1: Throwable) {
//                Log.d("gptErr", "${p1.message}")
//            }
//
//        })

        try {
            retrofitService.getChatCompletion(request).enqueue(object : Callback<ChatGPTResponse> {
                override fun onResponse(p0: Call<ChatGPTResponse>, p1: Response<ChatGPTResponse>) {
                    if (p1.isSuccessful) {
                        val chatResponse = p1.body()
                        chatResponse?.choices?.forEach {
//                        AlertDialog.Builder(requireContext()).setMessage("${it.message.content}")
//                            .create().show()
                            val intent = Intent(requireContext(), ChatBotActivity::class.java)
                            intent.putExtra("result", it.message.content)
                            binding.animationView.visibility = View.GONE
                            binding.tvAiLoading.visibility = View.GONE
                            binding.backgroundDim.visibility = View.GONE
                            binding.animationView.pauseAnimation()
                            startActivity(intent)
                            Log.d("gptG", it.message.content)
                        }
                    }
                }

                override fun onFailure(p0: Call<ChatGPTResponse>, p1: Throwable) {
                    Log.d("gptErr", "${p1.message}")
                }

            })
        } catch (e: Exception) {
            Log.d("GptTodoErr", "${e.message}")
        }



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
                0 -> binding.nummm.text = "오늘의 보상 횟수 0/5 "
                1 -> binding.nummm.text = "오늘의 보상 횟수 1/5 "
                2 -> binding.nummm.text = "오늘의 보상 횟수 2/5 "
                3 -> binding.nummm.text = "오늘의 보상 횟수 3/5 "
                4 -> binding.nummm.text = "오늘의 보상 횟수 4/5 "
                5 -> binding.nummm.text = "오늘의 보상 횟수 5/5 "

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
                    dataManager.updateTodo(data)
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
