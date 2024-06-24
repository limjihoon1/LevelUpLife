package com.limjihoon.myhero.activitis

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.ActivityChatBotBinding

class ChatBotActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBotBinding

    // 넣고 싶은 데이터 설정
    val dataList: List<PieEntry> = listOf(
        PieEntry(10F, "감자"),
        PieEntry(30F, "고구마"),
        PieEntry(25F, "배추"),
        PieEntry(35F, "양파"),
        PieEntry(20F, "고추"),
        PieEntry(50F, "삼겹살"),
        PieEntry(20F, "콩나물")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart2.setUsePercentValues(true)

        val dataSet = PieDataSet(dataList, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(this, R.color.pastel_rainbow1),
            ContextCompat.getColor(this, R.color.pastel_rainbow2),
            ContextCompat.getColor(this, R.color.pastel_rainbow3),
            ContextCompat.getColor(this, R.color.pastel_rainbow4),
            ContextCompat.getColor(this, R.color.pastel_rainbow5),
            ContextCompat.getColor(this, R.color.pastel_rainbow6),
            ContextCompat.getColor(this, R.color.pastel_rainbow7),
        )

        val dataSet2 = PieDataSet(dataList, "")
        dataSet2.colors = listOf(
            ContextCompat.getColor(this, R.color.pastel_ai1),
            ContextCompat.getColor(this, R.color.pastel_ai2),
            ContextCompat.getColor(this, R.color.pastel_ai3),
            ContextCompat.getColor(this, R.color.pastel_ai4),
            ContextCompat.getColor(this, R.color.pastel_ai5),
            ContextCompat.getColor(this, R.color.pastel_ai6),
            ContextCompat.getColor(this, R.color.pastel_ai7),
        )

        // pieChart 안에 들어갈 텍스트 크기
        dataSet.valueTextSize = 16F
        dataSet2.valueTextSize = 16F

        // pieChart 안에 들어간 value 값 표기 지우기
        // dataSet.setDrawValues(false)

        val piedata = PieData(dataSet)
        val piedata2 = PieData(dataSet2)

        binding.run {
            pieChart.apply {
                data = piedata
                description.isEnabled = false // 차트 설명 비활성화
                legend.isEnabled = false // 하단 설명 비활성화
                isRotationEnabled = true // 차트 회전 활성화
                setEntryLabelColor(Color.BLACK) // label 색상
                animateY(1400, Easing.EaseInOutQuad) // 1.4초 동안 애니메이션 설정
                animate()
            }

            pieChart2.apply {
                data = piedata2
                description.isEnabled = false // 차트 설명 비활성화
                legend.isEnabled = false // 하단 설명 비활성화
                isRotationEnabled = true // 차트 회전 활성화
                setEntryLabelColor(Color.BLACK) // label 색상
                animateY(1400, Easing.EaseInOutQuad) // 1.4초 동안 애니메이션 설정
                animate()
            }
        }


    }
}