package com.stranger.chat.chat_modules.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.data.Chat_Tile_Data;

public class Chat_BottomSheet extends BottomSheetDialogFragment {
    Chat_Tile_Data model;

    public Chat_BottomSheet(Chat_Tile_Data model) {

        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_bottom_sheet, container, false);

        Button delete = view.findViewById(R.id.delete);

        delete.setOnClickListener(v1 -> {
            Toast.makeText(getActivity(), "Algorithm Shared", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        return view;
    }
}
