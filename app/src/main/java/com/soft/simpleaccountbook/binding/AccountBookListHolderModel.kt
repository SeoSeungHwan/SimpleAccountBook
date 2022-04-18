package com.soft.simpleaccountbook.binding

import com.google.firebase.Timestamp
import com.soft.simpleaccountbook.util.TimeUtil

class AccountBookListHolderModel(
    val type : Int = 1,
    val time : Timestamp = Timestamp.now(),
    val balance : String ="",
    val content : String =""
) {
    fun timeStampToString(): String {
        return TimeUtil().timeStampToString(time)
    }

    fun typeToString() =
        when (type) {
            0 -> "수입"
            1 -> "지출"
            2 -> "이체"
            else -> "기타"
        }

}
