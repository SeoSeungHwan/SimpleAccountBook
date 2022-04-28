package com.soft.simpleaccountbook.binding

import com.google.firebase.Timestamp
import com.soft.simpleaccountbook.util.StringFormatUtil
import com.soft.simpleaccountbook.util.TimeUtil

class AccountBookListHolderModel(
    var id : String="",
    val type : Int = 1,
    val time : Timestamp = Timestamp.now(),
    val balance : String ="",
    val content : String =""
) {
    fun timeStampToString(): String {
        return TimeUtil().timeStampToString(time)
    }

    fun amountToFormat() : String{
        return StringFormatUtil().amountToFormat(balance.toInt())
    }
}
