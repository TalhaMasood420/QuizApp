package com.example.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.Student.HomeScreen;
import com.example.quizapp.Student.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminPage extends AppCompatActivity {

    ImageView logout;
    TextView totalStudentsCount, totalRequestsCount;
    Button requestsButton, gradeButton, subjectButton, questionButton, thematicButton;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        logout = findViewById(R.id.logout);
        totalStudentsCount = findViewById(R.id.totalStudentsCount);
        totalRequestsCount = findViewById(R.id.totalRequestsCount);
        requestsButton = findViewById(R.id.requestsButton);
        gradeButton = findViewById(R.id.gradeButton);
        subjectButton = findViewById(R.id.subjectButton);
        questionButton = findViewById(R.id.questionButton);
        thematicButton = findViewById(R.id.thematicButton);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(AdminPage.this, AdminSignIn.class);
            startActivity(intent);
            finish();
        });

        getStats();

        requestsButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminPage.this, ApproveRequests.class));
        });

        gradeButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminPage.this, CreateGrade.class));
        });

        subjectButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminPage.this, CreateSubject.class));
        });

        questionButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminPage.this, CreateQuestion.class));
        });

        thematicButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminPage.this, CreateThematic.class));
        });


    }

    public void getStats(){
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                totalStudentsCount.setText(String.valueOf(task.getResult().size()));
            }
        });

        db.collection("Requests").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                totalRequestsCount.setText(String.valueOf(task.getResult().size()));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getStats();
    }

}
