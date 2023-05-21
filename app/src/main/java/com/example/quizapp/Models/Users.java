package com.example.quizapp.Models;

public class Users {
    String name;
    String email;
    String password;
    String userId;
    String phoneNum;
    String obtainedMarks;
    String totalMarks;

    public Users(){}

    public Users(String name, String email, String password, String phoneNum)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
    }


    public Users(String name, String email, String password, String phoneNum, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.phoneNum = phoneNum;
    }

    public String getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(String obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
