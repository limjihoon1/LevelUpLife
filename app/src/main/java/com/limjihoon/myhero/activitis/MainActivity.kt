package com.limjihoon.myhero.activitis

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.ListFragment
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.ActivityMainBinding
import com.limjihoon.myhero.fragment.HomeFragment
import com.limjihoon.myhero.fragment.NotificationsFragment
import com.limjihoon.myhero.fragment.ProfileFragment
import com.limjihoon.myhero.fragment.SearchFragment
import com.limjihoon.myhero.fragment.SettingsFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
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

    }


}
