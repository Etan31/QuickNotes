package com.example.quicknotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Exit extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        View view = inflater.inflate(R.layout.exit_popup, container, false);

        // Find the TextViews by their ids within the inflated view
        TextView yesExitTextView = view.findViewById(R.id.yesExit);
        TextView closeTextView = view.findViewById(R.id.close);

        // Set click listeners for the TextViews
        yesExitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle "Yes" button press
                onYesButtonPressed();
            }
        });

        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle "No" button press
                onCloseButtonPressed();
            }
        });

        return view;
    }

    private void onYesButtonPressed() {
        // Handle "Yes" button press (e.g., exit the application)
        dismiss(); // Close the dialog
        getActivity().finish(); // Finish the activity (exit the application)
    }

    private void onCloseButtonPressed() {
        // Handle "No" button press (e.g., close the dialog)
        dismiss(); // Close the dialog
    }
}
