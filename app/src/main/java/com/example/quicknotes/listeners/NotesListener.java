package com.example.quicknotes.listeners;

import com.example.quicknotes.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
