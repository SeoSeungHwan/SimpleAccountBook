package com.soft.simpleaccountbook.view.viewmodel.base

import androidx.lifecycle.ViewModel
import com.soft.simpleaccountbook.model.repository.MyRepository

abstract class BaseMyRepositoryViewModel: ViewModel() {
    abstract val myRepository: MyRepository

}