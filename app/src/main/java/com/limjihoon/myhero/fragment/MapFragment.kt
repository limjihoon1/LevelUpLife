package com.limjihoon.myhero.fragment
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.activitis.MapActivity
import com.limjihoon.myhero.adapter.MapDrawerAdapter
import com.limjihoon.myhero.data.DocumentOfPlace
import com.limjihoon.myhero.data.Markers
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.databinding.FragmentSearchBinding
import com.limjihoon.myhero.model.DataManager
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.*



class MapFragment : Fragment(), MapDrawerAdapter.OnItemClickListener {
    lateinit var binding: FragmentSearchBinding
    private var kakaoMap: KakaoMap? = null
    var search = ""
    private var todouid = G.uid
    var latitude = 0.0
    var longityde = 0.0
    var items = mutableListOf<Markers>()
    var itemsd = mutableListOf<Todo>()
    private val handler = Handler()
    private var ma: MainActivity? = null
    var uid = ""


    private lateinit var dataManager: DataManager
    private lateinit var recyclerViewAdapter: MapDrawerAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val labelMap = HashMap<String, LabelOptions>()



    val retrofitServiceLoad: RetrofitService by lazy {
        RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr").create(RetrofitService::class.java)
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

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // LocationRequest 설정
        locationRequest = LocationRequest.create().apply {
            interval = 3000 // 10 seconds
            fastestInterval = 2000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // LocationCallback 설정
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    // 위치가 갱신될 때마다 지도 업데이트
                    updateMapLocation(location.latitude, location.longitude)

                }
            }
        }
        startLocationUpdates()



        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.start(mapLifiCycleCallback, mapShow)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.qBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
//        binding.searchBtn.setOnClickListener {
//            startActivity(Intent(requireContext(), MapActivity::class.java))
//        }
        binding.mySw.setOnClickListener {
            val latitude: Double = (activity as MainActivity).myLocation?.latitude ?: 37.555
            val longitude: Double = (activity as MainActivity).myLocation?.longitude ?: 126.9746

            updateMapLocation2(latitude, longitude) }


        val navigationView = view.findViewById<View>(R.id.navigation_view)
        val recyclerView = navigationView.findViewById<RecyclerView>(R.id.rec)
        recyclerViewAdapter = MapDrawerAdapter(requireContext(), items, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerViewAdapter
        }

        activity?.let {
            if (it is MainActivity) {
                ma = it
                dataManager = it.dataManager
                dataManager.memberFlow.value ?: return
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataManager.memberFlow.collect { member ->
                    updateInfo(member)
                }
            }
        }



    }

    override fun onResume() {
        super.onResume()
        fetchTodos()
    }

    override fun onItemClick(marker: Markers) {
        moveToMarkerLocation(marker.lat, marker.lng)

    }

    private fun moveToMarkerLocation(lat: Double, lng: Double) {
        val mypos: LatLng = LatLng.from(lat, lng)
        kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(mypos, 16)) ?: run {
            Toast.makeText(activity, "맵이 준비되지 않았습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateInfo(member: Member2?) {
        member?.let {
            uid = it.uid
        }

    }

    private fun fetchTodos() {
        retrofitServiceLoad.getTodo(uid).enqueue(object : Callback<List<Todo>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                val data = response.body()
                data?.let {
                    itemsd.clear()
                    itemsd.addAll(it)
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                Log.d("etodo", "${t.message}")
            }
        })
    }

    private val mapLifiCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {}

        override fun onMapError(p0: Exception?) {}
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

            loadMapMarkers(kakaoMap)

            val placeLists: List<DocumentOfPlace>? = (activity as MainActivity).kakaoData?.documents
            placeLists?.forEach {
                val mypo = LatLng.from(it.y.toDouble(), it.x.toDouble())
                val options = LabelOptions.from(mypo).setStyles(R.drawable.ic_pin)
                    .setTexts(it.place_name, "${it.distance}m").setTag(it)

                kakaoMap.labelManager!!.layer!!.addLabel(options)
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
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateTodoQuest(position: Int, exp: Int, level: Int, qcc: Int) {
        if (position >= itemsd.size) {
            Log.e("updateTodoQuest", "Invalid index: $position, size: ${itemsd.size}")
            return
        }

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)


        Log.d("updateTodoQuest", "${itemsd[position].uid}")
            retrofitService.updateQuest(position,itemsd[position].uid,itemsd[position].quest,exp,level,qcc).enqueue(object : Callback<String> {

                override fun onResponse(p0: Call<String>, p1: Response<String>) {

                    Log.d("updateTodoQuest", "${p1.body()}")
                    itemsd.removeAt(position)
                    recyclerViewAdapter.notifyItemRemoved(position)
                    ma?.getMember()
                    Toast.makeText(context, "${p1.body()}", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {
                    Toast.makeText(context, "업데이트 에러: ${p1.message}", Toast.LENGTH_SHORT).show()
                    Log.d("error", "${p1.message}")
                }

            })



    }
        private fun loadMapMarkers(kakaoMap: KakaoMap) {

        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.loadMap(G.uid).enqueue(object : Callback<List<Markers>> {
            override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {
                var data = response.body()
                data?.let {
                    items.clear()
                    items.addAll(it)
                    recyclerViewAdapter.notifyDataSetChanged()

                    items.forEach {
                        val mypo = LatLng.from(it.lat, it.lng)
                        val options = LabelOptions.from(mypo).setStyles(R.drawable.qqqqq)
                            .setTexts(it.workTodo).setTag(it)

                        kakaoMap.labelManager!!.layer!!.addLabel(options)
                        val latitude: Double = (activity as MainActivity).myLocation?.latitude ?: 37.555
                        val longitude: Double = (activity as MainActivity).myLocation?.longitude ?: 126.9746
                        if (isWithin50Meters(latitude, longitude, it.lat, it.lng)) {
                            Toast.makeText(activity, "마커가 50m 이내에 있습니다: ${it.workTodo}", Toast.LENGTH_SHORT).show()
                            //완료 시키기
                            updateTodoQuest(position = items.indexOf(it), ma?.dataManager?.memberFlow?.value!!.exp, ma?.dataManager?.memberFlow?.value!!.level, ma?.dataManager?.memberFlow?.value!!.qcc)


                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                Log.e("RetrofitError", t.message.toString())
            }
        })
    }

    private fun isWithin50Meters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        val R = 6371e3
        val phi1 = lat1 * PI / 180
        val phi2 = lat2 * PI / 180
        val deltaPhi = (lat2 - lat1) * PI / 180
        val deltaLambda = (lon2 - lon1) * PI / 180

        val a = sin(deltaPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = R * c
        return distance <= 50   /*미터 단위*/
    }

    private fun moveToMyLocation() {
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없으면 권한 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun updateMapLocation(lat: Double, lng: Double) {
        val mypos:LatLng = LatLng.from(lat, lng)
        kakaoMap?.labelManager?.layer?.removeAll()
        // 라벨 추가하기
        val labelOptions = LabelOptions.from(mypos)
            .setTexts("여기에 라벨 텍스트", "부가 정보")
            .setStyles(R.drawable.eeeee) // 원하는 스타일로 변경

        kakaoMap?.labelManager?.layer?.addLabel(labelOptions)
        loadMapMarkers(kakaoMap!!)

    }
    private fun updateMapLocation2(lat: Double, lng: Double) {

        val mypos:LatLng = LatLng.from(lat, lng)
        kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(mypos, 16)) ?: run {
            Toast.makeText(activity, "맵이 준비되지 않았습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}