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
import com.example.quizapp.Models.Questions;
import com.example.quizapp.Models.ThematicArea;
import com.example.quizapp.R;
import com.example.quizapp.Student.SingleQuestion;


import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>  {

    List<Questions> questionsList;
    Context context;

    public QuestionAdapter(List<Questions> questionsList, Context context) {
        this.questionsList = questionsList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_row, parent, false);
        return new QuestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        int pos = position;
        Questions questions = questionsList.get(position);
        holder.questionText.setText("Question " + Integer.toString(pos + 1));
        holder.openQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleQuestion.class);
                intent.putExtra("question", questions.getQuestion());
                intent.putExtra("option1", questions.getOption1());
                intent.putExtra("option2", questions.getOption2());
                intent.putExtra("option3", questions.getOption3());
                intent.putExtra("option4", questions.getOption4());
                intent.putExtra("correctAnswer", questions.getCorrectAnswer());
                intent.putExtra("questionId", questions.getQuestionId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        Button openQuestionButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            openQuestionButton = itemView.findViewById(R.id.openQuestionButton);

        }
    }
}
