package dev.armoury.android.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import dev.armoury.android.R
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.lifecycle.SingleLiveEvent
import dev.armoury.android.widget.MessageView
import dev.armoury.android.widget.data.MessageModel

abstract class ArmouryWebViewViewModel<UA : ArmouryUiAction>(applicationContext: Application) :
    ArmouryViewModel<UA>(applicationContext = applicationContext) {

    abstract fun loadingMessage(): Int?
    abstract fun loadingErrorMessage(): Int?

    private val _isPageLoaded = SingleLiveEvent<Boolean>(false)
    val isPageLoaded: LiveData<Boolean>
        get() = _isPageLoaded

    protected val _webUrl = SingleLiveEvent<String?>(null)
    val webUrl: LiveData<String?>
        get() = _webUrl

    override val messageViewCallback = object : MessageView.SimpleCallbacks() {
        override fun onButtonClicked(messageModel: MessageModel?) {
            _messageModel.value = loadingMessageModel
            _webUrl.call()
        }
    }

    private val errorMessage: MessageModel =
        MessageModel(
            state = MessageView.States.ERROR,
            descriptionTextRes = R.string.message_error_loading_web_page
        )

    init {
        loadingMessageModel.descriptionTextRes = R.string.message_loading_web_page
    }

    fun setPageLoaded() {
        _isPageLoaded.value = true
        _messageModel.value = MessageModel(state = MessageView.States.HIDE)
    }

    fun setPageFailed() {
        _messageModel.value = errorMessage
    }

}