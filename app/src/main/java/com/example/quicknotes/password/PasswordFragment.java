package com.example.quicknotes.password;

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

import androidx.fragment.app.Fragment;

import com.example.quicknotes.R;
import com.example.quicknotes.database.AppDatabase;

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

//          showCreatePasswordUI();
        // for creating new passowrd.

        // Set the click listener for the Submit button directly within this method
        EditText passwordEditText = view.findViewById(R.id.typepass);
        EditText retypePasswordEditText = view.findViewById(R.id.retypepass);
        Button submitButton = view.findViewById(R.id.SubmitBtn);


        Button backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Log.d("closed", "closed");
            // Dismiss the fragment or hide the container
            if (getActivity() != null) {
                View popupContainer = getActivity().findViewById(R.id.password_popup_container);
                if (popupContainer != null) {
                    popupContainer.setVisibility(View.GONE);
                }
            }
        });

        submitButton.setOnClickListener(v -> {
            Log.d("Save", "Submit button clicked");

            String password = passwordEditText.getText().toString();
            String retypePassword = retypePasswordEditText.getText().toString();

            if (!password.equals(retypePassword)) {
                // Passwords do not match, show an error message
                Toast.makeText(requireContext(), "Passwords do not match, please try again.", Toast.LENGTH_SHORT).show();
                return;
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
                    View container = getActivity().findViewById(R.id.password_popup_container);
                    if (container != null) {
                        container.setVisibility(View.GONE);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        });

        return view;
    }


}
