package com.example.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CreateQuestion extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button createThematicButton;
    Spinner gradeSpinner, subjectSpinner, thematicSpinner;
    FirebaseFirestore db;
    String selectedGrade, selectedSubject, selectedThematic;
    String[] subjects;
    String[] grades;
    String[] thematics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateQuestion.this, android.R.layout.simple_spinner_item, grades);
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

                                if (subjects.length == 0)
                                {
                                    Toast.makeText(CreateQuestion.this, "No subjects found for this grade", Toast.LENGTH_SHORT).show();
                                    subjectSpinner = findViewById(R.id.subjectSpinner);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateQuestion.this, android.R.layout.simple_spinner_item, new String[]{});
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    subjectSpinner.setAdapter(adapter);
                                    thematicSpinner = findViewById(R.id.thematicSpinner);
                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(CreateQuestion.this, android.R.layout.simple_spinner_item, new String[]{});
                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    thematicSpinner.setAdapter(adapter2);
                                    selectedSubject = "";
                                    selectedThematic = "";
                                }
                                else
                                {
                                    subjectSpinner = findViewById(R.id.subjectSpinner);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateQuestion.this, android.R.layout.simple_spinner_item, subjects);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    subjectSpinner.setAdapter(adapter);
                                    subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            selectedSubject = "";
                                            selectedSubject = parent.getItemAtPosition(position).toString();
                                            ((TextView) view).setTextColor(Color.BLACK);
                                            db.collection("ThematicAreas").whereEqualTo("subjectName", selectedSubject).get().addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    thematics = new String[task2.getResult().size()];
                                                    int i = 0;
                                                    for (QueryDocumentSnapshot document : task2.getResult()) {
                                                        thematics[i] = document.get("thematicAreaName").toString();
                                                        i++;
                                                    }

                                                    if (thematics.length == 0)
                                                    {
                                                        Toast.makeText(CreateQuestion.this, "No thematic areas found for this subject", Toast.LENGTH_SHORT).show();
                                                        thematicSpinner = findViewById(R.id.thematicSpinner);
                                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateQuestion.this, android.R.layout.simple_spinner_item, new String[]{});
                                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        thematicSpinner.setAdapter(adapter);
                                                        selectedThematic = "";
                                                    }
                                                    else
                                                    {
                                                        thematicSpinner = findViewById(R.id.thematicSpinner);
                                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateQuestion.this, android.R.layout.simple_spinner_item, thematics);
                                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        thematicSpinner.setAdapter(adapter);
                                                        thematicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                selectedThematic = "";
                                                                selectedThematic = parent.getItemAtPosition(position).toString();
                                                                ((TextView) view).setTextColor(Color.BLACK);
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {

                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }

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

            if (selectedGrade.equals("") || selectedSubject.equals("") || selectedThematic.equals(""))
            {
                Toast.makeText(CreateQuestion.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(CreateQuestion.this, CreateQuestionTwo.class);
                intent.putExtra("gradeName", selectedGrade);
                intent.putExtra("subjectName", selectedSubject);
                intent.putExtra("thematicAreaName", selectedThematic);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
