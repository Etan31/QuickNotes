package com.example.quicknotes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PasswordDao {

    @Query("SELECT * FROM passwords LIMIT 3")
    PasswordEntity getPassword();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void savePassword(PasswordEntity password);

//    @Delete
//    void deletePassword(PasswordEntity passwordEntity);
//    @Query("DELETE FROM passwords WHERE id = 1")
//    void deleteSavedPassword();

    @Query("SELECT * FROM passwords WHERE id = 1 LIMIT 1")
    PasswordEntity getSavedPassword();

}