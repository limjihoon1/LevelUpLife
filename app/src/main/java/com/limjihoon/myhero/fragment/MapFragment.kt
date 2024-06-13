package com.limjihoon.myhero.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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


    }
    private val mapLifiCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {

        }

        override fun onMapError(p0: Exception?) {

        }

    }

    private val mapShow: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {

            val latitude: Double = (activity as MainActivity).myLocation?.latitude ?: 37.555
            val longitude: Double = (activity as MainActivity).myLocation?.longitude ?: 126.9746
            val mypos: LatLng = LatLng.from(latitude, longitude)

            val cameraUpdate = CameraUpdateFactory.newCenterPosition(mypos, 16)
            kakaoMap.moveCamera(cameraUpdate)

            val labelOptions = LabelOptions.from(mypos).setStyles(R.drawable.sw)
            val labelOptionsLayout = kakaoMap.labelManager!!.layer!!
            labelOptionsLayout.addLabel(labelOptions)

            val placeLists: List<DocumentOfPlace>? = (activity as MainActivity).kakaoData?.documents
            placeLists?.forEach {
                val mypo = LatLng.from(it.y.toDouble(), it.x.toDouble())
                val options = LabelOptions.from(mypo).setStyles(R.drawable.sll)
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
                    options.setBodyOffset(0f,-10f)
                    options.setTag(tag)
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)

                }
            }


        }


    }



}




