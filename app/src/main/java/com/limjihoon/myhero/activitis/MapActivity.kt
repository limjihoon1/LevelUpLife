package com.limjihoon.myhero.activitis

import android.Manifest
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.mapwidget.InfoWindow
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
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.databinding.ActivityMapBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MapActivity : AppCompatActivity() {
    lateinit var binding: ActivityMapBinding

    val mapView : MapView by lazy { findViewById(R.id.map_view) }
    var myLocation:Location ?=null
    val locationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    var searchQuery:String="화장실"
    var latitude:Double=35.1796
    var longitude:Double=129.0756
    private var todouid =G.uid
    var searchPlaceResponse:KakaoData?=null

    var items = mutableListOf<Markers>()
    var kakaoMap:KakaoMap? =null

    var lat:Double=0.0
    var lng:Double=0.0
    var ss: Double = 35.55
    var tt: Double = 127.632
    var document :List<DocumentOfPlace>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lat=intent.getDoubleExtra("lat",0.0)
        lng=intent.getDoubleExtra("lng",0.0)

        searchPlaces()

        binding.search.setOnClickListener {
            searchQuery=binding.et.text.toString()
            searchPlaces()
//            recreate()
        }

    }

    private val mapLifiCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {

        }

        override fun onMapError(p0: Exception?) {

        }

    }


    fun searchPlaces(){
        val retrofit =RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        val call = retrofitService.kakaoSearchPlaceToString3(searchQuery, lng.toString(),lat.toString())

        call.enqueue(object :Callback<KakaoData>{
            override fun onResponse(
                call: Call<KakaoData>,
                response: Response<KakaoData>
            ) {
                // 응답받은 json 을 파싱한 객체 를 참조
                searchPlaceResponse = response.body()
                //먼저 데이터가 온전히 잘 왔는지 파악
                var meta :MetaOfPlace? = searchPlaceResponse?.meta
                document  = searchPlaceResponse?.documents
                if (document.isNullOrEmpty()){
                    androidx.appcompat.app.AlertDialog.Builder(this@MapActivity).setMessage("검색 결과가 없습니다").create().show()
                }else{
                    ss = document?.get(0)?.x?.toDouble() ?: 37.55
                    tt = document?.get(0)?.y?.toDouble() ?: 129.07

                    //마커설정
                    Toast.makeText(this@MapActivity, "$searchQuery\n${document?.get(0)?.x} , ${document?.get(0)?.y}", Toast.LENGTH_SHORT).show()
                    mapView.start(mapLifiCycleCallback,mapShow)
                }


            }

            override fun onFailure(call: Call<KakaoData>, t: Throwable) {
                androidx.appcompat.app.AlertDialog.Builder(this@MapActivity).setMessage("서버 오류 가 있습니다").create().show()
            }

        })




    }
    private fun requestMyrecatiomn(){
        //요청 객체 생성
//        val request:LocationRequest =LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).build()
        val request: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).build()

        //실시간으로 위치정보 갱신을 요청 - 퍼미션 체크 코드가 있어야만 함.
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {return
        }

        locationProviderClient.requestLocationUpdates(request,locationCallback, Looper.getMainLooper())
    }
    private val locationCallback: LocationCallback =object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            myLocation=p0.lastLocation
            //위치정보 탐색이 종료 되었으니 내 위치정보 업데이트를 이제 그만 하기
            locationProviderClient.removeLocationUpdates(this)//이 디스는 locationCallback 객체를 말함
        }
    }
    private val mapShow: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            // 맵에 로딩이 완료되면 실행되는 영역


            //카메라 위치 이동
            val myPos: LatLng = LatLng.from(lat,lng)
            var cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(myPos, 16)
            kakaoMap.moveCamera(cameraUpdate)

            //내 위치에 마커 만들이 - 아이콘모양 [ 백터는 안됨 ]
            //라벨(마커)의 위치와 스타일 지정하는 옵션 객체 생성
            var labelOptions: LabelOptions = LabelOptions.from(myPos).setStyles(R.drawable.eeeee)

            //라벨의 레이어 얻어오기
            val labelLayer: LabelLayer = kakaoMap.labelManager!!.layer!!

            // 라벨 추가
            labelLayer.addLabel(labelOptions)

            //여러지점에 대한 마커들 추가하기
            val positions: MutableList<LatLng> = mutableListOf()
            positions.add(LatLng.from(latitude, longitude))


            val placeLists:List<DocumentOfPlace>? = searchPlaceResponse?.documents
            placeLists?.forEach {
                //마커(라벨) 옵션 객체
                Log.d("마커", it.place_name)
                val mypo = LatLng.from(it.y.toDouble(),it.x.toDouble())
                val options =LabelOptions.from(mypo).setStyles(R.drawable.ic_pin).setTexts(it.place_name,"${it.distance}m").setTag(it)
                kakaoMap.labelManager!!.layer!!.addLabel(options)
            }


            //포지션 개수만큼 라커 추가
            for (pos in positions) {
                var op: LabelOptions = LabelOptions.from(pos).setStyles(R.drawable.qqqqq)
                    .setTexts("위도 : ${pos.latitude} , 경도 :${pos.longitude}")
                //카머 추가
                kakaoMap.labelManager!!.layer!!.addLabel(op)
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
                    options.setBodyOffset(0f, -150f)
                    options.setTag(tag)
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)
                }
            }
            kakaoMap.setOnInfoWindowClickListener { kakaoMap, infoWindow, guiId ->
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

                        retrofitService.insertMap(todouid, ss, place.y.toDouble(), place.x.toDouble()).enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.isSuccessful && response.body() != null) {
                                    Toast.makeText(this@MapActivity, "업데이트 성공: ${response.body()}", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(this@MapActivity, "업데이트 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                                    Log.d("업데이트 실패", "응답 실패: ${response.errorBody()?.string()}")
                                }
                                Log.d("성공", response.body().toString())
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Toast.makeText(this@MapActivity, "요청 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                                Log.d("실패", t.message.toString())
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

}