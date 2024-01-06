package com.example.quicknotes.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "files")
public class FileEntity {
    @PrimaryKey(autoGenerate = true)
    private int fileId;

    @ColumnInfo(name = "file_name")
    private String fileName;

    @ColumnInfo(name = "file_path")
    private String filePath;

    @ColumnInfo(name = "file_type")
    private String fileType;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    // You can add more columns such as file size, timestamp, etc.

    // Getters and setters...

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
