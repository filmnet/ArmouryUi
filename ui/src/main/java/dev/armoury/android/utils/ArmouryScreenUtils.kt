package dev.armoury.android.utils

import android.app.Activity
import android.content.pm.ActivityInfo

fun Activity.isPortrait() =
    requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
            requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT ||
            requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT