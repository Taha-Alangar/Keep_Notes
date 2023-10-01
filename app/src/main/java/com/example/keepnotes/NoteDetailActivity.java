package com.example.keepnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTExtView;
    String title,content,docId;
    boolean isEditMode;

    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);

        pageTitleTExtView=findViewById(R.id.page_title);
        deleteNoteTextViewBtn=findViewById(R.id.delete_note_textview_btn);
        saveNoteBtn.setOnClickListener((v) -> saveNote());

        //recieve data

        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        titleEditText.setText(title);
        contentEditText.setText(content);

        if (docId !=null && !docId.isEmpty()){
            isEditMode=true;
        }

        if (isEditMode){
            pageTitleTExtView.setText("Edit your Notes");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }
        saveNoteBtn.setOnClickListener((v)-> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase());


    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }

        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){


        DocumentReference documentReference;
        if (isEditMode){
            //update the note
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        }else {
            //create new note
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is added
                    Toast.makeText(NoteDetailActivity.this, "Notes Added Sucessfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // failure occur

                    Toast.makeText(NoteDetailActivity.this, "Failed to add notes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        void deleteNoteFromFirebase() {
            DocumentReference documentReference;
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);

            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        //note is deleted
                        Toast.makeText(NoteDetailActivity.this, "Notes deleted Sucessfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        // failure occur

                        Toast.makeText(NoteDetailActivity.this, "Failed to delete notes", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
