package com.dynamic;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.dynamic.database.DMDatabase;
import com.dynamic.database.DMDatabaseManager;
import com.dynamic.listeners.ApiHost;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.network.ConfigManager;
import com.dynamic.network.DMNetworkManager;
import com.dynamic.util.DMConstants;
import com.dynamic.util.DMPreferences;
import com.dynamic.util.DMProperty;

import java.util.HashMap;
import java.util.Map;

public class DynamicModule {

    private static volatile DynamicModule instance;
    private boolean isEnableDebugMode = false;
    private DMNetworkManager networkManager;
    private DMDatabaseManager databaseManager;
    private ConfigManager configManager;

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

    /**
     * @apiNote : Must call this method after method setDebugMode.
     */
    public DynamicModule addBaseUrlHost(Context context, String hostName, String baseUrl) {
        ConfigManager.getInstance(context).addHostUrl(hostName, baseUrl);
        return this;
    }
    public DynamicModule addBaseUrlHost(Context context, Map<String, String> hostMap) {
        ConfigManager.getInstance(context).addHostUrl(hostMap);
        return this;
    }

    public DynamicModule setImageBaseUrl(Context context, String hostName, String value) {
        DMPreferences.setImageBaseUrl(context, hostName, value);
        return this;
    }

    public String getImageBaseUrl(Context context) {
        return DMPreferences.getImageBaseUrl(context, ApiHost.HOST_DEFAULT);
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
    
    public void dispatchListClickListener(Activity activity, View view, DMProperty parent, DMContent item) {
        try {
            if (mListClickListener.size() > 0) {
                for (Map.Entry<Integer, DynamicCallback.OnDynamicListListener> entry : mListClickListener.entrySet()) {
                    Integer key = entry.getKey();
                    DynamicCallback.OnDynamicListListener callback = entry.getValue();
                    if (callback != null) {
                        callback.onItemClicked(activity, view, parent, item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DMNetworkManager getNetworkManager(Context context) {
        if(networkManager == null) networkManager = new DMNetworkManager(context);
        return networkManager;
    }

    public DMDatabaseManager getDatabaseManager(Context context) {
        if(databaseManager == null) databaseManager = new DMDatabaseManager(context);
        return databaseManager;
    }

    public void init(Context context) {
        addBaseUrlHost(context, ApiHost.HOST_DEFAULT, DMConstants.DEFAULT_BASE_URL);
        setImageBaseUrl(context, ApiHost.HOST_DEFAULT, DMConstants.DEFAULT_BASE_IMAGE_URL);
    }
}
