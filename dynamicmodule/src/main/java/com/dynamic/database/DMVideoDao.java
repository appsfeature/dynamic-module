package com.dynamic.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dynamic.model.DMVideo;

import java.util.List;

@Dao
public interface DMVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertVideos(List<DMVideo> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertVideo(DMVideo record);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(DMVideo record);

    @Query("SELECT * FROM dm_video WHERE videoId IS NOT NULL AND videoId != ''")
    List<DMVideo> getAllData();

    @RawQuery
    List<DMVideo> getAllData(SupportSQLiteQuery query);

    @Query("SELECT * FROM dm_video WHERE videoId IN (:videoIds)")
    List<DMVideo> getAllData(List<String> videoIds);

    @Query("SELECT * FROM dm_video WHERE videoId == :videoId")
    DMVideo getItemByVideoId(String videoId);

    @Query("DELETE FROM dm_video WHERE videoId ==:videoId")
    void delete(String videoId);

    @Query("DELETE FROM dm_video")
    void clearAllRecords();

}
