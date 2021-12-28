package dev.armoury.android

import android.app.Application
import android.content.Context

open class ArmouryBaseApplication : Application(){

    protected open val needMultiDex = false

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (needMultiDex) installMutlidex()
    }

    protected open fun installMutlidex() {
        TODO("You are using multidex Application, so you need to override this function")
    }


}