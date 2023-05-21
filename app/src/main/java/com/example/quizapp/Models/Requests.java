package com.example.quizapp.Models;

public class Requests {
    String userId;
    String gradeName;

    public Requests(String userId, String gradeName) {
        this.userId = userId;
        this.gradeName = gradeName;
    }

    public Requests() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
