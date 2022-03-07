package com.dynamic.network;

import android.content.Context;

import androidx.annotation.Nullable;

import com.dynamic.DynamicModule;
import com.dynamic.listeners.ApiHost;
import com.dynamic.listeners.ApiRequestType;
import com.dynamic.util.DMConstants;
import com.dynamic.util.DMUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.helper.model.base.BaseDataModel;
import com.helper.util.BaseConstants;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.helper.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private static ConfigManager instance;
    private final Context context;
    private final HashMap<String, String> apiHostUrlHashMap = new HashMap<>();
    private final HashMap<String, RetrofitApiInterface> apiInterfaceHashMap = new HashMap<>();
    public static final String HOST_DEFAULT = ApiHost.HOST_DEFAULT;

    private ConfigManager(Context context) {
        this.context = context;
    }

    public static ConfigManager getInstance(Context context) {
        if(instance == null) instance = new ConfigManager(context);
        return instance;
    }

    public static <T> List<T> addBaseUrlOnList(List<T> items, String imagePath, String pdfPath) {
        for (T item : items){
            if(item instanceof BaseDataModel){
                ((BaseDataModel)item).setImagePath(imagePath);
                ((BaseDataModel)item).setPdfPath(pdfPath);
            }
        }
        return items;
    }

    public void getData(@ApiRequestType int reqType, String endPoint, Map<String, String> params, NetworkCallback.Response<NetworkModel> callback) {
        getData(reqType, HOST_DEFAULT, endPoint, params, callback);
    }

    public void getData(@ApiRequestType int reqType, String hostName, String endPoint, Map<String, String> params, NetworkCallback.Response<NetworkModel> callback) {
        RetrofitApiInterface apiInterface = getApiInterface(hostName);
        if(apiInterface != null) {
            if (reqType == ApiRequestType.POST) {
                apiInterface.requestPost(endPoint, params)
                        .enqueue(new ResponseCallBack<>(callback));
            } else if (reqType == ApiRequestType.POST_FORM) {
                apiInterface.requestPostDataForm(endPoint, params)
                        .enqueue(new ResponseCallBack<>(callback));
            } else {
                apiInterface.requestGet(endPoint, params)
                        .enqueue(new ResponseCallBack<>(callback));
            }
        }else {
            DMUtility.logIntegration(TAG_OK_HTTP, Logger.getClassPath(Thread.currentThread().getStackTrace()) ,
                    "\nBaseNetworkManager.apiInterface == null",
                    "\nhostName : " + hostName,
                    "\nendPoint : " + endPoint
            );
        }
    }

    @Nullable
    private RetrofitApiInterface getApiInterface(String hostName) {
        return getHostInterface(getHostBaseUrl(hostName));
    }

    @Nullable
    private String getHostBaseUrl(String hostName) {
        return apiHostUrlHashMap.get(hostName);
    }

    public void addHostUrl(Map<String, String> hostMap) {
        for (Map.Entry<String,String> entry : hostMap.entrySet()) {
          String hostName = entry.getKey();
          String baseUrl = entry.getValue();
          addHostUrl(hostName, baseUrl);
        }
    }

    public void addHostUrl(String hostName, String baseUrl) {
        if (apiHostUrlHashMap.get(hostName) == null) {
            apiHostUrlHashMap.put(hostName, baseUrl);
            getHostInterface(baseUrl);
        }
    }

    private RetrofitApiInterface getHostInterface(String hostBaseUrl) {
        if(hostBaseUrl == null) return null;
        if (apiInterfaceHashMap.get(hostBaseUrl) != null) {
            return apiInterfaceHashMap.get(hostBaseUrl);
        } else {
            RetrofitApiInterface apiInterface = RetrofitBuilder.getClient(hostBaseUrl, DMUtility.getSecurityCode(context), DynamicModule.getInstance().isEnableDebugMode())
                    .create(RetrofitApiInterface.class);
            apiInterfaceHashMap.put(hostBaseUrl, apiInterface);
            return apiInterface;
        }
    }

    public static final String TAG_OK_HTTP = ConfigManager.class.getSimpleName() + "-okhttp-log";
}
