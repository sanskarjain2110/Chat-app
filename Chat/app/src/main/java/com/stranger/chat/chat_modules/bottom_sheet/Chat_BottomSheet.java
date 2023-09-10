package com.stranger.chat.chat_modules.bottom_sheet;

import static com.stranger.chat.fuctionality.Keys.BUNDLE_DATA;
import static com.stranger.chat.fuctionality.Keys.firebase.PHONE_NUMBER;
import static com.stranger.chat.fuctionality.Keys.firebase.PROFILE_PIC_URL;
import static com.stranger.chat.fuctionality.Keys.firebase.USERNAME;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.stranger.chat.chat_modules.ChatPage;

public class Chat_BottomSheet extends BottomSheetDialogFragment {
    Bundle model;

    public Chat_BottomSheet(Bundle model) {
        this.model = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_bottom_sheet, container, false);

        ImageView profilePic = view.findViewById(R.id.profilePic);
        TextView username = view.findViewById(R.id.username);
        TextView phone_number = view.findViewById(R.id.phone_number);
        Button delete = view.findViewById(R.id.delete);
        FloatingActionButton message = view.findViewById(R.id.message);

        Picasso.get().load(model.getString(PROFILE_PIC_URL))
                .placeholder(R.drawable.account_circle_24px)
                .into(profilePic);
        profilePic.setOnClickListener(v -> dismiss());// open conversatiion setting activity

        username.setText(model.getString(USERNAME));
        phone_number.setText(model.getString(PHONE_NUMBER));

        message.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChatPage.class);
            intent.putExtra(BUNDLE_DATA, model);
            requireActivity().startActivity(intent);
            dismiss();
        });

        delete.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Algorithm Shared", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        return view;
    }
}
