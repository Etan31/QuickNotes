package com.example.quicknotes;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.quicknotes.database.AppDatabase;

import java.util.Set;

public class PasswordFragment extends Fragment {

    public PasswordFragment() {
        // Required empty public constructor
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.popup_createpassword, container, false);



        // Set the click listener for the Submit button directly within this method
        EditText passwordEditText = view.findViewById(R.id.typepass);
        EditText retypePasswordEditText = view.findViewById(R.id.retypepass);
        Button submitButton = view.findViewById(R.id.SubmitBtn);

        submitButton.setOnClickListener(v -> {
            Log.d("Save", "Submit button clicked");
            Log.d("Save", "Hello from click listener"); // Add this line
            

            String password = passwordEditText.getText().toString();
            String retypePassword = retypePasswordEditText.getText().toString();
            Log.d("Save", "clicked");
            Log.d("Save", password);
            Log.d("Save", retypePassword);

            if (!password.equals(retypePassword)) {
                // Passwords do not match, show an error message
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return; // Return to avoid saving an incorrect password
            }

            // Use AsyncTask to perform the database operation in the background
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    // Perform your database operation here
                    AppDatabase.getInstance(requireContext()).passwordDao().savePassword(new PasswordEntity(password));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    // Optional: Close the fragment or hide the container on the main thread
                    View container = getView().findViewById(R.id.password_popup_container);
                    if (container != null) {
                        container.setVisibility(View.GONE);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        });

        return view;
    }
}
