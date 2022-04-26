package com.soft.simpleaccountbook.view.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soft.simpleaccountbook.binding.AccountBookListHolderModel
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.util.TimeUtil
import com.soft.simpleaccountbook.view.viewmodel.base.BaseMyRepositoryViewModel
import java.time.LocalDate

class AccountListFragmentViewModel(override val myRepository: MyRepository) :
    BaseMyRepositoryViewModel() {

    private val _accountListFocusLocalDateLiveData = MutableLiveData<LocalDate>()
    val accountListFocusLocalDateLiveData: LiveData<LocalDate>
        get() = _accountListFocusLocalDateLiveData

    private val _accountListLiveData = MutableLiveData<List<AccountBookListHolderModel>>()
    val accountListLiveData: LiveData<List<AccountBookListHolderModel>>
        get() = _accountListLiveData


    fun initFocusDate() {
        val localDate = LocalDate.now()
        _accountListFocusLocalDateLiveData.postValue(localDate)
        getAccountList(localDate)
    }

    fun changeFocusDate(year: Int, month: Int) {
        val localDate = LocalDate.of(year, month, 1)
        _accountListFocusLocalDateLiveData.postValue(localDate)
    }

    fun beforeFocusDate() {
        _accountListFocusLocalDateLiveData.postValue(
            accountListFocusLocalDateLiveData.value!!.minusMonths(
                1
            )
        )
    }

    fun afterFocusDate() {
        _accountListFocusLocalDateLiveData.postValue(
            accountListFocusLocalDateLiveData.value!!.plusMonths(
                1
            )
        )
    }

    fun getAccountList(localDate: LocalDate) {
        Log.d(TAG, "haha: ${localDate.monthValue}")
        val startTimestamp =
            TimeUtil().dateToTimeStamp(localDate.year, localDate.monthValue, 1, 0, 0)
        val endTimestamp = TimeUtil().dateToTimeStamp(localDate.year, localDate.plusMonths(1).monthValue, 1, 0, 0)
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