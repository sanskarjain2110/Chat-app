package com.stranger.chat.chat_modules.home_screen;

import static com.stranger.chat.fuctionality.Keys.BUNDLE_DATA;
import static com.stranger.chat.fuctionality.Keys.firebase.MESSAGE_ID;
import static com.stranger.chat.fuctionality.Keys.firebase.PHONE_NUMBER;
import static com.stranger.chat.fuctionality.Keys.firebase.PROFILE_PIC_URL;
import static com.stranger.chat.fuctionality.Keys.firebase.USERNAME;
import static com.stranger.chat.fuctionality.Keys.firebase.USER_ID;
import static com.stranger.chat.fuctionality.Text.getFormattedTimeDifference;

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

    String reciverUserId, reciverName, reciverNumber, reciverProfilePic;

    private final Activity activity;
    private final FragmentManager fragmentManager;
    private final String currentUser;

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
            if (error != null || snapshot == null) return;

            reciverName = (String) snapshot.get(USERNAME);
            reciverNumber = (String) snapshot.get(PHONE_NUMBER);
            reciverProfilePic = (String) snapshot.get(PROFILE_PIC_URL);

            Bundle sendData = new Bundle();
            sendData.putString(USER_ID, reciverUserId);
            sendData.putString(USERNAME, reciverName);
            sendData.putString(PHONE_NUMBER, reciverNumber);
            sendData.putString(PROFILE_PIC_URL, reciverProfilePic);
            sendData.putString(MESSAGE_ID, model.getMessageId());
            Chat_BottomSheet chatBottomSheet = new Chat_BottomSheet(sendData);

            holder.getUsernameField().setText(reciverName); // set reciver name

            Picasso.get().load(reciverProfilePic).placeholder(R.drawable.account_circle_24px)
                    .into(holder.getProfilePicField());// set reciver profile pic
            holder.getProfilePicField().setOnClickListener(view -> chatBottomSheet
                    .show(fragmentManager, "ModalBottomSheet"));

            holder.getLastChatTimeField().setText(getFormattedTimeDifference(model.getLastSeen())); // set date

            holder.getChatTile().setOnClickListener(view -> {
                Intent intent = new Intent(activity, ChatPage.class);
                intent.putExtra(BUNDLE_DATA, sendData);
                activity.startActivity(intent);
            });

            holder.getChatTile().setOnLongClickListener(view -> {
                chatBottomSheet.show(fragmentManager, "ModalBottomSheet");
                return true;
            });
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
