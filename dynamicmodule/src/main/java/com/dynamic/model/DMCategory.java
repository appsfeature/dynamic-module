package com.dynamic.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helper.util.GsonParser;

import java.io.Serializable;
import java.util.List;

//@Entity(tableName = "dm_category", inheritSuperIndices = true)
@Entity(tableName = "dm_category")
public class DMCategory implements Cloneable, Serializable {

    @Expose
    @PrimaryKey(autoGenerate = true)
    @SerializedName(value="cat_id")
    @ColumnInfo(name = "cat_id")
    private int catId;
    @Expose
    @SerializedName(value="sub_cat_id")
    @ColumnInfo(name = "sub_cat_id")
    private int subCatId;
    @Expose
    @SerializedName(value="pkg_id")
    @ColumnInfo(name = "pkg_id")
    private String pkgId;
    @Expose
    @SerializedName(value="title")
    @ColumnInfo(name = "title")
    private String title;
    @Expose
    @SerializedName(value="item_type")
    @ColumnInfo(name = "item_type")
    private int itemType;
    @Expose
    @SerializedName(value="image")
    @ColumnInfo(name = "image")
    private String image;
    @Expose
    @SerializedName(value="ranking")
    @ColumnInfo(name = "ranking")
    private int ranking;
    @Expose
    @SerializedName(value="visibility")
    @ColumnInfo(name = "visibility")
    private int visibility = 1;
    @Expose
    @SerializedName(value="json_data")
    @ColumnInfo(name = "json_data")
    private String jsonData;
    @Expose
    @SerializedName(value="other_property")
    @ColumnInfo(name = "other_property")
    private String otherProperty;
    @Expose
    @SerializedName(value="updated_at")
    @ColumnInfo(name = "updated_at")
    private String updatedAt;
    @Expose
    @SerializedName(value="created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;
    @Expose
    @Ignore
    @SerializedName(value="categories")
    private List<DMCategory> childCategoryList;
    @Expose
    @Ignore
    @SerializedName(value="contents", alternate={"data"})
    private List<DMContent> childList;
    @Ignore
    private int imageRes;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getOtherProperty() {
        return otherProperty;
    }

    public void setOtherProperty(String otherProperty) {
        this.otherProperty = otherProperty;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public List<DMCategory> getChildCategoryList() {
        return childCategoryList;
    }

    public void setChildCategoryList(List<DMCategory> childCategoryList) {
        this.childCategoryList = childCategoryList;
    }

    public List<DMContent> getChildList() {
        return childList;
    }

    public void setChildList(List<DMContent> childList) {
        this.childList = childList;
    }

    public DMOtherProperty getOtherPropertyModel() {
        return fromJson(DMOtherProperty.class);
    }

    public <T> T fromJson(Class<T> classOfT) {
        return GsonParser.getGson().fromJson(otherProperty, classOfT);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public DMCategory getClone() {
        try {
            return (DMCategory) clone();
        } catch (CloneNotSupportedException e) {
            return new DMCategory();
        }
    }
}
