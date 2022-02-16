package com.dynamic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class DMOtherProperty {

    @Expose
    @SerializedName(value="grid_count")
    private int gridCount;

    @Expose
    @SerializedName(value="grid_auto_adjust")
    private boolean gridAutoAdjust;

    @Expose
    @SerializedName(value="random_bg_color")
    private boolean randomBGColor;

    @Expose
    @SerializedName(value="random_icon_color")
    private boolean randomIconColor;

    @Expose
    @SerializedName(value="hide_title")
    private boolean hideTitle;

    public int getGridCount() {
        return gridCount;
    }

    public void setGridCount(int gridCount) {
        this.gridCount = gridCount;
    }

    public boolean isGridAutoAdjust() {
        return gridAutoAdjust;
    }

    public void setGridAutoAdjust(boolean gridAutoAdjust) {
        this.gridAutoAdjust = gridAutoAdjust;
    }

    public boolean isRandomBGColor() {
        return randomBGColor;
    }

    public void setRandomBGColor(boolean randomBGColor) {
        this.randomBGColor = randomBGColor;
    }

    public boolean isRandomIconColor() {
        return randomIconColor;
    }

    public void setRandomIconColor(boolean randomIconColor) {
        this.randomIconColor = randomIconColor;
    }

    public boolean isHideTitle() {
        return hideTitle;
    }

    public void setHideTitle(boolean hideTitle) {
        this.hideTitle = hideTitle;
    }
}
