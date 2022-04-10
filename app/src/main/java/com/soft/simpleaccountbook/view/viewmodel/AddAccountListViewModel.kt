package com.soft.simpleaccountbook.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.kakaobrain.pathfinder_prodo.viewmodel.base.BaseMyRepositoryViewModel
import com.router.nftforum.model.repository.MyRepository
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.model.AccountBookItem

class AddAccountListViewModel(override val myRepository: MyRepository): BaseMyRepositoryViewModel(){


    private val _addAccountBookItemLiveData = MutableLiveData<Boolean>()
    val addAccountBookItemLiveData: LiveData<Boolean>
        get() = _addAccountBookItemLiveData

    /**
     * Type
     * 0 : 수입
     * 1 : 지출
     * 2 : 이체
     * */
    val addAccountListTypeLiveData = MutableLiveData<Int>(1)
    fun changeAccountListType(type : Int){
        if(addAccountListTypeLiveData.value != type){
            addAccountListTypeLiveData.value = type
        }
    }
    //type: Int,date : Timestamp,account:Long,content: String
    fun addAccountBookItem(accountBookItem: AccountBookItem){
        GlobalApplication.db.collection(GlobalApplication.mySharedPreferences.getString("uid",null))
            .add(accountBookItem)
            .addOnSuccessListener {
                Log.d("Response", "가계부 추가 완료")
                _addAccountBookItemLiveData.postValue(true)
            }
            .addOnFailureListener {
                Log.d("error", "submit: ")
                _addAccountBookItemLiveData.postValue(false)
            }
    }
}