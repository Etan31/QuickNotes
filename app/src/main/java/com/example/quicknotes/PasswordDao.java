package com.example.quicknotes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PasswordDao {

    @Query("SELECT * FROM passwords LIMIT 1")
    PasswordEntity getPassword();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void savePassword(PasswordEntity password);
}