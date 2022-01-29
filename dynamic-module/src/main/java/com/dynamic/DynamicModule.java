package com.dynamic;

import android.content.Context;

import com.dynamic.database.DMDatabase;
import com.dynamic.util.DMPreferences;

public class DynamicModule {

    private static volatile DynamicModule instance;
    private boolean isEnableDebugMode = false;

    private DynamicModule() { }

    public static DynamicModule getInstance() {
        if (instance == null) {
            synchronized (DynamicModule.class) {
                if (instance == null) instance = new DynamicModule();
            }
        }
        return instance;
    }

    private DMDatabase dmDatabase;

    public DMDatabase getDatabase(Context context) {
        if (dmDatabase == null) {
            dmDatabase = DMDatabase.create(context);
        }
        return dmDatabase;
    }

    public boolean isEnableDebugMode() {
        return isEnableDebugMode;
    }

    public DynamicModule setDebugMode(Boolean isDebug) {
        isEnableDebugMode = isDebug;
        return this;
    }

    public DynamicModule setImageBaseUrl(Context context, String value) {
        DMPreferences.setImageBaseUrl(context, value);
        return this;
    }

    public String getImageBaseUrl(Context context) {
        return DMPreferences.getImageBaseUrl(context);
    }

    public DynamicModule setBaseUrl(Context context, String value) {
        DMPreferences.setBaseUrl(context, value);
        return this;
    }

    public String getBaseUrl(Context context) {
        return DMPreferences.getBaseUrl(context);
    }

}
