package com.sample.dynamicmodule;

import android.view.View;

import com.dynamic.DynamicModule;
import com.dynamic.listeners.DMContentType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.helper.application.BaseApplication;
import com.helper.util.BaseUtil;

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
                .setImageBaseUrl(getInstance(), BASE_IMAGE_URL)
                .addListClickListener(instance.hashCode(), new DynamicCallback.OnDynamicListListener() {
                    @Override
                    public void onItemClicked(View view, DMContent item) {
                        if (item.getItemType() == DMContentType.TYPE_LINK) {
                            if (BaseUtil.isValidUrl(item.getLink())) {
                                BaseUtil.showToast(view.getContext(), "Update Later!");
                            } else {
                                BaseUtil.showToast(view.getContext(), "Invalid Link!");
                            }
                        } else {
                            BaseUtil.showToast(view.getContext(), "Action Update Later");
                        }
                    }
                });
    }

}
