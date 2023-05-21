package com.example.quizapp.Models;

public class ThematicArea {
    String thematicAreaName;
    String subjectName;
    String gradeName;

    public ThematicArea(String thematicAreaName, String gradeName, String subjectName) {
        this.thematicAreaName = thematicAreaName;
        this.subjectName = subjectName;
        this.gradeName = gradeName;
    }

    public String getThematicAreaName() {
        return thematicAreaName;
    }

    public void setThematicAreaName(String thematicAreaName) {
        this.thematicAreaName = thematicAreaName;
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
