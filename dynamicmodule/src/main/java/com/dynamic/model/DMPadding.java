package com.dynamic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DMPadding implements Serializable {


    @Expose
    @SerializedName(value="left")
    private int left;

    @Expose
    @SerializedName(value="top")
    private int top;

    @Expose
    @SerializedName(value="right")
    private int right;

    @Expose
    @SerializedName(value="bottom")
    private int bottom;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }
}
