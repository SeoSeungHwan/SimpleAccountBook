package com.soft.simpleaccountbook.view.dialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.router.nftforum.view.base.BaseBottomSheetDialogFragment
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.databinding.DialogBottomSheetAddAccountListBinding
import com.soft.simpleaccountbook.model.data.AccountBookItem
import com.soft.simpleaccountbook.model.data.DateModel
import com.soft.simpleaccountbook.model.data.TimeModel
import com.soft.simpleaccountbook.model.repository.MyRepository
import com.soft.simpleaccountbook.util.ToastMessageUtil
import com.soft.simpleaccountbook.util.ViewUtil
import com.soft.simpleaccountbook.view.viewmodel.AddAccountListDialogViewModel
import com.soft.simpleaccountbook.view.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class AddAccountListBottomSheetDialog :
    BaseBottomSheetDialogFragment<DialogBottomSheetAddAccountListBinding>() {

    override val layoutId: Int
        get() = R.layout.dialog_bottom_sheet_add_account_list

    private val dialogViewModel: AddAccountListDialogViewModel by lazy {
        ViewModelProvider(this, MyRepositoryViewModelFactory(MyRepository())).get(
            AddAccountListDialogViewModel::class.java
        )
    }

    private lateinit var accountListRefreshListener: AccountListRefreshListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.viewModel = dialogViewModel

        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
        viewDataBinding.toolbar.setNavigationOnClickListener { view ->
            dismiss()
        }

    }

    override fun init() {
        initCurrentDateAndTime()
        setUpBtnListener()
        setUpObserver()
    }

    private fun initCurrentDateAndTime() {
            val nowDate = LocalDate.now()
            dialogViewModel.changeDateModel(
                DateModel(
                    nowDate.year,
                    nowDate.monthValue - 1,
                    nowDate.dayOfMonth
                )
            )

            val nowTime = LocalTime.now()
            dialogViewModel.changeTimeModel(TimeModel(nowTime.hour, nowTime.minute))
    }

    private fun setUpObserver() {
        dialogViewModel.accountListTypeLiveData.observe(viewLifecycleOwner) { type ->
            viewDataBinding.toolbar.title =
                when (type) {
                    0 -> "수입"
                    1 -> "지출"
                    2 -> "이체"
                    else -> ""
                }
        }
        dialogViewModel.addAccountBookItemLiveData.observe(viewLifecycleOwner) {
            if (it) {
                ToastMessageUtil().showShortToast(requireContext(), "성공적으로 등록되었습니다.")
                dismiss()
            } else {
                ToastMessageUtil().showShortToast(requireContext(), "실패하였습니다.")
                dismiss()
            }
            ViewUtil().hideLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
        }
        dialogViewModel.dateModelLiveData.observe(viewLifecycleOwner) {
            viewDataBinding.addAccountListDateEdittext.text =
                "${it.year}/${it.monthOfYear + 1}/${it.dayOfMonth}"
        }
        dialogViewModel.timeModelLiveData.observe(viewLifecycleOwner) {
            viewDataBinding.addAccountListTimeEdittext.text =
                "${it.hourOfDay}시 ${it.minute}분"
        }
    }

    private fun setUpBtnListener() {
        //TODO : 하위버전 날짜 및 시간선택 구현하기
        viewDataBinding.addAccountListDateEdittext.setOnClickListener {
            showDatePickerDialog()
        }
        viewDataBinding.addAccountListTimeEdittext.setOnClickListener {
            showTimePickerDialog()
        }
        viewDataBinding.addCountListDepositButton.setOnClickListener {
            dialogViewModel.changeAccountListType(0)
        }
        viewDataBinding.addCountListWithdrawButton.setOnClickListener {
            dialogViewModel.changeAccountListType(1)
        }
        viewDataBinding.addCountListTransferButton.setOnClickListener {
            dialogViewModel.changeAccountListType(2)
        }
        viewDataBinding.addAccountListSubmitButton.setOnClickListener {
            ViewUtil().showLoadingProgressBar(viewDataBinding.progressBar, activity?.window)
            dialogViewModel.addAccountBookItem(
                AccountBookItem(
                    dialogViewModel.accountListTypeLiveData.value!!,
                    dialogViewModel.getDateTimeModelToTimeStamp(),
                    viewDataBinding.addAccountListAmountEdittext.text.toString(),
                    viewDataBinding.addAccountListContentEdittext.text.toString()
                )
            )

        }
    }

    private fun showDatePickerDialog() {
        val listener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val dateModel = DateModel(year, monthOfYear, dayOfMonth)
            dialogViewModel.changeDateModel(dateModel)
        }
        val nowDate = LocalDate.now()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            listener,
            nowDate.year,
            nowDate.monthValue-1,
            nowDate.dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val timeModel = TimeModel(hourOfDay, minute)
            dialogViewModel.changeTimeModel(timeModel)
        }
        val nowTime = LocalTime.now()
        val timePickerDialog =
            TimePickerDialog(requireContext(), listener, nowTime.hour, nowTime.minute, true)
        timePickerDialog.show()
    }

    interface AccountListRefreshListener {
        fun finishDialog(year: Int, month: Int)
    }

    fun setAccountListRefreshListener(accountListRefreshListener: AccountListRefreshListener) {
        this.accountListRefreshListener = accountListRefreshListener
    }

    override fun onDestroy() {
        super.onDestroy()
        accountListRefreshListener.finishDialog(
            dialogViewModel.dateModelLiveData.value!!.year,
            dialogViewModel.dateModelLiveData.value!!.monthOfYear + 1
        )
    }

}
