package com.dynamic.database;

import android.content.Context;

import androidx.annotation.WorkerThread;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dynamic.DynamicModule;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.model.DMVideo;
import com.dynamic.util.DMPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DMDatabaseManager {
    private final Context context;
    private final DMDatabase database;
    private boolean isDisableCaching;
    private final Gson gson;

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
    public List<Long> insertCategories(List<DMCategory> list) {
        return database.dmCategoryDao().insertCategories(list);
    }

    @WorkerThread
    public Long insertCategory(DMCategory item) {
        return database.dmCategoryDao().insertCategory(item);
    }

    @WorkerThread
    public List<DMCategory> getAllCategories() {
        return database.dmCategoryDao().getAllData();
    }

    @WorkerThread
    public List<DMCategory> getAllCategories(String queryString, Object[] arguments) {
        return database.dmCategoryDao().getAllData(new SimpleSQLiteQuery(queryString, arguments));
    }

    @WorkerThread
    public List<DMCategory> getAllCategories(List<String> catIds) {
        return database.dmCategoryDao().getAllData(catIds);
    }

    @WorkerThread
    public List<DMCategory> getAllCategories(Map<String, String> whereClause) {
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
    public void insertData(int catId, List<DMContent> response) {
        if (isDisableCaching) return;
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                deleteContentsByCatId(catId);
                insertContents(response);
                return true;
            }
        });
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

    public void getDataByCategory(int catId, DynamicCallback.Listener<List<DMContent>> callback) {
        if (isDisableCaching) return;
        TaskRunner.getInstance().executeAsync(() -> database.dmContentDao().getDataBySubCategory(catId), result -> {
            if (result != null && result.size() > 0) {
                callback.onValidate(arraySortContent(result), new Response.Status<List<DMContent>>() {
                    @Override
                    public void onSuccess(List<DMContent> response) {
                        callback.onSuccess(response);
                    }
                });
            }
        });
    }

    public void getDynamicData(int catId, DynamicCallback.Listener<List<DMCategory>> callback) {
        getDynamicData(catId, null, callback);
    }

    public void getDynamicData(int catId, List<DMCategory> staticList, DynamicCallback.Listener<List<DMCategory>> callback) {
        if (isDisableCaching) return;
        List<DMCategory> list = gson.fromJson(DMPreferences.getDynamicData(context, catId), new TypeToken<List<DMCategory>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            if(staticList != null && staticList.size() > 0){
                list.addAll(staticList);
            }
            callback.onValidate(arraySortCategory(list), new Response.Status<List<DMCategory>>() {
                @Override
                public void onSuccess(List<DMCategory> response) {
                    callback.onSuccess(response);
                }
            });
        }else {
            if(staticList != null && staticList.size() > 0){
                callback.onValidate(arraySortCategory(staticList), new Response.Status<List<DMCategory>>() {
                    @Override
                    public void onSuccess(List<DMCategory> response) {
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

    public void saveDynamicData(int catId, List<DMCategory> response, Response.Status<Boolean> callback) {
        if (isDisableCaching){
            callback.onSuccess(true);
            return;
        }
        TaskRunner.getInstance().executeAsync(() -> {
            String jsonData = gson.toJson(response, new TypeToken<List<DMCategory>>() {
            }.getType());
            DMPreferences.setDynamicData(context, catId, jsonData);
            return true;
        }, new TaskRunner.CallbackWithError<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                callback.onSuccess(true);
            }

            @Override
            public void onError(Exception e) {
                callback.onSuccess(false);
            }
        });
    }

    public List<DMCategory> arraySortCategory(List<DMCategory> list) {
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

    public List<DMContent> arraySortContent(List<DMContent> list) {
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
