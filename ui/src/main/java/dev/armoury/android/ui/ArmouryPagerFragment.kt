package dev.armoury.android.ui

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.armoury.android.adapters.ArmouryFragmentPagerAdapter
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.viewmodel.ArmouryPagerViewModel

abstract class ArmouryPagerFragment<UA: ArmouryUiAction, T : ViewDataBinding, V : ArmouryPagerViewModel<UA, *>> :
    ArmouryFragment<UA, T, V>() {

    abstract fun createViewPagerAdapter(): ArmouryFragmentPagerAdapter<*>
    abstract fun getViewPager(): ViewPager2
    abstract fun getTabLayout(): TabLayout?

    override fun customizeLayout() {
        super.customizeLayout()
        getViewPager().apply {
            adapter = createViewPagerAdapter()
            getTabLayout()?.let {
                TabLayoutMediator(
                    /* Tab */ it,
                    /* ViewPager */ this
                ) { tab, position ->
                    val currentItem =
                        (adapter as ArmouryFragmentPagerAdapter<*>).items?.get(position)
                    currentItem.let { item ->
                        tab.text = item?.getDisplayTitle(context = activity)
                    }
                }.attach()
            }
        }
    }

}