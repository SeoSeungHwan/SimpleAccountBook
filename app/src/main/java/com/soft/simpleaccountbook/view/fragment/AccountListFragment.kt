package com.soft.simpleaccountbook.view.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.router.nftforum.view.base.BaseFragmentForViewBinding
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.adapter.AccountBookListRecyclerViewAdapter
import com.soft.simpleaccountbook.binding.AccountBookListHolderModel
import com.soft.simpleaccountbook.databinding.FragmentAccountListBinding
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.util.ToastMessageUtil
import com.soft.simpleaccountbook.util.ViewUtil
import com.soft.simpleaccountbook.view.dialog.AddAccountListBottomSheetDialog
import com.soft.simpleaccountbook.view.viewmodel.AccountListFragmentViewModel
import com.soft.simpleaccountbook.view.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory
import java.util.*


class AccountListFragment : BaseFragmentForViewBinding<FragmentAccountListBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_account_list

    private val viewModel: AccountListFragmentViewModel by lazy {
        ViewModelProvider(this, MyRepositoryViewModelFactory(MyRepository())).get(
            AccountListFragmentViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initFocusDate()
    }
    
    override fun init() {
        viewDataBinding.viewModel = viewModel
        setUpBtnListener()
        setUpObserver()
    }

    private fun setUpBtnListener() {
        viewDataBinding.accountListAddButton.setOnClickListener {
            val dialog = AddAccountListBottomSheetDialog()
            dialog.setAccountListRefreshListener(object :
                AddAccountListBottomSheetDialog.AccountListRefreshListener {
                override fun finishDialog(year: Int,month: Int) {
                    viewModel.changeFocusDate(year,month)
                    ViewUtil().showLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
                }
            })
            dialog.show(childFragmentManager, "AccountListFragment")
        }
    }

    private fun setUpObserver() {
        viewModel.accountListLiveData.observe(viewLifecycleOwner) {
            setUpRecyclerView(it)
            ViewUtil().hideLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
        }
    }

    private fun setUpRecyclerView(accountBookList: List<AccountBookListHolderModel>) {
        viewDataBinding.recyclerView.apply {
            adapter = AccountBookListRecyclerViewAdapter(accountBookList)
            layoutManager = LinearLayoutManager(context)
        }
    }
}