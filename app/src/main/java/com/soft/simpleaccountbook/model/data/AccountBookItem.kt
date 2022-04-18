package com.soft.simpleaccountbook.model.data

import com.google.firebase.Timestamp

data class AccountBookItem(
    val type : Int = 1,
    val time : Timestamp = Timestamp.now(),
    val account : String ="",
    val content : String =""

)
