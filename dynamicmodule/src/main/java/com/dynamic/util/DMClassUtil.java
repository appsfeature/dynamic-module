package com.dynamic.util;

import android.app.Activity;
import android.content.Intent;

import com.dynamic.activity.DynamicActivity;
import com.dynamic.activity.DynamicListActivity;

public class DMClassUtil {

    public static void openDynamicActivity(Activity activity, DMProperty property) {
        if (activity != null && property != null) {
            activity.startActivity(new Intent(activity, DynamicActivity.class)
                    .putExtra(DMConstants.CATEGORY_PROPERTY, property));
        }else {
            DMUtility.showPropertyError(activity);
        }
    }

    public static void openDynamicListActivity(Activity activity, DMProperty property) {
        if (activity != null && property != null) {
            activity.startActivity(new Intent(activity, DynamicListActivity.class)
                    .putExtra(DMConstants.CATEGORY_PROPERTY, property));
        }else {
            DMUtility.showPropertyError(activity);
        }
    }
}
