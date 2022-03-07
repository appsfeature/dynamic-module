package com.dynamic.network;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.helper.util.GsonParser;

/**
 * Created by AppsFeature on 3/29/2018.
 */

public class NetworkModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Object data;

    @SerializedName("image_path")
    @Expose
    private String imagePath;

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getData() {
        return GsonParser.getGson().toJson(data);
    }

    /**
     * @param classOfT : ModelName.class
     */
    public <T> T getData(Class<T> classOfT) {
        return GsonParser.getGson().fromJson(getData(), classOfT);
    }

    /**
     * @param typeCast : new TypeToken<ModelName>() {}
     */
    public <T> T getData(TypeToken<T> typeCast) {
        return GsonParser.fromJson(getData(), typeCast);
    }
}
