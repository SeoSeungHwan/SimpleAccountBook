package com.soft.simpleaccountbook.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soft.simpleaccountbook.binding.AccountBookListHolderModel
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.util.StringFormatUtil
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

    private val _accountListSum1LiveData = MutableLiveData<String>()
    val accountListSum1LiveData: LiveData<String>
        get() = _accountListSum1LiveData

    private val _accountListSum2LiveData = MutableLiveData<String>()
    val accountListSum2LiveData: LiveData<String>
        get() = _accountListSum2LiveData

    private val _accountListTotalLiveData = MutableLiveData<String>()
    val accountListTotalLiveData: LiveData<String>
        get() = _accountListTotalLiveData

    private val _removeCheckLiveData = MutableLiveData<Boolean>()
    val removeCheckLiveData: LiveData<Boolean>
        get() = _removeCheckLiveData


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
        val startTimestamp =
            TimeUtil().dateToTimeStamp(localDate.year, localDate.monthValue, 1, 0, 0)
        val endTimestamp =
            TimeUtil().dateToTimeStamp(localDate.year, localDate.plusMonths(1).monthValue, 1, 0, 0)
        GlobalApplication.db
            .collection(
                GlobalApplication
                    .mySharedPreferences.getString("uid", null)
            )
            .whereGreaterThanOrEqualTo("time", startTimestamp)
            .whereLessThan("time", endTimestamp)
            .get()
            .addOnSuccessListener {
                Log.d("Response", "getAccountList: ${it.documents}")
                val accountList = mutableListOf<AccountBookListHolderModel>()
                for (document in it) {
                    val new_Document=document.toObject(AccountBookListHolderModel::class.java)
                    new_Document.id = document.id
                    accountList.add(new_Document)
                }
                _accountListLiveData.postValue(accountList)
            }
            .addOnFailureListener {
                Log.d("error", "getAccountList: ")
            }
    }

    fun getAccountSum() {
        var sum1 = 0;
        var sum2 = 0;
        var total = 0;

        accountListLiveData.value?.forEach {
            //수입
            if (it.type == 0) {
                if (!it.balance.equals("")) {
                    sum1 += it.balance.toInt()
                    total += it.balance.toInt()
                }
            }
            //지출
            else if (it.type == 1) {
                if (!it.balance.equals("")) {
                    sum2 += it.balance.toInt()
                    total -= it.balance.toInt();

                }
            }
            _accountListSum1LiveData.postValue(StringFormatUtil().amountToFormat(sum1))
            _accountListSum2LiveData.postValue(StringFormatUtil().amountToFormat(sum2))
            _accountListTotalLiveData.postValue(StringFormatUtil().amountToFormat(total))

        }
    }

    fun removeAccountListItem(id : String){
        GlobalApplication.db.collection(GlobalApplication.mySharedPreferences.getString("uid",null))
            .document(id)
            .delete()
            .addOnSuccessListener {
                _removeCheckLiveData.postValue(true)
            }
            .addOnFailureListener {
                _removeCheckLiveData.postValue(false)
            }
    }
}