package com.soft.simpleaccountbook.model

import com.google.firebase.Timestamp

data class AccountBookItem(
    val type : Int,
    val time : Timestamp,
    val account : Long,
    val content : String
)
