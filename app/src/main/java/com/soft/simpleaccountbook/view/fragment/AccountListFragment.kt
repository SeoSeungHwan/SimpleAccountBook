package com.soft.simpleaccountbook.view.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import java.lang.Exception


class AccountListFragment : BaseFragmentForViewBinding<FragmentAccountListBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_account_list

    private val viewModel: AccountListFragmentViewModel by lazy {
        ViewModelProvider(this, MyRepositoryViewModelFactory(MyRepository())).get(
            AccountListFragmentViewModel::class.java
        )
    }

    //첫 시작할 때만 오늘 날짜 불러오기
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
        viewDataBinding.accountListBeforeButton.setOnClickListener {
            viewModel.beforeFocusDate()
            ViewUtil().showLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
        }
        viewDataBinding.accountListAfterButton.setOnClickListener {
            viewModel.afterFocusDate()
            ViewUtil().showLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
        }
    }

    private fun setUpObserver() {
        viewModel.accountListFocusLocalDateLiveData.observe(viewLifecycleOwner) {
            viewDataBinding.accountListMonthTextview.text = "${it.year}년 ${it.monthValue}월"
            viewModel.getAccountList(viewModel.accountListFocusLocalDateLiveData.value!!)

        }
        viewModel.accountListLiveData.observe(viewLifecycleOwner) {
            setUpRecyclerView(it)
            viewModel.getAccountSum()
            ViewUtil().hideLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
        }
        viewModel.removeCheckLiveData.observe(viewLifecycleOwner){
            if(it){
                ToastMessageUtil().showShortToast(requireContext(),"삭제가 완료되었습니다.")
                ViewUtil().hideLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
            }else{
                ToastMessageUtil().showShortToast(requireContext(),"오류가 발생하였습니다.")
                ViewUtil().hideLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
            }
        }
    }

    private fun setUpRecyclerView(accountBookList: List<AccountBookListHolderModel>) {
        viewDataBinding.recyclerView.apply {
            adapter = AccountBookListRecyclerViewAdapter(accountBookList,::showDialog)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showDialog(id: String){
        val menu = arrayOf("항목삭제", "항목수정")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("가계부 메뉴")
        builder.setItems(menu) { _, i ->
            when (i) {
                //아이템 삭제
                0 -> {
                    viewModel.removeAccountListItem(id)
                    viewModel.getAccountList(viewModel.accountListFocusLocalDateLiveData.value!!)
                    ViewUtil().showLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
                }
                //아이템 수정
                1 -> {

                }
            }
        }
        builder.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.accountListToolbar.inflateMenu(R.menu.account_list_menu)
        viewDataBinding.accountListToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.account_list_add_menu -> {
                    val dialog = AddAccountListBottomSheetDialog()
                    dialog.setAccountListRefreshListener(object :
                        AddAccountListBottomSheetDialog.AccountListRefreshListener {
                        override fun finishDialog(year: Int, month: Int) {
                            viewModel.changeFocusDate(year, month)
                            ViewUtil().showLoadingProgressBar(
                                viewDataBinding.progressBar,
                                activity?.window
                            )
                        }
                    })
                    dialog.show(childFragmentManager, "AccountListFragment")
                }
                else -> true
            }
            true
        }
    }
}