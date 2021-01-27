package Model;

import java.util.ArrayList;

public class Subject {
    private int id;
    private String name;
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
