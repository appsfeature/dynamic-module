package com.dynamic.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

import java.util.List;

@Dao
public interface DMContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertContents(List<DMContent> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertContent(DMContent record);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(DMContent record);

    @Query("SELECT * FROM dm_content WHERE title IS NOT NULL AND title != '' order by datetime(created_at) DESC")
    List<DMContent> getAllData();

    @RawQuery
    List<DMContent> getAllData(SupportSQLiteQuery query);

    @Query("SELECT * FROM dm_content WHERE id IN (:ids)")
    List<DMContent> getAllData(List<String> ids);

    @Query("SELECT * FROM dm_content WHERE sub_cat_id == :subCatId order by datetime(created_at) DESC")
    List<DMContent> getDataBySubCategory(int subCatId);

    @Query("SELECT * FROM dm_content WHERE id == :id AND title ==:title")
    DMContent getItemById(int id, String title);

//    @Query("UPDATE dm_content SET bookmark_pages =:bookmarkPages, openPagePosition =:openPagePosition, updated_at =:updatedAt, last_update =:updatedAt WHERE id ==:id AND title ==:title")
//    void updateBookmarkPages(int id, String title, String bookmarkPages, int openPagePosition, String updatedAt);
//
//    @Query("UPDATE dm_content SET bookmark_pages =:bookmarkPages, updated_at =:updatedAt, last_update =:updatedAt WHERE id ==:id AND title ==:title")
//    void updateBookmarkPages(int id, String title, String bookmarkPages, String updatedAt);
//
//    @Query("UPDATE dm_content SET openPagePosition =:openPagePosition, viewCount =:viewCount, viewCountFormatted =:viewCountFormatted, last_update =:lastUpdate WHERE id ==:id AND title ==:title")
//    void updateCurrentPosition(int id, String title, int openPagePosition, int viewCount, String viewCountFormatted, String lastUpdate);
//
//    @Query("UPDATE dm_content SET stats_json =:statistics WHERE id ==:id AND title ==:title")
//    void updateStatistics(int id, String title, String statistics);
//
//    @Query("UPDATE dm_content SET filePath =:filePath WHERE autoId ==:autoId")
//    void updateMigrationFilePath(int autoId, String filePath);

    @Query("DELETE FROM dm_content WHERE id ==:id AND title ==:title")
    void delete(int id, String title);

    @Query("DELETE FROM dm_content WHERE id ==:id")
    void delete(int id);

    @Query("DELETE FROM dm_content WHERE sub_cat_id ==:catId")
    void deleteContentsByCatId(int catId);

    @Query("DELETE FROM dm_content")
    void clearAllRecords();

}
