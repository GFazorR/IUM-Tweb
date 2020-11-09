package com.example.app_client.Model;

public class Booking {
    private int id;
    private String course;
    private String teacher;
    private String user;
    private int status;
    private String date;
    private String start;

    public Booking(int id, String course, String teacher, String user, int status, String date, String start) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.user = user;
        this.status = status;
        this.date = date;
        this.start = start;
    }

    public Booking(int id, String course, String teacher, String date) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.date = date;
    }
}
