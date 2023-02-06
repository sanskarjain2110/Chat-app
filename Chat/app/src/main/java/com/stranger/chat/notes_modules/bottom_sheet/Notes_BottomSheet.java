package com.stranger.chat.notes_modules.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.stranger.chat.R;
import com.stranger.chat.notes_modules.data.Note_Tile_Data;

public class Notes_BottomSheet extends BottomSheetDialogFragment {
    CollectionReference notesColleectionReferance;
    Note_Tile_Data model;

    public Notes_BottomSheet(Note_Tile_Data model, CollectionReference notesCollectionReferance) {
        this.model = model;
        this.notesColleectionReferance = notesCollectionReferance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_bottom_sheet, container, false);

        Button delete = view.findViewById(R.id.delete),
                edit = view.findViewById(R.id.edit);

        delete.setOnClickListener(v1 -> notesColleectionReferance.document(model.getNoteId()).delete().addOnSuccessListener(v12 -> dismiss()));

        edit.setOnClickListener(v -> dismiss());
        return view;
    }
}
