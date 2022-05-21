package com.sample.dynamicmodule;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dynamic.database.DMDatabaseConst;
import com.dynamic.database.DMDatabaseManager;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.network.DMNetworkManager;
import com.dynamic.DMClassUtil;
import com.dynamic.util.DMDataManager;
import com.dynamic.util.DMUtility;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BaseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

//    private static final int DASHBOARD_ID = 125; // home id
    private static final int DASHBOARD_ID = 165; // demo1 id
    private DMDataManager dataManager;
    private DMDatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = DMDataManager.get(this);
        databaseManager = new DMDatabaseManager(this);
    }

    public void onGetCategory(View view) {
        new DMNetworkManager(this).getCategory(0, new DynamicCallback.Listener<List<DMCategory>>() {
            @Override
            public void onSuccess(List<DMCategory> response) {
//                TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
//                    @Override
//                    public Boolean call() throws Exception {
//                        databaseManager.insertCategories(response);
//                        return true;
//                    }
//                });
            }

            @Override
            public void onFailure(Exception e) {
                BaseUtil.showToast(MainActivity.this, e.getMessage());
            }
        });
    }

    public void onGetContent(View view) {
        dataManager.getContent(0, new DynamicCallback.Listener<List<DMContent>>() {
            @Override
            public void onSuccess(List<DMContent> response) {

            }

            @Override
            public void onFailure(Exception e) {
                BaseUtil.showToast(MainActivity.this, e.getMessage());
            }
        });
    }

    public void onDBGet(View view) {
        TaskRunner.getInstance().executeAsync(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Map<String, String> map = new HashMap<>();
                map.put(DMDatabaseConst.SUB_CAT_ID, "0");
                map.put(DMDatabaseConst.VISIBILITY, "0");
                List<DMCategory> mCategories = databaseManager.getAllCategories(map);
                return mCategories.size();
            }
        }, new TaskRunner.CallbackWithError<Integer>() {
            @Override
            public void onComplete(Integer result) {
                BaseUtil.showToast(MainActivity.this, "Success : Size = " + result);
            }

            @Override
            public void onError(Exception e) {
                BaseUtil.showToast(MainActivity.this, e.getMessage());
            }
        });
    }

    public void onDBInsert(View view) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                DMCategory item = new DMCategory();
                item.setTitle("Category " + getRandom());
                databaseManager.insertCategory(item);
                return true;
            }
        }, new TaskRunner.Callback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                BaseUtil.showToast(MainActivity.this, "Success");
            }
        });
    }

    private int getRandom() {
        return new Random().nextInt(5000);
    }

    public void onDBUpdate(View view) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                DMCategory item = new DMCategory();
                item.setCatId(10);
                item.setTitle("Update Category");
                databaseManager.insertCategory(item);
                return true;
            }
        }, new TaskRunner.Callback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                BaseUtil.showToast(MainActivity.this, "Success");
            }
        });
    }

    public void onDBDelete(View view) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int catId = 0;
                databaseManager.deleteCategory(catId);
                return true;
            }
        }, new TaskRunner.Callback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                BaseUtil.showToast(MainActivity.this, "Success");
            }
        });
    }

    public void onOpenDMActivity(View view) {
//        startActivity(new Intent(this, AppDynamicActivity.class));
        DMClassUtil.openDynamicActivity(this, DMUtility.getProperty(DASHBOARD_ID, false));
    }
}