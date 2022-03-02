package com.dynamic.network;

import android.content.Context;
import android.text.TextUtils;

import com.dynamic.DynamicModule;
import com.dynamic.listeners.ApiRequestType;
import com.dynamic.listeners.DMApiConstants;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.util.BaseConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class DMNetworkManager extends BaseNetworkManager {

    private final Context context;
    private final Gson gson;

    public DMNetworkManager(Context context) {
        super(context, DynamicModule.getInstance().getBaseUrl(context));
        this.context = context;
        this.gson = new Gson();
    }

    public void insertCategory(DMCategory category, Response.Callback<NetworkModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("pkg_name", context.getPackageName());
        params.put("sub_cat_id", category.getSubCatId() + "");
        params.put("title", category.getTitle() + "");
        params.put("item_type", category.getItemType() + "");
        params.put("image", category.getImage() + "");
        params.put("ranking", category.getRanking() + "");
        params.put("visibility", category.getVisibility() + "");
        params.put("json_data", category.getJsonData() + "");
        params.put("other_property", category.getOtherProperty() + "");
        getData(ApiRequestType.POST, DMApiConstants.INSERT_CATEGORY, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                if(status && data != null && data.getStatus() != null && data.getStatus().equalsIgnoreCase(BaseConstants.SUCCESS)) {
                    callback.onSuccess(data);
                }else {
                    callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getCategory(int catId, Response.Callback<List<DMCategory>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("cat_id", catId + "");
        params.put("pkg_name", context.getPackageName());
        getData(ApiRequestType.GET, DMApiConstants.GET_CATEGORY, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
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
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getDataBySubCategory(int catId, Response.Callback<List<DMCategory>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("pkg_name", context.getPackageName());
        params.put("cat_id", catId + "");
        getData(ApiRequestType.GET, DMApiConstants.GET_DATA_BY_SUB_CATEGORY, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if(status && !TextUtils.isEmpty(data.getData())) {
                        List<DMCategory> list = gson.fromJson(data.getData(), new TypeToken<List<DMCategory>>() {
                        }.getType());
                        if(!TextUtils.isEmpty(data.getImagePath())){
                            DMPreferences.setImageBaseUrl(context, data.getImagePath());
                        }
                        if (list != null && list.size() > 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    }else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getDataByCategory(int catId, Response.Callback<List<DMContent>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("pkg_name", context.getPackageName());
        params.put("cat_id", catId + "");
        getData(ApiRequestType.GET, DMApiConstants.GET_DATA_BY_CATEGORY, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if(status && !TextUtils.isEmpty(data.getData())) {
                        List<DMContent> list = gson.fromJson(data.getData(), new TypeToken<List<DMContent>>() {
                        }.getType());
                        if (list != null && list.size() > 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    }else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getContent(Integer catId, Response.Callback<List<DMContent>> callback) {
        getContent(null, catId, callback);
    }

    public void getContent(Integer id, Integer parentId, Response.Callback<List<DMContent>> callback) {
        Map<String, String> params = getValidContentParams(id, parentId);
        getData(ApiRequestType.GET, DMApiConstants.GET_CONTENT, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if(status && !TextUtils.isEmpty(data.getData())) {
                        List<DMContent> list = gson.fromJson(data.getData(), new TypeToken<List<DMContent>>() {
                        }.getType());
                        if (list != null && list.size() > 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    }else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private Map<String, String> getValidContentParams(Integer id, Integer parentId) {
        Map<String, String> params = new HashMap<>();
        params.put("pkg_name", context.getPackageName());
        if(id != null && id > 0)
            params.put("id", id + "");
        if(parentId != null && parentId > 0)
            params.put("sub_cat_id", parentId + "");
        return params;
    }

}