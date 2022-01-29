package com.dynamic.util;

import android.content.Context;

import com.helper.util.BasePrefUtil;

public class DMPreferences extends BasePrefUtil {

    private static final String BASE_IMAGE_URL = "base_image_url";
    private static final String BASE_URL = "base_url";

    public static void setImageBaseUrl(Context context, String value) {
        setString(context, BASE_IMAGE_URL, value);
    }

    public static String getImageBaseUrl(Context context) {
        return getString(context, BASE_IMAGE_URL);
    }

    public static void setBaseUrl(Context context, String value) {
        setString(context, BASE_URL, value);
    }

    public static String getBaseUrl(Context context) {
        return getString(context, BASE_URL);
    }
}
