package com.example.quizapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.Requests;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder>  {
    List<Requests> requestsList;
    Context context;

    public RequestsAdapter(List<Requests> requestsList, Context context) {
        this.requestsList = requestsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_request_row, parent, false);
        return new RequestsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.ViewHolder holder, int position) {
        int pos = position;
        Requests requests = requestsList.get(position);
        holder.gradeName.setText(requests.getGradeName());

        FirebaseFirestore.getInstance().collection("Users").document(requests.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.userName.setText(document.get("name").toString());
                    }
                }
            }
        });

        holder.acceptAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Users").document(requests.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                Map<String, Object> grades = (Map<String, Object>) data.get("grades");
                                grades.put(requests.getGradeName(), "true");
                                FirebaseFirestore.getInstance().collection("Users").document(requests.getUserId()).update("grades", grades);
                                FirebaseFirestore.getInstance().collection("Requests").whereEqualTo("userId", requests.getUserId()).whereEqualTo("gradeName", requests.getGradeName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                FirebaseFirestore.getInstance().collection("Requests").document(document.getId()).delete();
                                                // remove from list
                                                requestsList.remove(pos);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        holder.declineAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Users").document(requests.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                Map<String, Object> grades = (Map<String, Object>) data.get("grades");
                                grades.put(requests.getGradeName(), "false");
                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("grades", grades);
                                FirebaseFirestore.getInstance().collection("Requests").whereEqualTo("userId", requests.getUserId()).whereEqualTo("gradeName", requests.getGradeName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                FirebaseFirestore.getInstance().collection("Requests").document(document.getId()).delete();
                                                requestsList.remove(pos);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

    }





    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gradeName, userName;
        Button acceptAccess, declineAccess;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gradeName = itemView.findViewById(R.id.gradeName);
            userName = itemView.findViewById(R.id.userName);
            acceptAccess = itemView.findViewById(R.id.acceptAccess);
            declineAccess = itemView.findViewById(R.id.declineAccess);

        }
    }
}























