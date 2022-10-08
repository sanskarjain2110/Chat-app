package com.stranger.chat.adappter;

import android.app.Activity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.stranger.chat.R;
import com.stranger.chat.data.AddChat_Tile_Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddChatAdapter extends FirestoreRecyclerAdapter<AddChat_Tile_Data, AddChatAdapter.AddChat_ViewHolder> {
    String host_username;

    Activity activity;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    public AddChatAdapter(FirestoreRecyclerOptions<AddChat_Tile_Data> options, Activity activity, String host_username) {
        super(options);
        this.activity = activity;
        this.host_username = host_username;
    }

    @Override
    protected void onBindViewHolder(@NonNull AddChat_ViewHolder holder, int position, @NonNull AddChat_Tile_Data model) {
        String reciver = model.getUsername(),
                phoneNumber = model.getPhoneNumber();

        holder.getUsernameField().setText(reciver);
        holder.getPhoneNumberField().setText(phoneNumber);

        holder.getAddChatTile().setOnClickListener(view -> {
            String messagekey = FirebaseFirestore.getInstance().collection("messagesData").document().getId(),
                    reciverKey = model.getUserId(),
                    senderKey = user.getUid();


            ArrayList<String> filter = new ArrayList<>();
            filter.add(reciverKey);
            filter.add(senderKey);

            Map<String, Object> userCollection = new HashMap<>();
            userCollection.put(reciverKey, reciver);
            userCollection.put(senderKey, host_username);

            Map<String, Object> roots = new HashMap<>();
            roots.put("messageId", messagekey);
            roots.put("lastText", " ");
            roots.put("lastSeen", " ");
            roots.put("filter", filter);
            roots.put("user", userCollection);

            FirebaseFirestore.getInstance().collection("messages").document(messagekey).set(roots);

            Map<String, Object> senderEnd = new HashMap<>();
            senderEnd.put(senderKey, messagekey);

            FirebaseFirestore.getInstance()
                    .collection("userMessageId")
                    .document(reciverKey)
                    .set(senderEnd, SetOptions.merge());

            Map<String, Object> reciverEnd = new HashMap<>();
            reciverEnd.put(reciverKey, messagekey);

            FirebaseFirestore.getInstance()
                    .collection("userMessageId")
                    .document(senderKey)
                    .set(reciverEnd, SetOptions.merge());

            activity.finish();
        });
    }

    @NonNull
    @Override
    public AddChat_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_chat_tile_sample, parent, false);

        return new AddChatAdapter.AddChat_ViewHolder(view);
    }

    static class AddChat_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout addChatTile;
        TextView usernameField, phoneNumberField;
        ImageView profilePicField;

        public AddChat_ViewHolder(@NonNull View itemView) {
            super(itemView);

            addChatTile = itemView.findViewById(R.id.addChatTile);

            profilePicField = itemView.findViewById(R.id.profilePicField);

            usernameField = itemView.findViewById(R.id.usernameField);
            phoneNumberField = itemView.findViewById(R.id.phoneNumberField);
        }

        public TextView getUsernameField() {
            return usernameField;
        }

        public LinearLayout getAddChatTile() {
            return addChatTile;
        }

        public TextView getPhoneNumberField() {
            return phoneNumberField;
        }

        public ImageView getProfilePicField() {
            return profilePicField;
        }
    }
}