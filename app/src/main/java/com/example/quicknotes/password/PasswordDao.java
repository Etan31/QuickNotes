package com.example.quicknotes.password;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PasswordDao {

    @Query("SELECT * FROM passwords LIMIT 1")
    PasswordEntity getPassword();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void savePassword(PasswordEntity password);

//    @Delete
//    void deletePassword(PasswordEntity passwordEntity);
//    @Query("DELETE FROM passwords WHERE id = 1")
//    void deleteSavedPassword();

    @Query("SELECT * FROM passwords WHERE id = 1 LIMIT 1")
    PasswordEntity getSavedPassword();


    @Query("SELECT * FROM passwords")
    List<PasswordEntity> getAllPasswords();
}