package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateNote extends AppCompatActivity {
    ImageView update;
    ImageView back;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText et_title;
    EditText et_note;
    EditText Updatetitle;
    EditText Updatenote;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        update=findViewById(R.id.Update);
        et_note=findViewById(R.id.note);
        et_title=findViewById(R.id.title);
        Updatetitle=findViewById(R.id.Updatetitle);
        Updatenote=findViewById(R.id.Updatenote);
    }

    public void back(View view) {
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void Update(View view) {
        String title=et_title.getText().toString();
        String note=et_note.getText().toString();
        String updatetitle=Updatetitle.getText().toString();
        String updatenote=Updatenote.getText().toString();

        Updatetitle.setText("");
        Updatenote.setText("");
      UpdateNote(updatetitle,updatenote,title);
    }

    @SuppressLint("NotConstructor")
    public void UpdateNote(String Updatetitle ,String Updatenote,String et_title){
        Map<String,Object> Note=new HashMap<>();
        Note.put("title",Updatetitle);
        Note.put("note",Updatenote);
        db.collection("Notes")
                .whereEqualTo("title",et_title)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documntID =documentSnapshot.getId();
                            db.collection("Notes").document(documntID).update(Note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "DocumentSnapshot Updated");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "DocumentSnapshot failed");

                                }
                            });
                        }
                    }
                });
    }
}