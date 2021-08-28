package dev.armoury.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO Refactor Needed
public abstract class ArmouryPickPhotoUtils {

    protected File image; // TODO
    protected final int mMinimumWith;
    protected final int mMinimumHeight;
    protected ArmouryPhotoUtils mArmouryPhotoUtils;

    protected String mBaseFolderName;

    public ArmouryPickPhotoUtils(String baseFolderName, int minimumWith, int minimumHeight) {
        this.mMinimumHeight = minimumHeight;
        this.mMinimumWith = minimumWith;
        this.mArmouryPhotoUtils = new ArmouryPhotoUtils();
        this.mBaseFolderName = baseFolderName;
    }

    public boolean isValid() {
        try {
            return mArmouryPhotoUtils.checkImageSize(image, mMinimumWith, mMinimumHeight);
        } catch (IOException e) {
//            TODO
        }
        return false;
    }

    public File getImage() {
        return image;
    }

    public String rotate(int height, int width) {
        try {
            int angle = mArmouryPhotoUtils.getAngle(image.getAbsolutePath());
            if (angle != 0) {
                String imagePath = rotateImage(angle, height, width);

                if (imagePath != null)
                    image = new File(imagePath);
            }
        } catch (IOException e) {
//            TODO
        } catch (Exception e) {
//            TODO
        }

        return image.getAbsolutePath();
    }

    private String rotateImage(int angle, int height, int width) {
        Bitmap bitmap = mArmouryPhotoUtils.rotateBitmap(image.getAbsolutePath(), angle, height, width);
        return mArmouryPhotoUtils.saveBitmapToFile(
                bitmap,
                getRotatedFilePath().getAbsolutePath());
    }

    protected File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public abstract File getRotatedFilePath();

}
