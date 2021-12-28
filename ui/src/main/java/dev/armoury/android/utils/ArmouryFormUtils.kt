package dev.armoury.android.utils

open class ArmouryFormUtils {

     // TODO More customization needed later
    fun isCellPhoneValid(cellPhone: CharSequence?, phoneLength: Int = 11, prefix: String? = "09") =
        !cellPhone.isNullOrEmpty() && cellPhone.length == phoneLength && cellPhone.startsWith(prefix = prefix ?: "")

    fun isPasswordValid(password: CharSequence?, minAcceptableLength: Int) =
        !password.isNullOrEmpty() && password.length >= minAcceptableLength

    fun isTextValid(text: CharSequence?, minLength: Int = 1) =
        text?.let { it.length >= minLength } ?: false

}