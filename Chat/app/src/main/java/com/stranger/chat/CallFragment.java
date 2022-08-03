package com.stranger.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stranger.chat.data.Call_Tile_Data;


public class CallFragment extends Fragment {
    RecyclerView callRecyclerView;
    Call_Tile_Data[] data = {
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Call_Tile_Data(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m.")
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_call, container, false);
        callRecyclerView = view.findViewById(R.id.callRecyclerView);
        CallAdapter callAdapter = new CallAdapter(data);
        callRecyclerView.setAdapter(callAdapter);
        callRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}

class CallAdapter extends RecyclerView.Adapter<CallAdapter.Call_ViewHolder> {
    Call_Tile_Data[] data;

    public CallAdapter(Call_Tile_Data[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Call_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_tile_sample, parent, false);

        return new CallAdapter.Call_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Call_ViewHolder holder, int position) {
        holder.getProfilePic().setImageResource(data[position].profilePicId);
        holder.getCallAction().setImageResource(data[position].callActionId);
        holder.getCallType().setImageResource(data[position].callTypeId);

        holder.getUsername().setText(data[position].username);
        holder.getCallTime().setText(data[position].callTime);
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