package com.dynamic.util;

import android.content.Context;

import com.dynamic.DynamicModule;
import com.dynamic.database.DMDatabaseManager;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.model.DMVideo;
import com.dynamic.network.DMNetworkManager;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class DMDataManager extends DMBaseSorting{
    private static DMDataManager instance;
    protected final DMNetworkManager networkManager;
    protected final DMDatabaseManager dbManager;

    public DMDataManager(Context context) {
        this.dbManager = DynamicModule.getInstance().getDatabaseManager(context);
        this.networkManager = DynamicModule.getInstance().getNetworkManager(context);
    }

    public static DMDataManager get(Context context) {
        if(instance == null) instance = new DMDataManager(context);
        return instance;
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

    public void getDataBySubCategory(int catId, DynamicCallback.Listener<List<DMCategory<DMContent>>> callback) {
        getDataBySubCategory(catId, null, false, callback);
    }

    public void getDataBySubCategory(int catId, List<DMCategory<DMContent>> staticList, boolean isOrderByAsc, DynamicCallback.Listener<List<DMCategory<DMContent>>> callback) {
        dbManager.getDynamicData(catId, staticList, isOrderByAsc, callback);
        networkManager.getDataBySubCategory(catId, new DynamicCallback.Listener<List<DMCategory<DMContent>>>() {
            @Override
            public void onSuccess(List<DMCategory<DMContent>> response) {
                dbManager.saveDynamicData(catId, response, isOrderByAsc, new Response.Status<List<DMCategory<DMContent>>>() {
                    @Override
                    public void onSuccess(List<DMCategory<DMContent>> result) {
                        if(staticList != null && staticList.size() > 0){
                            result.addAll(staticList);
                        }
                        callback.onValidate(arraySortCategory(result, isOrderByAsc), new Response.Status<List<DMCategory<DMContent>>>() {
                            @Override
                            public void onSuccess(List<DMCategory<DMContent>> result) {
                                callback.onSuccess(result);
                            }
                        });
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

    public void getDataByCategory(int catId, boolean isOrderByAsc, DynamicCallback.Listener<List<DMContent>> callback) {
        dbManager.getDataByCategory(catId, isOrderByAsc, callback);
        networkManager.getDataByCategory(catId, new DynamicCallback.Listener<List<DMContent>>() {
            @Override
            public void onSuccess(List<DMContent> response) {
                dbManager.insertData(catId, response, new TaskRunner.Callback<Boolean>() {
                    @Override
                    public void onComplete(Boolean result) {
                        dbManager.getDataByCategory(catId, isOrderByAsc, callback);
                    }
                });
//                callback.onValidate(arraySortContent(response), new Response.Status<List<DMContent>>() {
//                    @Override
//                    public void onSuccess(List<DMContent> response) {
//                        callback.onSuccess(response);
//                    }
//                });
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

    public void insertVideo(DMVideo videoDetail) {
        insertVideo(videoDetail, null);
    }
    public void insertVideo(DMVideo videoDetail, Response.Status<Boolean> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                dbManager.insertVideo(videoDetail);
                return true;
            }
        }, new TaskRunner.Callback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                if (callback != null) {
                    callback.onSuccess(true);
                }
            }
        });
    }

    public void getMergeVideoDetail(List<DMContent> list, Response.Status<List<DMContent>> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<DMVideo> videos = dbManager.getAllVideos();
                HashMap<String, DMVideo> hashMap = new HashMap<>();
                for (DMVideo item : videos){
                    hashMap.put(item.getVideoId(), item);
                }
                for (DMContent content: list){
                    if (content.getLink() != null) {
                        DMVideo mCon = hashMap.get(content.getLink());
                        if(mCon != null){
                            content.setVideoTime(mCon.getVideoTime());
                            content.setVideoDuration(mCon.getVideoDuration());
                        }
                    }
                }
                return true;
            }
        }, new TaskRunner.Callback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                if (callback != null) {
                    callback.onSuccess(list);
                }
            }
        });
    }
}
