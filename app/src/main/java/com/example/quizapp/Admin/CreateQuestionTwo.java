package com.example.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp.Models.Questions;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateQuestionTwo extends AppCompatActivity {

    EditText questionBox, option1Box, option2Box, option3Box, option4Box, correctAnswerBox;
    Button createQuestionButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_two);

        Intent intent = getIntent();
        String gradeName = intent.getStringExtra("gradeName");
        String subjectName = intent.getStringExtra("subjectName");
        String thematicAreaName = intent.getStringExtra("thematicAreaName");

        questionBox = findViewById(R.id.questionBox);
        option1Box = findViewById(R.id.option1Box);
        option2Box = findViewById(R.id.option2Box);
        option3Box = findViewById(R.id.option3Box);
        option4Box = findViewById(R.id.option4Box);
        correctAnswerBox = findViewById(R.id.correctAnswerBox);
        createQuestionButton = findViewById(R.id.createQuestionButton);
        db = FirebaseFirestore.getInstance();

        createQuestionButton.setOnClickListener(v -> {
            String question = questionBox.getText().toString();
            String option1 = option1Box.getText().toString();
            String option2 = option2Box.getText().toString();
            String option3 = option3Box.getText().toString();
            String option4 = option4Box.getText().toString();
            String correctAnswer = correctAnswerBox.getText().toString();

            if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || correctAnswer.isEmpty()) {
                Toast.makeText(CreateQuestionTwo.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!correctAnswer.equals(option1) && !correctAnswer.equals(option2) && !correctAnswer.equals(option3) && !correctAnswer.equals(option4)) {
                Toast.makeText(CreateQuestionTwo.this, "Correct answer must be one of the options", Toast.LENGTH_SHORT).show();
                return;
            }

            Questions questionObject = new Questions(gradeName, subjectName, thematicAreaName, question, option1, option2, option3, option4, correctAnswer);
            db.collection("Questions").add(questionObject).addOnSuccessListener(documentReference -> {
                String questionId = documentReference.getId();
                db.collection("Questions").document(questionId).update("questionId", questionId).addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateQuestionTwo.this, "Question created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(CreateQuestionTwo.this, AdminPage.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(CreateQuestionTwo.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            }).addOnFailureListener(e -> {
                Toast.makeText(CreateQuestionTwo.this, "Error creating question", Toast.LENGTH_SHORT).show();
            });

        });
    }
}