package com.stranger.chat.adappter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stranger.chat.R;

import java.text.MessageFormat;

public class MessagePageAdapter extends RecyclerView.Adapter<MessagePageAdapter.MessageViewHolder> {

    @NonNull
    @Override
    public MessagePageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_tile_sample, parent, false);

        return new MessagePageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagePageAdapter.MessageViewHolder holder, int position) {
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
