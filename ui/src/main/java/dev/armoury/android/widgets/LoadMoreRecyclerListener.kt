package dev.armoury.android.widgets

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class LoadMoreRecyclerListener : RecyclerView.OnScrollListener() {

    /**
     * The total number of items in the dataset after the last load
     */
    private var mPreviousTotal = 0
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private var mLoading = true

    private val THRESHOLD = 5

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = recyclerView.childCount
        //        We are going to show
        val totalItemCount = recyclerView.adapter?.itemCount ?: 0
        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false
                mPreviousTotal = totalItemCount
            }
        }
        if (!mLoading && (totalItemCount - visibleItemCount <= firstVisibleItem + THRESHOLD) && totalItemCount != 0) {
            // End has been reached
            onLoadMore()

            mLoading = true
        }
    }

    fun reset() {
        mPreviousTotal = 0
        mLoading = true
    }

    abstract fun onLoadMore()

}
