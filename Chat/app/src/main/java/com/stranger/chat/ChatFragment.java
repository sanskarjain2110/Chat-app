package com.stranger.chat;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChatFragment extends Fragment {
    RecyclerView chatRecyclerView, statusRecyclerView;
    Status_Grid_Data[] status_data = {
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
            new Status_Grid_Data(R.drawable.ic_launcher_background, "Rohan Sharma"),
    };

    Chat_Tile_Data[] chat_data = {
            new Chat_Tile_Data(R.drawable.img, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
            new Chat_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan Sharma", "hi this is rohan", "16/07/2022"),
    };

    public ChatFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        statusRecyclerView = view.findViewById(R.id.statusRecyclerView);
        StatusAdapter statusAdapter = new StatusAdapter(status_data, getContext());
        statusRecyclerView.setAdapter(statusAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        statusRecyclerView.setLayoutManager(gridLayoutManager);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        ChatAdapter chatAdapter = new ChatAdapter(chat_data, getContext());
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}

class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Status_ViewHolder> {
    final Status_Grid_Data[] data;
    Context context;

    public StatusAdapter(Status_Grid_Data[] data, Context context) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public Status_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_tile_sample, parent, false);

        return new StatusAdapter.Status_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Status_ViewHolder holder, int position) {
        holder.getProfilePic().setImageResource(data[position].profilePicId);
        holder.getUsername().setText(data[position].username);

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class Status_ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView username;

        public Status_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            username = itemView.findViewById(R.id.username);

        }

        public ImageView getProfilePic() {
            return profilePic;
        }

        public TextView getUsername() {
            return username;
        }
    }
}

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Chat_ViewHolder> {
    final Chat_Tile_Data[] data;

    Context context;

    public ChatAdapter(Chat_Tile_Data[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public Chat_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_tile_sample, parent, false);

        return new Chat_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_ViewHolder holder, int position) {
        holder.getProfilePic().setImageResource(data[position].profilePicId);
        holder.getSeenStatus().setImageResource(data[position].seenStatusId);
        holder.getUsername().setText(data[position].username);
        holder.getLastText().setText(data[position].lastText);
        holder.getLastChatTime().setText(data[position].lastChatTime);

        holder.getChatTile().setOnClickListener(view -> {
            Intent intent = new Intent(context, ResisterPage.class);
            intent.putExtra("data_pos", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class Chat_ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic, seenStatus;
        TextView username, lastText, lastChatTime;
        LinearLayout chatTile;

        public Chat_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            seenStatus = itemView.findViewById(R.id.seenStatus);
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

        public ImageView getSeenStatus() {
            return seenStatus;
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
