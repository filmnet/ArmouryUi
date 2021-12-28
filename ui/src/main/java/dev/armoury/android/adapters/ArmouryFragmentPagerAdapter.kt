package dev.armoury.android.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.armoury.android.data.ArmouryFragmentPagerItemModel


abstract class ArmouryFragmentPagerAdapter<PIM: ArmouryFragmentPagerItemModel>(
    val items: MutableList<PIM>?,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = items?.size ?: 0

}