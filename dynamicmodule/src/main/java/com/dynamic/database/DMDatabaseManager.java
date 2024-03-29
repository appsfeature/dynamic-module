package com.dynamic.database;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.WorkerThread;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dynamic.DynamicModule;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DBCategory;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.model.DMVideo;
import com.dynamic.util.DMBaseSorting;
import com.dynamic.util.DMPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.model.common.BaseUtilityAdapter;
import com.helper.task.TaskRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

public class DMDatabaseManager extends DMBaseSorting {
    protected final Context context;
    protected final DMDatabase database;
    protected boolean isDisableCaching;
    protected final Gson gson;

    public DMDatabaseManager(Context context) {
        this.database = DynamicModule.getInstance().getDatabase(context);
        this.context = context;
        this.gson = new Gson();
    }

    public void setDisableCaching(boolean disableCaching) {
        isDisableCaching = disableCaching;
    }

    public DMCategoryDao getCategoryDao() {
        return database.dmCategoryDao();
    }

    public DMContentDao getContentDao() {
        return database.dmContentDao();
    }

    /**
     * @apiNote Methods for handle categories
     */
    @WorkerThread
    public List<Long> insertCategories(List<DBCategory> list) {
        return database.dmCategoryDao().insertCategories(list);
    }

    @WorkerThread
    public Long insertCategory(DBCategory item) {
        return database.dmCategoryDao().insertCategory(item);
    }

    @WorkerThread
    public List<DBCategory> getAllCategories() {
        return database.dmCategoryDao().getAllData();
    }

    @WorkerThread
    public List<DBCategory> getAllCategories(String queryString, Object[] arguments) {
        return database.dmCategoryDao().getAllData(new SimpleSQLiteQuery(queryString, arguments));
    }

    @WorkerThread
    public List<DBCategory> getAllCategories(List<String> catIds) {
        return database.dmCategoryDao().getAllData(catIds);
    }

    @WorkerThread
    public List<DBCategory> getAllCategories(Map<String, String> whereClause) {
//        String query = "SELECT * FROM Flashcards WHERE category = ? OR category = ? ORDER BY RANDOM() LIMIT 1";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM dm_category WHERE ");
        List<Object> args = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, String> entry : whereClause.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" = ? ");
            args.add(entry.getValue());
            if (i < whereClause.size() - 1) {
                sb.append(" AND ");
            }
            i++;
        }
        sb.append("ORDER BY cat_id DESC");
        return database.dmCategoryDao().getAllData(new SimpleSQLiteQuery(sb.toString(), args.toArray()));
    }

    @WorkerThread
    public void deleteCategory(int catId) {
        database.dmCategoryDao().delete(catId);
    }

    @WorkerThread
    public void clearAllCategories() {
        database.dmCategoryDao().clearAllRecords();
    }


    /**
     * @apiNote Methods for handle content
     */
    @WorkerThread
    public List<Long> insertContents(List<DMContent> list) {
        return database.dmContentDao().insertContents(list);
    }

    @WorkerThread
    public Long insertContent(DMContent item) {
        return database.dmContentDao().insertContent(item);
    }

    //Contains both category and content
    public void insertData(int catId, List<DMContent> response, TaskRunner.Callback<Boolean> callback) {
        if (isDisableCaching) return;
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                deleteContentsByCatId(catId);
                insertContents(response);
                return true;
            }
        }, callback);
    }

    @WorkerThread
    public List<DMContent> getAllContents() {
        return database.dmContentDao().getAllData();
    }

    @WorkerThread
    public List<DMContent> getAllContents(List<String> catIds) {
        return database.dmContentDao().getAllData(catIds);
    }


    @WorkerThread
    public List<DMContent> getAllContents(String queryString, Object[] arguments) {
        return database.dmContentDao().getAllData(new SimpleSQLiteQuery(queryString, arguments));
    }

    @WorkerThread
    public void deleteContent(int id) {
        database.dmContentDao().delete(id);
    }


    @WorkerThread
    public void deleteContentsByCatId(int catId) {
        database.dmContentDao().deleteContentsByCatId(catId);
    }

    @WorkerThread
    public void clearAllContents() {
        database.dmContentDao().clearAllRecords();
    }

    public void getDataByCategory(int catId, boolean isOrderByAsc, DynamicCallback.Listener<List<DMContent>> callback) {
        if (isDisableCaching) return;
        TaskRunner.getInstance().executeAsync(new Callable<List<DMContent>>() {
            @Override
            public List<DMContent> call() throws Exception {
                if(isOrderByAsc){
                    return database.dmContentDao().getDataBySubCategoryAsc(catId);
                }else {
                    return database.dmContentDao().getDataBySubCategory(catId);
                }
            }
        }, new TaskRunner.Callback<List<DMContent>>() {
            @Override
            public void onComplete(List<DMContent> result) {
                if (result != null && result.size() > 0) {
                    callback.onValidate(DMDatabaseManager.this.arraySortContent(result), new Response.Status<List<DMContent>>() {
                        @Override
                        public void onSuccess(List<DMContent> response) {
                            callback.onSuccess(response);
                        }
                    });
                }
            }
        });
    }

    public void getDynamicData(int catId, DynamicCallback.Listener<List<DMCategory<DMContent>>> callback) {
        getDynamicData(catId, null, false, callback);
    }

    public void getDynamicData(int catId, List<DMCategory<DMContent>> staticList, boolean isOrderByAsc, DynamicCallback.Listener<List<DMCategory<DMContent>>> callback) {
        if (isDisableCaching) return;
        List<DMCategory<DMContent>> list = gson.fromJson(DMPreferences.getDynamicData(context, catId), new TypeToken<List<DMCategory<DMContent>>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            if(staticList != null && staticList.size() > 0){
                list.addAll(staticList);
            }
            callback.onValidate(arraySortCategory(list, isOrderByAsc), new Response.Status<List<DMCategory<DMContent>>>() {
                @Override
                public void onSuccess(List<DMCategory<DMContent>> response) {
                    callback.onSuccess(response);
                }
            });
        }else {
            if(staticList != null && staticList.size() > 0){
                callback.onValidate(arraySortCategory(staticList, isOrderByAsc), new Response.Status<List<DMCategory<DMContent>>>() {
                    @Override
                    public void onSuccess(List<DMCategory<DMContent>> response) {
                        callback.onSuccess(response);
                    }
                });
            }
        }
    }

    // list = List<Object>
    // Object = String, Integer, Date etc.
//    Collections.sort(list, new Comparator<Object>() {
//        @Override
//        public int compare(Object item, Object item2) {
//            return item2.compareToIgnoreCase(item);
//            return item2.compareTo(item);
//        }
//    });

    public void saveDynamicData(int catId, List<DMCategory<DMContent>> response, boolean isOrderByAsc, Response.Status<List<DMCategory<DMContent>>> callback) {
        if (isDisableCaching){
            callback.onSuccess(response);
            return;
        }
        TaskRunner.getInstance().executeAsync(() -> {
            arraySortCategory(response, isOrderByAsc);
            String jsonData = gson.toJson(response, new TypeToken<List<DMCategory<DMContent>>>() {
            }.getType());
            DMPreferences.setDynamicData(context, catId, jsonData);
            return true;
        }, new TaskRunner.CallbackWithError<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onSuccess(response);
            }
        });
    }

    /**
     * @apiNote Methods for handle videos
     */
    @WorkerThread
    public List<Long> insertVideos(List<DMVideo> list) {
        return database.dmVideoDao().insertVideos(list);
    }

    @WorkerThread
    public Long insertVideo(DMVideo item) {
        return database.dmVideoDao().insertVideo(item);
    }

    @WorkerThread
    public List<DMVideo> getAllVideos() {
        return database.dmVideoDao().getAllData();
    }

    @WorkerThread
    public DMVideo getItemByVideoId(String mVideoId) {
        return database.dmVideoDao().getItemByVideoId(mVideoId);
    }

    @WorkerThread
    public List<DMVideo> getAllVideos(String queryString, Object[] arguments) {
        return database.dmVideoDao().getAllData(new SimpleSQLiteQuery(queryString, arguments));
    }

    @WorkerThread
    public List<DMVideo> getAllVideos(List<String> catIds) {
        return database.dmVideoDao().getAllData(catIds);
    }

    @WorkerThread
    public List<DMVideo> getAllVideos(Map<String, String> whereClause) {
//        String query = "SELECT * FROM Flashcards WHERE Video = ? OR category = ? ORDER BY RANDOM() LIMIT 1";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM dm_category WHERE ");
        List<Object> args = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, String> entry : whereClause.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" = ? ");
            args.add(entry.getValue());
            if (i < whereClause.size() - 1) {
                sb.append(" AND ");
            }
            i++;
        }
        sb.append("ORDER BY cat_id DESC");
        return database.dmVideoDao().getAllData(new SimpleSQLiteQuery(sb.toString(), args.toArray()));
    }

    @WorkerThread
    public void deleteVideo(String videoId) {
        database.dmVideoDao().delete(videoId);
    }

    @WorkerThread
    public void clearAllVideos() {
        database.dmVideoDao().clearAllRecords();
    }
}
