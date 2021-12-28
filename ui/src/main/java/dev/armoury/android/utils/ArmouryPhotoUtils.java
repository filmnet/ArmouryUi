package dev.armoury.android.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// TODO Refactor Needed
public class ArmouryPhotoUtils {

	private static final String TAG = "PhotoUtil";

	private static final int IMAGE_COMPRESSION = 70;

	public static interface RotationCallback {

		void onRotationReady(int rotation);

	}

	public static Bitmap fitBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		if (width >= height) {
			if (width <= maxWidth)
				return bitmap;
			float ratio = height / width;
			width = maxWidth;
			height = ratio * width;
		} else if (width < height) {
			if (height <= maxHeight)
				return bitmap;
			float ratio = width / height;
			height = maxHeight;
			width = ratio * height;
		}
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) width,
                (int) height, false);
		return resizedBitmap;
	}

	public static Bitmap readImage(String imageFile, int width, int height) {
		// First decode with inJustDecodeBounds=true to check dimensions

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageFile, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imageFile, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if(width > height){
			int temp = reqHeight;
			reqHeight = reqWidth;
			reqWidth = temp;
		}

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static void writeImage(Bitmap bitmap, String imageFile) {
		try {
			OutputStream profileImageStream = new FileOutputStream(imageFile);
			bitmap.compress(CompressFormat.JPEG, IMAGE_COMPRESSION,
					profileImageStream);
			profileImageStream.close();
		} catch (IOException e) {
		}
	}

	public boolean checkImageSize(File image, int minimumWith, int minimumHeight) throws IOException {
		InputStream inputStream = new FileInputStream(image);

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
		int imageWidth = bitmapOptions.outWidth;
		int imageHeight = bitmapOptions.outHeight;
		inputStream.close();

		return imageWidth >= minimumWith && imageHeight >= minimumHeight;
	}

	public Bitmap rotateBitmap(String file, float angle, int height, int width) {
		Bitmap source = ArmouryPhotoUtils.readImage(file, height, width);

		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public int getAngle(String file) throws IOException {
		ExifInterface ei = new ExifInterface(file);
		int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

		switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
            default:
                return 0;
        }
    }

    public String saveBitmapToFile(Bitmap bitmap, String fileDestination) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileDestination);
            bitmap.compress(CompressFormat.PNG, 100, out);
            return fileDestination;
        } catch (Exception e) {
//            TODO
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
