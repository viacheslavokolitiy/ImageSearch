package me.imagesearch.utils;

import android.os.Environment;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public final class Constants {
    private static final String DIR = "/Imagesearch/images";
    public static final String CACHE_DIR = Environment.getExternalStorageDirectory() + DIR;
    public static final String PAGE = "page";
    public static final String PHOTO_URL_SELECTED = "selected_photo";
    public static final String IMAGE_URL = "selected_image";
    public static final String IMAGE_TITLE = "image_title";
}
