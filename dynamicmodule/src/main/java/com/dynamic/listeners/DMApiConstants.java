package com.dynamic.listeners;

public interface DMApiConstants {

    /*Category Apis list*/
    String INSERT_CATEGORY = "insert-category";//Where  :  pkg_id, cat_name, sub_cat_id
    String INSERT_UPDATE_CATEGORY = "insert-update-category";
    String UPDATE_CATEGORY = "update-category";
    String DELETE_CATEGORY = "delete-category";
    String GET_CATEGORY = "get-category";

    /*Content Apis list*/
    String INSERT_CONTENT = "insert-content";
    String INSERT_UPDATE_CONTENT = "insert-update-content";
    String UPDATE_CONTENT = "update-content";
    String DELETE_CONTENT = "delete-content";
    String GET_CONTENT = "get-content";
    String GET_CONTENT_BY_CATEGORY = "get-content-by-category";
    String GET_CONTENT_BY_SUB_CATEGORY = "get-content-by-sub-category";

    /*Json Data Apis list*/
    String INSERT_DATA = "insert-update-data";
    String UPDATE_DATA = "update-data";
    String DELETE_DATA = "delete-data";
    String GET_DATA = "get-data";
}
