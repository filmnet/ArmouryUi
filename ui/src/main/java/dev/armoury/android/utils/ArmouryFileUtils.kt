package dev.armoury.android.utils

import java.io.File

open class ArmouryFileUtils {

    private val TAG = "FileUtils"

    fun getFileByPath(filePath: String?): File? {
        if (filePath.isNullOrEmpty()) return null
        return try {
            File(filePath)
        } catch (e: Exception) {
            null
        }

    }

}