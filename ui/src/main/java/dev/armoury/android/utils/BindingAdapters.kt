package dev.armoury.android.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("enableSwipe")
fun SwipeRefreshLayout.setEnableSwipe(isEnable: Boolean?) {
    isEnable?.let {
        this.isEnabled = isEnable
    }
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.setIsRefreshing(isRefreshing: Boolean?) {
    isRefreshing?.let {
        setRefreshing(isRefreshing)
    }
}

@BindingAdapter("setListener")
fun SwipeRefreshLayout.addListener(refreshListener: SwipeRefreshLayout.OnRefreshListener?) {
    refreshListener?.let {
        setOnRefreshListener(it)
    }
}

// View Pager 2
//  Set the current tab of a ViewPager2
@BindingAdapter("currentTab")
fun ViewPager2.setCurrentActiveTab(tabIndex: Int?) {
    tabIndex?.let {
        currentItem = tabIndex
    }
}

//  Set the offScreen limit of a ViewPager2
@BindingAdapter("offScreenLimit")
fun ViewPager2.updateOffscreenLimit(count: Int?) {
    count?.let {
        offscreenPageLimit = count
    }
}
//  End of View Pager 2

/*
 * RecyclerView
 */
/**
 * Make a recycler view to act like a view pager
 */
@BindingAdapter("pager")
fun RecyclerView.actLikeViewPager(isPager: Boolean?) {
    if (isPager == true) PagerSnapHelper().attachToRecyclerView(this)
}

/*
 * End of the recycler view
 */