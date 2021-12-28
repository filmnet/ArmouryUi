package dev.armoury.android.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import dev.armoury.android.data.ArmouryFragmentPagerItemModel
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.lifecycle.SingleLiveEvent

abstract class ArmouryPagerViewModel<UA: ArmouryUiAction, IM : ArmouryFragmentPagerItemModel>(applicationContext: Application) :
    ArmouryViewModel<UA>(applicationContext) {

    protected val _tabCurrentItem = SingleLiveEvent<Int?>(null)
    val tabCurrentItem: LiveData<Int?>
        get() = _tabCurrentItem

    protected val _pagerItems = SingleLiveEvent<MutableList<IM>>(ArrayList())
    val pagerItems: LiveData<MutableList<IM>>
        get() = _pagerItems

    protected val _offscreenLimit = SingleLiveEvent<Int?>(null)
    val offScreenLimit: LiveData<Int?>
        get() = _offscreenLimit

}