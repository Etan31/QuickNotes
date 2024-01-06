package com.example.quicknotes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.quicknotes.database.AppDatabase;
import com.example.quicknotes.database.LockedNotesDatabase;
import com.example.quicknotes.database.NotesDatabase;
import com.example.quicknotes.entities.FileEntity;
import com.example.quicknotes.entities.Note;
import com.example.quicknotes.locked_notes.LockedNote;
import com.example.quicknotes.password.PasswordFragment;
import com.example.quicknotes.viewmodel.PasswordViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.example.quicknotes.locked_notes.LockedNote;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class createNote extends AppCompatActivity {
//    TODO: add the notes id or note name on the pdf file where the pdf is being stored.

    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;
    private String selectedNoteColor;
    private String selectedImagePath;
    private ImageView imageNote;
    private TextView textWebURL;
    private LinearLayout layoutWebURL;
    private Window view;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private AlertDialog dialogAddURL;
    private AlertDialog dialogDeleteNote;
    private Note alreadyAvailableNote;
    private String selectedPdfFilePath;
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);

        appDatabase = AppDatabase.getInstance(this);

        // Set up the back button click listener
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle back button press
                onBackButtonPressed();
            }
        });


        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        imageNote = findViewById(R.id.imageNote);
        textWebURL = findViewById(R.id.textWebURL);
        layoutWebURL = findViewById(R.id.layoutWebURL);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );


        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote(false);
            }
        });


        selectedNoteColor = "#333333";
        selectedImagePath = "";
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }
        findViewById(R.id.imageRemoveWebURL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textWebURL.setText(null);
                layoutWebURL.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.imageRemoveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });

        initMiscellaneous();
        setSubtitleIndicatorColor();
    }

    private void setViewOrUpdateNote(){
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(alreadyAvailableNote.getDateTime());



        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
            selectedImagePath = alreadyAvailableNote.getImagePath();
        }
        if (alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()) {
            textWebURL.setText(alreadyAvailableNote.getWebLink());
            layoutWebURL.setVisibility(View.VISIBLE);
        }


    }
    private void saveNote(boolean isLockedNote) {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNoteSubtitle.getText().toString().trim().isEmpty()
                && inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show();
            return;

        }

        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubtitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setColor(selectedNoteColor);
        note.setImagePath(selectedImagePath);

        if (layoutWebURL.getVisibility() == View.VISIBLE){
            note.setWebLink(textWebURL.getText().toString());
        }

        if (alreadyAvailableNote !=null){
            note.setId(alreadyAvailableNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            private final Note note;
            private final boolean isLockedNote;

            public SaveNoteTask(Note note, boolean isLockedNote) {
                this.note = note;
                this.isLockedNote = isLockedNote;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (isLockedNote) {
                    // Create a LockedNote object from the existing Note
                        LockedNote lockedNote = new LockedNote();
                        lockedNote.setTitle(note.getTitle());
                        lockedNote.setSubtitle(note.getSubtitle());
                        lockedNote.setNoteText(note.getNoteText());
                        lockedNote.setDateTime(note.getDateTime());
                        lockedNote.setColor(note.getColor());
                        lockedNote.setImagePath(note.getImagePath());
                        lockedNote.setWebLink(note.getWebLink());

                    // Insert the LockedNote into the locked notes database
                    LockedNotesDatabase.getInstance(getApplicationContext()).lockedNoteDao().insertLockedNote(lockedNote);
                } else {
                    // Insert the regular Note into the notes database
                    NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }


//        new SaveNoteTask().execute();
        new SaveNoteTask(note, isLockedNote).execute();

    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState((BottomSheetBehavior.STATE_EXPANDED));
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        //onclick listener for the locked notes
        final Button lockNotesBtn = layoutMiscellaneous.findViewById(R.id.lockNotesBtn);
        final Button attachFileBtn = layoutMiscellaneous.findViewById(R.id.attach_file);

        PasswordViewModel passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
        passwordViewModel.getPasswordLiveData().observe(this, savedPassword -> {
            if (savedPassword != null) {
                lockNotesBtn.setOnClickListener(v -> {
                    Toast.makeText(createNote.this, "Locked", Toast.LENGTH_SHORT).show();
                    saveNote(true);
                });
            } else {
                lockNotesBtn.setOnClickListener(v -> {
                     showCreatePasswordUI();
//                    initMiscellaneous();
                    Toast.makeText(createNote.this, "Not Locked", Toast.LENGTH_SHORT).show();
                });
            }
        });
        passwordViewModel.loadPassword(this);


        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#CCCE65";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setSubtitleIndicatorColor();
            }
        });



        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvailableNote.getColor()) {

            case "#FDBE3B":
                layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                break;
            case "#FF4842":
                layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                break;
            case "#3A52FC":
                layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                break;
            case "#000000":
                layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                break;
        }
        }

        attachFileBtn.setOnClickListener(v -> {
            String pdfFilePath = pickPdfFile();

            // Check if a valid PDF file path is obtained
            if (pdfFilePath != null && !pdfFilePath.isEmpty()) {
                insertPdfFile(pdfFilePath);
            } else {
                // Handle the case where a PDF file was not selected
                Toast.makeText(this, "Please select a PDF file", Toast.LENGTH_SHORT).show();
            }
        });





        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            createNote.this,
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddURLDialog();
            }
        });


        if (alreadyAvailableNote != null) {
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteNoteDialog();

                }


            });
        }
    }

    // Example insertion method in your activity or repository
    private static final int PICK_PDF_REQUEST_CODE = 1;

    private String pickPdfFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        startActivityForResult(intent, PICK_PDF_REQUEST_CODE);

        return selectedPdfFilePath;
    }



    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            // Handle exceptions, if any
            e.printStackTrace();
        }

        if (filePath == null) {
            // If the file path is still null, try an alternative approach for API 19 and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                    if (parcelFileDescriptor != null) {
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        FileInputStream fileInputStream = new FileInputStream(fileDescriptor);
                        FileDescriptor fd = fileInputStream.getFD();
                        filePath = fd.toString();
                        fileInputStream.close();
                        parcelFileDescriptor.close();

                    }
                } catch (IOException e) {
                    // Handle exceptions, if any
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }



//  to insert new pdf file
    private void insertPdfFile(String pdfFilePath) {
        // Check if the selected file is a PDF
        if (!isPdfFile(pdfFilePath)) {
            // Handle the case where the selected file is not a PDF
            Toast.makeText(this, "Only PDF files are allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a FileEntity object and set its properties
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(getFileNameFromPath(pdfFilePath));
        fileEntity.setFilePath(pdfFilePath);
        fileEntity.setFileType("pdf"); // You can set a specific type if needed

        // Use AsyncTask to insert into the database in the background
        new InsertPdfFileTask().execute((Runnable) fileEntity);
    }

//    inserting pdf on the background thread
    private class InsertPdfFileTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String pdfFilePath = params[0];

            // Access the Room database and perform the insertion
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(getFileNameFromPath(pdfFilePath));
            fileEntity.setFilePath(pdfFilePath);
            fileEntity.setFileType("pdf"); // You can set a specific type if needed

            appDatabase.fileDao().insertFile(fileEntity);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // This method runs on the main/UI thread, you can display Toast messages here
            Toast.makeText(getApplicationContext(), "PDF file inserted successfully", Toast.LENGTH_SHORT).show();
        }
    }




//to validate the file extention
    private boolean isPdfFile(String filePath) {
        String fileType = getFileExtension(getFileNameFromPath(filePath));
        return "pdf".equalsIgnoreCase(fileType);
    }

//  to get the file path
    private String getFileNameFromPath(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

//    to get the extension of a file(eg. .pdf)
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";  // No file extension found
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    //create new passsword when the user has current password
    private void showCreatePasswordUI() {
        PasswordFragment passwordFragment = new PasswordFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.password_popup_container, passwordFragment)
                .addToBackStack(null)
                .commit();

        // Set the visibility of the container to visible
        View passwordContainer = findViewById(R.id.password_popup_container);
        if (passwordContainer != null) {
            passwordContainer.setVisibility(View.VISIBLE);
        }
    }


    //show dialog to delete note
    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(createNote.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {

                            NotesDatabase.getDatabase(getApplicationContext()).noteDao()
                                    .deleteNote(alreadyAvailableNote);
                            return null;
                        }


                        @Override
                        protected void onPostExecute(Void avoid) {

                            super.onPostExecute(avoid);

                            Intent intent = new Intent();
                            intent.putExtra("isNote Deleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    new DeleteNoteTask().execute();
                }
            });


            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });

        }
            dialogDeleteNote.show();
    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

//    permission for reading images
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
//               Toast.makeText(this,"Permission Denied! You cannot add Image", Toast.LENGTH_SHORT).show();
//                selectImage();
                ActivityCompat.requestPermissions(createNote.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_STORAGE_PERMISSION);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for selecting pdf
        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri pdfUri = data.getData();
            if (pdfUri != null) {
                selectedPdfFilePath = getFilePathFromUri(pdfUri);

                // Log the selected PDF file path for debugging
                Log.d("PDF_PATH", "Selected PDF file path: " + selectedPdfFilePath);

                // Check if selectedPdfFilePath is not null before inserting into the database
                if (selectedPdfFilePath != null) {
                    // Use AsyncTask to insert into the database in the background
                    new InsertPdfFileTask().execute(selectedPdfFilePath);

                } else {
                    Log.e("PDF_PATH", "Error: selectedPdfFilePath is null");
                }
            } else {
                Log.e("PDF_PATH", "Error: pdfUri is null");
            }
        } else {
            Log.e("PDF_PATH", "Error: requestCode or resultCode doesn't match");
        }

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

                        selectedImagePath = getPathFromUri(selectedImageUri);

                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void showAddURLDialog(){
        if (dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(createNote.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url, (ViewGroup)  findViewById(R.id.layoutAddUrlContainer)
            );
           builder.setView(view);
           dialogAddURL = builder.create();
           if (dialogAddURL.getWindow() != null){
               dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
           }
           final EditText inputURL = view.findViewById(R.id.inputURL);
           inputURL.requestFocus();
           view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (inputURL.getText().toString().trim().isEmpty()){
                       Toast.makeText(createNote.this,"ENTER URL", Toast.LENGTH_SHORT).show();
                   }else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()){
                       Toast.makeText(createNote.this,"ENTER VALID URL", Toast.LENGTH_SHORT).show();
                   } else {
                       textWebURL.setText(inputURL.getText().toString());
                       layoutWebURL.setVisibility(View.VISIBLE);
                       dialogAddURL.dismiss();
                   }
               }
           });

           view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialogAddURL.dismiss();
               }
           });
            dialogAddURL.show();


        }
    }



    // Method to handle back button press
    private void onBackButtonPressed() {
        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Check if there are fragments in the back stack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the top fragment from the back stack
            fragmentManager.popBackStack();
        } else {
            // If no fragments in the back stack, finish the activity
            finish();
        }
    }





}
