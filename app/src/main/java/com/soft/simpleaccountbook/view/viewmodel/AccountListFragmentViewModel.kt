package com.soft.simpleaccountbook.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soft.simpleaccountbook.binding.AccountBookListHolderModel
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.view.viewmodel.base.BaseMyRepositoryViewModel

class AccountListFragmentViewModel(override val myRepository: MyRepository): BaseMyRepositoryViewModel(){

    private val _accountListLiveData = MutableLiveData<List<AccountBookListHolderModel>>()
    val accountListLiveData : LiveData<List<AccountBookListHolderModel>>
        get() = _accountListLiveData

    fun getAccountList(){
        GlobalApplication.db.collection(GlobalApplication.mySharedPreferences.getString("uid",null)).get()
            .addOnSuccessListener {
                Log.d("Response", "getAccountList: ${it.documents.toString()}")
                val accountList = mutableListOf<AccountBookListHolderModel>()
                for(document in it){
                    accountList.add(document.toObject(AccountBookListHolderModel::class.java))
                }
                _accountListLiveData.postValue(accountList)
            }
            .addOnFailureListener {
                Log.d("error", "getAccountList: ")
            }
    }
}