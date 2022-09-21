package com.stranger.chat.adappter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.MessagePage;
import com.stranger.chat.data.Chat_Tile_Data;

import java.util.Objects;

public class ChatAdapter extends FirebaseRecyclerAdapter<Chat_Tile_Data, ChatAdapter.Chat_ViewHolder> {
    Context context;


    public ChatAdapter(FirebaseRecyclerOptions<Chat_Tile_Data> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.Chat_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_tile_sample, parent, false);

        return new ChatAdapter.Chat_ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Chat_ViewHolder holder, int position, @NonNull Chat_Tile_Data model) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(),
                reciverUsername = " ";
        for (String s : model.getUsers().keySet()) {
            if (!Objects.equals(s, userId) && s != null) {
                reciverUsername = (String) model.getUsers().get(s);
                holder.getUsername().setText(reciverUsername);
            }
        }
        holder.getLastText().setText(model.getLastText());
        holder.getLastChatTime().setText(model.getLastSeen());

        String finalReciverUsername = reciverUsername;
        holder.getChatTile().setOnClickListener(view -> {
            Intent intent = new Intent(context, MessagePage.class);

            Bundle sendData = new Bundle();
            sendData.putString("reciverUsername", finalReciverUsername);
            sendData.putString("messageId", model.getMessageId());
            intent.putExtra("data", sendData);
            context.startActivity(intent);
        });
    }

    public static class Chat_ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView username, lastText, lastChatTime;
        LinearLayout chatTile;

        public Chat_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            username = itemView.findViewById(R.id.username);
            lastChatTime = itemView.findViewById(R.id.lastChatTime);
            lastText = itemView.findViewById(R.id.lastText);
            chatTile = itemView.findViewById(R.id.chatTile);
        }

        public ImageView getProfilePic() {
            return profilePic;
        }

        public LinearLayout getChatTile() {
            return chatTile;
        }

        public TextView getUsername() {
            return username;
        }

        public TextView getLastText() {
            return lastText;
        }

        public TextView getLastChatTime() {
            return lastChatTime;
        }
    }
}
