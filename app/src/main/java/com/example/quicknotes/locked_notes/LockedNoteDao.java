package com.example.quicknotes.locked_notes;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface LockedNoteDao {
    @Insert
    void insertLockedNote(LockedNote lockedNote);

    // Add other necessary methods for locked notes
}