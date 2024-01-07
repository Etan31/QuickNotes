package com.example.quicknotes;

import static com.example.quicknotes.HomeFragment.REQUEST_CODE_ADD_NOTE;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quicknotes.database.LockedNotesDatabase;
import com.example.quicknotes.locked_notes.LockedNote;
import com.example.quicknotes.locked_notes.LockedNotesAdapter;
import com.example.quicknotes.password.PasswordEntity;
import com.example.quicknotes.viewmodel.PasswordViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LockedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LockedFragment extends Fragment implements LockedNotesAdapter.OnLockedNoteClickListener{
// Has been removed the implements LockedNotesAdapter.OnLockedNoteClickListener above this line
    private RecyclerView lockedNotesRecyclerView;

    private LockedNotesAdapter lockedNotesAdapter;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public LockedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LockedFragment.
     */

    public static LockedFragment newInstance(String param1, String param2) {
        LockedFragment fragment = new LockedFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locked, container, false);

        // Initialize RecyclerView and Adapter
//        D ak maaram kun kanan ano in, basta importante in hahaha
        lockedNotesRecyclerView = view.findViewById(R.id.LockednotesRecyclerView);

        lockedNotesAdapter = new LockedNotesAdapter(new ArrayList<>());
        lockedNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lockedNotesRecyclerView.setAdapter(lockedNotesAdapter);

        int spanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        lockedNotesRecyclerView.setLayoutManager(gridLayoutManager);

        // Set the adapter
        lockedNotesRecyclerView.setAdapter(lockedNotesAdapter);
        // Load locked notes from the database and update the adapter
        loadLockedNotes();




        return view;
    }



//click function before entering locked notes on the side menu button
    private void loadLockedNotes() {
        new LoadLockedNotesTask(this).execute();
    }
//  to load all locked notes
    private void onLockedNotesLoaded(List<LockedNote> lockedNotes) {
        if (lockedNotes != null) {
            lockedNotesAdapter.setLockedNotes(lockedNotes);
//            Log.d("Click", "lockedFragment:134");

        }
    }

//    to lock notes on the background task
    private static class LoadLockedNotesTask extends AsyncTask<Void, Void, List<LockedNote>> {

        private final WeakReference<LockedFragment> fragmentReference;

        public LoadLockedNotesTask(LockedFragment fragment) {
            this.fragmentReference = new WeakReference<>(fragment);
        }


//        to Query and select all the locked notes from the database
        @Override
        protected List<LockedNote> doInBackground(Void... voids) {
            LockedFragment fragment = fragmentReference.get();
            if (fragment != null) {
                return LockedNotesDatabase.getInstance(fragment.requireContext()).lockedNoteDao().getAllLockedNotes();
            }
            return null;
        }

//      Then display the selected Query
//      This is when you click the side menu, locked notes
        @Override
        protected void onPostExecute(List<LockedNote> lockedNotes) {
            LockedFragment fragment = fragmentReference.get();
            if (fragment != null) {
                fragment.onLockedNotesLoaded(lockedNotes);
            }
        }
    }

    //when the locked note is clicked.
    private static final int REQUEST_CODE_UPDATE_LOCKED_NOTE = 1; // You can use any integer value
    @Override
    public void onLockedNoteClicked(LockedNote lockedNote, int position) {
        Log.d("Click", "Locked note clicked: " + lockedNote.getTitle());
        Intent intent = new Intent(requireContext(), OpenedLockedNoteActivity.class);
        intent.putExtra("lockedNote", lockedNote);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_LOCKED_NOTE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE_UPDATE_LOCKED_NOTE && resultCode == Activity.RESULT_OK) {
//            // Handle any logic you need after the OpenedLockedNoteActivity finishes
//            // In your case, you might want to check for password-related changes or updates.
//            // For example, you can reload locked notes or perform other actions.
//            loadLockedNotes();
//        }
//    }



}