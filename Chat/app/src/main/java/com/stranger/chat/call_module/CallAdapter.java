package com.stranger.chat.call_module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stranger.chat.R;
import com.stranger.chat.call_module.data.Userdata;


public class CallAdapter extends RecyclerView.Adapter<CallAdapter.Call_ViewHolder> {
    Userdata[] data;

    public CallAdapter(Userdata[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CallAdapter.Call_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_tile_sample, parent, false);

        return new CallAdapter.Call_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.Call_ViewHolder holder, int position) {
        holder.getProfilePic().setImageResource(data[position].getProfilePicId());
        holder.getCallAction().setImageResource(data[position].getCallActionId());
        holder.getCallType().setImageResource(data[position].getCallTypeId());

        holder.getUsername().setText(data[position].getLastText());
        holder.getCallTime().setText(data[position].getCallTime());
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class Call_ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic, callAction, callType;
        TextView username, callTime;

        public Call_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            callAction = itemView.findViewById(R.id.callAction);
            callType = itemView.findViewById(R.id.callType);

            username = itemView.findViewById(R.id.username);
            callTime = itemView.findViewById(R.id.callTime);
        }

        public ImageView getProfilePic() {
            return profilePic;
        }

        public ImageView getCallAction() {
            return callAction;
        }

        public ImageView getCallType() {
            return callType;
        }

        public TextView getUsername() {
            return username;
        }

        public TextView getCallTime() {
            return callTime;
        }
    }
}
