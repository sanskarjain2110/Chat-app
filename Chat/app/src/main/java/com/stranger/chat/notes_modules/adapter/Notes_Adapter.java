package com.stranger.chat.notes_modules.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.stranger.chat.R;
import com.stranger.chat.notes_modules.NoteEditorActivity;
import com.stranger.chat.notes_modules.data.Note_Tile_Data;

public class Notes_Adapter extends FirestoreRecyclerAdapter<Note_Tile_Data, Notes_Adapter.Topic_ViewHolder> {

    Activity activity;

    public Notes_Adapter(FirestoreRecyclerOptions<Note_Tile_Data> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Topic_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_tile_sample, parent, false);

        return new Notes_Adapter.Topic_ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Topic_ViewHolder holder, int position, @NonNull Note_Tile_Data model) {
        String title = model.getTitle(),
                lastUpdated = model.getLastUpdated(),
                description = model.getDescription(),
                noteId = model.getNoteId();

        holder.getTitle().setText(title);
        holder.getDescription().setText(description);
        holder.getLastUpdated().setText(lastUpdated);

        // to open content
        holder.getTile().setOnClickListener(view -> {
            Intent intent = new Intent(activity, NoteEditorActivity.class);
            intent.putExtra("noteId", noteId);
            activity.startActivity(intent);
        });

        // to open menu
        holder.getTile().setOnLongClickListener(v -> {
            Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    static class Topic_ViewHolder extends RecyclerView.ViewHolder {
        CardView tile;
        TextView title, description, lastUpdated;

        public Topic_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tile = itemView.findViewById(R.id.tile);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            lastUpdated = itemView.findViewById(R.id.lastUpdated);
        }

        public CardView getTile() {
            return tile;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getLastUpdated() {
            return lastUpdated;
        }
    }
}