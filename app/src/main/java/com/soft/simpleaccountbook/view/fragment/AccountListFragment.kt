package com.soft.simpleaccountbook.view.fragment

import android.os.Bundle
import com.kakaobrain.pathfinder_prodo.view.dialog.AddAccountListBottomSheetDialog
import com.router.nftforum.view.base.BaseFragmentForViewBinding
import com.soft.simpleaccountbook.R
import com.soft.simpleaccountbook.databinding.FragmentAccountListBinding


class AccountListFragment : BaseFragmentForViewBinding<FragmentAccountListBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_account_list

    override fun init() {
        setUpBtnListener()
    }

    private fun setUpBtnListener() {
        viewDataBinding.accountListAddButton.setOnClickListener {
            val dialog = AddAccountListBottomSheetDialog()
            dialog.show(childFragmentManager, "AccountListFragment")
        }
    }
}