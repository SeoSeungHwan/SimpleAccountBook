package com.soft.simpleaccountbook.common

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class GlobalApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // 앱 내 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}