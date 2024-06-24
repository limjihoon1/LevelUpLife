package com.limjihoon.myhero.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.BoardManageActivity
import com.limjihoon.myhero.activitis.LoginActivity
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.activitis.MemberManageActivity
import com.limjihoon.myhero.activitis.MyBoardActivity
import com.limjihoon.myhero.activitis.MyTodoActivity
import com.limjihoon.myhero.activitis.NotificationManageActivity
import com.limjihoon.myhero.data.Inventory
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.databinding.FragmentSetting2Binding
import com.limjihoon.myhero.databinding.FragmentSettingBinding
import com.limjihoon.myhero.model.DataManager
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSetting2Binding
    private lateinit var dataManager: DataManager
    private val auth = Firebase.auth
    private val spf by lazy {
        activity?.getSharedPreferences(
            "loginSave",
            AppCompatActivity.MODE_PRIVATE
        )
    }
    private val spf2 by lazy {
        activity?.getSharedPreferences(
            "userInfo",
            AppCompatActivity.MODE_PRIVATE
        )
    }
    private val spfEdit by lazy { spf?.edit() }
    private val spfEdit2 by lazy { spf2?.edit() }
    private var inventory: Inventory? = null
    private var hero = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetting2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ma = activity as MainActivity
        ma.dataManager.inventoryFlow.value ?: return

        dataManager = ma.dataManager
        inventory = ma.dataManager.inventoryFlow.value

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataManager.memberFlow.collect { member ->
                    updateUI(member)
                    setItemMenu(dataManager.memberFlow.value!!.level)
                }
            }
        }

        binding.changeImage.setOnClickListener { select(ma) }
        binding.settingBtn.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.END) }

        binding.navigationView.setNavigationItemSelectedListener { p0 ->
            when (p0.itemId) {
                R.id.menu_logout -> {
                    AlertDialog.Builder(requireContext()).setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?").setPositiveButton("확인") { dialog, id ->
                            spfEdit?.putBoolean("isLogin", false)
                            spfEdit2?.clear()
                            spfEdit?.apply()
                            spfEdit2?.apply()
                            G.uid = ""
                            G.nickname = ""
                            auth.signOut()
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            activity?.finish()
                            Toast.makeText(requireContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show()
                        }.setNegativeButton("취소") { dialog, id ->
                            dialog.dismiss()
                        }.create().show()
                }
                R.id.menu_die -> {
                    AlertDialog.Builder(requireContext()).setTitle("회원탈퇴")
                        .setMessage("탈퇴 하시겠습니까?").setPositiveButton("확인") { dialog, id ->
                            val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                            val retrofitService = retrofit.create(RetrofitService::class.java)

                            retrofitService.myMemberOut(G.uid).enqueue(object : Callback<String> {
                                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                                    spfEdit?.putBoolean("isLogin", false)
                                    spfEdit2?.clear()
                                    spfEdit?.apply()
                                    spfEdit2?.apply()
                                    G.uid = ""
                                    G.nickname = ""

                                    auth.currentUser?.delete()
                                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                                    activity?.finish()
                                    Toast.makeText(requireContext(), "탈퇴 완료", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(p0: Call<String>, p1: Throwable) {
                                    Log.d("err", p1.message.toString())
                                }

                            })

                        }.setNegativeButton("취소") { dialog, id ->
                            dialog.dismiss()
                        }.create().show()

                }
                R.id.menu_list -> {
                    startActivity(Intent(requireContext(), MyBoardActivity::class.java))
                }
                R.id.menu_todo -> {
                    startActivity(Intent(requireContext(), MyTodoActivity::class.java))
                }
                R.id.menu_member_manage -> {
                    startActivity(Intent(requireContext(), MemberManageActivity::class.java))
                }
                R.id.menu_board_manage -> {
                    startActivity(Intent(requireContext(), BoardManageActivity::class.java))
                }
                R.id.menu_notification_manage -> {
                    startActivity(Intent(requireContext(), NotificationManageActivity::class.java))
                }
                R.id.menu_logout2 -> {
                    AlertDialog.Builder(requireContext()).setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?").setPositiveButton("확인") { dialog, id ->
                            spfEdit?.putBoolean("isLogin", false)
                            spfEdit2?.clear()
                            spfEdit?.apply()
                            spfEdit2?.apply()
                            G.uid = ""
                            G.nickname = ""
                            auth.signOut()
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            activity?.finish()
                            Toast.makeText(requireContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show()
                        }.setNegativeButton("취소") { dialog, id ->
                            dialog.dismiss()
                        }.create().show()
                }
            }


            false
        }


    }

    override fun onResume() {
        super.onResume()
    }

    private fun setItemMenu(level: Int) {
        val headerLayout = binding.navigationView.getHeaderView(0).findViewById<RelativeLayout>(R.id.header_layout)
        val iv = binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.iv_hero)
        val tv = binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_nickname_hearder)
        val g1Items = listOf(
            R.id.menu_logout,
            R.id.menu_die,
            R.id.menu_list,
            R.id.menu_todo
        )
        val g2Items = listOf(
            R.id.menu_member_manage,
            R.id.menu_board_manage,
            R.id.menu_notification_manage,
            R.id.menu_logout2
        )

        // 모든 아이템 숨기기
        (g1Items + g2Items).forEach { binding.navigationView.menu.findItem(it).isVisible = false }

        // no 값에 따라 아이템 보이기
        if (level == 999) {
            g2Items.forEach { binding.navigationView.menu.findItem(it).isVisible = true }
            headerLayout.setBackgroundResource(R.color.myPrimaryColor)
            when(dataManager.memberFlow.value!!.hero){
                1 -> iv.setImageResource(R.drawable.level_up_char1)
                2 -> iv.setImageResource(R.drawable.level_up_char2)
                3 -> iv.setImageResource(R.drawable.level_up_char3)
                4 -> iv.setImageResource(R.drawable.level_up_char4)
                5 -> iv.setImageResource(R.drawable.level_up_char5)
                6 -> iv.setImageResource(R.drawable.level_up_char6)
                7 -> iv.setImageResource(R.drawable.level_up_char7)
                8 -> iv.setImageResource(R.drawable.level_up_char8)
                9 -> iv.setImageResource(R.drawable.level_up_char9)
                10 -> iv.setImageResource(R.drawable.level_up_char10)
                11 -> iv.setImageResource(R.drawable.level_up_char11)
                else -> iv.setImageResource(R.drawable.level_up_char_hiden2)
            }
            tv.text = dataManager.memberFlow.value!!.nickname
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            g1Items.forEach { binding.navigationView.menu.findItem(it).isVisible = true }
            when(dataManager.memberFlow.value!!.hero){
                1 -> iv.setImageResource(R.drawable.level_up_char1)
                2 -> iv.setImageResource(R.drawable.level_up_char2)
                3 -> iv.setImageResource(R.drawable.level_up_char3)
                4 -> iv.setImageResource(R.drawable.level_up_char4)
                5 -> iv.setImageResource(R.drawable.level_up_char5)
                6 -> iv.setImageResource(R.drawable.level_up_char6)
                7 -> iv.setImageResource(R.drawable.level_up_char7)
                8 -> iv.setImageResource(R.drawable.level_up_char8)
                9 -> iv.setImageResource(R.drawable.level_up_char9)
                10 -> iv.setImageResource(R.drawable.level_up_char10)
                11 -> iv.setImageResource(R.drawable.level_up_char11)
                else -> iv.setImageResource(R.drawable.level_up_char_hiden2)
            }
            tv.text = dataManager.memberFlow.value!!.nickname
        }
    }

    private fun updateUI(member: Member2?) {
        var progress: Double = 0.0

        inventory?.let { inv ->
            member?.let { mem ->
                val charIds = listOf(
                    R.id.char1, R.id.char2, R.id.char3, R.id.char4,
                    R.id.char5, R.id.char6, R.id.char7, R.id.char8,
                    R.id.char9, R.id.char10, R.id.char11
                )
                val charDrawables = listOf(
                    R.drawable.level_up_char1, R.drawable.level_up_char2, R.drawable.level_up_char3, R.drawable.level_up_char4,
                    R.drawable.level_up_char5, R.drawable.level_up_char6, R.drawable.level_up_char7, R.drawable.level_up_char8,
                    R.drawable.level_up_char9, R.drawable.level_up_char10, R.drawable.level_up_char11
                )
                val charCounts = listOf(
                    inv.char1, inv.char2, inv.char3, inv.char4,
                    inv.char5, inv.char6, inv.char7, inv.char8,
                    inv.char9, inv.char10, inv.char11
                )

                // 이미지 뷰 리스트와 캐릭터 정보 리스트를 함께 순회
                for (i in charIds.indices) {
                    if (charCounts[i] >= 1) {
                        val charBinding = when (charIds[i]) {
                            R.id.char1 -> binding.char1
                            R.id.char2 -> binding.char2
                            R.id.char3 -> binding.char3
                            R.id.char4 -> binding.char4
                            R.id.char5 -> binding.char5
                            R.id.char6 -> binding.char6
                            R.id.char7 -> binding.char7
                            R.id.char8 -> binding.char8
                            R.id.char9 -> binding.char9
                            R.id.char10 -> binding.char10
                            R.id.char11 -> binding.char11
                            else -> null
                        }
                        charBinding?.setImageResource(charDrawables[i])
                        progress += 91
                        binding.progressText.text = "${progress / 10}"
                    }
                }

                // 모든 캐릭터가 1 이상인 경우 숨겨진 캐릭터 표시 및 progress 설정
                if (charCounts.all { it >= 1 }) {
                    binding.charhiden.setImageResource(R.drawable.level_up_char_hiden2)
                    binding.progressText.text = "100"
                }

                // 선택된 영웅 이미지 설정
                val heroDrawable = when (mem.hero) {
                    1 -> R.drawable.level_up_char1
                    2 -> R.drawable.level_up_char2
                    3 -> R.drawable.level_up_char3
                    4 -> R.drawable.level_up_char4
                    5 -> R.drawable.level_up_char5
                    6 -> R.drawable.level_up_char6
                    7 -> R.drawable.level_up_char7
                    8 -> R.drawable.level_up_char8
                    9 -> R.drawable.level_up_char9
                    10 -> R.drawable.level_up_char10
                    11 -> R.drawable.level_up_char11
                    else -> R.drawable.level_up_char_hiden2
                }
                binding.myChar.setImageResource(heroDrawable)
            }
        }

        binding.nickname.text = member!!.nickname
        binding.level.text = "${member.level}"
        binding.coin.text = "${member.coin}"
        binding.tvExp2.text = "${member.exp}/50"

        val ppp = (progress / 10).toInt()
        binding.progressBar.progress = ppp

    }

    fun select(ma: MainActivity) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.custum_dialog_select_char2, null)
        val gridLayout = dialogView.findViewById<GridLayout>(R.id.gridLayout)
        val imageViews = mutableListOf<ImageView>()

        builder.setView(dialogView)

        val dialog = builder.create()

        inventory?.let { inv ->
            val charIds = listOf(
                R.id.selected_char1, R.id.selected_char2, R.id.selected_char3, R.id.selected_char4,
                R.id.selected_char5, R.id.selected_char6, R.id.selected_char7, R.id.selected_char8,
                R.id.selected_char9, R.id.selected_char10, R.id.selected_char11, R.id.selected_charhiden
            )
            val charDrawables = listOf(
                R.drawable.level_up_char1, R.drawable.level_up_char2, R.drawable.level_up_char3, R.drawable.level_up_char4,
                R.drawable.level_up_char5, R.drawable.level_up_char6, R.drawable.level_up_char7, R.drawable.level_up_char8,
                R.drawable.level_up_char9, R.drawable.level_up_char10, R.drawable.level_up_char11, R.drawable.level_up_char_hiden2
            )
            val charCounts = listOf(
                inv.char1, inv.char2, inv.char3, inv.char4,
                inv.char5, inv.char6, inv.char7, inv.char8,
                inv.char9, inv.char10, inv.char11, inv.charHiden
            )

            // 이미지 뷰 리스트와 캐릭터 정보 리스트를 함께 순회
            for (i in charIds.indices) {
                val charId = charIds[i]
                val charDrawable = charDrawables[i]
                val charCount = charCounts[i]

                if (charCount >= 1) {
                    val newImageView = ImageView(requireContext()).apply {
                        id = charId
                        layoutParams = GridLayout.LayoutParams().apply {
                            width = resources.getDimensionPixelSize(R.dimen.image_width)
                            height = resources.getDimensionPixelSize(R.dimen.image_height)
                        }
                        setImageResource(charDrawable)
                        setBackgroundResource(R.drawable.char_bg)

                        setOnClickListener { clickedView ->
                            imageViews.forEach { it.setBackgroundResource(R.drawable.char_bg) }
                            clickedView.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

                            hero = when (clickedView.id) {
                                R.id.selected_char1 -> 1
                                R.id.selected_char2 -> 2
                                R.id.selected_char3 -> 3
                                R.id.selected_char4 -> 4
                                R.id.selected_char5 -> 5
                                R.id.selected_char6 -> 6
                                R.id.selected_char7 -> 7
                                R.id.selected_char8 -> 8
                                R.id.selected_char9 -> 9
                                R.id.selected_char10 -> 10
                                R.id.selected_char11 -> 11
                                R.id.selected_charhiden -> 12
                                else -> 0
                            }
                            binding.myChar.setImageResource(charDrawable)
                        }
                    }
                    gridLayout.addView(newImageView)
                    imageViews.add(newImageView)
                }
            }
        }

        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        dialogButton.setOnClickListener {

            if (hero != 0) {
                val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                val retrofitService = retrofit.create(RetrofitService::class.java)

                retrofitService.updateHero(G.uid, hero).enqueue(object : Callback<String> {
                    override fun onResponse(p0: Call<String>, p1: Response<String>) {
                        ma.getMember()
                        Toast.makeText(requireContext(), "${p1.body()}", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    override fun onFailure(p0: Call<String>, p1: Throwable) {
                        Log.d("err", p1.message.toString())
                    }

                })
            } else {
                Toast.makeText(requireContext(), "캐릭터 변경을 취소했습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }


        }
    }

}
