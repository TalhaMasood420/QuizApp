package com.example.quizapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Models.Users;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText name, email, password, phoneNum;
    Button signup;
    FirebaseAuth mAuth;
    TextView signin;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNum = findViewById(R.id.phoneNum);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait while we create your account");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (name.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") || phoneNum.getText().toString().equals("")){
                    Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                }
                else{
                    String emailStr = email.getText().toString();
                    String passwordStr = password.getText().toString();
                    String nameStr = name.getText().toString();
                    String phoneNumStr = phoneNum.getText().toString();
                    mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String id = mAuth.getCurrentUser().getUid();
                                Users user = new Users(nameStr,emailStr,passwordStr,phoneNumStr);
                                user.setUserId(id);
                                db.collection("Users").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        db.collection("Grades").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    List<String> gradeNames = new ArrayList<>();
                                                    for (QueryDocumentSnapshot document : task.getResult()){
                                                        String grade = document.getString("name");
                                                        gradeNames.add(grade);
                                                    }
                                                    Map<String, String> userGrades = new HashMap<>();
                                                    for (int i = 0; i < gradeNames.size(); i++){
                                                        userGrades.put(gradeNames.get(i), "false");
                                                    }
                                                    db.collection("Users").document(id).update("grades", userGrades);
                                                    db.collection("Users").document(id).update("obtainedMarks", "0");
                                                    db.collection("Users").document(id).update("totalMarks", "0");

                                                }
                                            }
                                        });
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SignUp.this, HomeScreen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Error in creating account", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                    }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    });
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SignUp.this.finish();
            }
        });

    }

}