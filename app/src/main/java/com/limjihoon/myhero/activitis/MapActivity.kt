package com.limjihoon.myhero.activitis

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.data.DocumentOfPlace
import com.limjihoon.myhero.data.KakaoData
import com.limjihoon.myhero.data.Markers
import com.limjihoon.myhero.data.MetaOfPlace
import com.limjihoon.myhero.databinding.ActivityMapBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MapActivity : AppCompatActivity() {
    lateinit var binding: ActivityMapBinding

    val mapView: MapView by lazy { findViewById(R.id.map_view) }
    var myLocation: Location? = null
    val locationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    var searchQuery: String = "ㄱ"
    var latitude: Double = 35.1796        //내위치
    var longitude: Double = 129.0756
    private var todouid = G.uid
    var searchPlaceResponse: KakaoData? = null

    var items = mutableListOf<Markers>()
    var kakaoMap: KakaoMap? = null

    var lat: Double = 0.0     //검색위치
    var lng: Double = 0.0
    var lat2: Double = 0.0    //터치 위치
    var lng2: Double = 0.0

    var ss: Double = 35.55
    var tt: Double = 127.632
    var document: List<DocumentOfPlace>? = null

    private var isLabelAdded = false
    private var currentLabel: Label? = null
    private var latLng2: LatLng? = null

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lat = intent.getDoubleExtra("lat", 0.0)
        lng = intent.getDoubleExtra("lng", 0.0)

        searchPlaces()

        binding.search.setOnClickListener {
            searchQuery = binding.et.text.toString()
            searchPlaces()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한이 허용되면 현재 위치를 요청
                requestMyLocation()
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestMyLocation() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            myLocation = p0.lastLocation
            // 위치 정보를 받아서 카메라 이동
            moveCameraToMyLocation()
            locationProviderClient.removeLocationUpdates(this)
        }
    }

    private fun moveCameraToMyLocation() {
        myLocation?.let {
            val myPos = LatLng.from(it.latitude, it.longitude)
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(myPos, 16)
            kakaoMap?.moveCamera(cameraUpdate)
        }
    }

    private val mapLifiCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {}

        override fun onMapError(p0: Exception?) {}
    }

    private val mapShow: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            this@MapActivity.kakaoMap = kakaoMap

            // 위치 권한 확인 및 요청
            if (ActivityCompat.checkSelfPermission(
                    this@MapActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this@MapActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermission()
            } else {
                requestMyLocation()
            }

            val myPos: LatLng = LatLng.from(lat, lng)
            var cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(myPos, 16)
            kakaoMap.moveCamera(cameraUpdate)
            if (searchQuery != "ㄱ") {
                val searchPos: LatLng = LatLng.from(tt, ss)
                cameraUpdate = CameraUpdateFactory.newCenterPosition(searchPos, 16)
                kakaoMap.moveCamera(cameraUpdate)
            }

            var labelOptions: LabelOptions = LabelOptions.from(myPos).setStyles(R.drawable.eeeee)
            val labelLayer: LabelLayer = kakaoMap.labelManager!!.layer!!
            labelLayer.addLabel(labelOptions)

            val positions: MutableList<LatLng> = mutableListOf()
            positions.add(LatLng.from(latitude, longitude))

            val placeLists: List<DocumentOfPlace>? = searchPlaceResponse?.documents
            placeLists?.forEach {
                val mypo = LatLng.from(it.y.toDouble(), it.x.toDouble())
                val options = LabelOptions.from(mypo)
                    .setStyles(R.drawable.ic_pin)
                    .setTexts(it.place_name, "${it.distance}m")
                    .setTag(it)
                kakaoMap.labelManager!!.layer!!.addLabel(options)
            }

            binding.btnAddMaker.setOnClickListener {
                Toast.makeText(this@MapActivity, "원하는 위치의 라벨을 추가해주세요!!", Toast.LENGTH_SHORT)
                    .show()

                kakaoMap.setOnMapClickListener { kakaoMap, latLng, pointF, poi ->
                    if (isLabelAdded) {
                        return@setOnMapClickListener
                    }
                    currentLabel?.let {
                        kakaoMap.labelManager!!.layer!!.remove(it)
                    }
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()

                    val options = LabelOptions.from(latLng).setStyles(R.drawable.ic_pin)
                    val newLabel = kakaoMap.labelManager!!.layer!!.addLabel(options)
                    currentLabel = newLabel
                    latLng2 = latLng
                    lat2 = latLng.latitude
                    lng2 = latLng.longitude

                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16, 16, 16, 16)
                    layout.setBackground(R.drawable.char_bg, true)
                    val infoText = GuiText("이 곳에 일정 추가 하기")
                    infoText.setTextSize(28)
                    infoText.setTextColor(Color.WHITE)
                    layout.addView(infoText)
                    val infoOptions = InfoWindowOptions.from(latLng)
                    infoOptions.body = layout
                    infoOptions.setBodyOffset(0f, -100f)
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(infoOptions)

                    isLabelAdded = true
                }
                isLabelAdded = false
            }

            kakaoMap.setOnLabelClickListener { kakaoMap, layer, label ->
                label.apply {
                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16, 16, 16, 16)
                    layout.setBackground(R.drawable.char_bg, true)
                    texts.forEach {
                        val textt = GuiText(it)
                        textt.setTextSize(28)
                        textt.setTextColor(Color.WHITE)
                        layout.addView(textt)
                    }
                    val options = InfoWindowOptions.from(label.position)
                    options.body = layout
                    options.setBodyOffset(0f, -100f)
                    options.setTag(tag)
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)
                }
            }

            kakaoMap.setOnInfoWindowClickListener { kakaoMap, infoWindow, guiId ->
                if (lat2 != 0.0) {
                    val builder = AlertDialog.Builder(this@MapActivity)
                    val inflater = layoutInflater
                    val dialogView = inflater.inflate(R.layout.custum_dialog_input_todo_map, null)
                    builder.setView(dialogView)

                    val dialog = builder.create()
                    dialog.show()

                    val btnconfirm: Button = dialogView.findViewById(R.id.confirmButton)
                    val todolist: EditText = dialogView.findViewById(R.id.scheduleEditText)
                    val btncancel: Button = dialogView.findViewById(R.id.cancelButton)

                    btnconfirm.setOnClickListener {
                        val ss = todolist.text.toString()

                        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                        val retrofitService = retrofit.create(RetrofitService::class.java)

                        retrofitService.insertMap(G.uid, ss, lat2, lng2, 0)
                            .enqueue(object : Callback<String> {
                                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                                    Toast.makeText(this@MapActivity, "${p1.body()}", Toast.LENGTH_SHORT)
                                        .show()
                                    currentLabel?.let {
                                        kakaoMap.labelManager!!.layer!!.remove(it)
                                    }
                                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()

                                    val options = LabelOptions.from(latLng2).setStyles(R.drawable.qqqqq)
                                    val newLabel = kakaoMap.labelManager!!.layer!!.addLabel(options)
                                    currentLabel = newLabel
                                    lat2 = 0.0
                                    dialog.dismiss()
                                }

                                override fun onFailure(p0: Call<String>, p1: Throwable) {
                                    Log.d("일정 추가 실패", "응답 실패: ${p1.message}")
                                }
                            })
                    }
                    btncancel.setOnClickListener {
                        currentLabel?.let {
                            kakaoMap.labelManager!!.layer!!.remove(it)
                        }
                        kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                        dialog.dismiss()
                        return@setOnClickListener
                    }
                    return@setOnInfoWindowClickListener
                }

                val tag = infoWindow.tag
                if (tag is DocumentOfPlace) {
                    val place = tag
                    val builder = AlertDialog.Builder(this@MapActivity)
                    val inflater = layoutInflater
                    val dialogView = inflater.inflate(R.layout.custum_dialog_input_todo_map, null)
                    builder.setView(dialogView)
                    val tv: TextView = dialogView.findViewById(R.id.tv_place)
                    tv.text = place.place_name

                    val dialog = builder.create()
                    dialog.show()

                    val btnconfirm: Button = dialogView.findViewById(R.id.confirmButton)
                    val todolist: EditText = dialogView.findViewById(R.id.scheduleEditText)
                    val btncancel: Button = dialogView.findViewById(R.id.cancelButton)

                    btnconfirm.setOnClickListener {
                        val ss = todolist.text.toString()

                        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
                        val retrofitService = retrofit.create(RetrofitService::class.java)

                        retrofitService.insertMap(G.uid, ss, tt, this@MapActivity.ss, 0)
                            .enqueue(object : Callback<String> {
                                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                                    Toast.makeText(this@MapActivity, "일정 추가 성공: ${p1.body()}", Toast.LENGTH_SHORT)
                                        .show()

                                    currentLabel?.let {
                                        kakaoMap.labelManager!!.layer!!.remove(it)
                                    }
                                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                                    var latLng = LatLng.from(tt, this@MapActivity.ss)

                                    val options = LabelOptions.from(latLng).setStyles(R.drawable.qqqqq)
                                    val newLabel = kakaoMap.labelManager!!.layer!!.addLabel(options)
                                    currentLabel = newLabel

                                    dialog.dismiss()
                                }

                                override fun onFailure(p0: Call<String>, p1: Throwable) {
                                    Log.d("일정 추가 실패", "응답 실패: ${p1.message}")
                                }
                            })
                    }
                    btncancel.setOnClickListener {
                        dialog.dismiss()
                    }
                } else {
                    Toast.makeText(this@MapActivity, "장소 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun searchPlaces() {
        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        val call = retrofitService.kakaoSearchPlaceToString3(searchQuery, lng.toString(), lat.toString())

        call.enqueue(object : Callback<KakaoData> {
            override fun onResponse(
                call: Call<KakaoData>,
                response: Response<KakaoData>
            ) {
                searchPlaceResponse = response.body()
                val meta: MetaOfPlace? = searchPlaceResponse?.meta
                document = searchPlaceResponse?.documents
                if (document.isNullOrEmpty()) {
                    AlertDialog.Builder(this@MapActivity).setMessage("검색 결과가 없습니다").create().show()
                } else {
                    ss = document?.get(0)?.x?.toDouble() ?: 37.55
                    tt = document?.get(0)?.y?.toDouble() ?: 129.07

                    mapView.start(mapLifiCycleCallback, mapShow)
                }
            }

            override fun onFailure(call: Call<KakaoData>, t: Throwable) {
                AlertDialog.Builder(this@MapActivity).setMessage("서버 오류 가 있습니다").create().show()
            }
        })
    }
}