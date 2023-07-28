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
@Entity(tableName = "dm_video")
public class DMVideo implements Cloneable, Serializable {

//    @Expose
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName(value="id")
//    @ColumnInfo(name = "id")
//    private int id;
    @Expose
    @PrimaryKey
    @SerializedName(value="videoId")
    @ColumnInfo(name = "videoId")
    @NonNull
    private String videoId;
    @Expose
    @SerializedName(value="catId")
    @ColumnInfo(name = "catId")
    private int catId;
    @Expose
    @SerializedName(value="videoTime")
    @ColumnInfo(name = "videoTime")
    private int videoTime = 0;
    @Expose
    @SerializedName(value="videoDuration")
    @ColumnInfo(name = "videoDuration")
    private int videoDuration = 0;
    @Expose
    @SerializedName(value="isRead")
    @ColumnInfo(name = "isRead")
    private int isRead = 0;
    @Expose
    @SerializedName(value="isFav")
    @ColumnInfo(name = "isFav")
    private int isFav = 0;
    @Expose
    @SerializedName(value="json_data")
    @ColumnInfo(name = "json_data")
    private String jsonData;
    @Expose
    @SerializedName(value="channelId")
    @ColumnInfo(name = "channelId")
    private String channelId;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public int getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(int videoDuration) {
        this.videoDuration = videoDuration;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public DMVideo getClone() {
        try {
            return (DMVideo) clone();
        } catch (CloneNotSupportedException e) {
            return new DMVideo();
        }
    }
}
