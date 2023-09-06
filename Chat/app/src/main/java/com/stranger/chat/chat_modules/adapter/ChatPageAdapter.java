package com.stranger.chat.chat_modules.adapter;

import static com.stranger.chat.fuctionality.Text.getFormattedTimeDifference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.stranger.chat.chat_modules.adapter.chatPage_ViewHolder.ImageViewHolder;
import com.stranger.chat.chat_modules.adapter.chatPage_ViewHolder.TextViewHolder;
import com.stranger.chat.chat_modules.data.ChatPage_Tile_Data;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;
import com.stranger.chat.fuctionality.Text;

public class ChatPageAdapter extends FirestoreRecyclerAdapter<ChatPage_Tile_Data, RecyclerView.ViewHolder> {
    private static final int
            SENDER_TEXT = R.layout.message_sender_text_tile_sample,
            RECEIVER_TEXT = R.layout.message_receiver_text_tile_sample,
            SENDER_IMAGE = R.layout.message_sender_image_tile_sample,   // image
            RECEIVER_IMAGE = R.layout.message_receiver_image_tile_sample,

    SENDER_AUDIO = R.layout.message_sender_text_tile_sample,
            RECEIVER_AUDIO = R.layout.message_receiver_text_tile_sample,

    SENDER_VIDEO = R.layout.message_sender_text_tile_sample,
            RECEIVER_VIDEO = R.layout.message_receiver_text_tile_sample;

    private final CollectionReference reference;
    private final Context context;
    private final String currentUserId = FirebaseDatabaseConnection.currentUser.getUid();

    public ChatPageAdapter(FirestoreRecyclerOptions<ChatPage_Tile_Data> options,
                           Context context, CollectionReference reference) {
        super(options);
        this.context = context;
        this.reference = reference;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSender().equals(currentUserId)) {  // if sender
            if (getItem(position).getType().equals("text")) return SENDER_TEXT;
            else if (getItem(position).getType().equals("image")) return SENDER_IMAGE;
            else if (getItem(position).getType().equals("audio")) return SENDER_AUDIO;
            else return SENDER_VIDEO;
        } else {    // if reciver
            if (getItem(position).getType().equals("text")) return RECEIVER_TEXT;
            else if (getItem(position).getType().equals("image")) return RECEIVER_IMAGE;
            else if (getItem(position).getType().equals("audio")) return RECEIVER_AUDIO;
            else return RECEIVER_VIDEO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == SENDER_TEXT || viewType == RECEIVER_TEXT) return new TextViewHolder(view);
        else if (viewType == SENDER_IMAGE || viewType == RECEIVER_IMAGE)
            return new ImageViewHolder(view);
        else return new TextViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,
                                    @NonNull ChatPage_Tile_Data model) {

        switch (holder.getItemViewType()) {
            case SENDER_TEXT:
            case RECEIVER_TEXT:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                holderFunctionality(textViewHolder.getMessageTextView(), textViewHolder.getTimeStamp(), textViewHolder.getTile(), model);
                break;
            case SENDER_IMAGE:
            case RECEIVER_IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                holderFunctionality(imageViewHolder.getMessageTextView(), imageViewHolder.getTimeStamp(), imageViewHolder.getTile(), model);
                Picasso.get().load(model.getUri()).placeholder(R.drawable.image_48px).into(imageViewHolder.getImage());
                break;
            default:
                break;
        }
    }

    // menu
    Boolean popupMenu(View view, ChatPage_Tile_Data model) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.chat_page_tile_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.reply) {
                Toast.makeText(context, "Not Implemented", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.forward) {
                Toast.makeText(context, "Not Implemented", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.copy) {
                new Text(context).copyString(model.getMessage());
            } else if (item.getItemId() == R.id.info) {
                Toast.makeText(context, "Not Implemented", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.delete)
                reference.document(model.getDilogId()).delete();
            else return false;
            return true;
        });
        popup.setForceShowIcon(true);
        popup.show();

        return true;
    }

    void holderFunctionality(TextView messageView, TextView timeStampView, CardView cardView, @NonNull ChatPage_Tile_Data model) {
        // message
        if (model.getMessage() != null)
            messageView.setText(model.getMessage());
        else messageView.setVisibility(View.GONE);

        // time stamp
        String time = getFormattedTimeDifference(model.getTimeStamp());
        if (time != null) timeStampView.setText(time);
        else timeStampView.setVisibility(View.GONE);

        // menu
        if (model.getSender().equals(currentUserId))
            cardView.setOnLongClickListener(v -> popupMenu(cardView, model));
    }
}

