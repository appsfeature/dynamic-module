package com.dynamic.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dynamic.model.DMCategory;

import java.util.List;

@Dao
public interface DMCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertCategories(List<DMCategory> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertCategory(DMCategory record);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(DMCategory record);

    @Query("SELECT * FROM dm_category WHERE title IS NOT NULL AND title != '' order by datetime(created_at) DESC")
    List<DMCategory> getAllData();

    @RawQuery
    List<DMCategory> getAllData(SupportSQLiteQuery query);

    @Query("SELECT * FROM dm_category WHERE cat_id IN (:catIds)")
    List<DMCategory> getAllData(List<String> catIds);

    @Query("SELECT * FROM dm_category WHERE cat_id == :catId AND title ==:title")
    DMCategory getItemByCatId(int catId, String title);

//    @Query("UPDATE dm_category SET bookmark_pages =:bookmarkPages, openPagePosition =:openPagePosition, updated_at =:updatedAt, last_update =:updatedAt WHERE id ==:id AND title ==:title")
//    void updateBookmarkPages(int id, String title, String bookmarkPages, int openPagePosition, String updatedAt);
//
//    @Query("UPDATE dm_category SET bookmark_pages =:bookmarkPages, updated_at =:updatedAt, last_update =:updatedAt WHERE id ==:id AND title ==:title")
//    void updateBookmarkPages(int id, String title, String bookmarkPages, String updatedAt);
//
//    @Query("UPDATE dm_category SET openPagePosition =:openPagePosition, viewCount =:viewCount, viewCountFormatted =:viewCountFormatted, last_update =:lastUpdate WHERE id ==:id AND title ==:title")
//    void updateCurrentPosition(int id, String title, int openPagePosition, int viewCount, String viewCountFormatted, String lastUpdate);
//
//    @Query("UPDATE dm_category SET stats_json =:statistics WHERE id ==:id AND title ==:title")
//    void updateStatistics(int id, String title, String statistics);
//
//    @Query("UPDATE dm_category SET filePath =:filePath WHERE autoId ==:autoId")
//    void updateMigrationFilePath(int autoId, String filePath);

    @Query("DELETE FROM dm_category WHERE cat_id ==:catId")
    void delete(int catId);

    @Query("DELETE FROM dm_category WHERE cat_id ==:catId AND title ==:title")
    void delete(int catId, String title);

    @Query("DELETE FROM dm_category")
    void clearAllRecords();

}
