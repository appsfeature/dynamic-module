package com.dynamic;

import android.app.Activity;
import android.content.Intent;

import com.dynamic.activity.DynamicActivity;
import com.dynamic.activity.DynamicListActivity;
import com.dynamic.util.DMConstants;
import com.dynamic.util.DMProperty;
import com.dynamic.util.DMUtility;

public class DMClassUtil {

    public static void openDynamicActivity(Activity activity, DMProperty property) {
        if (activity != null && property != null) {
            activity.startActivity(new Intent(activity, DynamicActivity.class)
                    .putExtra(DMConstants.CATEGORY_PROPERTY, property));
        }else {
            DMUtility.showPropertyError(activity);
        }
    }
    public static void openDynamicActivity2(Activity activity, DMProperty property) throws DynamicModuleException{
        try {
            if (activity != null && property != null) {
                activity.startActivity(new Intent(activity, DynamicActivity.class)
                        .putExtra(DMConstants.CATEGORY_PROPERTY, property));
            }else {
                DMUtility.showPropertyError(activity);
            }
        } catch (Exception e) {
            throw new DynamicModuleException(e);
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
