package com.example.quicknotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.quicknotes.dao.FileDao;
import com.example.quicknotes.entities.FileEntity;
import com.example.quicknotes.password.PasswordDao;
import com.example.quicknotes.password.PasswordEntity;

@Database(entities = {PasswordEntity.class, FileEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PasswordDao passwordDao();
    public abstract FileDao fileDao();  // Add this line

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
