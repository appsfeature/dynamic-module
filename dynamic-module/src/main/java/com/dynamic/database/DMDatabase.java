package com.dynamic.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

@Database(entities = {DMCategory.class, DMContent.class}, version = 1, exportSchema = false)
public abstract class DMDatabase extends RoomDatabase {
    private static final String DB_NAME = "dynamic-module-db";

    public abstract DMCategoryDao dmCategoryDao();
    public abstract DMContentDao dmContentDao();

    public static DMDatabase create(Context context) {
        return Room.databaseBuilder(context, DMDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }
}