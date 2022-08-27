package com.stranger.chat.adappter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stranger.chat.R;
import com.stranger.chat.data.Userdata;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Status_ViewHolder> {
    final Userdata[] data;
    Context context;

    public StatusAdapter(Userdata[] data, Context context) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public StatusAdapter.Status_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_tile_sample, parent, false);

        return new StatusAdapter.Status_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.Status_ViewHolder holder, int position) {
//        holder.getProfilePic().setImageResource(data[position].profilePicId);
//        holder.getUsername().setText(data[position].username);

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
