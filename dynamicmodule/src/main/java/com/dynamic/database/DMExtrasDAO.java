package com.dynamic.database;

import androidx.room.Dao;

@Dao
public interface DMExtrasDAO {
//    @Insert
//    Long insertHistory(DynamicModel record);
//
//    @Query("SELECT * FROM dynamic_model order by datetime(created_at) DESC")
//    List<HistoryModelResponse> getList();
//
//    @Query("SELECT *, COUNT(*) AS rowCount FROM (SELECT * FROM pdf_history ORDER BY createdAt DESC) AS x GROUP BY id ORDER BY createdAt DESC")
//    List<HistoryModelResponse> getPDFHistoryUnique();
//
//    @Query("DELETE FROM pdf_history WHERE id ==:id")
//    void delete(int id);
//
//    @Query("UPDATE pdf_history SET jsonData =:jsonData WHERE autoId ==:autoId")
//    void updateMigrationJsonData(int autoId, String jsonData);
//
//    @Query("DELETE FROM pdf_history")
//    void clearAllRecords();
//
//    @Query("SELECT COUNT(*) FROM pdf_history")
//    int getPDFHistoryRowCount();
//
//    @Query("SELECT MIN(autoId) FROM (Select autoId from pdf_history order by datetime(createdAt) DESC limit :maxCount)")
//    int getMinRowCountWithLimit(int maxCount);
//
//    @Query("DELETE FROM pdf_history WHERE autoId < :autoId")
//    void clearAllRecordsLessThanAutoId(int autoId);
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    List<Long> insertListRecords(List<PDFModel> list);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Long insertOnlySingleRecord(PDFModel record);
//
//    @Query("SELECT * FROM pdf_viewer WHERE bookmark_pages IS NOT NULL AND bookmark_pages != '' order by datetime(last_update) DESC")
//    List<PDFModel> fetchAllData();
//
//    @Query("SELECT * FROM pdf_viewer WHERE title IS NOT NULL AND title != '' order by datetime(last_update) DESC")
//    List<PDFModel> fetchAllDownloadedData();
//
//    @Query("SELECT * FROM pdf_viewer WHERE id == :id AND title ==:title")
//    PDFModel getPdfById(int id, String title);
//
//    @Query("UPDATE pdf_viewer SET bookmark_pages =:bookmarkPages, openPagePosition =:openPagePosition, updated_at =:updatedAt, last_update =:updatedAt WHERE id ==:id AND title ==:title")
//    void updateBookmarkPages(int id, String title, String bookmarkPages, int openPagePosition, String updatedAt);
//
//    @Query("UPDATE pdf_viewer SET bookmark_pages =:bookmarkPages, updated_at =:updatedAt, last_update =:updatedAt WHERE id ==:id AND title ==:title")
//    void updateBookmarkPages(int id, String title, String bookmarkPages, String updatedAt);
//
//    @Query("UPDATE pdf_viewer SET openPagePosition =:openPagePosition, viewCount =:viewCount, viewCountFormatted =:viewCountFormatted, last_update =:lastUpdate WHERE id ==:id AND title ==:title")
//    void updateCurrentPosition(int id, String title, int openPagePosition, int viewCount, String viewCountFormatted, String lastUpdate);
//
//    @Query("UPDATE pdf_viewer SET stats_json =:statistics WHERE id ==:id AND title ==:title")
//    void updateStatistics(int id, String title, String statistics);
//
//    @Query("UPDATE pdf_viewer SET filePath =:filePath WHERE autoId ==:autoId")
//    void updateMigrationFilePath(int autoId, String filePath);
//
//    @Query("DELETE FROM pdf_viewer WHERE id ==:id AND title ==:title")
//    void delete(int id, String title);
//
//    @Query("DELETE FROM pdf_viewer WHERE id ==:id AND pdf ==:pdfFileName")
//    void deleteByFileName(int id, String pdfFileName);
}
