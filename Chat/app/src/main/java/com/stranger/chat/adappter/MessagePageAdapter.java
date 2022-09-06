package com.stranger.chat.adappter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.stranger.chat.R;
import com.stranger.chat.data.MessageData;

public class MessagePageAdapter extends FirebaseRecyclerAdapter<MessageData, MessagePageAdapter.MessageViewHolder> {
    public MessagePageAdapter(FirebaseRecyclerOptions<MessageData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageData model) {
        holder.getUsername().setText(model.getUsername());
        holder.getMessageArea().setText(model.getMessageArea());
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