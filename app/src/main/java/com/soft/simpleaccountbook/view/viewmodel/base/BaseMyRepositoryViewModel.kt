package com.kakaobrain.pathfinder_prodo.viewmodel.base

import androidx.lifecycle.ViewModel
import com.router.nftforum.model.repository.MyRepository


abstract class BaseMyRepositoryViewModel: ViewModel() {
    abstract val myRepository: MyRepository

}