package com.example.quicknotes.locked_notes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locked_notes_table")
public class LockedNote {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String subtitle;
    private String noteText;
    private String dateTime;
    private String color;
    private String imagePath;
    private String webLink;

    // Constructors
    public LockedNote() {
        // Default constructor
    }

    // Parameterized constructor
    public LockedNote(String title, String subtitle, String noteText, String dateTime,
                      String color, String imagePath, String webLink) {
        this.title = title;
        this.subtitle = subtitle;
        this.noteText = noteText;
        this.dateTime = dateTime;
        this.color = color;
        this.imagePath = imagePath;
        this.webLink = webLink;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
