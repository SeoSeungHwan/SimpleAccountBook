package com.soft.simpleaccountbook.view.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.databinding.ActivityLoginBinding
import com.soft.simpleaccountbook.util.ToastMessageUtil
import com.soft.simpleaccountbook.view.base.BaseActivityForViewBinding

class LoginActivity : BaseActivityForViewBinding<ActivityLoginBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_login

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    override fun onStart() {
        super.onStart()
        val currentUser = GlobalApplication.auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    override fun init() {
        settingActivityLauncher()
        setUpBtnListener()
    }

    private fun settingActivityLauncher() {
        activityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    var task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        var account = task.getResult(ApiException::class.java)!!
                        firebaseAuthWithGoogle(account.idToken!!)
                        Log.d("GoogleLogin", "firebaseAuthWithGoogle: " + account.id)
                    } catch (e: ApiException) {
                        Log.d("GoogleLogin", "Google sign in failed: " + e.message)
                        ToastMessageUtil().showShortToast(this,"로그인에 실패하였습니다.")
                    }
                }else{
                    ToastMessageUtil().showShortToast(this,"로그인에 실패하였습니다.")
                }
            }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        GlobalApplication.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = GlobalApplication.auth.currentUser
                    if (user != null) {
                        updateUI(user)
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    ToastMessageUtil().showShortToast(this,"로그인에 실패하였습니다.")
                }
            }
    }

    private fun updateUI(user : FirebaseUser){
        val intent = Intent(this,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        GlobalApplication.mySharedPreferences.setString("uid",user.uid)
        startActivity(intent)
    }

    private fun setUpBtnListener() {
        viewDataBinding.signInButton.setOnClickListener {
            activityLauncher.launch(GlobalApplication.googleSignInClient.signInIntent)
        }
    }
}