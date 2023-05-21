package com.example.quizapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.Grade;
import com.example.quizapp.Models.Subject;
import com.example.quizapp.Models.ThematicArea;
import com.example.quizapp.R;
import com.example.quizapp.Student.SingleGrade;
import com.example.quizapp.Student.SingleSubject;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder>  {

    List<Subject> subjectList;
    Context context;

    public SubjectAdapter(List<Subject> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_row, parent, false);
        return new SubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position) {
        int pos = position;
        Subject subject = subjectList.get(position);
        holder.subjectNameText.setText(subject.getSubjectName());
        Intent intent = ((Activity) context).getIntent();
        String gradeName = intent.getStringExtra("gradeName");



        holder.openSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleSubject.class);
                intent.putExtra("subjectName", subject.getSubjectName());
                intent.putExtra("gradeName", gradeName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameText;
        Button openSubjectButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameText = itemView.findViewById(R.id.subjectNameText);
            openSubjectButton = itemView.findViewById(R.id.openSubjectButton);

        }
    }
}
