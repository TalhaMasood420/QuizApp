package com.example.quizapp.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quizapp.Adapters.RequestsAdapter;
import com.example.quizapp.Models.Requests;
import com.example.quizapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ApproveRequests extends AppCompatActivity {


    RecyclerView pendingrequestsrecyclerview;
    List<Requests> requestsList;
    RequestsAdapter requestsAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        pendingrequestsrecyclerview = findViewById(R.id.pendingrequestsrecyclerview);
        requestsList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pendingrequestsrecyclerview.setLayoutManager(layoutManager2);
        getData2();

    }

    public void getData2(){
        db.collection("Requests").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(DocumentSnapshot documentSnapshot : task.getResult()){
                    Requests requests = new Requests(documentSnapshot.getString("userId"), documentSnapshot.getString("gradeName"));
                    requestsList.add(requests);
                }
                requestsAdapter = new RequestsAdapter(requestsList, ApproveRequests.this);
                pendingrequestsrecyclerview.setAdapter(requestsAdapter);
                requestsAdapter.notifyDataSetChanged();
            }
        });
    }
}