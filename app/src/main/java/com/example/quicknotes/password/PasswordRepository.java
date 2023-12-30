package com.example.quicknotes.password;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.quicknotes.database.AppDatabase;

import java.util.List;

public class PasswordRepository {
    private PasswordDao passwordDao;
    private LiveData<List<PasswordEntity>> allPasswords;

    public PasswordRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        passwordDao = db.passwordDao();
        allPasswords = (LiveData<List<PasswordEntity>>) (LiveData<List<PasswordEntity>>) passwordDao.getAllPasswords();
    }

    public LiveData<List<PasswordEntity>> getAllPasswords() {
        return allPasswords;
    }
}