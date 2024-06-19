package com.limjihoon.myhero.activitis

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.LocationCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessaging
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.adapter.ViewPagerAdapter
import com.limjihoon.myhero.data.Inventory
import com.limjihoon.myhero.data.KakaoData
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.databinding.ActivityMainBinding
import com.limjihoon.myhero.fragment.HomeFragment
import com.limjihoon.myhero.fragment.ListFragment
import com.limjihoon.myhero.fragment.RendumFragment
import com.limjihoon.myhero.fragment.MapFragment
import com.limjihoon.myhero.fragment.SettingsFragment
import com.limjihoon.myhero.model.DataManager
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    val dataManager = DataManager()
    var tutorial = true
    var myLocation: Location? = null
    var kakaoData: KakaoData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navView: BottomNavigationView = findViewById(R.id.bnv)
        navView.itemIconTintList = null

        getMeberInventory()

        if (dataManager.memberFlow.value == null && dataManager.inventoryFlow.value == null) {
            Toast.makeText(this, "로딩중...", Toast.LENGTH_SHORT).show()
        }

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnv_home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, HomeFragment()).commit()

                R.id.bnv_search -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, MapFragment()).commit()

                R.id.bnv_notifications -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, ListFragment()).commit()

                R.id.bnv_profile -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, RendumFragment()).commit()

                R.id.bnv_settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, SettingsFragment()).commit()
            }
            true
        }
        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        checkFirstRun()
        val permissionstate =
            checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionLauncher: ActivityResultLauncher<String> =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it == true) {
                    startLast()
                } else {
                    AlertDialog.Builder(this).setMessage("위치정보 허용 해야함").create().show()
                    finish()
                }

            }
        if (permissionstate == PackageManager.PERMISSION_DENIED) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            Toast.makeText(this, "위치정보 허용", Toast.LENGTH_SHORT).show()
        } else {
            startLast()
        }

        askNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("qwer1", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("qwer2", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })


    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun getMeberInventory() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                val retrofitService = retrofit.create(RetrofitService::class.java)
                val response = retrofitService.getMember(G.uid).execute()

                if (response.isSuccessful && response.body() != null) {
                    dataManager.updateMember(response.body()!!)

                    val retrofit2 = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                    val retrofitService2 = retrofit2.create(RetrofitService::class.java)
                    val response2 = retrofitService2.getInventory(dataManager.memberFlow.value!!.no).execute()
//                    Log.d("asqw", "${response.body()}, ${response2.body()}")

                    if (response2.isSuccessful && response2.body() != null) {
                        dataManager.updateInventory(response2.body()!!)

                        supportFragmentManager.beginTransaction().add(R.id.frame, HomeFragment()).commit()
                    }
                }

            } catch (e: Exception) {
                Log.d("getErr", "${e.message}")
            }
        }
    }

    fun getMember() {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getMember(G.uid).enqueue(object : Callback<Member2> {
            override fun onResponse(p0: Call<Member2>, p1: Response<Member2>) {

                if (p1.body() != null) {
                    dataManager.updateMember(p1.body()!!)

                    getInventory(dataManager.memberFlow.value!!.no)
                } else {
                    Toast.makeText(this@MainActivity, "회원 정보가 넘어오지 않았습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("uid..", "${G.uid}")
                }


            }

            override fun onFailure(p0: Call<Member2>, p1: Throwable) {
                Toast.makeText(this@MainActivity, "${p1.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun getInventory(no: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getInventory(no).enqueue(object : Callback<Inventory> {
            override fun onResponse(p0: Call<Inventory>, p1: Response<Inventory>) {
                if (p1.body() != null) {
                    dataManager.updateInventory(p1.body()!!)
                } else {
                    Toast.makeText(this@MainActivity, "인벤토리 정보가 넘어오지 않았습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(p0: Call<Inventory>, p1: Throwable) {
                Toast.makeText(this@MainActivity, "${p1.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun startLast() {
        val request = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한이 부여되지 않았을 경우 처리
            return
        }

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }

    val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            myLocation = p0.lastLocation
            LocationServices.getFusedLocationProviderClient(this@MainActivity)
                .removeLocationUpdates(this)
            myStation()
        }

    }

    private fun myStation() {
        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        val call = retrofitService.kakoDataSearch(
            "지하철역",
            myLocation!!.longitude.toString(),
            myLocation!!.latitude.toString()
        )
        call.enqueue(object : Callback<KakaoData> {
            override fun onResponse(call: Call<KakaoData>, response: Response<KakaoData>) {
                kakaoData = response.body()

            }

            override fun onFailure(call: Call<KakaoData>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkFirstRun() {
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", tutorial)

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
                    tutorial = true

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
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setMessage("앱을 종료 하시겠습니까?")
            .setPositiveButton("확인") { dialog, which ->
                super.onBackPressed()
            }
            .setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}





