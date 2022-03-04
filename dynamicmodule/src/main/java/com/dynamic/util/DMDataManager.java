package com.dynamic.util;

import android.content.Context;
import android.text.TextUtils;

import com.dynamic.database.DMDatabaseManager;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.network.DMNetworkManager;
import com.google.gson.Gson;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public class DMDataManager {
    private final Context context;
    private final Gson gson;
    private final DMNetworkManager networkManager;
    private final DMDatabaseManager dbManager;

    public DMDataManager(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.dbManager = new DMDatabaseManager(context);
        this.networkManager = new DMNetworkManager(context);
    }

    public void setDisableCaching(boolean disableCaching) {
        dbManager.setDisableCaching(disableCaching);
    }

    public void getContent(int catId, DynamicCallback.Listener<List<DMContent>> callback) {
        networkManager.getContent(catId, new DynamicCallback.Listener<List<DMContent>>() {
            @Override
            public void onSuccess(List<DMContent> response) {
                callback.onValidate(arraySortContent(response), new Response.Status<List<DMContent>>() {
                    @Override
                    public void onSuccess(List<DMContent> response) {
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }

    public void getDynamicData(int catId, DynamicCallback.Listener<List<DMCategory>> callback) {
        getDynamicData(catId, null, callback);
    }

    public void getDynamicData(int catId, List<DMCategory> staticList, DynamicCallback.Listener<List<DMCategory>> callback) {
        dbManager.getDynamicData(catId, staticList, callback);
        networkManager.getDataBySubCategory(catId, new DynamicCallback.Listener<List<DMCategory>>() {
            @Override
            public void onSuccess(List<DMCategory> response) {
                dbManager.saveDynamicData(catId, response);
                if(staticList != null && staticList.size() > 0){
                    response.addAll(staticList);
                }
                callback.onValidate(arraySortCategory(response), new Response.Status<List<DMCategory>>() {
                    @Override
                    public void onSuccess(List<DMCategory> response) {
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }

    public void getDataByCategory(int catId, DynamicCallback.Listener<List<DMContent>> callback) {
        dbManager.getDataByCategory(catId, callback);
        networkManager.getDataByCategory(catId, new DynamicCallback.Listener<List<DMContent>>() {
            @Override
            public void onSuccess(List<DMContent> response) {
                dbManager.insertData(response);
                callback.onValidate(arraySortContent(response), new Response.Status<List<DMContent>>() {
                    @Override
                    public void onSuccess(List<DMContent> response) {
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }


    private List<DMCategory> arraySortCategory(List<DMCategory> list) {
        Collections.sort(list, new Comparator<DMCategory>() {
            @Override
            public int compare(DMCategory item, DMCategory item2) {
                Integer value = item.getRanking();
                Integer value2 = item2.getRanking();
                return value.compareTo(value2);
            }
        });
        return list;
    }

    private List<DMContent> arraySortContent(List<DMContent> list) {
        Collections.sort(list, new Comparator<DMContent>() {
            @Override
            public int compare(DMContent item, DMContent item2) {
                Integer value = item.getRanking();
                Integer value2 = item2.getRanking();
                return value.compareTo(value2);
            }
        });
        return list;
    }
}
