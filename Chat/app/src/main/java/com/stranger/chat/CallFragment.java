package com.stranger.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stranger.chat.adappter.CallAdapter;
import com.stranger.chat.data.Userdata;

public class CallFragment extends Fragment {
    RecyclerView callRecyclerView;
    Userdata[] data = {
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
            new Userdata(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, "Rohan", "20/07/2022, 07:30 a.m."),
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

