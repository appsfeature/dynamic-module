# dynamic-module

#### Library size is : Kb
  
## Setup Project

Add this to your project build.gradle
``` gradle
allprojects {
    repositories {
        maven {
            url "https://jitpack.io"
        }
    }
}
```

Add this to your project build.gradle

#### Dependency
[![](https://jitpack.io/v/appsfeature/dynamic-module.svg)](https://jitpack.io/#appsfeature/dynamic-module)
```gradle
dependencies {
        implementation 'com.github.appsfeature:dynamic-module:1.0'
}
```


### Statistics Usage methods

### Application class setup
```java
public class AppApplication extends BaseApplication {

    private static final String BASE_URL = "http://appsfeature.com/droidapps/api/v1/database/";
    private static final String BASE_IMAGE_URL = "http://appsfeature.com/droidapps/public/uploads/images/";
    private static AppApplication instance;

    public static AppApplication getInstance() {
        return instance;
    }

    @Override
    public boolean isDebugMode() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DynamicModule.getInstance()
                .setDebugMode(isDebugMode())
                .setBaseUrl(getInstance(), BASE_URL)
                .setImageBaseUrl(getInstance(), BASE_IMAGE_URL)
                .addListClickListener(instance.hashCode(), new DynamicCallback.OnDynamicListListener() {
                    @Override
                    public void onItemClicked(Activity activity, View view, DMProperty parent, DMContent item) {
                        if (item.getItemType() == DMContentType.TYPE_LINK) {
                            if (BaseUtil.isValidUrl(item.getLink())) {
                                BaseUtil.showToast(view.getContext(), "Update Later!");
                            } else {
                                BaseUtil.showToast(view.getContext(), "Invalid Link!");
                            }
                        } else {
                            BaseUtil.showToast(view.getContext(), "Action Update Later");
                        }
                    }
                });
    }

}
```

### DynamicActivity class for showing dynamic list or slider(ViewPager)
```java
public class DynamicActivity extends AppCompatActivity implements DynamicCallback.OnDynamicPagerListener
        , DynamicCallback.OnDynamicListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        fragmentMapping(DynamicPagerFragment.getInstance(), R.id.content);
        fragmentMapping(DynamicListFragment.getInstance(), R.id.content2);
    }

    private void fragmentMapping(Fragment fragment, int layoutId) {
        getSupportFragmentManager().beginTransaction().replace(layoutId, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(View view, DMContent item) {

    }
}
```

### MainActivity class setup
```java
public class MainActivity extends AppCompatActivity {

    private DMNetworkManager networkManager;
    private DMDatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkManager = new DMNetworkManager(this);
        databaseManager = new DMDatabaseManager(this);
    }

    public void onGetCategory(View view) {
        networkManager.getDynamicCategory(new Response.Callback<List<DMCategory>>() {
            @Override
            public void onSuccess(List<DMCategory> response) {

            }

            @Override
            public void onFailure(Exception e) {
                BaseUtil.showToast(MainActivity.this, e.getMessage());
            }
        });
    }

    public void onGetContent(View view) {
        networkManager.getDynamicContent(new Response.Callback<List<DMContent>>() {
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
}
```
