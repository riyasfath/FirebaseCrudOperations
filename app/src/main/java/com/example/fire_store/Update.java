package com.example.fire_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {

    EditText newn,oldn;
    Button b1,delete;
    FirebaseFirestore fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        newn = findViewById(R.id.ed2u);
        oldn = findViewById(R.id.ed1u);
        b1 =findViewById(R.id.button);
        delete = findViewById(R.id.deleteb);

        fb = FirebaseFirestore.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldname= oldn.getText().toString().trim();
                String newname = newn.getText().toString().trim();
                oldn.setText("");
                newn.setText("");
                UpdateData(oldname,newname);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = oldn.getText().toString();
                DeleteData(name);
            }
        });

    }

    private void DeleteData(String name) {

        fb.collection("students").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();




                    fb.collection("students").document(documentID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Update.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Update.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    startActivity(new Intent(Update.this,Read_data.class));
                }
            }

        });
    }

    private void UpdateData(String oldname, String newname) {
        Map<String, Object> userdetails = new HashMap<>();
        userdetails.put("name", newname);

        fb.collection("students").whereEqualTo("name", oldname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();


                    fb.collection("students").document(documentID).update(userdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Update.this, "Data updated", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Update.this, "Failure in update", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(Update.this,Read_data.class));
                }
            }

        });
    }
}