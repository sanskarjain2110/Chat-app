package com.stranger.chat.chat_modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.bottom_sheet.ChatPage_BottomSheet;
import com.stranger.chat.chat_modules.data.MessagePage_Tile_Data;

public class ChatPageAdapter extends FirestoreRecyclerAdapter<MessagePage_Tile_Data, ChatPageAdapter.ReciverChatViewHolder> {
    CollectionReference reference;
    Context context;
    FragmentManager fragmentManager;

    public ChatPageAdapter(FirestoreRecyclerOptions<MessagePage_Tile_Data> options,
                           FragmentManager fragmentManager, Context context, CollectionReference reference) {
        super(options);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.reference = reference;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReciverChatViewHolder holder, int position, @NonNull MessagePage_Tile_Data model) {
        holder.getTile().setOnLongClickListener(v -> {
            ChatPage_BottomSheet bottomSheet = new ChatPage_BottomSheet(model, context, reference);
            bottomSheet.show(fragmentManager, "ModalBottomSheet");
            return true;
        });
        holder.getMessageTextView().setText(model.getMessage());
        holder.getTimeStamp().setText(model.getTimeStamp());

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ReciverChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_reciver_tile_sample, parent, false);
        return new ReciverChatViewHolder(view);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        notifyDataSetChanged();
    }

    static class ReciverChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout messsageArea;
        CardView tile;
        TextView messageTextView, timeStamp;

        public ReciverChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messsageArea = itemView.findViewById(R.id.messageArea);
            tile = itemView.findViewById(R.id.tile);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }

        public LinearLayout getMesssageArea() {
            return messsageArea;
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

    static class SenderChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout messsageArea;
        CardView tile;
        TextView messageTextView, timeStamp;

        public SenderChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messsageArea = itemView.findViewById(R.id.messageArea);
            tile = itemView.findViewById(R.id.tile);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }

        public LinearLayout getMesssageArea() {
            return messsageArea;
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
}