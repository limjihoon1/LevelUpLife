package com.limjihoon.myhero.activitis

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.adapter.LegendRecyclerAdapter
import com.limjihoon.myhero.adapter.LegendRecyclerAdapter2
import com.limjihoon.myhero.data.AnalysisResult
import com.limjihoon.myhero.data.LegendItem
import com.limjihoon.myhero.data.LegendItem2
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.databinding.ActivityChatBotBinding

class ChatBotActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBotBinding
    private val dataList: MutableList<PieEntry> = mutableListOf()
    private val dataList2: MutableList<PieEntry> = mutableListOf()
    var items = mutableListOf<LegendItem>()
    var items2 = mutableListOf<LegendItem2>()

    // 넣고 싶은 데이터 설정
    val dataList3: List<PieEntry> = listOf(
        PieEntry(10F, "감자"),
        PieEntry(30F, "고구마"),
        PieEntry(25F, "배추"),
        PieEntry(35F, "양파"),
        PieEntry(20F, "고추"),
        PieEntry(50F, "삼겹살"),
        PieEntry(20F, "콩나물")
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val result = intent.getStringExtra("result")
        val analysisResult = Gson().fromJson(result, AnalysisResult::class.java)

        binding.tvTitle.text = "${G.nickname}님의 오늘 하루 분석표"
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart2.setUsePercentValues(true)
        binding.legendRecyclerView.adapter = LegendRecyclerAdapter(this, items)
        binding.legendRecyclerView2.adapter = LegendRecyclerAdapter2(this, items2)

        for (i in 0 until analysisResult.myTodoList.size) {
            dataList.add(PieEntry(analysisResult.myTodoList[i].percentage, analysisResult.myTodoList[i].todo))
        }

        for (i in 0 until analysisResult.recommendTodo.size) {
            dataList2.add(PieEntry(analysisResult.recommendTodo[i].percentage, analysisResult.recommendTodo[i].todo))
        }

//        Log.d("dataErr", "${analysisResult.Mytodolist.get(0).todo}")

        val dataSet = PieDataSet(dataList, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(this, R.color.pastel_rainbow1),
            ContextCompat.getColor(this, R.color.pastel_rainbow2),
            ContextCompat.getColor(this, R.color.pastel_rainbow3),
            ContextCompat.getColor(this, R.color.pastel_rainbow4),
            ContextCompat.getColor(this, R.color.pastel_rainbow5),
            ContextCompat.getColor(this, R.color.pastel_rainbow6),
            ContextCompat.getColor(this, R.color.pastel_rainbow7),
            ContextCompat.getColor(this, R.color.pastel_rainbow8),
            ContextCompat.getColor(this, R.color.pastel_rainbow9),
            ContextCompat.getColor(this, R.color.pastel_rainbow10),
        )

        val dataSet2 = PieDataSet(dataList2, "")
        dataSet2.colors = listOf(
            ContextCompat.getColor(this, R.color.pastel_ai1),
            ContextCompat.getColor(this, R.color.pastel_ai2),
            ContextCompat.getColor(this, R.color.pastel_ai3),
            ContextCompat.getColor(this, R.color.pastel_ai4),
            ContextCompat.getColor(this, R.color.pastel_ai5),
            ContextCompat.getColor(this, R.color.pastel_ai6),
            ContextCompat.getColor(this, R.color.pastel_ai7),
            ContextCompat.getColor(this, R.color.pastel_ai8),
            ContextCompat.getColor(this, R.color.pastel_ai9),
            ContextCompat.getColor(this, R.color.pastel_ai10),
        )

        // pieChart 안에 들어갈 텍스트 크기
        dataSet.valueTextSize = 16F
        dataSet2.valueTextSize = 16F

        // pieChart 안에 들어간 value 값 표기 지우기
        dataSet.setDrawValues(true)
        dataSet2.setDrawValues(true)

        val piedata = PieData(dataSet)
        val piedata2 = PieData(dataSet2)

        binding.run {
            pieChart.apply {
                data = piedata
                for (i in 0 until data.entryCount) {
                    val entry = data.dataSet.getEntryForIndex(i)
                    val legendItem = LegendItem(data.colors[i], entry.label)
                    items.add(legendItem)
                }
                notifyDataSetChanged()
                description.isEnabled = false // 차트 설명 비활성화
                legend.orientation = Legend.LegendOrientation.VERTICAL // 범례 위치
                legend.isEnabled = false // 하단 설명 비활성화
                isRotationEnabled = true // 차트 회전 활성화
                setEntryLabelColor(Color.BLACK) // label 색상
                setDrawEntryLabels(false)
                animateY(1400, Easing.EaseInOutQuad) // 1.4초 동안 애니메이션 설정
                animate()
            }

            pieChart2.apply {
                data = piedata2
                for (i in 0 until data.entryCount) {
                    val entry = data.dataSet.getEntryForIndex(i)
                    val legendItem = LegendItem2(data.colors[i], entry.label)
                    items2.add(legendItem)
                }
                notifyDataSetChanged()
                description.isEnabled = false // 차트 설명 비활성화
//                legend.orientation = Legend.LegendOrientation.VERTICAL // 범례 위치
                legend.isEnabled = false // 하단 설명 비활성화
                isRotationEnabled = true // 차트 회전 활성화
                setEntryLabelColor(Color.BLACK) // label 색상
                setDrawEntryLabels(false)
                animateY(1400, Easing.EaseInOutQuad) // 1.4초 동안 애니메이션 설정
                animate()
            }
            tvMsg.text = "AI 소감평 : ${analysisResult.msg}"
        }

    }
}