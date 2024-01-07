package com.example.quicknotes;

import static android.content.Intent.getIntent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.quicknotes.locked_notes.LockedNote;
import com.example.quicknotes.password.PasswordEntity;
import com.example.quicknotes.viewmodel.PasswordViewModel;

public class OpenedLockedNoteActivity extends AppCompatActivity{

    //TODO: FIND WHERE IS THE ON CLICK
    // then add a method for the onclick showEnterPasswordUI()
    private TextView inputNoteTitle;
    private TextView inputNoteSubtitle;
    private TextView inputNote;
    private TextView textDateTime;
    private ImageView imageNote;
    private ImageView imageRemoveImage;
    private LinearLayout layoutWebURL;
    private TextView textWebURL;
    private ImageView imageRemoveWebURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opened_lockednote);

        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle back button press
                onBackButtonPressed();
            }
        });

        // Initialize your views
        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNote = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);
        imageNote = findViewById(R.id.imageNote);
        imageRemoveImage = findViewById(R.id.imageRemoveImage);
        layoutWebURL = findViewById(R.id.layoutWebURL);
        textWebURL = findViewById(R.id.textWebURL);
        imageRemoveWebURL = findViewById(R.id.imageRemoveWebURL);

//This is the on click method before opening the locked notes

//        -------------------------DO NOT DELETE----------------------------------------------
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("lockedNote")) {
//            LockedNote lockedNote = intent.getParcelableExtra("lockedNote");
//            displayLockedNoteDetails(lockedNote);
//
//        }



        PasswordViewModel passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
        passwordViewModel.getPasswordLiveData().observe(this, savedPassword -> {
            Log.d("Click", "openLockeNote:81");
            if (savedPassword != null) {
                showEnterPasswordUI(savedPassword); // Use the method to show password UI
                Toast.makeText(this, "This password", Toast.LENGTH_SHORT).show();
                Log.d("Click", "openLockeNote:85");

//                    displayLockedNoteDetails(lockedNote);
                Intent intent = getIntent();
                if (intent != null && intent.hasExtra("lockedNote")) {
                    Log.d("Click", "openLockeNote:80");

                    // Retrieve the LockedNote object from the Intent
                    LockedNote lockedNote = intent.getParcelableExtra("lockedNote");
                    displayLockedNoteDetails(lockedNote);
                    Toast.makeText(this, "Intent password", Toast.LENGTH_SHORT).show();

                }else{
                    Log.d("Click", "openLockeNote98");
                    Toast.makeText(this, "Else password", Toast.LENGTH_SHORT).show();
                }

            } else {
                // Display locked note details if no password is set
                Toast.makeText(this, "No password", Toast.LENGTH_SHORT).show();
            }
        });

//        PasswordViewModel passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
//        passwordViewModel.getPasswordLiveData().observe(getViewLifecycleOwner(), savedPassword -> {
//            if (savedPassword != null) {
//
//                changeResetPasswordButton.setOnClickListener(v -> {
//                    showEnterPasswordUI(savedPassword);
//                });
//
//            } else {
//
//                changeResetPasswordButton.setOnClickListener(v -> {
//                    showCreatePasswordUI();
//                });
//
//            }
//        });


    }

    private void showEnterPasswordUI(PasswordEntity savedPassword) {
        Toast.makeText(this, "ShowEnterPasswordUI", Toast.LENGTH_SHORT).show();
        EnterPasswordDialogFragment2 dialogFragment = new EnterPasswordDialogFragment2(savedPassword);
        dialogFragment.show(getSupportFragmentManager(), "EnterPasswordDialogFragment2");

        // Set the result as OK
        setResult(Activity.RESULT_OK);
    }




    private void displayLockedNoteDetails(LockedNote lockedNote) {
        Log.d("Click", "displayLockedNoteDetails");

        // Set the text of your EditText views with the details of the locked note
        inputNoteTitle.setText(lockedNote.getTitle());
        inputNoteSubtitle.setText(lockedNote.getSubtitle());
        inputNote.setText(lockedNote.getNoteText());
        textDateTime.setText(lockedNote.getDateTime());

        // Check for image and display if available
        if (lockedNote.getImagePath() != null && !lockedNote.getImagePath().trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(lockedNote.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
        } else {
            imageNote.setVisibility(View.GONE);
        }


        // Check for web link and display if available
        if (lockedNote.getWebLink() != null && !lockedNote.getWebLink().trim().isEmpty()) {
            layoutWebURL.setVisibility(View.VISIBLE);
            textWebURL.setText(lockedNote.getWebLink());
            imageRemoveWebURL.setVisibility(View.VISIBLE);
        } else {
            layoutWebURL.setVisibility(View.GONE);
            imageRemoveWebURL.setVisibility(View.GONE);
        }
    }

    private void onBackButtonPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }




}
