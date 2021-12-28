package dev.armoury.android.ui

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.utils.ArmouryMessageUtils
import dev.armoury.android.viewmodel.ArmouryListViewModel
import dev.armoury.android.widget.data.MessageModel

abstract class ArmouryListFragment<UA: ArmouryUiAction, T : ViewDataBinding, V : ArmouryListViewModel<UA, *>> :
    ArmouryFragment<UA, T, V>() {

    abstract override fun getRefreshLayout(): SwipeRefreshLayout?

    private val refreshingListener = Observer<Boolean> {
        it?.let { isRefreshing -> getRefreshLayout()?.isRefreshing = isRefreshing }
    }

    private val refreshEnableListener = Observer<Boolean> {
        it?.let { isEnable -> getRefreshLayout()?.isEnabled = isEnable }
    }

    private val refreshFailedMessageListener = Observer<MessageModel> {
        it?.let {
            ArmouryMessageUtils.showRefreshErrorMessage(
                rootView = viewDataBinding.root,
                errorMessage = it
            )
        }
    }

    override fun customizeLayout() {
        super.customizeLayout()
        getRefreshLayout()?.apply { setOnRefreshListener(viewModel.refreshListener) }
    }

    override fun startObserving() {
        super.startObserving()
        viewModel.refreshing.observe(this, refreshingListener)
        viewModel.swipeRefreshEnable.observe(this, refreshEnableListener)
        viewModel.refreshFailedMessage.observe(this, refreshFailedMessageListener)
    }

}