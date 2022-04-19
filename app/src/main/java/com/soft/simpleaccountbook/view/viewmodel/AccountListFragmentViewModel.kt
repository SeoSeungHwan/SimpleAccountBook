package com.soft.simpleaccountbook.view.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soft.simpleaccountbook.binding.AccountBookListHolderModel
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.util.TimeUtil
import com.soft.simpleaccountbook.view.viewmodel.base.BaseMyRepositoryViewModel
import java.time.LocalDate
import java.util.*

class AccountListFragmentViewModel(override val myRepository: MyRepository) :
    BaseMyRepositoryViewModel() {

    private val _accountListFocusLocalDateLiveData = MutableLiveData<LocalDate>()

    private val _accountListLiveData = MutableLiveData<List<AccountBookListHolderModel>>()
    val accountListLiveData: LiveData<List<AccountBookListHolderModel>>
        get() = _accountListLiveData


    fun initFocusDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDate = LocalDate.now()
            _accountListFocusLocalDateLiveData.postValue(localDate)
            getAccountList(localDate.year,localDate.monthValue)
        } else {
            //TODO 하위버전 준비하기( Date이용)
        }
    }

    fun changeFocusDate(year: Int, month: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDate = LocalDate.of(year,month,1)
            _accountListFocusLocalDateLiveData.postValue(localDate)
            getAccountList(year,month)
        } else {
            //TODO 하위버전 준비하기( Date이용)
        }
    }

    fun getAccountList(year: Int, month: Int) {
        val startTimestamp = TimeUtil().dateToTimeStamp(year, month-1, 1, 0, 0)
        val endTimestamp = TimeUtil().dateToTimeStamp(year, month, 1, 0, 0)
        GlobalApplication.db
            .collection(
                GlobalApplication
                    .mySharedPreferences.getString("uid", null)
            )
            .whereGreaterThanOrEqualTo("time", startTimestamp)
            .whereLessThan("time", endTimestamp)
            .get()
            .addOnSuccessListener {
                Log.d("Response", "getAccountList: ${it.documents.toString()}")
                val accountList = mutableListOf<AccountBookListHolderModel>()
                for (document in it) {
                    accountList.add(document.toObject(AccountBookListHolderModel::class.java))
                }
                _accountListLiveData.postValue(accountList)
            }
            .addOnFailureListener {
                Log.d("error", "getAccountList: ")
            }
    }
}