package dev.armoury.android.adapters.callbacks

import dev.armoury.android.data.ArmouryListRowModel

open class ArmouryClickCallbacks<T : ArmouryListRowModel>(val clickListener: (item : T) -> Unit) {

    fun onClick(item : T) = clickListener(item)

}