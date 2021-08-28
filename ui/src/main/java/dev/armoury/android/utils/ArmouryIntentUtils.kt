package dev.armoury.android.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

open class ArmouryIntentUtils {

    fun openWebPage(context: Context, url: String?) {
        url?.let {
            val webPage = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
    }

    fun getOpenWebPageInt(context: Context, url: String?): Intent? {
        val webPage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        return if (intent.resolveActivity(context.packageManager) != null) {
            intent
        } else {
            null
        }
    }

    fun shareText(context: Context, text: String?) {
        getShareTextIntent(context = context, text = text)?.let {
            context.applicationContext.startActivity(it)
        }
    }

    fun getShareTextIntent(context: Context, text: String?): Intent? {
        return text?.let {
            val shareTextIntent = Intent(Intent.ACTION_SEND)
            shareTextIntent.type = "text/plain"
            shareTextIntent.putExtra(Intent.EXTRA_TEXT, it)
            if (shareTextIntent.resolveActivity(context.packageManager) != null) shareTextIntent else null
        }
    }


}