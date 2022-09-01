package com.stranger.chat.adappter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stranger.chat.R;
import com.stranger.chat.data.AddChat_Tile_Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddChatAdapter extends RecyclerView.Adapter<AddChatAdapter.AddChat_ViewHolder> {
    ArrayList<AddChat_Tile_Data> data;
    Context context;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference myRef = database.getReference();

    public AddChatAdapter(ArrayList<AddChat_Tile_Data> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public AddChat_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_chat_tile_sample, parent, false);

        return new AddChatAdapter.AddChat_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddChat_ViewHolder holder, int position) {
        holder.getUsernameField().setText(data.get(position).getUsername());
        holder.getPhoneNumberField().setText(data.get(position).getPhoneNumber());

        holder.getAddChatTile().setOnClickListener(view -> {

            String messagekey = myRef.child("messagesData").push().getKey(),
                    reciver = data.get(position).getUserId(),
                    sender = user.getUid();

            Map<String, Object> roots = new HashMap<>();

            Map<String, Object> user = new HashMap<>();
            user.put("0", reciver);
            user.put("1", sender);

            roots.put("users", user);
            roots.put("messages", "");

            myRef.child("messagesData").child(messagekey).setValue(roots);

            FirebaseDatabase.getInstance().getReference().child("usersMessageId").child(reciver).child(sender).setValue(messagekey);
            FirebaseDatabase.getInstance().getReference().child("usersMessageId").child(sender).child(reciver).setValue(messagekey);

            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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