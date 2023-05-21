package com.example.quizapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.quizapp.Adapters.AllowedGradeAdapter;
import com.example.quizapp.Adapters.SubjectAdapter;
import com.example.quizapp.Models.Grade;
import com.example.quizapp.Models.Subject;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleGrade extends AppCompatActivity {

    RecyclerView subjectsRecyclerView;
    TextView gradeHeadingText;
    List<Subject> subjectsList;
    SubjectAdapter subjectAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_grade);

        Intent intent = getIntent();
        String gradeName = intent.getStringExtra("gradeName");
        gradeHeadingText = findViewById(R.id.gradeHeadingText);
        gradeHeadingText.setText(gradeName);
        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        subjectsList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        subjectsRecyclerView.setLayoutManager(layoutManager2);
        getData2();
    }

    public void getData2(){

        db.collection("Subjects").whereEqualTo("gradeName", gradeHeadingText.getText().toString()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Subject subject = new Subject(document.get("subjectName").toString(), document.get("gradeName").toString());
                    subjectsList.add(subject);
                }
                subjectAdapter = new SubjectAdapter(subjectsList, this);
                subjectsRecyclerView.setAdapter(subjectAdapter);
                subjectAdapter.notifyDataSetChanged();
            }
        });
    }
}
