package com.stranger.chat.notes_modules.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.stranger.chat.R;
import com.stranger.chat.notes_modules.data.Task_Tile_Data;

import java.util.HashMap;
import java.util.Map;

public class Task_Adapter extends FirestoreRecyclerAdapter<Task_Tile_Data, Task_Adapter.Item_ViewHolder> {

    CollectionReference todoCollectionReference;

    public Task_Adapter(FirestoreRecyclerOptions<Task_Tile_Data> options, CollectionReference todoCollectionReference) {
        super(options);
        this.todoCollectionReference = todoCollectionReference;
    }

    @NonNull
    @Override
    public Item_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_tile_sample, parent, false);

        return new Item_ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Item_ViewHolder holder, int position, @NonNull Task_Tile_Data model) {
        holder.getTitle().setText(String.format("%s ", model.getTitle()));
        holder.getDescription().setText(String.format("%s ", model.getDescription()));

        holder.getStatus().setChecked(model.isStatus());
        holder.getStatus().setOnClickListener(v -> {
            boolean status = holder.getStatus().isChecked();

            Map<String, Object> data = new HashMap<>();
            data.put("status", status);

            todoCollectionReference.document(model.getTaskId()).update(data);
        });

        holder.getDeleteTask().setOnClickListener(v -> {

        });
    }

    static class Item_ViewHolder extends RecyclerView.ViewHolder {

        EditText title, description;
        CheckBox status;
        ImageView deleteTask;

        public Item_ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

            status = itemView.findViewById(R.id.status);
            deleteTask = itemView.findViewById(R.id.deleteTask);
        }

        public ImageView getDeleteTask() {
            return deleteTask;
        }

        public EditText getTitle() {
            return title;
        }

        public EditText getDescription() {
            return description;
        }

        public CheckBox getStatus() {
            return status;
        }
    }
}