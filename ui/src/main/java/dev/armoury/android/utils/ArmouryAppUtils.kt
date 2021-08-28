package dev.armoury.android.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

open class ArmouryAppUtils {

    fun restartApplication(context: Context) {
        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.applicationContext.startActivity(intent)
    }

    /*
     * Permission Related
     */
    /**
     * Check whether we have the storage permission or not
     */
    fun hasWriteExternalPermission(context: Context) = hasNeededPermission(
        context = context,
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * Check whether we have the given permission or not
     */
    private fun hasNeededPermission(
        context: Context,
        permission: String
    ) = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    /*
     * End of the permission related
     */

    /*
     * Check if an application is installed or not
     */
    /**
     * Check whether the Bazaar is installed or not
     */
    fun isBazaarInstalled(packageManager: PackageManager): Boolean {
        return isAppInstalled(
            packageManager = packageManager,
            applicationPackageName = PackageNames.BAZAAR
        )
    }

    /**
     * Check whether an application withe the given packageName is intalled or not
     */
    private fun isAppInstalled(
        packageManager: PackageManager,
        applicationPackageName: String
    ): Boolean {
        return try {
            packageManager.getPackageInfo(applicationPackageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    object PackageNames {

        const val BAZAAR = "com.farsitel.bazaar"

    }

    /*
     * End of the check if an application is installed or not
     */

}