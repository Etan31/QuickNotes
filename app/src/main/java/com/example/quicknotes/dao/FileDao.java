package com.example.quicknotes.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quicknotes.entities.FileEntity;

import java.util.List;

@Dao
public interface FileDao {
    @Insert
    long insertFile(FileEntity fileEntity);

    @Query("SELECT * FROM files WHERE fileId = :fileId")
    FileEntity getFileById(int fileId);

    @Query("SELECT * FROM files")
    List<FileEntity> getAllFiles();

    // You can add more queries as needed, e.g., delete a file, update file information, etc.
}

