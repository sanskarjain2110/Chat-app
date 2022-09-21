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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stranger.chat.R;
import com.stranger.chat.data.AddChat_Tile_Data;

import java.util.HashMap;
import java.util.Map;

public class AddChatAdapter extends FirebaseRecyclerAdapter<AddChat_Tile_Data, AddChatAdapter.AddChat_ViewHolder> {
    String host_username;

    Activity activity;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public AddChatAdapter(FirebaseRecyclerOptions<AddChat_Tile_Data> options, Activity activity, String host_username) {
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
            String messagekey = myRef.child("messagesData").push().getKey(),
                    reciverKey = model.getUserId(),
                    senderKey = user.getUid();

            Map<String, Object> user = new HashMap<>();
            user.put(reciverKey, reciver);
            user.put(senderKey, host_username);


            Map<String, Object> roots = new HashMap<>();
            roots.put("messageId", messagekey);
            roots.put("lastText", " ");
            roots.put("lastSeen", " ");
            roots.put("users", user);

            FirebaseDatabase.getInstance().getReference().child("messagesId").child(messagekey).setValue(roots);
            FirebaseDatabase.getInstance().getReference().child("userMessageId").child(reciverKey).child(senderKey).setValue(messagekey);
            FirebaseDatabase.getInstance().getReference().child("userMessageId").child(senderKey).child(reciverKey).setValue(messagekey);

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