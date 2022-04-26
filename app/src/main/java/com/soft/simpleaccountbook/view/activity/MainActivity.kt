package com.soft.simpleaccountbook.view.activity


import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.databinding.ActivityMainBinding
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.view.base.BaseActivityForViewBinding
import com.soft.simpleaccountbook.view.viewmodel.AccountListFragmentViewModel
import com.soft.simpleaccountbook.view.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class MainActivity : BaseActivityForViewBinding<ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun init() {
        initBottomNavigation()
    }

    private fun initBottomNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        viewDataBinding.bottomNavigationView.setupWithNavController(navController)
    }
}