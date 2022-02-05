package com.dynamic.util;

import android.content.Context;
import android.text.TextUtils;

import com.dynamic.model.DMContent;
import com.dynamic.network.DMNetworkManager;
import com.google.gson.Gson;
import com.helper.callback.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DMDataManager {
    private final Context context;
    private final Gson gson;
    private final DMNetworkManager networkManager;

    public DMDataManager(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.networkManager = new DMNetworkManager(context);
    }

    public void getContent(int catId, boolean isValidate, Response.Callback<List<DMContent>> callback) {
        networkManager.getContent(catId, new Response.Callback<List<DMContent>>() {
            @Override
            public void onSuccess(List<DMContent> response) {
                if(isValidate) {
                    List<DMContent> mList = validateData(response);
                    callback.onSuccess(mList);
                }else {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFailure(Exception e) {
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
