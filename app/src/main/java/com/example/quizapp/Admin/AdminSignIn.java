package com.example.quizapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminSignIn extends AppCompatActivity {

    EditText email, password;
    Button signin;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait while we are signing you in");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(AdminSignIn.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                }
                else{
                    progressDialog.show();

                    db.collection("Admins").document(email.getText().toString()).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()){
                            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.dismiss();
                                    email.setText("");
                                    password.setText("");
                                    Intent intent = new Intent(AdminSignIn.this, AdminPage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AdminSignIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(AdminSignIn.this, "You are not an admin", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(AdminSignIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    });

                }
            }
        });
    }
}