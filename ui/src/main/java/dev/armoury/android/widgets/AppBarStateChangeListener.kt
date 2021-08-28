package dev.armoury.android.widgets

import androidx.annotation.StringDef
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    @set:State
    @get:State
    //  TODO
    private var currentState: String = States.IDLE
        set(value) {
            if (field != value) {
                field = value
                onStateChanged(field)
            }
        }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        currentState = when (abs(verticalOffset)) {
            0 -> States.EXPANDED
            in appBarLayout.totalScrollRange..Int.MAX_VALUE -> States.COLLAPSED
            else -> States.IDLE
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        States.EXPANDED,
        States.IDLE,
        States.COLLAPSED
    )
    annotation class State

    object States {
        const val EXPANDED = "expanded"
        const val IDLE = "idle"
        const val COLLAPSED = "collapsed"
    }

    abstract fun onStateChanged(@State state: String)
}