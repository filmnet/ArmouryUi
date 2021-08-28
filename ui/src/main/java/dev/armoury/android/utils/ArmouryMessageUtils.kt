package dev.armoury.android.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import dev.armoury.android.widget.data.MessageModel

open class ArmouryMessageUtils {

    companion object {

        fun getProperMessage(context: Context?,
                             messageModel: MessageModel?) =
            messageModel?.let {
                if (!it.hasDescription()) null
                else if(!it.descriptionText.isNullOrEmpty()) it.descriptionText
                else context?.getString(it.descriptionTextRes)
            }

        fun showRefreshErrorMessage(rootView : View, errorMessage : MessageModel,
                                    length : Int = Snackbar.LENGTH_LONG) {
            Snackbar.make(
                rootView,
                getProperMessage(rootView.context, errorMessage) ?: "",
                length
            )
        }

    }

}