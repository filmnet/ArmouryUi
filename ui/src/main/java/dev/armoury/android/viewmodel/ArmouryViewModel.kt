package dev.armoury.android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.armoury.android.R
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.data.ErrorModel
import dev.armoury.android.lifecycle.SingleLiveEvent
import dev.armoury.android.utils.ArmouryConnectionUtils
import dev.armoury.android.utils.RequestErrorReasons
import dev.armoury.android.widget.MessageView
import dev.armoury.android.widget.data.MessageModel
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import javax.net.ssl.SSLHandshakeException

abstract class ArmouryViewModel<UA : ArmouryUiAction>(protected val applicationContext: Application) :
    AndroidViewModel(applicationContext), ViewModelGeneralFunctions {

    private val _authenticationFailed = SingleLiveEvent(value = false)
    val authenticationFailed: LiveData<Boolean>
        get() = _authenticationFailed

    //    MessageModel
    protected val _messageModel = MutableLiveData<MessageModel>()
    val messageModel: LiveData<MessageModel>
        get() = _messageModel

    protected open val loadingMessageModel: MessageModel =
        MessageModel(state = MessageView.States.LOADING)

    protected open val emptyMessageModel: MessageModel =
        MessageModel(state = MessageView.States.NORMAL)

    private val _uiAction = SingleLiveEvent<UA?>(value = null)
    val uiAction: LiveData<UA?>
        get() = _uiAction

    private val _showLoading = SingleLiveEvent(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    var customizeBackButton: Boolean = false
        protected set

    var customizeNavigateUpButton: Boolean = false
        protected set

    /**
     * All request codes which are for those requests that are not
     * needed to show a loading indicator, should be added to this set
     */
    protected open val donShowLoadingRequests: Set<Int> = setOf()

    protected var errorModel: ErrorModel? = null

    protected open fun getSomethingWentWrongMessageModel() =
        MessageModel(
            state = MessageView.States.ERROR,
            titleTextRes = R.string.title_error_something_went_wrong,
            descriptionTextRes = R.string.message_error_something_went_wrong,
            buttonTextRes = R.string.button_retry
        )

    //  TODO A Better name and approach should be found
    private fun showLoading(isLoading: Boolean = true, requestCode: Int) {
        if (isListRequest(requestCode) || requestCode in donShowLoadingRequests) return
        _showLoading.value = isLoading
    }

    protected var nextApiUrl: String? = null

    init {
        Timber.v("ViewModel initialized")
    }

    /**
     * Refresh Related
     */
    //  Refresh Listener
    val refreshListener =
        SwipeRefreshLayout.OnRefreshListener { sendServerRequest(isRefresh = true) }

    //  Enable Swipe Refresh
    internal val _swipeRefreshEnable = MutableLiveData(false)
    val swipeRefreshEnable: LiveData<Boolean>
        get() = _swipeRefreshEnable

    protected val _refreshFailedMessage = MutableLiveData<MessageModel>()
    val refreshFailedMessage: LiveData<MessageModel>
        get() = _refreshFailedMessage

    //  End of Enable Swipe Refresh
    //  is Refreshing
    internal val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean>
        get() = _refreshing
    //  End of is refreshing
    /**
     * End of Refresh Related
     */

    //    Coroutines Related
    //    Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    //    the Coroutine runs using the Main (UI) dispatcher
    protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    open val messageViewCallback = object : MessageView.SimpleCallbacks() {
        override fun onButtonClicked(messageModel: MessageModel?) {
            if (messageModel?.state == MessageView.States.ERROR)
                sendFirstRequestAgain()
        }
    }

    protected fun sendFirstRequestAgain() {
        nextApiUrl = null
        sendServerRequest(false)
    }

    protected open fun sendServerRequest(isRefresh: Boolean) {}

    /**
     * When the [ViewModel] is finished,
     * we cancel our coroutine [viewModelJob],
     * which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        Timber.v("ViewModel going to be cleared")
        cancelBackgroundTasks()
    }

    protected open fun cancelBackgroundTasks() {
        viewModelJob.cancel()
    }

    protected fun <T> sendRequest(
        request: Deferred<Response<T>>,
        requestCode: Int
    ) {
        showLoading(isLoading = true, requestCode = requestCode)
        coroutineScope.launch {
            try {
                val requestResult = request.await()
                showLoading(isLoading = false, requestCode = requestCode)
                when {
                    requestResult.isSuccessful ->
                        onResponseGot(
                            body = requestResult.body(),
                            requestCode = requestCode,
                            responseCode = requestResult.code()
                        )
                    else -> {
                        handleError(
                            resultCode = requestResult.code(),
                            errorBody = requestResult.errorBody(),
                            requestCode = requestCode
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e("Error in Catch of sending request try: ${e.message}")
                showLoading(isLoading = false, requestCode = requestCode)
                handleException(
                    exception = e,
                    requestCode = requestCode
                )
            }
        }
    }

    protected abstract suspend fun <T> onResponseGot(body: T?, requestCode: Int, responseCode: Int)

    private fun handleBadResponse(requestCode: Int) {
        val errorMessageModel = getSomethingWentWrongMessageModel()
        onRequestFailed(
            ErrorModel(
                messageModel = errorMessageModel,
                responseCode = ArmouryConnectionUtils.BAD_RESPONSE,
                requestCode = requestCode,
                code = ArmouryConnectionUtils.BAD_RESPONSE,
                reason = RequestErrorReasons.BAD_RESPONSE
            )
        )
    }

    private fun handleError(
        resultCode: Int,
        errorBody: ResponseBody?,
        requestCode: Int
    ) {
        errorBody?.let {
            try {
                val errorModel = getResponseErrorModel(
                    resultCode = resultCode,
                    errorBody = it,
                    requestCode = requestCode
                )
                if (isAuthorizationError(errorModel.responseCode)) {
                    _authenticationFailed.value = true
                } else {
                    onRequestFailed(errorModel = errorModel)
                }
            } catch (e: Exception) {
                handleBadResponse(requestCode = requestCode)
            }
        } ?: run {
            if (isAuthorizationError(resultCode)) {
                _authenticationFailed.value = true
            } else {
                onSomethingWentWrong(requestCode = requestCode)
            }
        }
    }

    protected open fun isAuthorizationError(code: Int) =
        when (code) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                true
            }
            else -> false
        }

    private fun handleException(
        exception: Exception,
        requestCode: Int
    ) {
        when (exception) {
            is HttpException -> {
                try {
                    handleError(
                        resultCode = 0,
                        errorBody = exception.response()?.errorBody(),
                        requestCode = requestCode
                    )
                } catch (e: Exception) {
                    onSomethingWentWrong(requestCode = requestCode)
                }
            }
            is IOException -> {
                if (ArmouryConnectionUtils.isInternetAvailable(context = applicationContext)) {
                    if (exception is SSLHandshakeException) {
                        onSSLHandshakeException(requestCode = requestCode)
                    } else {
                        onSomethingWentWrong(requestCode = requestCode)
                    }
                } else {
                    val errorMessageModel = MessageModel(
                        state = MessageView.States.ERROR,
                        titleTextRes = R.string.title_error_no_internet,
                        descriptionTextRes = R.string.message_error_no_internet,
                        buttonTextRes = R.string.button_retry
                    )
                    onRequestFailed(
                        errorModel = ErrorModel(
                            messageModel = errorMessageModel,
                            code = ArmouryConnectionUtils.NO_INTERNET,
                            responseCode = ArmouryConnectionUtils.NO_INTERNET,
                            requestCode = requestCode,
                            reason = RequestErrorReasons.NO_INTERNET
                        )
                    )
                }
            }
            else -> {
                onSomethingWentWrong(requestCode = requestCode)
            }
        }
    }

    private fun onSomethingWentWrong(requestCode: Int) {
        val errorMessageModel = MessageModel(
            state = MessageView.States.ERROR,
            titleTextRes = R.string.title_error_something_went_wrong,
            descriptionTextRes = R.string.message_error_something_went_wrong,
            buttonTextRes = R.string.button_retry
        )
        onRequestFailed(
            errorModel = ErrorModel(
                messageModel = errorMessageModel,
                responseCode = ArmouryConnectionUtils.SOMETHING_WENT_WRONG,
                code = ArmouryConnectionUtils.SOMETHING_WENT_WRONG,
                requestCode = requestCode,
                reason = RequestErrorReasons.SOMETHING_WENT_WRONG
            )
        )
    }

    private fun onSSLHandshakeException(requestCode: Int) {
        val errorMessageModel = MessageModel(
            state = MessageView.States.ERROR,
            titleTextRes = R.string.title_error_handshake_certificate_exception,
            descriptionTextRes = R.string.message_error_handshake_certificate_exception,
            buttonTextRes = R.string.button_retry
        )
        onRequestFailed(
            errorModel = ErrorModel(
                messageModel = errorMessageModel,
                responseCode = ArmouryConnectionUtils.SOMETHING_WENT_WRONG,
                code = ArmouryConnectionUtils.SOMETHING_WENT_WRONG,
                requestCode = requestCode,
                reason = RequestErrorReasons.SOMETHING_WENT_WRONG
            )
        )
    }

    internal open fun onRequestFailed(errorModel: ErrorModel) {
        handleErrorInChild(errorModel)
    }

    protected open fun isListRequest(requestCode: Int): Boolean = false

    protected fun setUiAction(action: UA, delayInMillis: Long = 0) {
        _uiAction.value = action
    }

    protected abstract fun handleErrorInChild(errorModel: ErrorModel)
}

//  TODO : Better naming
interface ViewModelGeneralFunctions {

    fun getResponseErrorModel(
        resultCode: Int,
        errorBody: ResponseBody,
        requestCode: Int
    ): ErrorModel

}