package com.sample.dynamicmodule;

import com.dynamic.DynamicModule;
import com.helper.application.BaseApplication;

public class AppApplication extends BaseApplication {


    private static final String BASE_URL = "http://appsfeature.com/droidapps/api/v1/database/";
    private static final String BASE_IMAGE_URL = "http://appsfeature.com/droidapps/public/uploads/images/";
    private static AppApplication instance;

    public static AppApplication getInstance() {
        return instance;
    }

    @Override
    public boolean isDebugMode() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DynamicModule.getInstance()
                .setDebugMode(isDebugMode())
                .setBaseUrl(getInstance(), BASE_URL)
                .setImageBaseUrl(getInstance(), BASE_IMAGE_URL);
    }

}
