package com.example.quicknotes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.example.quicknotes.database.AppDatabase;
import com.example.quicknotes.password.PasswordDao;
import com.example.quicknotes.password.PasswordEntity;

import java.util.ArrayList;
import java.util.List;

public class EnterPasswordDialogFragment extends DialogFragment {
    private static final String TAG = "EnterPasswordDialogFrag";
    private List<PasswordEntity> passwords = new ArrayList<>();


    // Constructor to pass the savedPassword value
    public EnterPasswordDialogFragment(PasswordEntity savedPassword) {
        // Do something with savedPassword if needed
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View enterPasswordView = inflater.inflate(R.layout.popup_lock, container, false);

        EditText passwordEditText = enterPasswordView.findViewById(R.id.retypepass);
        Button submitButton = enterPasswordView.findViewById(R.id.SubmitBtn);


        Button backBtn = enterPasswordView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Log.d("closed", "closed");
            if (getDialog() != null) {
                getDialog().dismiss();
            }
        });


        // Assuming this is in your activity or fragment
        AppDatabase appDatabase = Room.databaseBuilder(requireContext(), AppDatabase.class, "app_database").build();
        PasswordDao passwordDao = appDatabase.passwordDao();



        submitButton.setOnClickListener(v -> {
            Log.d("password", "clicked submit");

            String enteredPassword = passwordEditText.getText().toString();
            Log.d("password", "Entered Password: " + enteredPassword);

            // Launch a coroutine in the background
            new Thread(() -> {
                List<PasswordEntity> savedPasswords = passwordDao.getAllPasswords();

                // Use the main thread dispatcher to update the UI with the result
                new Handler(Looper.getMainLooper()).post(() -> {
                    for (PasswordEntity savedPassword : savedPasswords) {
                        if (enteredPassword.equals(savedPassword.getPassword())) {
                            Log.d("password", "Password matched!");
                            // Do whatever you need to do when the password matches
                            break;
                        }
                    }
                });
            }).start();
        });






        return enterPasswordView;
    }
}