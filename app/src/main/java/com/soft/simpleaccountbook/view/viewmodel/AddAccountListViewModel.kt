package com.soft.simpleaccountbook.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kakaobrain.pathfinder_prodo.viewmodel.base.BaseMyRepositoryViewModel
import com.router.nftforum.model.repository.MyRepository

class AddAccountListViewModel(override val myRepository: MyRepository): BaseMyRepositoryViewModel(){

    /**
     * Type
     * 0 : 수입
     * 1 : 지출
     * 2 : 이체
     * */
    val addAccountListTypeLiveData = MutableLiveData<Int>(1)
    fun changeAccountListType(type : Int){
        if(addAccountListTypeLiveData.value != type){
            addAccountListTypeLiveData.value = type
        }
    }
}