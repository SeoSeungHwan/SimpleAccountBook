package com.soft.simpleaccountbook.view.fragment

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.kakaobrain.pathfinder_prodo.view.dialog.AddAccountListBottomSheetDialog
import com.kakaobrain.pathfinder_prodo.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory
import com.router.nftforum.view.base.BaseFragmentForViewBinding
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.common.GlobalApplication
import com.soft.simpleaccountbook.databinding.FragmentAccountListBinding
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.view.viewmodel.AccountListFragmentViewModel
import com.soft.simpleaccountbook.view.viewmodel.AddAccountListDialogViewModel


class AccountListFragment : BaseFragmentForViewBinding<FragmentAccountListBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_account_list

    private val viewModel: AccountListFragmentViewModel by lazy {
        ViewModelProvider(this, MyRepositoryViewModelFactory(MyRepository())).get(
            AccountListFragmentViewModel::class.java
        )
    }

    override fun init() {
        setUpBtnListener()
        viewModel.getAccountList()
    }

    private fun setUpBtnListener() {
        viewDataBinding.accountListAddButton.setOnClickListener {
            val dialog = AddAccountListBottomSheetDialog()
            dialog.show(childFragmentManager, "AccountListFragment")
        }
    }
}