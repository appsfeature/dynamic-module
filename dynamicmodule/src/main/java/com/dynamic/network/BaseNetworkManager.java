package com.dynamic.network;

import android.content.Context;

import com.dynamic.DynamicModule;
import com.dynamic.listeners.ApiRequestType;
import com.dynamic.model.DMCategory;
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
import java.util.List;
import java.util.Map;

public class BaseNetworkManager {
    public static String keyImagePath = BaseConstants.DEFAULT_KEY_IMAGE_PATH;
    public static String keyPdfPath = BaseConstants.DEFAULT_KEY_PDF_PATH;
    private final RetrofitApiInterface apiInterface;
    private Gson gson;

    public BaseNetworkManager() {
        this.apiInterface = null;
    }

    public BaseNetworkManager(Context context, String baseUrl) {
        apiInterface = RetrofitBuilder.getClient(baseUrl, DMUtility.getSecurityCode(context), DynamicModule.getInstance().isEnableDebugMode())
                .create(RetrofitApiInterface.class);
    }
    public BaseNetworkManager(Context context, String baseUrl, String securityCode) {
        apiInterface = RetrofitBuilder.getClient(baseUrl, securityCode, DynamicModule.getInstance().isEnableDebugMode())
                .create(RetrofitApiInterface.class);
    }

    public interface ParserConfigData<T> {
        void onSuccess(T t, String imagePath, String pdfPath);

        void onFailure(Exception error);
    }
    public interface ParserConfigDataSimple<T> {
        void onSuccess(T t);

        void onFailure(Exception error);
    }

    public Gson getGson() {
        if(gson == null) gson = new Gson();
        return gson;
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


    /**
     * @param data : JSON data
     * @param type : new TypeToken<ModelName>() {}.getType()
     * @param callback : response
     * @param <T> : typecast
     */
    public static <T> void parseConfigData(String data, Type type, ParserConfigDataSimple<T> callback) {
        try {
            if (!BaseUtil.isEmptyOrNull(data)) {
                final T t = GsonParser.getGson()
                        .fromJson(data, type);
                if (t != null) {
                    callback.onSuccess(t);
                } else {
                    callback.onFailure(new Exception(BaseConstants.NO_DATA));
                }
            } else {
                callback.onFailure(new Exception(BaseConstants.EMPTY_OR_NULL_DATA));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            callback.onFailure(e);
        }
    }

    /**
     * @param data : JSON data
     * @param objectKey : parse key from data object
     * @param type : new TypeToken<ModelName>() {}.getType()
     * @param callback : response
     * @param <T> : typecast
     */
    public static <T> void parseConfigData(String data, String objectKey, Type type, ParserConfigData<T> callback) {
        try {
            if (!BaseUtil.isEmptyOrNull(data)) {
                JSONObject object = new JSONObject(data);
                if (!BaseUtil.isEmptyOrNull(object.optString(objectKey))) {
                    String imagePath = object.optString(keyImagePath);
                    String pdfPath = object.optString(keyPdfPath);
                    final T t = GsonParser.getGson()
                            .fromJson(object.optString(objectKey), type);
                    if (t != null) {
                        if (t instanceof BaseDataModel) {
                            ((BaseDataModel) t).setImagePath(imagePath);
                            ((BaseDataModel) t).setPdfPath(pdfPath);
                        }
                        callback.onSuccess(t, imagePath, pdfPath);
                    } else {
                        callback.onFailure(new Exception(BaseConstants.NO_DATA));
                    }
                } else {
                    callback.onFailure(new Exception(BaseConstants.NO_DATA));
                }
            } else {
                callback.onFailure(new Exception(BaseConstants.EMPTY_OR_NULL_DATA));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            callback.onFailure(e);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure(e);
        }
    }

    public void getData(@ApiRequestType int reqType, String endPoint, Map<String, String> params, NetworkCallback.Response<NetworkModel> callback) {
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
            Logger.logIntegration(BaseNetworkManager.class.getSimpleName(), "BaseNetworkManager.apiInterface == null\n" + Logger.getClassPath(Thread.currentThread().getStackTrace()));
        }
    }
}
