package dev.armoury.android.adapters.callbacks

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import dev.armoury.android.data.ArmouryListRowModel

class ArmouryListItemDiffCallback<T : ArmouryListRowModel> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) =
        oldItem.type == newItem.type && oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem == newItem

}
