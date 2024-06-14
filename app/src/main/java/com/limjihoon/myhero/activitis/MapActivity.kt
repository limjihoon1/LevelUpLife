package com.limjihoon.myhero.activitis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
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
import com.limjihoon.myhero.R
import com.limjihoon.myhero.data.DocumentOfPlace
import com.limjihoon.myhero.data.KakaoData
import com.limjihoon.myhero.databinding.ActivityMapBinding
import java.lang.Exception

class MapActivity : AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    var kakaoData: KakaoData?=null
    val mapView : MapView by lazy { findViewById(R.id.map_view) }
    var myLocation:Location ?=null
    val locationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    var lat:Double=0.0
    var lng:Double=0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lat=intent.getDoubleExtra("lat",0.0)
        lng=intent.getDoubleExtra("lng",0.0)
        mapView.start(mapLifiCycleCallback, mapShow)

    }

    private val mapLifiCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {

        }

        override fun onMapError(p0: Exception?) {

        }

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
            positions.add(LatLng.from(37.556, 126.977))
            positions.add(LatLng.from(37.557, 126.971))
            positions.add(LatLng.from(37.576, 126.967))
            positions.add(LatLng.from(37.556, 126.969))

            //포지션 개수만큼 라커 추가
            for (pos in positions) {
                var op: LabelOptions = LabelOptions.from(pos).setStyles(R.drawable.qqqqq)
                    .setTexts("위도 : ${pos.latitude} , 경도 :${pos.longitude}")
                //카머 추가
                kakaoMap.labelManager!!.layer!!.addLabel(op)
            }


            //라벨 클릭할 때 반응하기
            kakaoMap.setOnLabelClickListener(object : KakaoMap.OnLabelClickListener {
                override fun onLabelClicked(
                    kakaoMap: KakaoMap?,
                    layer: LabelLayer?,
                    label: Label?
                ) {
                    //정보창 [ INFO WINDOW ]
                    val layout: GuiLayout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16, 16, 16, 16)
                    layout.setBackground(R.drawable.char_bg, true)

                    //라벨 글씨를 보여주는 TextView 같은 GuiText
                    label!!.texts.forEach {
                        val guiText: GuiText = GuiText(it)
                        guiText.setTextSize(30)
                        guiText.setTextColor(Color.WHITE)
                        layout.addView(guiText)

                    }

                    //정보창 객체 만들기
                    val infoWindoOptions: InfoWindowOptions = InfoWindowOptions.from(label.position)
                    infoWindoOptions.body = layout
                    infoWindoOptions.tag = "https://www.mrhi.or.kr"

                    kakaoMap!!.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap!!.mapWidgetManager!!.infoWindowLayer.addInfoWindow(infoWindoOptions)

                }
            })
            //정보창 클릭 처리
            kakaoMap.setOnInfoWindowClickListener(object : KakaoMap.OnInfoWindowClickListener {
                override fun onInfoWindowClicked(
                    kakaoMap: KakaoMap?,
                    infoWindow: InfoWindow?,
                    guiId: String?
                ) {
                    val info: String = infoWindow!!.body.getTag().toString()

                    val intent: Intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(info))
                }

            })

        }

    }
}