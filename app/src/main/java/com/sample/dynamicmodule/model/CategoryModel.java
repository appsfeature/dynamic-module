package com.sample.dynamicmodule.model;

import com.dynamic.model.DMCategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel extends DMCategory<ContentModel> {

    @Expose
    @SerializedName(value="id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
