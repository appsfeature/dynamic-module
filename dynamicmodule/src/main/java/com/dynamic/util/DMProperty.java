package com.dynamic.util;

import androidx.annotation.NonNull;

import com.dynamic.listeners.DMCategoryType;

import java.io.Serializable;

public class DMProperty implements Serializable, Cloneable {

    private String title;
    private String otherProperty;
    private int catId;
    private int itemType = DMCategoryType.TYPE_LIST;
    private boolean isDisableCaching;

    public String getTitle() {
        return title;
    }

    public DMProperty setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getCatId() {
        return catId;
    }

    public DMProperty setCatId(int catId) {
        this.catId = catId;
        return this;
    }

    public int getItemType() {
        return itemType;
    }

    public DMProperty setItemType(int itemType) {
        this.itemType = itemType;
        return this;
    }

    public boolean isDisableCaching() {
        return isDisableCaching;
    }

    public DMProperty setDisableCaching(boolean disableCaching) {
        isDisableCaching = disableCaching;
        return this;
    }

    public String getOtherProperty() {
        return otherProperty;
    }

    public DMProperty setOtherProperty(String otherProperty) {
        this.otherProperty = otherProperty;
        return this;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public DMProperty getClone() {
        try {
            return (DMProperty) clone();
        } catch (CloneNotSupportedException e) {
            return new DMProperty();
        }
    }
}
