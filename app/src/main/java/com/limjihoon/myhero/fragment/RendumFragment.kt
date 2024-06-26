package com.limjihoon.myhero.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.data.Inventory
import com.limjihoon.myhero.databinding.FragmentProfile2Binding
import com.limjihoon.myhero.databinding.FragmentProfileBinding
import com.limjihoon.myhero.model.DataManager
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


class RendumFragment : Fragment() {
    private lateinit var binding: FragmentProfile2Binding
    private lateinit var dataManager: DataManager
    private var inventory: Inventory? = null
    private var hero = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfile2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ma = activity as MainActivity
        ma.dataManager.memberFlow.value ?: return
        dataManager = ma.dataManager
        inventory = dataManager.inventoryFlow.value

        inventory?.let { inv ->
            val charCounts = listOf(
                inv.char1, inv.char2, inv.char3, inv.char4,
                inv.char5, inv.char6, inv.char7, inv.char8,
                inv.char9, inv.char10, inv.char11
            )

            if (charCounts.all { it >= 1 } && inv.charHiden == 0) {
                getHiden(ma)
            }
        }

        binding.rendumBtn.setOnClickListener {
            rendum(ma)
        }

    }

    override fun onResume() {
        super.onResume()
        getMember(MainActivity())
    }

    private fun getMember(ma: MainActivity) {
        ma.getMember()
    }

    private fun getHiden(ma: MainActivity) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.custum_dialog_rendum, null)
        val img: ImageView = dialogView.findViewById(R.id.dialogImage)

        img.setImageResource(R.drawable.level_up_char_hiden2)

        builder.setView(dialogView)

        val dialog = builder.create()

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.updateInventoryHiden(G.uid).enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, p1: Response<String>) {
                ma.getMember()
                Toast.makeText(requireContext(), "${p1.body()}", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(p0: Call<String>, p1: Throwable) {
                Log.d("invenErr", p1.message.toString())
            }

        })

        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.dialogButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun rendum(ma: MainActivity) {
        val randomNumber = Random.nextInt(1, 12)

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_rendum, null)
        val img: ImageView = dialogView.findViewById(R.id.dialogImage)

        when (randomNumber) {
            1 -> {
                img.setImageResource(R.drawable.level_up_char1)
                hero = 1
            }

            2 -> {
                img.setImageResource(R.drawable.level_up_char2)
                hero = 2
            }

            3 -> {
                img.setImageResource(R.drawable.level_up_char3)
                hero = 3
            }

            4 -> {
                img.setImageResource(R.drawable.level_up_char4)
                hero = 4
            }

            5 -> {
                img.setImageResource(R.drawable.level_up_char5)
                hero = 5
            }

            6 -> {
                img.setImageResource(R.drawable.level_up_char6)
                hero = 6
            }

            7 -> {
                img.setImageResource(R.drawable.level_up_char7)
                hero = 7
            }

            8 -> {
                img.setImageResource(R.drawable.level_up_char8)
                hero = 8
            }

            9 -> {
                img.setImageResource(R.drawable.level_up_char9)
                hero = 9
            }

            10 -> {
                img.setImageResource(R.drawable.level_up_char10)
                hero = 10
            }

            else -> {
                img.setImageResource(R.drawable.level_up_char11)
                hero = 11
            }
        }

        builder.setView(dialogView)

        val dialog = builder.create()

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.updateInventory(G.uid, hero).enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, p1: Response<String>) {
                ma.getMember()

                if (p1.body() == "캐릭터 뽑기 완료") {
                    dialog.show()
                    Toast.makeText(requireContext(), "${p1.body()}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "${p1.body()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<String>, p1: Throwable) {
                Log.d("invenErr", p1.message.toString())
            }

        })

        val dialogButton: Button = dialogView.findViewById(R.id.dialogButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

}