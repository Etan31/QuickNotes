package com.example.quicknotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.quicknotes.locked_notes.LockedNote;
import com.example.quicknotes.locked_notes.LockedNoteDao;

@Database(entities = {LockedNote.class}, version = 1)
public abstract class LockedNotesDatabase extends RoomDatabase {
    public abstract LockedNoteDao lockedNoteDao();

    private static LockedNotesDatabase instance;

    public static synchronized LockedNotesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            LockedNotesDatabase.class, "locked_notes_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
