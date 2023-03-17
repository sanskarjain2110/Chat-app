package com.stranger.chat.chat_modules.bottom_sheet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stranger.chat.R;

import java.util.Objects;

public class ChatPage_BottomSheet extends BottomSheetDialogFragment {
    Context context;
    TextView camera, gallery, contact;

    public ChatPage_BottomSheet(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_page_bottom_sheet, container, false);

        camera = view.findViewById(R.id.camera);
        gallery = view.findViewById(R.id.gallery);
        contact = view.findViewById(R.id.contact);

        camera.setOnClickListener(v ->
                dismiss());

        gallery.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            requireActivity().startActivityForResult(openGalleryIntent, 101);
            dismiss();
        });

        contact.setOnClickListener(v -> dismiss());
        return view;
    }
}
