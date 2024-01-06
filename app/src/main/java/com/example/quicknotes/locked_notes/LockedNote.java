package com.example.quicknotes.locked_notes;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locked_notes_table")
public class LockedNote implements Parcelable {

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


//    data to be sent and can be readable accoss multiple files
    protected LockedNote(Parcel in) {
        id = in.readInt();
        title = in.readString();
        subtitle = in.readString();
        noteText = in.readString();
        dateTime = in.readString();
        color = in.readString();
        imagePath = in.readString();
        webLink = in.readString();
    }

    public static final Parcelable.Creator<LockedNote> CREATOR = new Parcelable.Creator<LockedNote>() {
        @Override
        public LockedNote createFromParcel(Parcel in) {
            return new LockedNote(in);
        }

        @Override
        public LockedNote[] newArray(int size) {
            return new LockedNote[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(noteText);
        dest.writeString(dateTime);
        dest.writeString(color);
        dest.writeString(imagePath);
        dest.writeString(webLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
