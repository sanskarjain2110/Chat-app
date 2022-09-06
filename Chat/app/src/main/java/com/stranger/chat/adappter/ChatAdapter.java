package com.stranger.chat.adappter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stranger.chat.R;
import com.stranger.chat.data.Chat_Tile_Data;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Chat_ViewHolder> {
    final ArrayList<Chat_Tile_Data> data;

    Context context;

    public ChatAdapter(ArrayList<Chat_Tile_Data> data, Context context) {
        this.data = data;
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
    public void onBindViewHolder(@NonNull ChatAdapter.Chat_ViewHolder holder, int position) {
        String username = data.get(position).getUsername();


        holder.getUsername().setText(username);
        holder.getLastText().setText(data.get(position).getLastText());
        holder.getLastChatTime().setText(data.get(position).getLastSeen());

        holder.getChatTile().setOnClickListener(view -> {
            Toast.makeText(context, data.get(position).getMessageId(), Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(context, MessagePage.class);
//
//            Bundle sendData = new Bundle();
//            sendData.putString("username", username);
//            sendData.putString("messageId", data.get(position).getMessageId());
//            intent.putExtra("data", sendData);
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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
