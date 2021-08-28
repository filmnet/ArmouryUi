package dev.armoury.android.utils

import java.util.*

open class ArmouryTimeUtils {

    fun getRemainingDisplayTime(remainingTime: Long): String {
        val minutes = remainingTime / MINUTE_IN_MILLI
        val seconds = remainingTime / SECOND_IN_MILLI % 60
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }

    companion object {
        const val SECOND_IN_MILLI = 1000L
        const val MINUTE_IN_MILLI = 60 * SECOND_IN_MILLI
        const val HOUR_IN_MILLI = 60 * MINUTE_IN_MILLI
        const val DAY_IN_MILLI = 24 * HOUR_IN_MILLI
    }

}