package com.example.quizapp.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.quizapp.Adapters.QuestionAdapter;
import com.example.quizapp.Adapters.ThematicAdapter;
import com.example.quizapp.Models.Questions;
import com.example.quizapp.Models.ThematicArea;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SingleThematic extends AppCompatActivity {
    RecyclerView questionsRecyclerView;
    TextView thematicHeadingText;
    List<Questions> questionsList;
    QuestionAdapter questionAdapter;
    FirebaseFirestore db;
    String gradeName;
    String subjectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_thematic);

        Intent intent = getIntent();
        gradeName = intent.getStringExtra("gradeName");
        subjectName = intent.getStringExtra("subjectName");
        String thematicArea = intent.getStringExtra("thematicArea");
        thematicHeadingText = findViewById(R.id.thematicHeadingText);
        thematicHeadingText.setText(thematicArea);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        questionsList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        questionsRecyclerView.setLayoutManager(layoutManager2);
        getData2();
    }

    public void getData2(){
        db.collection("Questions").whereEqualTo("gradeName", gradeName).whereEqualTo("subjectName", subjectName).whereEqualTo("thematicAreaName", thematicHeadingText.getText().toString()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Questions questions = new Questions(document.get("gradeName").toString(), document.get("subjectName").toString(), document.get("thematicAreaName").toString(), document.get("question").toString(), document.get("option1").toString(), document.get("option2").toString(), document.get("option3").toString(), document.get("option4").toString(), document.get("correctAnswer").toString());
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String userId = auth.getCurrentUser().getUid();
                    questions.setQuestionId(document.getId());
                    String solvedId = document.get("questionId").toString() + userId;
                    db.collection("Solved").document(solvedId).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot document1 = task1.getResult();
                            if (!document1.exists()) {
                                questionsList.add(questions);
                                questionAdapter = new QuestionAdapter(questionsList, SingleThematic.this);
                                questionsRecyclerView.setAdapter(questionAdapter);
                            }
                        }
                    });

                }
                questionAdapter = new QuestionAdapter(questionsList, this);
                questionsRecyclerView.setAdapter(questionAdapter);
                questionAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        // clear array
        questionsList.clear();
        // get data again
        getData2();
    }







}