package dev.armoury.android.adapters

import androidx.recyclerview.widget.RecyclerView
import dev.armoury.android.adapters.viewholder.ArmouryViewHolder
import dev.armoury.android.data.ArmouryListRowModel

abstract class ArmouryStaticDataAdapter<LRM : ArmouryListRowModel, VH : ArmouryViewHolder<*>> : RecyclerView.Adapter<VH>() {

    protected var itemsRows: MutableList<LRM> = ArrayList()
    protected  var totalItemCount = 0

    fun submitItems(list: List<LRM>?) {
        itemsRows.clear()
        list?.let {
            itemsRows.addAll(it)
        }
        totalItemCount = itemsRows.size
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = itemsRows[position].type

}