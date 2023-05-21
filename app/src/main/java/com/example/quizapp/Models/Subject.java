package com.example.quizapp.Models;

public class Subject {
    String subjectName;
    String gradeName;

    public Subject(String subjectName, String gradeName) {
        this.subjectName = subjectName;
        this.gradeName = gradeName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
