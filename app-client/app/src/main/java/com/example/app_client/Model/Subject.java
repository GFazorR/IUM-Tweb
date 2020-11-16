package com.example.app_client.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Subject {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("teachers")
    @Expose
    private ArrayList<Teacher> teachers;

    public Subject(int id, String name, ArrayList<Teacher> teachers) {
        this.id = id;
        this.name = name;
        this.teachers = teachers;
    }

    @Override
    public String toString() {
        return id +", " +name+", " + teachers.toString();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public ArrayList<Teacher> getTeachers() { return teachers; }



}
