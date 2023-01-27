package com.stranger.chat.chat_modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.stranger.chat.chat_modules.bottom_sheet.MessagePage_BottomSheet;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.data.MessagePage_Tile_Data;

public class MessagePageAdapter extends FirestoreRecyclerAdapter<MessagePage_Tile_Data, MessagePageAdapter.MessageViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    public MessagePageAdapter(FirestoreRecyclerOptions<MessagePage_Tile_Data> options, FragmentManager fragmentManager, Context context) {
        super(options);
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessagePage_Tile_Data model) {
        holder.getUsername().setText(model.getSender());
        holder.getMessageArea().setText(model.getMessage());
        holder.getTimeStamp().setText(model.getTimeStamp());

        holder.getTile().setOnLongClickListener(v -> {
            MessagePage_BottomSheet bottomSheet = new MessagePage_BottomSheet(model,context);
            bottomSheet.show(fragmentManager, "ModalBottomSheet");
            return true;
        });
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_tile_sample, parent, false);
        return new MessagePageAdapter.MessageViewHolder(view);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout tile;
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

        public RelativeLayout getTile() {
            return tile;
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