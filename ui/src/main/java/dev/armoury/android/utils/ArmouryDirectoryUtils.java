package dev.armoury.android.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO Refactor Needed
public class ArmouryDirectoryUtils {

    public static final String PROJECT_NAME = "ProjectName";

    private static final int FILE_NAME_MAX_LENGTH = 10;
    private File baseDir;
    private File pictureDir;
    private File tempDir;

    private final String PICTURE_FOLDER = "Pictures";
    private final String TEMP_FOLDER = ".temp";

    private static String baseFolderName;

    private static ArmouryDirectoryUtils directories;

    public static ArmouryDirectoryUtils getInstance(String baseFolderName) {
        if (directories == null) {
            directories = new ArmouryDirectoryUtils();
            ArmouryDirectoryUtils.baseFolderName = baseFolderName;
        }
        return directories;
    }

    /**
     * get project base directory
     *
     * @return File object
     * @info: if project directory doesn't exist, this method create it
     * @address: /sdcard/{project_name}
     */
    public File getBaseDir() {
        if (baseDir == null)
            baseDir = new File(
                    Environment
                            .getExternalStorageDirectory(),
                    baseFolderName
            );

        if (!baseDir.exists())
            baseDir.mkdir();
        return baseDir;
    }

    /**
     * get picture directory
     *
     * @return File object
     * @info: if picture directory doesn't exist, this method create it
     * @address: /sdcard/{project_name}/Pictures
     */
    public File getPictureDir() {
        if (pictureDir == null)
            pictureDir = new File(
                    getBaseDir().getAbsolutePath(),
                    PICTURE_FOLDER
            );

        if (!pictureDir.exists())
            pictureDir.mkdir();

        return pictureDir;
    }

    /**
     * Return a file name in Picture directory
     *
     * @param fileName
     * @return File Object
     * @warning: this method doesn't create file !! it just return file object with fileName
     */
    public File getNewFileInPictureDir(String fileName) {
        return new File(
                getPictureDir().getAbsolutePath(),
                fileName
        );
    }

    public File getNewFileInRoot(String fileName) {
        return new File(
                getBaseDir().getAbsoluteFile(),
                fileName
        );
    }

    public File getNewFileInPictureDirWithType(String type) {
        return new File(
                getPictureDir().getAbsolutePath(),
                getImageFileName(type)
        );
    }

    /**
     * get temp directory
     *
     * @return File object
     * @info: if temp directory doesn't exist, this method create it
     * @info: temp directory is hidden
     * @address: /sdcard/{project_name}/.temp
     */
    public File getTempDir() {
        if (tempDir == null)
            tempDir = new File(
                    getBaseDir().getAbsolutePath(),
                    TEMP_FOLDER
            );

        if (!tempDir.exists())
            tempDir.mkdir();

        return tempDir;
    }

    /**
     * Return a file name in Temp directory
     *
     * @param fileName
     * @return File Object
     * @warning: this method doesn't create file !! it just return file object with fileName
     */
    public File getNewFileInTempDir(String fileName) {
        return new File(
                getTempDir().getAbsolutePath(),
                fileName
        );
    }

    public File getNewFileInTempDir() {
        while (true) {
            File file = new File(
                    getTempDir().getAbsolutePath(),
                    getRandomFileName());
            if (!file.exists())
                return file;
        }
    }

    public static String getRandomFileName() {
        return new SimpleDateFormat("'temp-'yyyy-MM-dd-hh-mm-SSS").format(new Date());
    }

    public static String getImageFileName(String type) {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + type;
    }

    public static File getParentFile(@NonNull Context context) {
        final File externalSaveDir = context.getExternalCacheDir();
        if (externalSaveDir == null) {
            return context.getCacheDir();
        } else {
            return externalSaveDir;
        }
    }
}
