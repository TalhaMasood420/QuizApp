package com.example.quizapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quizapp.Adapters.RequestableGradeAdapter;
import com.example.quizapp.Models.Grade;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddGrade extends AppCompatActivity {

    RecyclerView requestableClassesRecyclerViewMain;
    List<Grade> gradesList;
    RequestableGradeAdapter gradeAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        db = FirebaseFirestore.getInstance();
        requestableClassesRecyclerViewMain = findViewById(R.id.requestableClassesRecyclerViewMain);
        gradesList = new ArrayList<>();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        requestableClassesRecyclerViewMain.setLayoutManager(layoutManager2);
        getData2();


    }

    public void getData2(){

        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Map<String, Object> data = document.getData();
                        Map<String, Object> grades = (Map<String, Object>) data.get("grades");
                        for (Map.Entry<String, Object> entry : grades.entrySet()) {
                            if(entry.getValue().equals("false")){
                                gradesList.add(new Grade(entry.getKey()));
                            }
                        }
                        gradeAdapter = new RequestableGradeAdapter(gradesList, AddGrade.this);
                        requestableClassesRecyclerViewMain.setAdapter(gradeAdapter);
                        gradeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}