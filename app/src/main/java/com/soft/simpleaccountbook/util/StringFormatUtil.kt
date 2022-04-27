package com.soft.simpleaccountbook.util

import java.text.DecimalFormat

class StringFormatUtil {
    fun amountToFormat(amount:Int) : String{
        val decimalFormat = DecimalFormat("###,###")
        return decimalFormat.format(amount)
    }
}