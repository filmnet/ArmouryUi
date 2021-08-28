package dev.armoury.android.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.armoury.android.data.ArmouryListRowModel
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.data.ErrorModel
import dev.armoury.android.utils.ArmouryListUtils
import dev.armoury.android.widget.MessageView
import dev.armoury.android.widget.data.MessageModel
import dev.armoury.android.widgets.LoadMoreRecyclerListener
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class ArmouryListViewModel<UA: ArmouryUiAction, LRM : ArmouryListRowModel>(application: Application) :
    ArmouryViewModel<UA>(applicationContext = application) {

    //    Variables
    @get:ArmouryListUtils.RequestType
    @setparam:ArmouryListUtils.RequestType
    protected var requestType: Int = ArmouryListUtils.RequestTypes.NOT_SET

    protected val _adapterRows = MutableLiveData<MutableList<LRM>>(ArrayList())
    val adapterRows: LiveData<MutableList<LRM>>
        get() = _adapterRows

    protected val loadMoreFailedMessageModel = MessageModel(state = MessageView.States.ERROR)

    //    LiveData
    //    Loading State
    private val _state: MutableLiveData<Int> = MutableLiveData(ArmouryListUtils.States.NOT_SET)
    val state: LiveData<Int>
        get() = _state

    //    Listeners
    val loadMoreClickHandles: MessageView.Callbacks = object : MessageView.SimpleCallbacks() {
        override fun onButtonClicked(messageModel: MessageModel?) {
            sendServerRequest(false)
            onLoadMoreRequestResend()
        }
    }

    val loadMoreRecyclerListener: LoadMoreRecyclerListener = object : LoadMoreRecyclerListener() {
        override fun onLoadMore() {
            if (hasMorePages()) sendServerRequest(false)
        }
    }

    //  TODO : Maybe we should cancel previous request when we are going to send a Refresh Request
    final override fun sendServerRequest(isRefresh: Boolean) {
        //  TODO Should be checked later
        if (isInSending()) {
            Timber.i("New request for the list received. It's not possible to send this request. Previous request is not responded from the server yet")
            return
        }
        /**
         * For now, we  are not going to let the user
         * refresh the page when we are in [ArmouryListUtils.States.LOADING]
         * or [ArmouryListUtils.States.LOADING_MORE] state.
         */
        _swipeRefreshEnable.value = false
        when {
            isRefresh -> {
                nextApiUrl = generateFirstRequestUrl()
                _state.value = ArmouryListUtils.States.LOADING_REFRESH
                requestType = ArmouryListUtils.RequestTypes.REFRESH
                sendRefreshRequest()
            }
            nextApiUrl.isNullOrEmpty() -> {
                nextApiUrl = generateFirstRequestUrl()
                _state.value = ArmouryListUtils.States.LOADING
                _messageModel.value = loadingMessageModel
                requestType = ArmouryListUtils.RequestTypes.FIRST_REQUEST
                sendFirstRequest()
            }
            else -> {
                _state.value = ArmouryListUtils.States.LOADING_MORE
                requestType = ArmouryListUtils.RequestTypes.LOAD_MORE
                sendLoadMoreRequest()
            }
        }
    }

    protected fun hasMorePages() = !nextApiUrl.isNullOrEmpty()

    protected abstract fun sendProperRequest() // TODO Better name

    protected open fun sendFirstRequest() {
        Timber.v("Going to send the first request")
        sendProperRequest()
    }

    protected open fun sendRefreshRequest() {
        Timber.v("Going to send refresh request")
        sendProperRequest()
    }

    protected open fun sendLoadMoreRequest() {
        Timber.v("Going to send the load more request")
        sendProperRequest()
    }

    protected abstract fun generateFirstRequestUrl(): String?

    override suspend fun <T> onResponseGot(body: T?, requestCode: Int, responseCode: Int) {
        if (isListRequest(requestCode)) {
            coroutineScope.launch {
                val (hasResponse, nextUrl) = handleListResponse(body, requestCode)
                onResponseGot(successful = true, hasResponse = hasResponse, nextApiUrl = nextUrl)
            }
        }
    }

    //  TODO Should be checked
    abstract suspend fun <T> handleListResponse(body: T?, requestCode: Int): Pair<Boolean, String?>

    override fun onRequestFailed(errorModel: ErrorModel) {
        if (isListRequest(errorModel.requestCode)) {
            onResponseGot(successful = false, errorModel = errorModel)
        } else {
            super.onRequestFailed(errorModel)
        }
    }

    // TODO : Should be reviewed later
    private fun onResponseGot(
        successful: Boolean,
        hasResponse: Boolean = false,
        nextApiUrl: String? = null,
        errorModel: ErrorModel? = null
    ) {
        if (successful) this.nextApiUrl = nextApiUrl
        this.errorModel = errorModel
        when (requestType) {
            ArmouryListUtils.RequestTypes.FIRST_REQUEST -> {
                if (successful) {
                    if (hasResponse) {
                        _state.value = ArmouryListUtils.States.LOADING_DONE
                        _messageModel.value = MessageModel(state = MessageView.States.HIDE)
                    } else {
                        _state.value = ArmouryListUtils.States.LOADING_EMPTY
                        _messageModel.value = emptyMessageModel
                    }
                } else {
                    _state.value = ArmouryListUtils.States.LOADING_FAILED
                    _messageModel.value = this.errorModel?.messageModel
                }
            }
            ArmouryListUtils.RequestTypes.LOAD_MORE -> {
                if (successful) {
                    _state.value = ArmouryListUtils.States.LOADING_MORE_DONE
                } else {
                    _state.value = ArmouryListUtils.States.LOADING_MORE_FAILED
                    onLoadingMoreFailed()
                }
            }
            ArmouryListUtils.RequestTypes.REFRESH -> {
                _refreshing.value = false
                if (successful) {
                    if (hasResponse) {
                        _state.value = ArmouryListUtils.States.LOADING_REFRESH_DONE
                        _refreshing.value = false
                    } else {
                        _messageModel.value = emptyMessageModel
                        _state.value = ArmouryListUtils.States.LOADING_EMPTY
                    }
                    loadMoreRecyclerListener.reset()
                } else {
                    _refreshFailedMessage.value = errorModel?.messageModel
                    _state.value = ArmouryListUtils.States.LOADING_REFRESH_FAILED
                    _refreshing.value = false
                }
            }
        }
        _swipeRefreshEnable.value = true
    }

    private fun onLoadingMoreFailed() {
        _adapterRows.value?.let { list ->
            list[list.lastIndex].let { lastRow ->
                if (isLoadMoreRow(lastRow)) {
                    updateLoadMoreRow(lastRow)
                }
            }
        }
    }

    private fun onLoadMoreRequestResend() {
        _adapterRows.value?.let { list ->
            list[list.lastIndex].let { lastRow ->
                if (isLoadMoreRow(lastRow)) {
                    updateLoadMoreRow(lastRow)
                }
            }
        }
    }

    protected abstract fun isLoadMoreRow(lastRow: ArmouryListRowModel): Boolean

    protected abstract fun updateLoadMoreRow(lastRow: ArmouryListRowModel)

    protected fun clearList() {
        _adapterRows.value = null
        loadMoreRecyclerListener.reset()
    }

    private fun isInSending() = _state.value == ArmouryListUtils.States.LOADING ||
            _state.value == ArmouryListUtils.States.LOADING_MORE ||
            _state.value == ArmouryListUtils.States.LOADING_REFRESH

    abstract override fun isListRequest(requestCode : Int) : Boolean

    protected fun isLoadMoreRequest() = requestType == ArmouryListUtils.RequestTypes.LOAD_MORE

}