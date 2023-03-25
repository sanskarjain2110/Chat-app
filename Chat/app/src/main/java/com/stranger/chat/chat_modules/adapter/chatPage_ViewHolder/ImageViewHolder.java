package com.stranger.chat.chat_modules.adapter.chatPage_ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.stranger.chat.R;

public class ImageViewHolder extends TextViewHolder {
    ImageView image;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
    }

    public ImageView getImage() {
        return image;
    }
}
