package com.example.quicknotes.password;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "passwords")
public class PasswordEntity {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private String password;

    // Constructors, getters, and setters

    public PasswordEntity(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}