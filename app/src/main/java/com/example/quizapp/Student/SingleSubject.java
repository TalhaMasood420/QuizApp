package com.example.quizapp.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.quizapp.Adapters.SubjectAdapter;
import com.example.quizapp.Adapters.ThematicAdapter;
import com.example.quizapp.Models.Subject;
import com.example.quizapp.Models.ThematicArea;
import com.example.quizapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SingleSubject extends AppCompatActivity {

    RecyclerView thematicsRecyclerView;
    TextView subjectHeadingText;
    List<ThematicArea> thematicsList;
    ThematicAdapter thematicAdapter;
    FirebaseFirestore db;
    String gradeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_subject);

        Intent intent = getIntent();
        String subjectName = intent.getStringExtra("subjectName");
        gradeName = intent.getStringExtra("gradeName");
        subjectHeadingText = findViewById(R.id.subjectHeadingText);
        subjectHeadingText.setText(subjectName);
        thematicsRecyclerView = findViewById(R.id.thematicsRecyclerView);
        thematicsList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        thematicsRecyclerView.setLayoutManager(layoutManager2);
        getData2();
    }

    public void getData2(){

        db.collection("ThematicAreas").whereEqualTo("gradeName", gradeName).whereEqualTo("subjectName", subjectHeadingText.getText().toString()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    ThematicArea thematics = new ThematicArea(document.get("thematicAreaName").toString(), document.get("gradeName").toString(), document.get("subjectName").toString());
                    thematicsList.add(thematics);
                }
                thematicAdapter = new ThematicAdapter(thematicsList, this);
                thematicsRecyclerView.setAdapter(thematicAdapter);
                thematicAdapter.notifyDataSetChanged();
            }
        });
    }
}