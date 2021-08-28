package dev.armoury.android.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import dev.armoury.android.ui.ArmouryFragment
import java.io.File

//  TODO Review Needed
class ArmouryCameraUtils(baseFolderName: String, minimumWith: Int, minimumHeight: Int) :
    ArmouryPickPhotoUtils(baseFolderName, minimumWith, minimumHeight) {

    override fun getRotatedFilePath(): File = image


    fun takePhoto(
        context: Context,
        fragment: ArmouryFragment<*, *, *>,
        providerAddress: String,
        requestCode: Int
    ) {
        try {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    image = try {
                        createImageFile(context)
                    } catch (e: Exception) {
                        null
                    }
                    image?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            context,
                            providerAddress,
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        fragment.startActivityForResult(takePictureIntent, requestCode)
                    }
                }
            }
        } catch (ignore: Exception) {}
    }


}
