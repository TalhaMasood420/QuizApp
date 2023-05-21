package com.example.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Models.ThematicArea;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CreateThematic extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText thematicNameBox;
    Button createThematicButton;
    Spinner gradeSpinner, subjectSpinner;
    FirebaseFirestore db;
    String selectedGrade, selectedSubject;
    String[] subjects;
    String[] grades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thematic);

        thematicNameBox = findViewById(R.id.thematicNameBox);
        createThematicButton = findViewById(R.id.createThematicButton);
        db = FirebaseFirestore.getInstance();

        db.collection("Grades").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                grades = new String[task.getResult().size()];
                int i = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    grades[i] = document.get("name").toString();
                    i++;
                }

                gradeSpinner = findViewById(R.id.gradeSpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateThematic.this, android.R.layout.simple_spinner_item, grades);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gradeSpinner.setAdapter(adapter);
                gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedGrade = "";
                        selectedGrade = parent.getItemAtPosition(position).toString();
                        ((TextView) view).setTextColor(Color.BLACK);
                        db.collection("Subjects").whereEqualTo("gradeName", selectedGrade).get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                subjects = new String[task1.getResult().size()];
                                int i = 0;
                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                    subjects[i] = document.get("subjectName").toString();
                                    i++;
                                }

                                subjectSpinner = findViewById(R.id.subjectSpinner);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateThematic.this, android.R.layout.simple_spinner_item, subjects);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subjectSpinner.setAdapter(adapter);
                                subjectSpinner.setOnItemSelectedListener(CreateThematic.this);
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        createThematicButton.setOnClickListener(v -> {
            if (subjects.length == 0 || grades.length == 0) {
                Toast.makeText(CreateThematic.this, "Insufficient Data", Toast.LENGTH_SHORT).show();
                return;
            }
            String thematicName = thematicNameBox.getText().toString();
            if (thematicName.isEmpty() || selectedGrade.isEmpty() || selectedSubject.isEmpty()) {
                Toast.makeText(CreateThematic.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            db.collection("ThematicAreas").add(new ThematicArea(thematicName, selectedGrade, selectedSubject)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateThematic.this, "Thematic area created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateThematic.this, "Error creating thematic area", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSubject = "";
        selectedSubject = parent.getItemAtPosition(position).toString();
        ((TextView) view).setTextColor(Color.BLACK);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
