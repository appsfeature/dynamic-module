package com.dynamic;

import android.content.Context;
import android.view.View;

import com.dynamic.database.DMDatabase;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMPreferences;

import java.util.HashMap;
import java.util.Map;

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

    
    private final HashMap<Integer, DynamicCallback.OnDynamicListListener> mListClickListener = new HashMap<>();
    
    public DynamicModule addListClickListener(int hashCode, DynamicCallback.OnDynamicListListener callback) {
        synchronized (mListClickListener) {
            this.mListClickListener.put(hashCode, callback);
        }
        return this;
    }
    
    public void removeListClickListener(int hashCode) {
        if (mListClickListener.get(hashCode) != null) {
            synchronized (mListClickListener) {
                this.mListClickListener.remove(hashCode);
            }
        }
    }
    
    public void dispatchListClickListener(View view, DMContent item) {
        try {
            if (mListClickListener.size() > 0) {
                for (Map.Entry<Integer, DynamicCallback.OnDynamicListListener> entry : mListClickListener.entrySet()) {
                    Integer key = entry.getKey();
                    DynamicCallback.OnDynamicListListener callback = entry.getValue();
                    if (callback != null) {
                        callback.onItemClicked(view, item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
