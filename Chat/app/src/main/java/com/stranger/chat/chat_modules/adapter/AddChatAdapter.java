package com.stranger.chat.chat_modules.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.MessagePage;
import com.stranger.chat.chat_modules.data.AddChat_Tile_Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddChatAdapter extends FirestoreRecyclerAdapter<AddChat_Tile_Data, AddChatAdapter.AddChat_ViewHolder> {

    Activity activity;
    Context context;

//    static String messageId = null;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    public AddChatAdapter(FirestoreRecyclerOptions<AddChat_Tile_Data> options, Activity activity, Context context) {
        super(options);
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public AddChat_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_chat_tile_sample, parent, false);

        return new AddChatAdapter.AddChat_ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onBindViewHolder(@NonNull AddChat_ViewHolder holder, int position, @NonNull AddChat_Tile_Data model) {
        String reciver = model.getUsername(),
                reciverId = model.getUserId(),
                phoneNumber = model.getPhoneNumber(),
                profilePicUri = model.getProfilePic(),
                senderId = user.getUid();

        holder.getUsernameField().setText(reciver);
        holder.getPhoneNumberField().setText(phoneNumber);

        if (profilePicUri != null) {
            // download image from fireStorage
            Picasso.get().load(profilePicUri).into(holder.getProfilePicField());
        }

        holder.getAddChatTile().setOnClickListener(view -> {
            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Creating...");
            progressDialog.show();

            // array of reciverId and senderId
            ArrayList<String> usersId = new ArrayList<>();
            usersId.add(reciverId);// reciverId
            usersId.add(senderId);// senderId

            progressDialog.setMessage("Checking History...");
            // checking messageId document Exist
            FirebaseFirestore.getInstance().collection("userMessageId").document(senderId).addSnapshotListener((snapshot, error) -> {
                if (error != null) {
                    return;
                }
                if (snapshot != null && snapshot.get(reciverId) != null) {
                    String messageId = (String) snapshot.get(reciverId);

                    Bundle sendData = new Bundle();
                    sendData.putString("reciversUserId", reciverId);
                    sendData.putString("messageId", messageId);
                    sendData.putString("reciversname", reciver);

                    Intent intent = new Intent(context, MessagePage.class);
                    intent.putExtra("data", sendData);
                    activity.startActivity(intent);

                } else {
                    progressDialog.setMessage("creating chanal...\nplease wait");
                    // messageId collection not exist so create new collection
                    FirebaseFirestore.getInstance().collection("messages").document().addSnapshotListener((snapshot1, error1) -> {
                        if (error1 != null) {
                            return;
                        }
                        if (snapshot1 != null) {
                            String messagekey = snapshot1.getId();
                            Map<String, Object> roots = new HashMap<>();
                            roots.put("messageId", messagekey);
                            roots.put("lastText", " ");
                            roots.put("lastSeen", " ");
                            roots.put("usersId", usersId);

                            // inserting document with new messageId in message collection
                            FirebaseFirestore.getInstance().collection("messages").document(messagekey).set(roots);

                            // adding data to userMessageId collection
                            Map<String, Object> senderEnd = new HashMap<>();
                            senderEnd.put(senderId, messagekey);
                            FirebaseFirestore.getInstance()
                                    .collection("userMessageId")
                                    .document(reciverId)
                                    .set(senderEnd, SetOptions.merge());

                            Map<String, Object> reciverEnd = new HashMap<>();
                            reciverEnd.put(reciverId, messagekey);
                            FirebaseFirestore.getInstance()
                                    .collection("userMessageId")
                                    .document(senderId)
                                    .set(reciverEnd, SetOptions.merge());
                        }
                    });
                }
                progressDialog.dismiss();
                activity.finish();
            });
        });
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