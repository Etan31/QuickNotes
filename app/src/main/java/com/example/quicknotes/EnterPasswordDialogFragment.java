package com.example.quicknotes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quicknotes.password.PasswordEntity;

public class EnterPasswordDialogFragment extends DialogFragment {
    private static final String TAG = "EnterPasswordDialogFrag";
    private PasswordEntity savedPassword;

    // Constructor to pass the savedPassword value
    public EnterPasswordDialogFragment(PasswordEntity savedPassword) {
        this.savedPassword = savedPassword;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View enterPasswordView = inflater.inflate(R.layout.popup_lock, container, false);

        EditText passwordEditText = enterPasswordView.findViewById(R.id.retypepass);
        Button submitButton = enterPasswordView.findViewById(R.id.SubmitBtn);

        Log.d(TAG, "Dialog Fragment created");

        Button backBtn = enterPasswordView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Log.d("closed", "closed");
            if (getDialog() != null) {
                getDialog().dismiss();
            }
        });


//        Button backBtn = enterPasswordView.findViewById(R.id.backBtn);
//        backBtn.setOnClickListener(v -> {
//            Log.d("closed", "closed");
//            getView().findViewById(R.id.password_popup_container).setVisibility(View.GONE);
//
//            // Check the visibility of the container
////            if (getView() != null) {
////                boolean isContainerVisible = getView().findViewById(R.id.password_popup_container).getVisibility() == View.VISIBLE;
////                Log.d("closed", "Container is visible: " + isContainerVisible);
////
////                // Handle close button click
////                getView().findViewById(R.id.password_popup_container).setVisibility(View.GONE);
////            }
//        });

        submitButton.setOnClickListener(v -> {
            Log.d(TAG, "Submit button clicked");

            String enteredPassword = passwordEditText.getText().toString();
            Log.d(TAG, "Entered Password: " + enteredPassword);
            Log.d(TAG, "Saved Password: " + savedPassword.getPassword());


            if (enteredPassword.equals(savedPassword.getPassword()) || (enteredPassword.isEmpty() && savedPassword.getPassword() == null)) {
                // Password is correct, handle accordingly
                Log.d(TAG, "Correct Password");
                // For example, navigate to the main activity
                Toast.makeText(requireContext(), "Correct Password", Toast.LENGTH_SHORT).show();
            } else {
                // Password is incorrect, show an error message
                Log.d(TAG, "Incorrect Password");
                Toast.makeText(requireContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
            }

        });

        return enterPasswordView;
    }
}