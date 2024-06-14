package com.limjihoon.myhero.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.LoginActivity
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.data.Inventory
import com.limjihoon.myhero.databinding.FragmentSettingBinding

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ma = activity as MainActivity
        ma.inventory ?: return

        inventory = ma.inventory
        updateUI2(ma)

        binding.changeImage.setOnClickListener { select(ma) }
        binding.settingBtn.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.END) }

        binding.navigationView.setNavigationItemSelectedListener { p0 ->
            if (p0.itemId == R.id.menu_logout) {
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

            false
        }


//        AlertDialog.Builder(requireContext()).setMessage("${ma.inventory?.char1}").create().show()

    }

    private fun updateUI2(ma: MainActivity) {
        var progress: Double = 0.0

        if (inventory!!.char1 >= 1) {
            binding.char1.setImageResource(R.drawable.level_up_char1)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char2 >= 1) {
            binding.char2.setImageResource(R.drawable.level_up_char2)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char3 >= 1) {
            binding.char3.setImageResource(R.drawable.level_up_char3)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char4 >= 1) {
            binding.char4.setImageResource(R.drawable.level_up_char4)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char5 >= 1) {
            binding.char5.setImageResource(R.drawable.level_up_char5)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char6 >= 1) {
            binding.char6.setImageResource(R.drawable.level_up_char6)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char7 >= 1) {
            binding.char7.setImageResource(R.drawable.level_up_char7)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char8 >= 1) {
            binding.char8.setImageResource(R.drawable.level_up_char8)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char9 >= 1) {
            binding.char9.setImageResource(R.drawable.level_up_char9)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char10 >= 1) {
            binding.char10.setImageResource(R.drawable.level_up_char10)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char11 >= 1) {
            binding.char11.setImageResource(R.drawable.level_up_char11)
            progress += 91
            binding.progressText.text = "${progress / 10}"
        }
        if (inventory!!.char1 >= 1 && inventory!!.char2 >= 1 && inventory!!.char3 >= 1 && inventory!!.char4 >= 1 && inventory!!.char5 >= 1 && inventory!!.char6 >= 1 && inventory!!.char7 >= 1 && inventory!!.char8 >= 1 && inventory!!.char9 >= 1 && inventory!!.char10 >= 1 && inventory!!.char11 >= 1) {
            binding.charhiden.setImageResource(R.drawable.level_up_char_hiden2)

            binding.progressText.text = "100"
        }

        if (ma.member!!.hero == 1) {
            binding.myChar.setImageResource(R.drawable.level_up_char1)
        } else if (ma.member!!.hero == 2) {
            binding.myChar.setImageResource(R.drawable.level_up_char2)
        } else if (ma.member!!.hero == 3) {
            binding.myChar.setImageResource(R.drawable.level_up_char3)
        } else if (ma.member!!.hero == 4) {
            binding.myChar.setImageResource(R.drawable.level_up_char4)
        } else if (ma.member!!.hero == 5) {
            binding.myChar.setImageResource(R.drawable.level_up_char5)
        } else if (ma.member!!.hero == 6) {
            binding.myChar.setImageResource(R.drawable.level_up_char6)
        } else if (ma.member!!.hero == 7) {
            binding.myChar.setImageResource(R.drawable.level_up_char7)
        } else if (ma.member!!.hero == 8) {
            binding.myChar.setImageResource(R.drawable.level_up_char8)
        } else if (ma.member!!.hero == 9) {
            binding.myChar.setImageResource(R.drawable.level_up_char9)
        } else if (ma.member!!.hero == 10) {
            binding.myChar.setImageResource(R.drawable.level_up_char10)
        } else if (ma.member!!.hero == 11) {
            binding.myChar.setImageResource(R.drawable.level_up_char11)
        } else {
            binding.myChar.setImageResource(R.drawable.level_up_char_hiden2)
        }
        binding.nickname.text = ma.member!!.nickname
        binding.level.text = "Lv : ${ma.member!!.level}"
        binding.coin.text = "${ma.member!!.coin} Coin"
        binding.tvExp2.text = "${ma.member!!.exp} / 50"

        val ppp = (progress / 10).toInt()
        binding.progressBar.progress = ppp

    }

    fun select(ma: MainActivity) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.custum_dialog_select_char2, null)
        val gridLayout = dialogView.findViewById<GridLayout>(R.id.gridLayout)
        val image1: ImageView = dialogView.findViewById(R.id.select_char1)
        val image2: ImageView = dialogView.findViewById(R.id.select_char2)
        val image3: ImageView = dialogView.findViewById(R.id.select_char3)
        val image4: ImageView = dialogView.findViewById(R.id.select_char4)
        val image5: ImageView = dialogView.findViewById(R.id.select_char5)
        val image6: ImageView = dialogView.findViewById(R.id.select_char6)
        val image7: ImageView = dialogView.findViewById(R.id.select_char7)
        val image8: ImageView = dialogView.findViewById(R.id.select_char8)
        val image9: ImageView = dialogView.findViewById(R.id.select_char9)
        val image10: ImageView = dialogView.findViewById(R.id.select_char10)
        val image11: ImageView = dialogView.findViewById(R.id.select_char11)
        val imagehiden: ImageView = dialogView.findViewById(R.id.select_charhiden)
        val imageViews = mutableListOf<ImageView>()

        builder.setView(dialogView)

        val dialog = builder.create()

        if (inventory!!.char1 <= 1) {
            gridLayout.removeView(image1)
        }
        if (inventory!!.char1 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char1 // 기존 id와 동일하게 설정
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char1)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char2 <= 1) {
            gridLayout.removeView(image2)
        }
        if (inventory!!.char2 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char2 // 기존 id와 동일하게 설정
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char2)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char3 <= 1) {
            gridLayout.removeView(image3)
        }
        if (inventory!!.char3 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char3 // 기존 id와 동일하게 설정
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char3)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char4 <= 1) {
            gridLayout.removeView(image4)
        }
        if (inventory!!.char4 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char4
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char4)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char5 <= 1) {
            gridLayout.removeView(image5)
        }
        if (inventory!!.char5 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char5
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char5)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char6 <= 1) {
            gridLayout.removeView(image6)
        }
        if (inventory!!.char6 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char6
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char6)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char7 <= 1) {
            gridLayout.removeView(image7)
        }
        if (inventory!!.char7 >= 1){
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char7
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char7)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char8 <= 1) {
            gridLayout.removeView(image8)
        }
        if (inventory!!.char8 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char8
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char8)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char9 <= 1) {
            gridLayout.removeView(image9)
        }
        if (inventory!!.char9 >= 1) {
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char9
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char9)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char10 <= 1) {
            gridLayout.removeView(image10)
        }
        if (inventory!!.char10 >= 1){
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char10
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char10)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.char11 <= 1) {
            gridLayout.removeView(image11)
        }
        if (inventory!!.char11 >= 1){
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_char11
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char11)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }
        if (inventory!!.charHiden <= 1) {
            gridLayout.removeView(imagehiden)
        }
        if (inventory!!.charHiden >= 1){
            val newImageView = ImageView(requireContext()).apply {
                id = R.id.select_charhiden
                layoutParams = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.image_width)
                    height = resources.getDimensionPixelSize(R.dimen.image_height)
                }
                setImageResource(R.drawable.level_up_char_hiden2)
                setBackgroundResource(R.drawable.char_bg)
            }
            gridLayout.addView(newImageView)
            imageViews.add(newImageView)
        }

        imageViews.forEach {
            it.setOnClickListener {
                imageViews.forEach {
                    it.setBackgroundResource(R.drawable.char_bg)
                }

                it.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

                when(it.id){
                    R.id.select_char1 -> binding.myChar.setImageResource(R.drawable.level_up_char1)
                    R.id.select_char2 -> binding.myChar.setImageResource(R.drawable.level_up_char2)
                    R.id.select_char3 -> binding.myChar.setImageResource(R.drawable.level_up_char3)
                    R.id.select_char4 -> binding.myChar.setImageResource(R.drawable.level_up_char4)
                    R.id.select_char5 -> binding.myChar.setImageResource(R.drawable.level_up_char5)
                    R.id.select_char6 -> binding.myChar.setImageResource(R.drawable.level_up_char6)
                    R.id.select_char7 -> binding.myChar.setImageResource(R.drawable.level_up_char7)
                    R.id.select_char8 -> binding.myChar.setImageResource(R.drawable.level_up_char8)
                    R.id.select_char9 -> binding.myChar.setImageResource(R.drawable.level_up_char9)
                    R.id.select_char10 -> binding.myChar.setImageResource(R.drawable.level_up_char10)
                    R.id.select_char11 -> binding.myChar.setImageResource(R.drawable.level_up_char11)
                    R.id.select_charhiden -> binding.myChar.setImageResource(R.drawable.level_up_char_hiden2)
                }
            }
        }

        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }
    }

}
