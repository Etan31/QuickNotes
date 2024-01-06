package com.example.quicknotes.locked_notes;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quicknotes.OpenedLockedNoteActivity;
import com.example.quicknotes.R;

import java.util.List;

public class LockedNotesAdapter extends RecyclerView.Adapter<LockedNotesAdapter.LockedNoteViewHolder> {

    private static List<LockedNote> lockedNotes;
    private static OnLockedNoteClickListener onLockedNoteClickListener;

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
//        TODO: add a call to see get the password of the the app.
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(holder.itemView.getContext(), OpenedLockedNoteActivity.class);
            intent.putExtra("lockedNote", lockedNote);
            // Start the OpenedLockedNoteActivity
            holder.itemView.getContext().startActivity(intent);

        });
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
