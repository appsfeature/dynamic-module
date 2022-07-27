package com.sample.dynamicmodule.model;

import com.dynamic.model.DMContent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppContent extends DMContent {

    @Expose
    @SerializedName(value="short_description")
    private String shortDescription;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
