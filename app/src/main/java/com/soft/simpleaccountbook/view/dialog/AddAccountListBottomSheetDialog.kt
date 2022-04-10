package com.kakaobrain.pathfinder_prodo.view.dialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.kakaobrain.pathfinder_prodo.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory
import com.router.nftforum.model.repository.MyRepository
import com.router.nftforum.view.base.BaseBottomSheetDialogFragment
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.databinding.DialogBottomSheetAddAccountListBinding
import com.soft.simpleaccountbook.model.AccountBookItem
import com.soft.simpleaccountbook.util.ToastMessageUtil
import com.soft.simpleaccountbook.view.viewmodel.AddAccountListViewModel
import java.time.LocalDate
import java.time.LocalTime

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
        initCurrentDateAndTime()
        setUpBtnListener()
        setUpObserver()
    }

    private fun initCurrentDateAndTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nowDate = LocalDate.now()
            val nowTime = LocalTime.now()
            viewDataBinding.addAccountListDateEdittext.text = "${nowDate.year}/${nowDate.monthValue}/${nowDate.dayOfMonth}"
            viewDataBinding.addAccountListTimeEdittext.text = "${nowTime.hour}시 ${nowTime.minute}분"
        }
    }

    private fun setUpObserver() {
        viewModel.addAccountListTypeLiveData.observe(viewLifecycleOwner) { type ->
            viewDataBinding.toolbar.title =
                when (type) {
                    0 -> "수입"
                    1 -> "지출"
                    2 -> "이체"
                    else -> ""
                }
        }
        viewModel.addAccountBookItemLiveData.observe(viewLifecycleOwner){
            if(it){
                ToastMessageUtil().showShortToast(requireContext(),"성공적으로 등록되었습니다.")
                dismiss()
            }else{
                ToastMessageUtil().showShortToast(requireContext(),"실패하였습니다.")
                dismiss()
            }
        }
    }

    private fun setUpBtnListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewDataBinding.addAccountListDateEdittext.setOnClickListener {
                showDatePickerDialog()
            }
            viewDataBinding.addAccountListTimeEdittext.setOnClickListener {
                showTimePickerDialog()
            }
        }
        viewDataBinding.addCountListDepositButton.setOnClickListener {
            viewModel.changeAccountListType(0)
        }
        viewDataBinding.addCountListWithdrawButton.setOnClickListener {
            viewModel.changeAccountListType(1)
        }
        viewDataBinding.addCountListTransferButton.setOnClickListener {
            viewModel.changeAccountListType(2)
        }
        viewDataBinding.addAccountListSubmitButton.setOnClickListener {
            viewModel.addAccountBookItem(AccountBookItem(
                1,Timestamp.now(),0,"테스트"
            ))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val listener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            viewDataBinding.addAccountListDateEdittext.text =
                "${year}/${monthOfYear + 1}/${dayOfMonth}"
        }
        val nowDate = LocalDate.now()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            listener,
            nowDate.year,
            nowDate.monthValue - 1,
            nowDate.dayOfMonth
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePickerDialog() {
        val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            viewDataBinding.addAccountListTimeEdittext.text = "${hourOfDay}시 ${minute}분"
        }
        val nowTime = LocalTime.now()
        val timePickerDialog =
            TimePickerDialog(requireContext(), listener, nowTime.hour, nowTime.minute, true)
        timePickerDialog.show()
    }

}
