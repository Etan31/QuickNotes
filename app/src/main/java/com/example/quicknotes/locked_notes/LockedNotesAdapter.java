package com.example.quicknotes.locked_notes;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quicknotes.EnterPasswordDialogFragment;
import com.example.quicknotes.EnterPasswordDialogFragment2;
import com.example.quicknotes.OpenedLockedNoteActivity;
import com.example.quicknotes.R;
import com.example.quicknotes.password.PasswordEntity;
import com.example.quicknotes.viewmodel.PasswordViewModel;

import java.util.List;

public class LockedNotesAdapter extends RecyclerView.Adapter<LockedNotesAdapter.LockedNoteViewHolder> implements ViewModelStoreOwner {

    private static List<LockedNote> lockedNotes;
    private static OnLockedNoteClickListener onLockedNoteClickListener;

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return null;
    }

    public interface OnLockedNoteClickListener {
        void onLockedNoteClicked(LockedNote lockedNote, int position);
    }

    public void setOnLockedNoteClickListener(OnLockedNoteClickListener listener) {
        this.onLockedNoteClickListener = listener;
    }


    public LockedNotesAdapter(List<LockedNote> lockedNotes) {
        this.lockedNotes = lockedNotes;
    }

    @NonNull
    @Override
    public LockedNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_lockednotes, parent, false);
        return new LockedNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LockedNoteViewHolder holder, int position) {
        LockedNote lockedNote = lockedNotes.get(position);
        holder.bind(lockedNote);

        // onclick listener of the item_container_lockednotes.xml
//        TODO: onclick of the holder of locked password, no password set yet.
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(holder.itemView.getContext(), OpenedLockedNoteActivity.class);
//            intent.putExtra("lockedNote", lockedNote);
//
//            // Start the OpenedLockedNoteActivity
//            holder.itemView.getContext().startActivity(intent);
//
//        });

        holder.itemView.setOnClickListener(v -> {
            Log.d("Click", "LockedNotesAdapter:81");

            if (onLockedNoteClickListener != null) {
                Log.d("Click", "LockedNotesAdapter:84");
                onLockedNoteClickListener.onLockedNoteClicked(lockedNote, position);
            } else {
                Log.d("Click", "LockedNotesAdapter:87");
                showPasswordDialog(holder.itemView.getContext(), lockedNote);
            }
        });

    }

    private void showPasswordDialog(Context context, LockedNote lockedNote) {
        Log.d("Click", "LockedNotesAdapter:95");

        // Check if a password is set
        if (context instanceof AppCompatActivity) {
            PasswordViewModel passwordViewModel = new ViewModelProvider((AppCompatActivity) context)
                    .get(PasswordViewModel.class);




            passwordViewModel.getPasswordLiveData().observe((LifecycleOwner) context, savedPassword -> {
                Log.d("Click", "LiveData observed");
                if (savedPassword != null) {
                    showEnterPasswordUI(context, savedPassword, lockedNote);
                    Log.d("Click", "LockedNotesAdapter:97");
                } else {
                    // Handle the case when no password is set
                    Toast.makeText(context, "No password set for this note", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Log.d("Click", "LockedNotesAdapter:110");
        }
    }

    private void showEnterPasswordUI(Context context, PasswordEntity savedPassword, LockedNote lockedNote) {
        EnterPasswordDialogFragment dialogFragment = new EnterPasswordDialogFragment(savedPassword);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "EnterPasswordDialogFragment");
    }




    @Override
    public int getItemCount() {
        return lockedNotes.size();
    }

    public void setLockedNotes(List<LockedNote> lockedNotes) {
        this.lockedNotes = lockedNotes;
        notifyDataSetChanged();
    }

//  To display the icon and Notes title to the frame.
    public static class LockedNoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView lockImageView;
        private final TextView lockTitleTextView;

        public LockedNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            lockImageView = itemView.findViewById(R.id.imageView12);
            lockTitleTextView = itemView.findViewById(R.id.Lock_lbl);
        }

        public void bind(LockedNote lockedNote) {
            // Bind data to views
            // For example:
            lockTitleTextView.setText(lockedNote.getTitle());
            // You can also load an image into lockImageView using libraries like Glide
        }

//        to get the position of which container is clicked
    @Override
    public void onClick(View view) {
        if (onLockedNoteClickListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onLockedNoteClickListener.onLockedNoteClicked(lockedNotes.get(position), position);
            }
        }
    }
}
}
