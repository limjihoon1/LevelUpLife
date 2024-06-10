package com.limjihoon.myhero.activitis

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.ListFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.limjihoon.myhero.R
import com.limjihoon.myhero.adapter.ViewPagerAdapter
import com.limjihoon.myhero.databinding.ActivityMainBinding
import com.limjihoon.myhero.fragment.HomeFragment
import com.limjihoon.myhero.fragment.NotificationsFragment
import com.limjihoon.myhero.fragment.ProfileFragment
import com.limjihoon.myhero.fragment.SearchFragment
import com.limjihoon.myhero.fragment.SettingsFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    var tutorial=true
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.frame, HomeFragment()).commit()

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnv_home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, HomeFragment()).commit()

                R.id.bnv_search -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, SearchFragment()).commit()

                R.id.bnv_notifications -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, NotificationsFragment()).commit()

                R.id.bnv_profile -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, ProfileFragment()).commit()

                R.id.bnv_settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, SettingsFragment()).commit()
            }
            true
        }
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        checkFirstRun()

    }

    private fun checkFirstRun() {
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            showMultiPageDialog()
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstRun", tutorial)
            editor.apply()
        }
    }

    private fun showMultiPageDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custum_dialog_viewpager, null)
        val viewPager = dialogView.findViewById<ViewPager2>(R.id.view_pager)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter


        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Next", null)
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()


        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == adapter.itemCount - 1) {
                    positiveButton.text = "확인 (다시는 보지 않음)"
                    tutorial=true

                } else {
                    positiveButton.text = "다음으로"
                }
            }
        })


        positiveButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < adapter.itemCount - 1) {

                viewPager.currentItem = currentItem + 1
            } else {

                dialog.dismiss()
            }
        }
    }
}





