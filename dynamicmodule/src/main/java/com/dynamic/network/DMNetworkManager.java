package com.dynamic.network;

import android.content.Context;
import android.text.TextUtils;

import com.dynamic.DynamicModule;
import com.dynamic.listeners.ApiRequestType;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.util.BaseConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DMNetworkManager extends BaseNetworkManager {

    private static final String GET_CATEGORY = "get-category";
    private static final String GET_CONTENT = "get-content";
    private final Context context;
    private final Gson gson;

    public DMNetworkManager(Context context) {
        super(context, DynamicModule.getInstance().getBaseUrl(context));
        this.context = context;
        this.gson = new Gson();
    }


    public void getDynamicCategory(Response.Callback<List<DMCategory>> callback) {
        Map<String, String> params = new HashMap<>();
        getDynamicCategory(params, callback);
    }

    public void getDynamicCategory(Map<String, String> params, Response.Callback<List<DMCategory>> callback) {
        params.put("pkg_id", context.getPackageName());
        getData(ApiRequestType.GET, GET_CATEGORY, params, new NetworkResponseCallBack.OnNetworkCall() {
            @Override
            public void onComplete(boolean status, NetworkBaseModel data) {
                try {
                    if(status && !TextUtils.isEmpty(data.getData())) {
                        List<DMCategory> list = gson.fromJson(data.getData(), new TypeToken<List<DMCategory>>() {
                        }.getType());
                        if (list != null && list.size() > 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    }else {
                        callback.onFailure(new Exception(data.getMessage()));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getDynamicContent(Response.Callback<List<DMContent>> callback) {
        Map<String, String> params = new HashMap<>();
        getDynamicContent(params, true, callback);
    }

    public void getDynamicContent(Map<String, String> params, boolean isValidate, Response.Callback<List<DMContent>> callback) {
        params.put("pkg_id", context.getPackageName());
        getData(ApiRequestType.GET, GET_CONTENT, params, new NetworkResponseCallBack.OnNetworkCall() {
            @Override
            public void onComplete(boolean status, NetworkBaseModel data) {
                try {
                    if(status && !TextUtils.isEmpty(data.getData())) {
                        List<DMContent> list = gson.fromJson(data.getData(), new TypeToken<List<DMContent>>() {
                        }.getType());
                        if(isValidate) {
                            list = validateData(list);
                        }
                        if (list != null && list.size() > 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    }else {
                        callback.onFailure(new Exception(data.getMessage()));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private List<DMContent> validateData(List<DMContent> list) {
        ArrayList<DMContent> finalList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (DMContent item : list){
                if(item.getVisibility() == 1 && validateDate(item.getUpdatedAt(), item.getOtherProperty())){
                    finalList.add(item);
                }
            }
        }
        return finalList;
    }

    private boolean validateDate(String liveAt, String expiryDate) {
        boolean status = true;
        long liveTimeMills = getTimeInMillis(liveAt);
        if(!TextUtils.isEmpty(liveAt) && liveTimeMills > 0){
            if(System.currentTimeMillis() < liveTimeMills){
                status = false;
            }
        }
        long expiryDateMills = getTimeInMillis(expiryDate);
        if(!TextUtils.isEmpty(expiryDate) && expiryDateMills > 0){
            if(System.currentTimeMillis() > expiryDateMills){
                status = false;
            }
        }
        return status;
    }

    private long getTimeInMillis(String date) {
        try {
            if(date == null) return 0;
            String format = date.contains("T") ? "yyyy-MM-dd'T'HH:mm" : "yyyy-MM-dd HH:mm:ss";
            Date mDate = new SimpleDateFormat(format, Locale.US).parse(date);
            if(mDate != null) return mDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//    private List<DMContent> loadSampleData() {
//        List<DMContent> response = new ArrayList<>();
//        DMContent item = new DMContent();
//        item.setTitle("Happy 1");
//        item.setVisibility(1);
//        item.setLink("https://www.bizwiz.co.in");
//        item.setImage("https://www.bizwiz.co.in/v1/images/aboutus.png");
//        response.add(item);
//
//        item = new DMContent();
//        item.setTitle("Happy 2");
//        item.setVisibility(1);
////        item.setOtherProperty("2022-01-27 12:00:00");
//        item.setOtherProperty("2022-01-27T11:25");
//        item.setImage("https://www.bizwiz.co.in/v1/images/aboutus.png");
//        response.add(item);
//
//        item = new DMContent();
//        item.setTitle("Happy 3");
//        item.setVisibility(1);
//        item.setImage("https://www.bizwiz.co.in/v1/images/aboutus.png");
//        response.add(item);
//        return response;
//    }

}
