package com.soft.simpleaccountbook.common

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soft.simpleaccountbook.BuildConfig

class GlobalApplication: Application() {

    companion object{
        lateinit var auth : FirebaseAuth
        lateinit var googleSignInClient: GoogleSignInClient
    }
    override fun onCreate() {
        super.onCreate()

        // 앱 내 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //Firebase로그인 초기화
        initFirebaseAuth()
    }

    private fun initFirebaseAuth() {
        auth = Firebase.auth

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.default_web_client_id)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }


}