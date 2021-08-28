package dev.armoury.android.adapters

import androidx.recyclerview.widget.ListAdapter
import dev.armoury.android.adapters.callbacks.ArmouryListItemDiffCallback
import dev.armoury.android.adapters.viewholder.ArmouryViewHolder
import dev.armoury.android.data.ArmouryListRowModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ArmouryDynamicDataAdapter<LRM : ArmouryListRowModel, VH : ArmouryViewHolder<*>> :
    ListAdapter<LRM, VH>(ArmouryListItemDiffCallback()) {

    private val adapterScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    fun submitItem(list: List<LRM>?) {

        adapterScope.launch {
            val items: List<LRM> = when (list) {
                null -> emptyList()
                else -> list
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type

}