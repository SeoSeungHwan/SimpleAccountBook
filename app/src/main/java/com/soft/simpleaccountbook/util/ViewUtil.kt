package com.soft.simpleaccountbook.util

import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar

class ViewUtil {
    fun showLoadingProgressBar(loadingProgressbar: ProgressBar, window: Window?) {
        loadingProgressbar.bringToFront()
        loadingProgressbar.visibility = View.VISIBLE
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideLoadingProgressBar(loadingProgressbar: ProgressBar, window: Window?) {
        loadingProgressbar.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}