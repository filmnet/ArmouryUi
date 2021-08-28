package dev.armoury.android.data

import android.content.Context

abstract class ArmouryFragmentPagerItemModel {

    abstract val title: String?
    abstract val titleRes: Int?
    abstract val iconRes: Int?
    abstract val type: Int

    fun getDisplayTitle(context: Context): String? =
        if (!title.isNullOrEmpty()) title
        else {
            titleRes?.let { context.getString(it) } ?: run { null }
        }

}