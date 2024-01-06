package com.example.quicknotes.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.quicknotes.entities.Note;
import com.example.quicknotes.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes order by id DESC")
    List<Note> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote (Note note);

    @Delete
    void deleteNote(Note note);

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :noteId")default
    LiveData<NoteWithFiles> getNoteWithFiles() {
        return getNoteWithFiles(1);
    }

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :noteId")
    LiveData<NoteWithFiles> getNoteWithFiles(int noteId);


}
