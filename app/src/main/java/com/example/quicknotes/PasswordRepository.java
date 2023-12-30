package com.example.quicknotes;

import android.util.Log;

import java.util.List;

public class PasswordRepository {
    private PasswordDao passwordDao;
    public PasswordRepository(PasswordDao passwordDao) {
        this.passwordDao = passwordDao;
    }

    public PasswordEntity getSavedPassword() {
        return passwordDao.getSavedPassword();
    }

    public void checkPassword(String enteredPassword) {
        List<PasswordEntity> allPasswords = passwordDao.getAllPasswords();
        boolean passwordMatches = false;

        for (PasswordEntity savedPassword : allPasswords) {
            if (enteredPassword.equals(savedPassword.getPassword())) {
                // Password matches
                passwordMatches = true;
                break;
            }
        }

        if (passwordMatches) {
            // Log "Password matches"
            Log.d("Password Check", "Password matches");
        } else {
            // Log "No password matches"
            Log.d("Password Check", "No password matches");
        }
    }
}
