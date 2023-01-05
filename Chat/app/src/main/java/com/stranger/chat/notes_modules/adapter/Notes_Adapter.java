package com.stranger.chat.notes_modules.adapter;

import android.content.Context;
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
import com.stranger.chat.notes_modules.ContentActivity;
import com.stranger.chat.notes_modules.data.Topics_Tile_Data;

public class Notes_Adapter extends FirestoreRecyclerAdapter<Topics_Tile_Data, Notes_Adapter.Topic_ViewHolder> {

    Context context;

    public Notes_Adapter(FirestoreRecyclerOptions<Topics_Tile_Data> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public Topic_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_tile_sample, parent, false);

        return new Notes_Adapter.Topic_ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Topic_ViewHolder holder, int position, @NonNull Topics_Tile_Data model) {
        String topic = model.getTopic(),
                lastUpdated = model.getLastUpdated(),
                content = model.getContent(),
                topicId = model.getTopicId();

        holder.getTopic().setText(topic);
        holder.getContent().setText(content);
        holder.getLastUpdated().setText(lastUpdated);

        // to open content
        holder.getTile().setOnClickListener(view -> {
            Intent intent = new Intent(context, ContentActivity.class);
            intent.putExtra("topicId", topicId);
            context.startActivity(intent);
        });

        // to open menu
        holder.getTile().setOnLongClickListener(v -> {
            Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    static class Topic_ViewHolder extends RecyclerView.ViewHolder {
        CardView tile;
        TextView topic, content, lastUpdated;

        public Topic_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tile = itemView.findViewById(R.id.tile);
            topic = itemView.findViewById(R.id.topic);
            content = itemView.findViewById(R.id.content);
            lastUpdated = itemView.findViewById(R.id.lastUpdated);
        }

        public CardView getTile() {
            return tile;
        }

        public TextView getTopic() {
            return topic;
        }

        public TextView getContent() {
            return content;
        }

        public TextView getLastUpdated() {
            return lastUpdated;
        }
    }
}