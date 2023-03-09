package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notepad.Adapters.NoteAdapter;
import com.example.notepad.Models.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements NoteAdapter.ItemClickListener, NoteAdapter.ItemClickListener2{
   ImageView add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Note> items;
    NoteAdapter[] myListData;
    NoteAdapter adapter;
    EditText Updatetitle;
    EditText Updatenote;
    GridLayoutManager layoutManager = new GridLayoutManager(this,2);
    RecyclerView rv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Updatetitle=findViewById(R.id.Updatetitle);
        Updatenote=findViewById(R.id.Updatenote);
        add=findViewById(R.id.addnote);
        rv = findViewById(R.id.recyclr);
        items = new ArrayList<Note>();
        adapter =new NoteAdapter(this,items,this,this);
        getNote();
    }
    public  void getNote(){
        db.collection("Notes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("drn", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    String id = documentSnapshot.getId();
                                    String title = documentSnapshot.getString("title");
                                    String Note = documentSnapshot.getString("note");

                                    Note note = new Note(id, title ,Note);
                                    items.add(note);

                                    rv.setLayoutManager(layoutManager);
                                    rv.setHasFixedSize(true);
                                    rv.setAdapter(adapter);
                                    ;
                                    adapter.notifyDataSetChanged();
                                    Log.e("LogDATA", items.toString());

                                }
                            }
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LogDATA", "get failed with ");


                    }
                });
    }


    public void addnote(View view) {
        Intent intent= new Intent(this,AddNote.class);
        startActivity(intent);
    }

    public  void Delete(final Note note){
        db.collection("Notes").document(note.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        items.remove(note);
                        Toast.makeText(MainActivity.this, " Removed Successfully", Toast.LENGTH_SHORT).show();

                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("logData","get failed with delete");
                    }
                });
    }

    public void updateNote(final Note note) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name");
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_update_note, null);
        builder.setView(customLayout);
        builder.setPositiveButton(
                "Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Updatetitle = customLayout.findViewById(R.id.Updatetitle);
                        Updatenote = customLayout.findViewById(R.id.Updatenote);

                        db.collection("Notes").document(note.getId()).update("title", Updatetitle.getText().toString(),"note",Updatenote.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("dareen", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("dareen", "Error updating document", e);
                                    }
                                });
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void onItemClick(int position, String id) {
        Delete(items.get(position));
    }

    @Override
    public void onItemClick2(int position, String id) {
        updateNote(items.get(position));
    }
}