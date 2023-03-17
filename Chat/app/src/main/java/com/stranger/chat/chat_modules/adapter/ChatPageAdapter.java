package com.stranger.chat.chat_modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.data.ChatPage_Tile_Data;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;
import com.stranger.chat.fuctionality.Helper;

public class ChatPageAdapter extends FirestoreRecyclerAdapter<ChatPage_Tile_Data, RecyclerView.ViewHolder> {
    private static final int
            SENDER_MESSAGE = R.layout.message_sender_tile_sample,
            RECEIVER_MESSAGE = R.layout.message_receiver_tile_sample,

    SENDER_AUDIO = R.layout.message_sender_tile_sample,
            RECEIVER_AUDIO = R.layout.message_receiver_tile_sample,

    SENDER_VIDEO = R.layout.message_sender_tile_sample,
            RECEIVER_VIDEO = R.layout.message_receiver_tile_sample;

    CollectionReference reference;
    Context context;
    FragmentManager fragmentManager;
    String currentUserId = FirebaseDatabaseConnection.currentUser.getUid();

    public ChatPageAdapter(FirestoreRecyclerOptions<ChatPage_Tile_Data> options,
                           FragmentManager fragmentManager, Context context, CollectionReference reference) {
        super(options);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.reference = reference;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSender().equals(currentUserId)) {
            switch (getItem(position).getType()) {
                case "text":
                case "image":
                    return SENDER_MESSAGE;
                case "audio":
                    return SENDER_AUDIO;
                case "video":
                    return SENDER_VIDEO;
            }
        } else {
            switch (getItem(position).getType()) {
                case "text":
                case "image":
                    return RECEIVER_MESSAGE;
                case "audio":
                    return RECEIVER_AUDIO;
                case "video":
                    return RECEIVER_VIDEO;
            }
        }
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SENDER_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(SENDER_MESSAGE, parent, false);
                return new SenderChatViewHolder(view);
            case RECEIVER_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(RECEIVER_MESSAGE, parent, false);
                return new ReceiverChatViewHolder(view);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,
                                    @NonNull ChatPage_Tile_Data model) {

        switch (holder.getItemViewType()) {
            case SENDER_MESSAGE:
                SenderChatViewHolder senderViewHolder = (SenderChatViewHolder) holder;

                if (model.getMessage() != null)
                    senderViewHolder.getMessageTextView().setText(model.getMessage());
                else senderViewHolder.getMessageTextView().setVisibility(View.GONE);

                senderViewHolder.getTimeStamp().setText(model.getTimeStamp());

                switch (model.getType()) {
                    case "text":
                        break;
                    case "image":
                        senderViewHolder.getImage().setVisibility(View.VISIBLE);
                        Picasso.get().load(model.getUri()).into(senderViewHolder.getImage());
                        break;
                    case "video":
                    case "audio":
                    default:
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }

                senderViewHolder.getTile().setOnLongClickListener(v -> popupMenu(senderViewHolder.getTile(), model));
                break;
            case RECEIVER_MESSAGE:
                ReceiverChatViewHolder receiverViewHolder = (ReceiverChatViewHolder) holder;

                if (model.getMessage() != null)
                    receiverViewHolder.getMessageTextView().setText(model.getMessage());
                else receiverViewHolder.getMessageTextView().setVisibility(View.GONE);

                receiverViewHolder.getTimeStamp().setText(model.getTimeStamp());

                switch (model.getType()) {
                    case "text":
                        break;
                    case "image":
                        receiverViewHolder.getImage().setVisibility(View.VISIBLE);
                        Picasso.get().load(model.getUri()).into(receiverViewHolder.getImage());
                        break;
                    case "video":
                    case "audio":
                    default:
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        notifyDataSetChanged();
    }

    static class ReceiverChatViewHolder extends RecyclerView.ViewHolder {
        CardView tile;
        ImageView image;
        TextView messageTextView, timeStamp;

        public ReceiverChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tile = itemView.findViewById(R.id.tile);
            image = itemView.findViewById(R.id.image);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }

        public CardView getTile() {
            return tile;
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getMessageTextView() {
            return messageTextView;
        }

        public TextView getTimeStamp() {
            return timeStamp;
        }

    }

    static class SenderChatViewHolder extends RecyclerView.ViewHolder {
        CardView tile;
        ImageView image;
        TextView messageTextView, timeStamp;

        public SenderChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tile = itemView.findViewById(R.id.tile);
            image = itemView.findViewById(R.id.image);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }

        public CardView getTile() {
            return tile;
        }

        public ImageView getImage() {
            return image;
        }


        public TextView getMessageTextView() {
            return messageTextView;
        }

        public TextView getTimeStamp() {
            return timeStamp;
        }
    }

    Boolean popupMenu(View view, ChatPage_Tile_Data model) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.chat_tile_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.reply) {
                Toast.makeText(context, "Not Implemented", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.forward) {
                Toast.makeText(context, "Not Implemented", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.copy) {
                Helper.copyString(context, model.getMessage());
            } else if (item.getItemId() == R.id.info) {
                Toast.makeText(context, "Not Implemented", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.delete) {
                reference.document(model.getDilogId()).delete();
            } else return false;
            return true;
        });
        popup.setForceShowIcon(true);
        popup.show();

        return true;
    }
}