package com.example.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Models.Subject;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CreateSubject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText subjectNameBox;
    Button createSubjectButton;
    Spinner gradeSpinner;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        subjectNameBox = findViewById(R.id.subjectNameBox);
        createSubjectButton = findViewById(R.id.createSubjectButton);
        db = FirebaseFirestore.getInstance();

        db.collection("Grades").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String[] grades = new String[task.getResult().size()];
                int i = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    grades[i] = document.get("name").toString();
                    i++;
                }

                gradeSpinner = findViewById(R.id.gradeSpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateSubject.this, android.R.layout.simple_spinner_item, grades);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gradeSpinner.setAdapter(adapter);
                gradeSpinner.setOnItemSelectedListener(CreateSubject.this);


            }
        });

        createSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subjectNameBox.getText().toString().equals("")){
                    Toast.makeText(CreateSubject.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                }
                else{
                    db.collection("Subjects").whereEqualTo("subjectName", subjectNameBox.getText().toString()).whereEqualTo("gradeName", gradeSpinner.getSelectedItem().toString()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Subject subject = new Subject(subjectNameBox.getText().toString(), gradeSpinner.getSelectedItem().toString());
                            db.collection("Subjects").add(subject).addOnSuccessListener(aVoid -> {
                                Toast.makeText(CreateSubject.this, "Subject created successfully", Toast.LENGTH_SHORT).show();
                                subjectNameBox.setText("");
                            }).addOnFailureListener(e -> Toast.makeText(CreateSubject.this, "Error creating subject", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(CreateSubject.this, "Subject already exists", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(CreateSubject.this, "Error creating subject", Toast.LENGTH_SHORT).show());
                }
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(Color.BLACK);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}