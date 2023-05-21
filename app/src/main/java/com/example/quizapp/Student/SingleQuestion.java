package com.example.quizapp.Student;

import static java.util.Map.of;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SingleQuestion extends AppCompatActivity {

    TextView questionBox;
    RadioButton answer1Box, answer2Box, answer3Box, answer4Box;
    Button submitButton;
    FirebaseFirestore db;
    String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_question);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        String option1 = intent.getStringExtra("option1");
        String option2 = intent.getStringExtra("option2");
        String option3 = intent.getStringExtra("option3");
        String option4 = intent.getStringExtra("option4");
        String correctAnswer = intent.getStringExtra("correctAnswer");
        questionId = intent.getStringExtra("questionId");

        questionBox = findViewById(R.id.questionBox);
        answer1Box = findViewById(R.id.answer1Box);
        answer2Box = findViewById(R.id.answer2Box);
        answer3Box = findViewById(R.id.answer3Box);
        answer4Box = findViewById(R.id.answer4Box);
        submitButton = findViewById(R.id.submitButton);
        db = FirebaseFirestore.getInstance();

        questionBox.setText(question);
        answer1Box.setText(option1);
        answer2Box.setText(option2);
        answer3Box.setText(option3);
        answer4Box.setText(option4);

        submitButton.setOnClickListener(v -> {
            if (answer1Box.isChecked()) {
                if (answer1Box.getText().toString().equals(correctAnswer)) {
                    correctSolve();
                } else {
                    wrongSolve();
                }
            } else if (answer2Box.isChecked()) {
                if (answer2Box.getText().toString().equals(correctAnswer)) {
                    correctSolve();
                } else {
                    wrongSolve();
                }
            } else if (answer3Box.isChecked()) {
                if (answer3Box.getText().toString().equals(correctAnswer)) {
                    correctSolve();
                } else {
                    wrongSolve();
                }
            } else if (answer4Box.isChecked()) {
                if (answer4Box.getText().toString().equals(correctAnswer)) {
                    correctSolve();
                } else {
                    wrongSolve();
                }
            }
        });
    }

    public void correctSolve() {
        Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String solvedId = questionId + userId;
        Map<String, Object> solved = new HashMap<>();
        solved.put("status", "solved");
        db.collection("Solved").document(solvedId).set(solved);
        db.collection("Users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String obtainedMarks = task.getResult().getString("obtainedMarks");
                String totalMarks = task.getResult().getString("totalMarks");
                int obtainedMarksInt = Integer.parseInt(obtainedMarks);
                int totalMarksInt = Integer.parseInt(totalMarks);
                obtainedMarksInt++;
                totalMarksInt++;
                db.collection("Users").document(userId).update("obtainedMarks", String.valueOf(obtainedMarksInt));
                db.collection("Users").document(userId).update("totalMarks", String.valueOf(totalMarksInt));
            }
        });

        finish();

    }

    public void wrongSolve() {
        Toast.makeText(this, "Wrong Answer, Try Again Later", Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        db.collection("Users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String totalMarks = task.getResult().getString("totalMarks");
                int totalMarksInt = Integer.parseInt(totalMarks);
                totalMarksInt++;
                db.collection("Users").document(userId).update("totalMarks", String.valueOf(totalMarksInt));
            }
        });
        finish();
    }
}