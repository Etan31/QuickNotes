package com.example.quicknotes.locked_notes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LockedNoteDao {
    @Insert
    void insertLockedNote(LockedNote lockedNote);

    @Query("SELECT * FROM locked_notes_table")
    List<LockedNote> getAllLockedNotes();
}