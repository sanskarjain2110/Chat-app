package com.stranger.chat.notes_modules.bottom_sheet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.stranger.chat.R;
import com.stranger.chat.fuctionality.Text;
import com.stranger.chat.notes_modules.NoteEditorActivity;
import com.stranger.chat.notes_modules.ToDos;

import java.util.HashMap;
import java.util.Map;

public class NotesCreate_BottomSheet extends BottomSheetDialogFragment {
    private final CollectionReference notesCollectionReference;
    Button createNoteButton, createListButton;

    LinearLayout todoNameEditor;
    EditText edit;
    Button done;

    Context context;

    public NotesCreate_BottomSheet(Context context, CollectionReference notesCollectionReference) {
        this.context = context;
        this.notesCollectionReference = notesCollectionReference;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_create_bottom_sheet, container, false);

        createNoteButton = view.findViewById(R.id.createNoteButton);
        createListButton = view.findViewById(R.id.createListButton);

        todoNameEditor = view.findViewById(R.id.todoNameEditor);
        edit = view.findViewById(R.id.edit);
        done = view.findViewById(R.id.done);

        createNoteButton.setOnClickListener(view1 -> {
            startActivity(new Intent(context, NoteEditorActivity.class));
            dismiss();
        });

        createListButton.setOnClickListener(view1 -> todoNameEditor.setVisibility(View.VISIBLE));

        done.setText(R.string.create_todos);
        done.setOnClickListener(view1 -> {
            String listName = edit.getText().toString().trim();
            if (!listName.equals("")) {
                String noteId = notesCollectionReference.document().getId();

                Map<String, Object> data = new HashMap<>();
                data.put("title", listName);
                data.put("noteId", noteId);
                data.put("lastUpdated", new Text(context).timeStamp());
                data.put("type", "notes");
                notesCollectionReference.document(noteId).set(data).addOnSuccessListener(unused -> {
                    Intent intent = new Intent(context, ToDos.class);
                    intent.putExtra("noteId", noteId);
                    startActivity(intent);

                    dismiss();
                });
            } else {
                Toast.makeText(context, "Set list name first", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}