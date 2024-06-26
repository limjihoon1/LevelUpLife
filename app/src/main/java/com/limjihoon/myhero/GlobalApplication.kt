package com.limjihoon.myhero

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        //카카오 SDK 초기화 작업
//        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        //카카오맵 SDK 초기화 작업
        KakaoMapSdk.init(this, getString(R.string.kakao_map))
    }
}