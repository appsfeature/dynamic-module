package com.dynamic.database;

import android.content.Context;

import androidx.annotation.WorkerThread;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dynamic.DynamicModule;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;

import java.util.ArrayList;
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
        String query = "SELECT * FROM Flashcards WHERE category = ? OR category = ? ORDER BY RANDOM() LIMIT 1";
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
    public void insertData(List<DMContent> response) {
        if (isDisableCaching) return;
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
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
    public void clearAllContents() {
        database.dmContentDao().clearAllRecords();
    }

    public void getDataByCategory(int catId, Response.Callback<List<DMContent>> callback) {
        if (isDisableCaching) return;
        TaskRunner.getInstance().executeAsync(() -> database.dmContentDao().getDataBySubCategory(catId), result -> {
            if (result != null && result.size() > 0) {
                callback.onSuccess(result);
            }
        });
    }

    public void getDynamicData(int catId, Response.Callback<List<DMCategory>> callback) {
        if (isDisableCaching) return;
        List<DMCategory> list = gson.fromJson(DMPreferences.getDynamicData(context, catId), new TypeToken<List<DMCategory>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            callback.onSuccess(list);
        }
    }

    public void saveDynamicData(int catId, List<DMCategory> response) {
        if (isDisableCaching) return;
        TaskRunner.getInstance().executeAsync(() -> {
            String jsonData = gson.toJson(response, new TypeToken<List<DMCategory>>() {
            }.getType());
            DMPreferences.setDynamicData(context, catId, jsonData);
            return true;
        });
    }
}
