package com.soft.simpleaccountbook.view.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.model.data.AccountBookItem
import com.soft.simpleaccountbook.model.data.DateModel
import com.soft.simpleaccountbook.model.data.TimeModel
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.view.viewmodel.base.BaseMyRepositoryViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class AddAccountListDialogViewModel(override val myRepository: MyRepository): BaseMyRepositoryViewModel(){


    private val _addAccountBookItemLiveData = MutableLiveData<Boolean>()
    val addAccountBookItemLiveData: LiveData<Boolean>
        get() = _addAccountBookItemLiveData

    private val _dateModelLiveData = MutableLiveData<DateModel>()
    val dateModelLiveData: LiveData<DateModel>
        get() = _dateModelLiveData

    private val _timeModelLiveData = MutableLiveData<TimeModel>()
    val timeModelLiveData: LiveData<TimeModel>
        get() = _timeModelLiveData

    /**
     * Type
     * 0 : 수입
     * 1 : 지출
     * 2 : 이체
     * */
    private val _accountListTypeLiveData = MutableLiveData<Int>(1)
    val accountListTypeLiveData : LiveData<Int>
        get() = _accountListTypeLiveData

    fun changeAccountListType(type : Int){
        if(_accountListTypeLiveData.value != type){
            _accountListTypeLiveData.value = type
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

    fun changeDateModel(dateModel: DateModel){
        _dateModelLiveData.postValue(dateModel)
    }

    fun changeTimeModel(timeModel: TimeModel){
        _timeModelLiveData.postValue(timeModel)
    }

    fun getDateTimeModelToTimeStamp() : Timestamp {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDateTime = LocalDateTime.of(
                dateModelLiveData.value!!.year,
                dateModelLiveData.value!!.monthOfYear + 1,
                dateModelLiveData.value!!.dayOfMonth,
                timeModelLiveData.value!!.hourOfDay,
                timeModelLiveData.value!!.minute
            )
            val zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"))
            val localDateTimeseconds = zonedDateTime.toEpochSecond()
            return Timestamp(localDateTimeseconds, 0)
        }else{
            return Timestamp(Date(dateModelLiveData.value!!.year,
                dateModelLiveData.value!!.monthOfYear + 1,
                dateModelLiveData.value!!.dayOfMonth,
                timeModelLiveData.value!!.hourOfDay,
                timeModelLiveData.value!!.minute))
        }
    }
}