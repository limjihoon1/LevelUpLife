package com.limjihoon.myhero.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.data.DocumentOfPlace
import com.limjihoon.myhero.databinding.FragmentSearchBinding
import java.lang.Exception


class MapFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    private var kakaoMap: KakaoMap? = null
    var search=""
    private val handler = Handler()
    private val updateTask = object : Runnable {
        override fun run() {
            search()  // search 메소드 호출
            handler.postDelayed(this, 1000)  // 1초마다 반복
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

    return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.start(mapLifiCycleCallback, mapShow)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.qBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        binding.mySw.setOnClickListener { moveToMyLocation() }
        binding.search.setOnClickListener { search() }

        handler.post(updateTask)
    }
    private val mapLifiCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {

        }

        override fun onMapError(p0: Exception?) {

        }

    }

    private val mapShow: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            this@MapFragment.kakaoMap = kakaoMap

            val latitude: Double = (activity as MainActivity).myLocation?.latitude ?: 37.555
            val longitude: Double = (activity as MainActivity).myLocation?.longitude ?: 126.9746
            val mypos: LatLng = LatLng.from(latitude, longitude)

            val cameraUpdate = CameraUpdateFactory.newCenterPosition(mypos, 16)
            kakaoMap.moveCamera(cameraUpdate)

            val labelOptions = LabelOptions.from(mypos).setStyles(R.drawable.eeeee)
            val labelOptionsLayout = kakaoMap.labelManager!!.layer!!
            labelOptionsLayout.addLabel(labelOptions)

            val placeLists: List<DocumentOfPlace>? = (activity as MainActivity).kakaoData?.documents
            placeLists?.forEach {
                val mypo = LatLng.from(it.y.toDouble(), it.x.toDouble())
                val options = LabelOptions.from(mypo).setStyles(R.drawable.qqqqq)
                    .setTexts(it.place_name, "${it.distance}m").setTag(it)
                    
                kakaoMap.labelManager!!.layer!!.addLabel(options)
            }
            kakaoMap.setOnLabelClickListener { kakaoMap, layer, label ->
                label.apply {
                    val layout= GuiLayout(Orientation.Vertical)
                    layout.setPadding(16,16,16,16)
                    layout.setBackground(R.drawable.char_bg,true)
                    texts.forEach {
                        val textt= GuiText(it)
                        textt.setTextSize(28)
                        textt.setTextColor(android.graphics.Color.WHITE)
                        layout.addView(textt)
                    }
                    val options= InfoWindowOptions.from(label.position)
                    options.body=layout
                    options.setBodyOffset(0f,-150f)
                    options.setTag(tag)
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)

                }
            }
        }
    }
    private fun search(){

    }
    private fun moveToMyLocation() {
        kakaoMap?.let {
            val latitude: Double = (activity as MainActivity).myLocation?.latitude ?: 37.555
            val longitude: Double = (activity as MainActivity).myLocation?.longitude ?: 126.9746
            val mypos: LatLng = LatLng.from(latitude, longitude)

            it.moveCamera(CameraUpdateFactory.newCenterPosition(mypos, 16))
        } ?: run {
            Toast.makeText(activity, "잠시 기다린후 다시한번 눌러주세요", Toast.LENGTH_SHORT).show()
        }
    }

}