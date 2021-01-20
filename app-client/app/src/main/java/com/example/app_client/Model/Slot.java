package com.example.app_client.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Slot {
    @SerializedName("date")
    @Expose
    private LocalDateTime date;
    @SerializedName("teachers")
    @Expose
    private ArrayList<Teacher> teachers;
    @SerializedName("subjectName")
    @Expose
    private String subjectName;
    @SerializedName("subjectId")
    @Expose
    private int subjectId;
    @SerializedName("isAvailable")
    @Expose
    private boolean isAvailable;

    public Slot(LocalDateTime date, Subject subject) {
        this.date = date;
        this.subjectId = subject.getId();
        this.teachers = new ArrayList<>(subject.getTeachers());
        this.subjectName = subject.getName();
        this.isAvailable = true;
    }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public ArrayList<Teacher> getTeachers() { return teachers; }
    public void setTeachers(ArrayList<Teacher> teachers) { this.teachers = teachers; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
