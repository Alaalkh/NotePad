package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNote extends AppCompatActivity {
    ImageView add;
    ImageView back;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText et_title;
    EditText et_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        add=findViewById(R.id.add);
        et_note=findViewById(R.id.note);
        et_title=findViewById(R.id.title);
    }

    public void back(View view) {
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void addtoFirebase(View view) {
        String title=et_title.getText().toString();
        String note=et_note.getText().toString();

        if (!title.isEmpty() && !note.isEmpty()){


            Map<String, Object> user = new HashMap<>();
            user.put("title", title.toString());
            user.put("note", note.toString());


// Add a new document with a generated ID
            db.collection("Notes")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(AddNote.this, " Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });
        }else{
            Toast.makeText(this, "Please fill the filed", Toast.LENGTH_SHORT).show();
        }
    }

}
