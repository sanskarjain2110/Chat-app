package com.stranger.chat.chat_modules.adapter;

import android.app.Activity;
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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.MessagePage;
import com.stranger.chat.chat_modules.data.ChatFragment_Tile_Data;

import java.util.Objects;

public class ChatFragmentAdapter extends FirestoreRecyclerAdapter<ChatFragment_Tile_Data, ChatFragmentAdapter.Chat_ViewHolder> {
    Activity activity;
    String currentUser, reciversUserId, reciversname;

    public ChatFragmentAdapter(@NonNull FirestoreRecyclerOptions<ChatFragment_Tile_Data> options, Activity activity, String currentUser) {
        super(options);
        this.activity = activity;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ChatFragmentAdapter.Chat_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_fragment_tile_sample, parent, false);

        return new ChatFragmentAdapter.Chat_ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Chat_ViewHolder holder, int position, @NonNull ChatFragment_Tile_Data model) {
        for (String userId : model.getUsersId()) {
            if (!Objects.equals(userId, currentUser)) {
                reciversUserId = userId;
                break;
            }
        }

        FirebaseFirestore.getInstance()
                .collection("users").document(reciversUserId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (snapshot != null) {
                        reciversname = (String) snapshot.get("username");
                        holder.getUsernameField().setText(reciversname);
                        // download image from fireStorage
                        Picasso.get().load((String) snapshot.get("profilePic")).into(holder.getProfilePicField());
                    }
                });

        holder.getLastTextField().setText(model.getLastText());
        holder.getLastChatTimeField().setText(model.getLastSeen());

        holder.getChatTile().setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putString("reciversUserId", reciversUserId);
            sendData.putString("messageId", model.getMessageId());
            sendData.putString("reciversname", reciversname);

            Intent intent = new Intent(activity, MessagePage.class);
            intent.putExtra("data", sendData);
            activity.startActivity(intent);
        });
    }

    public static class Chat_ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicField;
        TextView usernameField, lastTextField, lastChatTimeField;
        LinearLayout chatTile;

        public Chat_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicField = itemView.findViewById(R.id.profilePic);
            usernameField = itemView.findViewById(R.id.username);
            lastChatTimeField = itemView.findViewById(R.id.lastChatTime);
            lastTextField = itemView.findViewById(R.id.lastText);
            chatTile = itemView.findViewById(R.id.chatTile);
        }

        public ImageView getProfilePicField() {
            return profilePicField;
        }

        public TextView getUsernameField() {
            return usernameField;
        }

        public TextView getLastTextField() {
            return lastTextField;
        }

        public TextView getLastChatTimeField() {
            return lastChatTimeField;
        }

        public LinearLayout getChatTile() {
            return chatTile;
        }
    }
}
