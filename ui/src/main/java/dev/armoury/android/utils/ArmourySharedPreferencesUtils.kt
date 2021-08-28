package dev.armoury.android.utils

import android.content.SharedPreferences

abstract class ArmourySharedPreferencesUtils {

    /**
     * String related functions
     */
    protected fun saveString(key: String, value: String) {
        getSharedPreferences()
            .edit()
            .putString(key, value)
            .apply()
    }

    protected fun getString(key: String, defaultValue: String? = null) = getSharedPreferences().getString(key, defaultValue)
    /**
     * End of the string related functions
     */

    /**
     * Boolean related functions
     */
    protected fun saveBoolean(key: String, value: Boolean) {
        getSharedPreferences()
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    protected fun getBoolean(key:String, defaultValue : Boolean = false) = getSharedPreferences() .getBoolean(key, defaultValue)
    /**
     * End of the boolean related functions
     */

    /**
     * Int related functions
     */
    //  Save an int value to the shared preferences
    protected fun saveInt(key: String, value: Int) {
        getSharedPreferences()
            .edit()
            .putInt(key, value)
            .apply()
    }
    //  Get an int value from the shared preferences
    protected fun getInt(key: String, defaultValue: Int) = getSharedPreferences().getInt(key, defaultValue)
    /**
     * End of the int related function
     */

    /**
     *  It's going to delete one or more items which their keys are passed to the function
     */
    protected fun deleteValues(vararg keys: String) {
        if (keys.isEmpty()) return
        val editor = getSharedPreferences().edit()
        for (key in keys) {
            editor.remove(key)
        }
        editor.apply()
    }

    protected abstract fun getSharedPreferences(): SharedPreferences


}