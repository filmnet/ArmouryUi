package dev.armoury.android.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import java.net.URI
import java.net.URISyntaxException

//  TODO DI !?
open class ArmouryConnectionUtils {

    fun getValidUrl(url: String?, secureUrl: Boolean = true) =
        url?.let {
            if (it.startsWith(prefix = "http", ignoreCase = true) ||
                it.startsWith(prefix = "https", ignoreCase = true)
            ) {
                it
            } else {
                "${if (secureUrl) "https" else "http"}://$it"
            }
        }

    fun getAbsoluteUrl(url: String?, baseUrl: String): String? =
        url?.let {
            try {
                val uri = URI(url)
                if (!uri.isAbsolute) {
                    "${baseUrl.removeSuffix("/")}/${url.removePrefix("/")}"
                } else {
                    url
                }
            } catch (e: URISyntaxException) {
                null
            }
        }


    companion object {

        const val BAD_RESPONSE = 600
        const val SOMETHING_WENT_WRONG = 601
        const val NO_INTERNET = 602

        @Suppress("DEPRECATION")
        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }

        @Suppress("DEPRECATION")
        fun connectionIsWifi(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    it.getNetworkCapabilities(cm.activeNetwork)?.run {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    } ?: false
                } else {
                    it.activeNetworkInfo?.run {
                        type == ConnectivityManager.TYPE_WIFI
                    }
                }
            } ?: false
        }

        /**
         * Append an array of query params to a url and return the result.
         * If the url is not formatted correctly, we are going to return null value.
         *
         * @param uri : The url that we are going to attach the query params to it
         * @param queryParams : Array of query params which are going to be attached to the given url
         *
         * @return null or new [URI]
         *
         */
        fun appendQueryParams(uri: String, queryParams: Array<String>?): URI? {
            return try {
                queryParams?.let {
                    val oldUri = URI(uri)
                    var newQuery = oldUri.query
                    for (query in queryParams) {
                        if (newQuery == null) newQuery = query
                        else newQuery += "&$query"
                    }
                    URI(oldUri.scheme, oldUri.authority, oldUri.path, newQuery, oldUri.fragment)
                } ?: run {
                    URI(uri)
                }
            } catch (e: URISyntaxException) {
                null
            }
        }
    }

}

object RequestErrorReasons {

    const val BAD_RESPONSE = "badResponse"
    const val NO_INTERNET = "noInternet"
    const val SOMETHING_WENT_WRONG = "somethingWentWrong"

}