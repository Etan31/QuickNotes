package com.example.quicknotes.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "files", foreignKeys = @ForeignKey(
        entity = Note.class,
        parentColumns = "id",
        childColumns = "noteId",
        onDelete = ForeignKey.CASCADE
), indices = {@Index("noteId")})
public class FileEntity implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int fileId;

    @ColumnInfo(name = "noteId")
    private int noteId;

    @ColumnInfo(name = "file_path")
    private String filePath;

    @ColumnInfo(name = "file_type")
    private String fileType;

    @ColumnInfo(name = "file_name")
    private String fileName;


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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

}
