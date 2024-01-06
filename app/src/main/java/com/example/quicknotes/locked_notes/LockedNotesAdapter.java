package com.example.quicknotes.locked_notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quicknotes.R;

import java.util.List;

public class LockedNotesAdapter extends RecyclerView.Adapter<LockedNotesAdapter.LockedNoteViewHolder> {

    private List<LockedNote> lockedNotes;

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
    public static class LockedNoteViewHolder extends RecyclerView.ViewHolder {

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
    }
}
