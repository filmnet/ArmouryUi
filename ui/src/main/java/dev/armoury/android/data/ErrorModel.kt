package dev.armoury.android.data

import dev.armoury.android.widget.data.MessageModel

data class ErrorModel(
    val messageModel: MessageModel,
    val responseCode: Int,
    val code: Int? = null,
    val requestCode: Int,
    val reason: String?
)