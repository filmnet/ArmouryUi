package dev.armoury.android.ui

import android.graphics.Color
import android.webkit.*
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.viewmodel.ArmouryWebViewViewModel

abstract class ArmouryWebViewFragment<UA: ArmouryUiAction, T : ViewDataBinding, V : ArmouryWebViewViewModel<UA>> :
    ArmouryFragment<UA, T, V>() {

    private val webUrlObserver: Observer<String?> by lazy {
        Observer<String?> { url -> if (!url.isNullOrEmpty()) loadWebPage(url) }
    }

    override fun startObserving() {
        super.startObserving()
        viewModel.webUrl.observe(this, webUrlObserver)
    }

    override fun doOtherTasks() {
        getWebView().apply {
            setBackgroundColor(Color.TRANSPARENT)
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            val settings: WebSettings = settings
            settings.javaScriptEnabled = true
            settings.setSupportZoom(false)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(false)

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    viewModel.setPageLoaded()
                }

                override fun onReceivedError(
                    view: WebView,
                    request: WebResourceRequest,
                    error: WebResourceError
                ) {
                    viewModel.setPageFailed()
                }
            }
        }
    }

    private fun loadWebPage(webUrl: String) {
        getWebView().loadUrl(webUrl)
    }

    protected abstract fun getWebView(): WebView

}