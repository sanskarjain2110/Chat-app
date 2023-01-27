package com.stranger.chat.notes_modules.bottom_sheet;

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
import com.stranger.chat.notes_modules.data.Note_Tile_Data;

public class Notes_BottomSheet extends BottomSheetDialogFragment {
    Note_Tile_Data model;

    public Notes_BottomSheet(Note_Tile_Data model) {

        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_bottom_sheet, container, false);

        Button delete = view.findViewById(R.id.delete);

        delete.setOnClickListener(v1 -> {
            Toast.makeText(getActivity(), "Algorithm Shared", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        return view;
    }
}
