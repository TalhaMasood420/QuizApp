package com.example.quizapp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Student.HomeScreen;
import com.example.quizapp.Models.Grade;
import com.example.quizapp.Models.Requests;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class RequestableGradeAdapter extends RecyclerView.Adapter<RequestableGradeAdapter.ViewHolder>  {
    List<Grade> gradesList;
    Context context;


    public RequestableGradeAdapter(List<Grade> gradesList, Context context) {
        this.gradesList = gradesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestableGradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_grade_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestableGradeAdapter.ViewHolder holder, int position) {
        int pos = position;
        Grade grades = gradesList.get(position);
        holder.gradeName.setText(grades.getName());
        holder.requestAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db;
                FirebaseAuth mAuth;
                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getCurrentUser().getUid();
                String gradeName = grades.getName();
                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Map<String, Object> data = document.getData();
                                Map<String, Object> grades = (Map<String, Object>) data.get("grades");
                                grades.put(gradeName, "pending");
                                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("grades", grades);
                            }
                        }
                    }
                });
                Requests requests = new Requests(uid, gradeName);
                db.collection("Requests").add(requests);
                Intent intent = new Intent(context, HomeScreen.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gradesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gradeName;
        Button requestAccess;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gradeName = itemView.findViewById(R.id.gradeName);
            requestAccess = itemView.findViewById(R.id.requestAccess);

        }
    }
}
