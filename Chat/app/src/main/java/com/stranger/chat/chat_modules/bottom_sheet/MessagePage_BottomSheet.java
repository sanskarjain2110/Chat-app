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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.data.MessagePage_Tile_Data;

public class MessagePage_BottomSheet extends BottomSheetDialogFragment {
    Context context;
    MessagePage_Tile_Data model;
    CollectionReference reference;

    TextView username, timeStamp, message;
    Button delete, copy;

    public MessagePage_BottomSheet(MessagePage_Tile_Data model, Context context, CollectionReference reference) {
        this.context = context;
        this.model = model;
        this.reference = reference;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mesage_bottom_sheet, container, false);

        username = view.findViewById(R.id.username);
        timeStamp = view.findViewById(R.id.timeStamp);
        message = view.findViewById(R.id.messageTextView);

        delete = view.findViewById(R.id.delete);
        copy = view.findViewById(R.id.copy);

        username.setText(model.getSender());
        message.setText(model.getMessage());
        timeStamp.setText(model.getTimeStamp());

        delete.setOnClickListener(v -> reference.document(model.getDilogId()).delete().addOnSuccessListener(unused -> {
            Toast.makeText(context, "message deleted", Toast.LENGTH_SHORT).show();
            dismiss();
        }));

        copy.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label", model.getMessage());
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        return view;
    }
}
