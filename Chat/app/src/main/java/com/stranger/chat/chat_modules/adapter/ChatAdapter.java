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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.ChatPage;
import com.stranger.chat.chat_modules.bottom_sheet.Chat_BottomSheet;
import com.stranger.chat.chat_modules.data.Chat_Tile_Data;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;

import java.util.Objects;

public class ChatAdapter extends FirestoreRecyclerAdapter<Chat_Tile_Data, ChatAdapter.Chat_ViewHolder> {
    Activity activity;
    String currentUser, reciverUserId, reciverName;
    FragmentManager fragmentManager;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Chat_Tile_Data> options, Activity activity, String currentUser, FragmentManager fragmentManager) {
        super(options);
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.currentUser = currentUser;
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
        for (String userId : model.getUsersId()) {
            if (!Objects.equals(userId, currentUser)) {
                reciverUserId = userId;
                break;
            }
        }

        FirebaseDatabaseConnection.userDocument(reciverUserId).addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }
            if (snapshot != null) {
                reciverName = (String) snapshot.get("username");
                holder.getUsernameField().setText(reciverName);
                // download image from fireStorage
                Picasso.get().load((String) snapshot.get("profilePic")).into(holder.getProfilePicField());
            }
        });

        holder.getLastChatTimeField().setText(model.getLastSeen());

        holder.getChatTile().setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putString("reciverUserId", reciverUserId);
            sendData.putString("messageId", model.getMessageId());
            sendData.putString("reciverName", reciverName);

            Intent intent = new Intent(activity, ChatPage.class);
            intent.putExtra("data", sendData);
            activity.startActivity(intent);
        });

        holder.getChatTile().setOnLongClickListener(view -> {
            Chat_BottomSheet chat_bottomSheet = new Chat_BottomSheet(model);
            chat_bottomSheet.show(fragmentManager, "ModalBottomSheet");
            return true;
        });

    }

    public static class Chat_ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicField;
        TextView usernameField, lastChatTimeField;
        LinearLayout chatTile;

        public Chat_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicField = itemView.findViewById(R.id.profilePic);
            usernameField = itemView.findViewById(R.id.username);
            lastChatTimeField = itemView.findViewById(R.id.lastChatTime);
            chatTile = itemView.findViewById(R.id.chatTile);
        }

        public ImageView getProfilePicField() {
            return profilePicField;
        }

        public TextView getUsernameField() {
            return usernameField;
        }

        public TextView getLastChatTimeField() {
            return lastChatTimeField;
        }

        public LinearLayout getChatTile() {
            return chatTile;
        }
    }
}
