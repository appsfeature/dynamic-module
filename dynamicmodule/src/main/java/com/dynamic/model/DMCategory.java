package com.dynamic.model;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DMCategory<T> extends DBCategory implements Cloneable, Serializable {

    @Expose
    @Ignore
    @SerializedName(value="categories")
    private List<DMCategory<T>> childCategoryList;
    @Expose
    @Ignore
    @SerializedName(value="contents", alternate={"data", "subcategories"})
    private List<T> childList;


    public List<DMCategory<T>> getChildCategoryList() {
        return childCategoryList;
    }

    public void setChildCategoryList(List<DMCategory<T>> childCategoryList) {
        this.childCategoryList = childCategoryList;
    }

    public List<T> getChildList() {
        return childList;
    }

    public void setChildList(List<T> childList) {
        this.childList = childList;
    }



    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public DMCategory<T> getClone() {
        try {
            return (DMCategory<T>) clone();
        } catch (CloneNotSupportedException e) {
            return new DMCategory<T>();
        }
    }
}
