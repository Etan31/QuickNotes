package com.example.quicknotes;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quicknotes.database.AppDatabase;
import com.example.quicknotes.viewmodel.PasswordViewModel;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PasswordEntity savedPassword;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    ----------------------------------------
    private static class PasswordAsyncTask extends AsyncTask<Void, Void, PasswordEntity> {
    private WeakReference<Context> contextWeakReference;
    private WeakReference<Button> changeResetPasswordButtonWeakReference;
    private WeakReference<SettingFragment> fragmentWeakReference;

    PasswordAsyncTask(SettingFragment fragment, Context context, Button changeResetPasswordButton) {
        this.contextWeakReference = new WeakReference<>(context);
        this.changeResetPasswordButtonWeakReference = new WeakReference<>(changeResetPasswordButton);
        this.fragmentWeakReference = new WeakReference<>(fragment);
    }

    @Override
    protected PasswordEntity doInBackground(Void... voids) {
        if (isCancelled()) {
            return null;
        }

        Context context = contextWeakReference.get();
        if (context != null) {
            return AppDatabase.getInstance(context.getApplicationContext()).passwordDao().getPassword();
        }
        return null;
    }

//    @Override
//    protected void onPostExecute(PasswordEntity savedPassword) {
//        SettingFragment fragment = fragmentWeakReference.get();
//        Button changeResetPasswordButton = changeResetPasswordButtonWeakReference.get();
//
//        if (fragment != null && changeResetPasswordButton != null) {
//            if (savedPassword != null) {
//                // Password is saved, show enter password UI or handle accordingly
//                changeResetPasswordButton.setOnClickListener(v -> {
//                    // Show UI to enter password
//                    Toast.makeText(fragment.requireContext(), "1Password", Toast.LENGTH_SHORT).show();
////                    fragment.showEnterPasswordUI(savedPassword);
//                });
//            } else {
//                // No password saved, show create password UI or handle accordingly
//                changeResetPasswordButton.setOnClickListener(v -> {
//                    // Show UI to create password
//                    fragment.showCreatePasswordUI();
//                    Toast.makeText(fragment.requireContext(), "1Prwerwassword", Toast.LENGTH_SHORT).show();
//                });
//            }
//        }
//    }
}





//    ----------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Button changeResetPasswordButton = view.findViewById(R.id.change_resetPass);
        new PasswordAsyncTask(this, requireContext(), changeResetPasswordButton).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



        TextView exitTextView = view.findViewById(R.id.exit);
        exitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the exit confirmation dialog
                openDialog();
            }
        });

        // Set click listener for the "Share" TextView
        TextView shareTextView = view.findViewById(R.id.share);
        shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Share the application
                shareApp();
            }
        });

        // Set click listener for the "Rate Us" TextView
        TextView rateUsTextView =view.findViewById(R.id.rate);
        rateUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rate the application on the Play Store
//                rateApp();
                showCreatePasswordUI();
            }
        });


        //clicked for change/reset Password
//        Button changeResetPasswordButton  =view.findViewById(R.id.change_resetPass);
//        PasswordEntity savedPassword = AppDatabase.getInstance(requireContext()).passwordDao().getPassword();

//        if (savedPassword != null) {
//            // Password is saved, show enter password UI or handle accordingly
//            changeResetPasswordButton.setOnClickListener(v -> {
//                // Show UI to enter password
//                // You can use a Dialog, start a new activity, or replace the fragment, etc.
//                // Example: showEnterPasswordDialog();
//                Toast.makeText(requireContext(), "11Password", Toast.LENGTH_SHORT).show();
//                showEnterPasswordUI(savedPassword);
//            });
//        } else {
//            // No password saved, show create password UI or handle accordingly
//            changeResetPasswordButton.setOnClickListener(v -> {
//                // Show UI to create password
//                // You can use a Dialog, start a new activity, or replace the fragment, etc.
//                // Example: showCreatePasswordDialog();
//                Toast.makeText(requireContext(), "21Password", Toast.LENGTH_SHORT).show();
//                showCreatePasswordUI();
//            });
//        }

        PasswordViewModel passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
        passwordViewModel.getPasswordLiveData().observe(getViewLifecycleOwner(), savedPassword -> {
            if (savedPassword != null) {
                // Password is saved, show enter password UI or handle accordingly
                changeResetPasswordButton.setOnClickListener(v -> {
                    // Show UI to enter password
                    // Example: showEnterPasswordDialog();
                    Toast.makeText(requireContext(), "1Password", Toast.LENGTH_SHORT).show();
//                    showEnterPasswordUI(savedPassword);
                    Log.d("password", "You have password set.");
                    Log.d("popup", "popup_lock is displayed");
                    showEnterPasswordUI(savedPassword);
                });
            } else {
                // No password saved, show create password UI or handle accordingly
                changeResetPasswordButton.setOnClickListener(v -> {
                    // Show UI to create password
                    // Example: showCreatePasswordDialog();
                    showCreatePasswordUI();
                    Log.d("password", "You don't have password set.");
                    Log.d("popup", "popup_createpassword is displayed");


                });
            }
        });

        passwordViewModel.loadPassword(requireContext());


        return view;
    }

    private void showEnterPasswordUI(PasswordEntity savedPassword) {
        // Set the fragment layout for entering password
        View enterPasswordView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_lock, null);
        EnterPasswordDialogFragment dialogFragment = new EnterPasswordDialogFragment(savedPassword);
        dialogFragment.show(getChildFragmentManager(), "EnterPasswordDialogFragment");
        Log.d("showEnterPasswordUI", "clicked functions yessss");
    }

    private void showCreatePasswordUI() {
        Log.d("Savjojghke", "showCreatePasswordUI called");

        // Create an instance of your PasswordFragment
        PasswordFragment passwordFragment = new PasswordFragment();

        // Use a transaction to add the PasswordFragment to the container
        getParentFragmentManager().beginTransaction()
                .replace(R.id.password_popup_container, passwordFragment) // Use the existing instance
                .addToBackStack(null)
                .commit();

        // Set the visibility of the container to visible
        getView().findViewById(R.id.password_popup_container).setVisibility(View.VISIBLE);
    }






    private void clicked() {
        Log.d("SavesasdASF", "click");

        View createPasswordView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_createpassword, null);
        EditText passwordEditText = createPasswordView.findViewById(R.id.typepass);
        EditText retypePasswordEditText = createPasswordView.findViewById(R.id.retypepass);
        Button submitButton = createPasswordView.findViewById(R.id.SubmitBtn);

        submitButton.setOnClickListener(v -> {
            Log.d("Savesss", "Clicked button");

            String password = passwordEditText.getText().toString();
            String retypePassword = retypePasswordEditText.getText().toString();
            Log.d("Save", "clicked");
            Log.d("Save", password);
            Log.d("Save", retypePassword);

            if (!password.equals(retypePassword)) {
                // Passwords do not match, show an error message
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return; // Return to avoid saving incorrect password
            }

            // Passwords match, save the password
            AppDatabase.getInstance(requireContext()).passwordDao().savePassword(new PasswordEntity(password));

            // Log the Submit button click
            Log.d("Save", "Submit button clicked");

            // Handle accordingly (e.g., navigate to the main activity)

            // Optional: Close the fragment or hide the container
            getView().findViewById(R.id.password_popup_container).setVisibility(View.GONE);

            // Rest of the code...
        });
    }



    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "I found this amazing app, check it out: [App Link]");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Uri playStoreUri = Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
            startActivity(browserIntent);
        }
    }

    private void openDialog() {
        Exit exitDialog = new Exit();
        exitDialog.show(getChildFragmentManager(), "exit_dialog");
    }







}