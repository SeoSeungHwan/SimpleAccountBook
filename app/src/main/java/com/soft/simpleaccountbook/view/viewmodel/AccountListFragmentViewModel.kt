package com.soft.simpleaccountbook.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakaobrain.pathfinder_prodo.viewmodel.base.BaseMyRepositoryViewModel
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.model.data.AccountBookItem
import com.soft.simpleaccountbook.model.repository.MyRepository

class AccountListFragmentViewModel(override val myRepository: MyRepository): BaseMyRepositoryViewModel(){

    private val _accountListLiveData = MutableLiveData<List<AccountBookItem>>()
    val accountListLiveData : LiveData<List<AccountBookItem>>
        get() = _accountListLiveData

    fun getAccountList(){
        GlobalApplication.db.collection(GlobalApplication.mySharedPreferences.getString("uid",null)).get()
            .addOnSuccessListener {
                Log.d("Response", "getAccountList: ${it.documents.toString()}")
                val accountList = mutableListOf<AccountBookItem>()
                for(document in it){
                    accountList.add(document.toObject(AccountBookItem::class.java))
                }
            }
            .addOnFailureListener {
                Log.d("error", "getAccountList: ")
            }
    }
}