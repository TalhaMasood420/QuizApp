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

import com.example.quizapp.Admin.AdminSignIn;
import com.example.quizapp.Models.Users;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {

    EditText email, password;
    Button signin;
    TextView signup, adminLogin;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        adminLogin = findViewById(R.id.adminLogin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait while we are signing you in");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signup.setOnClickListener(v -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
        });

        adminLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignIn.this, AdminSignIn.class));
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(SignIn.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                }
                else{

                    progressDialog.show();
                    db.collection("Users").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.getDocuments().size() > 0){
                            // get all users
                            Users user = new Users();
                            for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++){
                                user = queryDocumentSnapshots.getDocuments().get(i).toObject(Users.class);
                                if (user.getEmail().equals(email.getText().toString())){
                                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            progressDialog.dismiss();
                                            email.setText("");
                                            password.setText("");
                                            startActivity(new Intent(SignIn.this, HomeScreen.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(SignIn.this, "User does not exist", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, "No user found", Toast.LENGTH_LONG).show();
                        }
                    });



                }
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SignIn.this.finish();
            }
        });

    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mAuth.getCurrentUser() != null){
//            Intent intent = new Intent(SignIn.this, HomeScreen.class);
//            startActivity(intent);
//        }
//    }
}