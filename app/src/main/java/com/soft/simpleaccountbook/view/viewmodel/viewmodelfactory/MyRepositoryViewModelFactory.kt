package com.kakaobrain.pathfinder_prodo.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.router.nftforum.model.repository.MyRepository

class MyRepositoryViewModelFactory(private val myRepository: MyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MyRepository::class.java).newInstance(myRepository)
    }
}