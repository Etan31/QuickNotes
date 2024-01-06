package com.example.quicknotes.dao;

import androidx.room.Embedded;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import com.example.quicknotes.entities.FileEntity;
import com.example.quicknotes.entities.Note;

import java.util.List;

public class NoteWithFiles {

    @Embedded
    public Note note;

    @Relation(
            parentColumn = "id",   // Assuming the primary key of Note is named "id"
            entityColumn = "noteId"
    )
    public List<FileEntity> files;
}