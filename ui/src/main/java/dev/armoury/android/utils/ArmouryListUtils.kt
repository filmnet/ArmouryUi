package dev.armoury.android.utils

import androidx.annotation.IntDef

open class ArmouryListUtils {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        States.NOT_SET,
        States.LOADING, States.LOADING_FAILED, States.LOADING_DONE, States.LOADING_EMPTY,
        States.LOADING_MORE, States.LOADING_MORE_FAILED, States.LOADING_MORE_DONE,
        States.LOADING_REFRESH, States.LOADING_REFRESH_DONE, States.LOADING_REFRESH_FAILED
    )
    annotation class State

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(RequestTypes.FIRST_REQUEST, RequestTypes.REFRESH, RequestTypes.LOAD_MORE)
    annotation class RequestType

    object RequestTypes {

        const val NOT_SET = 0
        const val FIRST_REQUEST = 10
        const val REFRESH = 20
        const val LOAD_MORE = 30

    }

    object States {

        const val NOT_SET = 0

        const val LOADING = 10
        const val LOADING_FAILED = 11
        const val LOADING_DONE = 12
        const val LOADING_EMPTY = 13

        const val LOADING_MORE = 20
        const val LOADING_MORE_FAILED = 21
        const val LOADING_MORE_DONE = 22

        const val LOADING_REFRESH = 30
        const val LOADING_REFRESH_DONE = 31
        const val LOADING_REFRESH_FAILED = 32


    }

}

fun MutableList<*>.removeLastItem() {
    val lastIndex = this.lastIndex
    if (lastIndex >= 0) this.removeAt(lastIndex)
}