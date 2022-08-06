package com.stranger.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;

public class MessagePage extends AppCompatActivity {

    RecyclerView messageFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        messageFrame = findViewById(R.id.mainScreen);
        MessagePageAdapter messagePageAdapter = new MessagePageAdapter();
        messageFrame.setAdapter(messagePageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        messageFrame.setLayoutManager(linearLayoutManager);

    }
}

class MessagePageAdapter extends RecyclerView.Adapter<MessagePageAdapter.MessageViewHolder> {

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_tile_sample, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.getMessageArea().setText(MessageFormat.format("Sample {0}", position));
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        CardView tile;
        ImageView profilePic;
        TextView messageArea, timeStamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            tile = itemView.findViewById(R.id.tile);

            profilePic = itemView.findViewById(R.id.profilePic);

            messageArea = itemView.findViewById(R.id.messageArea);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }

        public CardView getTile() {
            return tile;
        }

        public ImageView getProfilePic() {
            return profilePic;
        }

        public TextView getMessageArea() {
            return messageArea;
        }

        public TextView getTimeStamp() {
            return timeStamp;
        }
    }
}