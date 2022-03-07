package com.dynamic.util;

import android.content.Context;

import com.dynamic.DynamicModule;
import com.helper.util.BasePrefUtil;

public class DMPreferences extends BasePrefUtil {

    private static final String BASE_IMAGE_URL = "base_image_url";
    private static final String DYNAMIC_DATA = "dynamic_data";

    public static void setImageBaseUrl(Context context, String hostName, String value) {
        setString(context, BASE_IMAGE_URL + hostName, value);
    }

    public static String getImageBaseUrl(Context context, String hostName) {
        return getString(context, BASE_IMAGE_URL + hostName, DMConstants.DEFAULT_BASE_IMAGE_URL);
    }

    public static void setDynamicData(Context context, int catId, String value) {
        setString(context, DYNAMIC_DATA + catId, value);
    }

    public static String getDynamicData(Context context, int catId) {
        return getString(context, DYNAMIC_DATA + catId, "");
    }
}
