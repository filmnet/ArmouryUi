package dev.armoury.android.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.File;

//  TODO : Refactor Needed
public class ArmouryGalleryUtils extends ArmouryPickPhotoUtils {

    private final Intent intent =
            new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    public ArmouryGalleryUtils(String baseFolderName, int minimumWith, int minimumHeight) {
        super(baseFolderName, minimumWith, minimumHeight);
    }

    @Override
    public File getRotatedFilePath() {
        //TODO rotated file should be saved in temp directory
        return ArmouryDirectoryUtils.getInstance(mBaseFolderName).getNewFileInPictureDirWithType(getFileType());
    }

    public void pickPhoto(Fragment fragment, int requestCode) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fragment.startActivityForResult(intent, requestCode);
    }

    public void pickPhoto(AppCompatActivity activity, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public void getPhotoFile(Context context, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        image = new File(cursor.getString(columnIndex));
        cursor.close();
    }

    private String getFileType() {
        String photoPath = getImage().getAbsolutePath();
        return photoPath.substring(photoPath.lastIndexOf("."));
    }
}
