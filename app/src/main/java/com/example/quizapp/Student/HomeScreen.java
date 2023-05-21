package com.example.quizapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizapp.Adapters.AllowedGradeAdapter;
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

public class HomeScreen extends AppCompatActivity {

    ImageView addIcon, logoutIcon;
    RecyclerView allowedClassesRecyclerViewMain;
    List<Grade> gradesList;
    AllowedGradeAdapter gradeAdapter;
    FirebaseFirestore db;
    TextView scoreBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        logoutIcon = findViewById(R.id.logoutIcon);
        addIcon = findViewById(R.id.addIcon);
        scoreBox = findViewById(R.id.scoreBox);
        allowedClassesRecyclerViewMain = findViewById(R.id.allowedClassesRecyclerViewMain);
        gradesList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        logoutIcon.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent = new Intent(HomeScreen.this, SignIn.class);
            startActivity(intent);
            finish();
        });
        addIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, AddGrade.class);
            startActivity(intent);
        });

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        allowedClassesRecyclerViewMain.setLayoutManager(layoutManager2);
        getData2();
        getScore();

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
                            if(entry.getValue().equals("true")){
                                gradesList.add(new Grade(entry.getKey()));
                            }
                        }
                        gradeAdapter = new AllowedGradeAdapter(gradesList, HomeScreen.this);
                        allowedClassesRecyclerViewMain.setAdapter(gradeAdapter);
                        gradeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void getScore(){
        // get obtainedMarks and totalMarks from Users collection
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Map<String, Object> data = document.getData();
                        int obtainedMarks = Integer.parseInt(data.get("obtainedMarks").toString());
                        int totalMarks = Integer.parseInt(data.get("totalMarks").toString());
                        int percentage = (obtainedMarks * 100) / totalMarks;
                        Log.d("percentage", String.valueOf(percentage));
                        String percentageStr = String.valueOf(percentage);
                        Log.d("percentageStr", percentageStr);
                        percentageStr = percentageStr + " % )";
                        String score = data.get("obtainedMarks").toString() + " / " + data.get("totalMarks").toString() + " ( " + percentageStr;
                        scoreBox.setText(score);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getScore();
    }
}