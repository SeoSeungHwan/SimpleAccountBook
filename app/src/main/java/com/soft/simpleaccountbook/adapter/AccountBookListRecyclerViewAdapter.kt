package com.soft.simpleaccountbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soft.simpleaccountbook.binding.AccountBookListHolderModel
import com.soft.simpleaccountbook.databinding.HolderAccountListBinding
import com.soft.simpleaccountbook.model.data.AccountBookItem

class AccountBookListRecyclerViewAdapter(private val modelList: List<AccountBookListHolderModel>): RecyclerView.Adapter<AccountBookListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = HolderAccountListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val holderModel = modelList[position]
        holder.bind(holderModel)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class ViewHolder(val viewDataBinding: HolderAccountListBinding): RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(holderModel: AccountBookListHolderModel) {
            viewDataBinding.holderModel = holderModel
        }
    }
}