package dev.armoury.android.data

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

@Deprecated(message = "Start using ArmouryFragmentPagerItemModel")
open class FragmentPagerItemModel(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    @DrawableRes val iconRes: Int? = null,
    val type: Int
) {

    fun getDisplayTitle(context: Context): String? =
        when {
            !title.isNullOrEmpty() -> title
            titleRes != null -> context.getString(titleRes)
            else -> null
        }

}