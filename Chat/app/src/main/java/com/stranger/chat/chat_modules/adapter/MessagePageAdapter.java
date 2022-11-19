package com.stranger.chat.chat_modules.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.data.MessagePage_Tile_Data;

public class MessagePageAdapter extends FirestoreRecyclerAdapter<MessagePage_Tile_Data, MessagePageAdapter.MessageViewHolder> {
    public MessagePageAdapter(FirestoreRecyclerOptions<MessagePage_Tile_Data> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessagePage_Tile_Data model) {
        holder.getUsername().setText(model.getSender());
        holder.getMessageArea().setText(model.getMessage());
        holder.getTimeStamp().setText(model.getTimeStamp());
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_tile_sample, parent, false);
        return new MessagePageAdapter.MessageViewHolder(view);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        CardView tile;
        ImageView profilePic;
        TextView messageArea, timeStamp, username;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            tile = itemView.findViewById(R.id.tile);

            profilePic = itemView.findViewById(R.id.profilePic);

            username = itemView.findViewById(R.id.username);
            messageArea = itemView.findViewById(R.id.messageTextView);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }

        public CardView getTile() {
            return tile;
        }

        public ImageView getProfilePic() {
            return profilePic;
        }

        public TextView getMessageArea() {
            return messageArea;
        }

        public TextView getTimeStamp() {
            return timeStamp;
        }

        public TextView getUsername() {
            return username;
        }
    }
}