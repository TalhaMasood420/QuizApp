package com.example.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp.Models.Grade;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateGrade extends AppCompatActivity {

    EditText gradeNameBox;
    Button createGradeButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grade);

        gradeNameBox = findViewById(R.id.gradeNameBox);
        createGradeButton = findViewById(R.id.createGradeButton);
        db = FirebaseFirestore.getInstance();

        createGradeButton.setOnClickListener(v -> {
            String gradeName = gradeNameBox.getText().toString();
            if (gradeName.isEmpty()) {
                Toast.makeText(CreateGrade.this, "Please enter a grade name", Toast.LENGTH_SHORT).show();
                return;
            }
            db.collection("Grades").whereEqualTo("name", gradeName).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.isEmpty()) {
                    Grade grade = new Grade(gradeName);
                    db.collection("Grades").add(grade).addOnSuccessListener(aVoid -> {
                        Toast.makeText(CreateGrade.this, "Grade created successfully", Toast.LENGTH_SHORT).show();
                        gradeNameBox.setText("");
                    }).addOnFailureListener(e -> Toast.makeText(CreateGrade.this, "Error creating grade", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(CreateGrade.this, "Grade already exists", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(CreateGrade.this, "Error creating grade", Toast.LENGTH_SHORT).show());
        });




    }
}