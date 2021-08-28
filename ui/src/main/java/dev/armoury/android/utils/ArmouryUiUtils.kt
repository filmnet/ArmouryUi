package dev.armoury.android.utils

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


open class ArmouryUiUtils {

    fun hideKeyboard(view: View?) {
        view?.let {
            val inputManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getDeviceDimens(context: Context): Pair<Int, Int> {
        val display =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return Pair(size.x, size.y)
    }

    fun convertDpToPixel(context: Context, dp: Int) =
        dp * (context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT

    fun convertPixelToDp(context: Context, pixel: Int) =
        pixel * DisplayMetrics.DENSITY_DEFAULT / (context.resources.displayMetrics.densityDpi)
}

fun Fragment.hideKeyboard() {
    view?.rootView?.findFocus()?.let { view ->
        val inputManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}