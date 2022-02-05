package com.dynamic.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

//@Entity(tableName = "dm_content", inheritSuperIndices = true)
@Entity(tableName = "dm_content")
public class DMContent implements Cloneable, Serializable {

    @Expose
    @PrimaryKey(autoGenerate = true)
    @SerializedName(value="id")
    @ColumnInfo(name = "id")
    private int id;
    @Expose
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
    @SerializedName(value="description")
    @ColumnInfo(name = "description")
    private String description;
    @Expose
    @SerializedName(value="item_type")
    @ColumnInfo(name = "item_type")
    private int itemType = 0;
    @Expose
    @SerializedName(value="image")
    @ColumnInfo(name = "image")
    private String image;
    @Expose
    @SerializedName(value="link")
    @ColumnInfo(name = "link")
    private String link;
    @Expose
    @SerializedName(value="visibility")
    @ColumnInfo(name = "visibility")
    private int visibility = 1;
    @Expose
    @SerializedName(value="ranking")
    @ColumnInfo(name = "ranking")
    private int ranking = 0;
    @Expose
    @SerializedName(value="json_data")
    @ColumnInfo(name = "json_data")
    private String jsonData;
    @Expose
    @SerializedName(value="other_property", alternate={"expiry_date"})
    @ColumnInfo(name = "other_property")
    private String otherProperty;
    @Expose
    @SerializedName(value="updated_at", alternate={"live_at"})
    @ColumnInfo(name = "updated_at")
    private String updatedAt;
    @Expose
    @SerializedName(value="created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public DMContent getClone() {
        try {
            return (DMContent) clone();
        } catch (CloneNotSupportedException e) {
            return new DMContent();
        }
    }
}
