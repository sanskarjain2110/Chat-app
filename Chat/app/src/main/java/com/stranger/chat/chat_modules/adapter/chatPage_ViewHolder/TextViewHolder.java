package com.stranger.chat.chat_modules.adapter.chatPage_ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stranger.chat.R;

public class TextViewHolder extends RecyclerView.ViewHolder {
    CardView tile;
    TextView messageTextView, timeStamp;

    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        tile = itemView.findViewById(R.id.tile);
        messageTextView = itemView.findViewById(R.id.messageTextView);
        timeStamp = itemView.findViewById(R.id.timeStamp);
    }

    public CardView getTile() {
        return tile;
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public TextView getTimeStamp() {
        return timeStamp;
    }
}
