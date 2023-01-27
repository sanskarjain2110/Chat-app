package com.stranger.chat.chat_modules.bottom_sheet;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.data.MessagePage_Tile_Data;

public class MessagePage_BottomSheet extends BottomSheetDialogFragment {
    Context context;
    MessagePage_Tile_Data model;

    public MessagePage_BottomSheet(MessagePage_Tile_Data model, Context context) {
        this.context = context;
        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mesage_bottom_sheet, container, false);

        Button delete = view.findViewById(R.id.delete);
        Button copy = view.findViewById(R.id.copy);

        delete.setOnClickListener(v1 -> {
            Toast.makeText(getActivity(), "Algorithm Shared", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        copy.setOnClickListener(v12 -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label", model.getMessage());
            clipboardManager.setPrimaryClip(clipData);
            dismiss();
        });
        return view;
    }
}
