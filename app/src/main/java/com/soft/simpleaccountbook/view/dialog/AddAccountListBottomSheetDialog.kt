package com.kakaobrain.pathfinder_prodo.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.kakaobrain.pathfinder_prodo.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory
import com.router.nftforum.model.repository.MyRepository
import com.router.nftforum.view.base.BaseBottomSheetDialogFragment
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.databinding.DialogBottomSheetAddAccountListBinding
import com.soft.simpleaccountbook.view.viewmodel.AddAccountListViewModel

class AddAccountListBottomSheetDialog :
    BaseBottomSheetDialogFragment<DialogBottomSheetAddAccountListBinding>() {

    override val layoutId: Int
        get() = R.layout.dialog_bottom_sheet_add_account_list

    private val viewModel: AddAccountListViewModel by lazy {
        ViewModelProvider(this, MyRepositoryViewModelFactory(MyRepository())).get(
            AddAccountListViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.viewModel = viewModel

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
        viewDataBinding.toolbar.setNavigationOnClickListener { view ->
            dismiss()
        }

    }

    override fun init() {
        setUpBtnListener()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.addAccountListTypeLiveData.observe(viewLifecycleOwner){ type ->
            viewDataBinding.toolbar.title =
            when(type){
                0 -> "수입"
                1 -> "지출"
                2 -> "이체"
                else -> ""
            }
        }
    }
    private fun setUpBtnListener() {
        viewDataBinding.addCountListDepositButton.setOnClickListener {
            viewModel.changeAccountListType(0)
        }
        viewDataBinding.addCountListWithdrawButton.setOnClickListener {
            viewModel.changeAccountListType(1)
        }
        viewDataBinding.addCountListTransferButton.setOnClickListener {
            viewModel.changeAccountListType(2)
        }
    }


}
