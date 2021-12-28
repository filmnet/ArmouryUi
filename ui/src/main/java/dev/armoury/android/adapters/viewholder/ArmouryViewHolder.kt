package dev.armoury.android.adapters.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ArmouryViewHolder<vdb : ViewDataBinding>(dataBinding: vdb) : RecyclerView.ViewHolder(dataBinding.root)