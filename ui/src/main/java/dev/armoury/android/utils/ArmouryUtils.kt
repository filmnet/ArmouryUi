package dev.armoury.android.utils

import androidx.lifecycle.MutableLiveData

/**
 * And extension function to notify a MutableLiveData
 * that one or more attributes of it updated.
 */
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}