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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.stranger.chat.R;
import com.stranger.chat.notes_modules.NoteEditorActivity;
import com.stranger.chat.notes_modules.ToDos;
import com.stranger.chat.notes_modules.bottom_sheet.Notes_BottomSheet;
import com.stranger.chat.notes_modules.data.Note_Tile_Data;

import java.util.Objects;

public class Notes_Adapter extends FirestoreRecyclerAdapter<Note_Tile_Data, Notes_Adapter.Topic_ViewHolder> {

    FragmentManager fragmentManager;
    Activity activity;
    CollectionReference notesCollectionReferance;

    public Notes_Adapter(FirestoreRecyclerOptions<Note_Tile_Data> options, Activity activity, FragmentManager fragmentManager, CollectionReference notesCollectionReference) {
        super(options);
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.notesCollectionReferance = notesCollectionReference;
    }

    @NonNull
    @Override
    public Topic_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_tile_sample, parent, false);

        return new Notes_Adapter.Topic_ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Topic_ViewHolder holder, int position, @NonNull Note_Tile_Data model) {
        String title = model.getTitle(),
                type = model.getType(),
                noteId = model.getNoteId();

        holder.getTitle().setText(title);

        if (Objects.equals(type, "note")) {
            String description = model.getDescription();
            holder.getDescription().setText(description);
        } else {
            holder.getDescription().setVisibility(View.GONE);
        }

        // to open notes editoe
        holder.getTile().setOnClickListener(view -> {
            Intent intent;
            if (Objects.equals(model.getType(), "notes")) {
                intent = new Intent(activity, NoteEditorActivity.class);
            } else if (Objects.equals(model.getType(), " ")) {
                intent = new Intent(activity, ToDos.class);
            } else {
                Toast.makeText(activity, "someting is not right", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("noteId", noteId);
            activity.startActivity(intent);
        });

        // to open menu
        holder.getTile().setOnLongClickListener(v -> {
            Notes_BottomSheet notes_bottomSheet = new Notes_BottomSheet(model, notesCollectionReferance);
            notes_bottomSheet.show(fragmentManager, "ModalBottomSheet");
            return true;
        });
    }

    static class Topic_ViewHolder extends RecyclerView.ViewHolder {
        CardView tile;
        TextView title, description;

        public Topic_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tile = itemView.findViewById(R.id.tile);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
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

    }
}