package com.example.app_client.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Booking {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("subject")
    @Expose
    private String subjectName;

    @SerializedName("teacher")
    @Expose
    private String teacher;

    @SerializedName("teacher")
    @Expose
    private int teacherId;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("date")
    @Expose
    private LocalDateTime dateTime;


    public Booking(String subjectName, String user, int status) {
        this.subjectName = subjectName;
        this.user = user;
        this.status = status;
    }

    public int getId() { return id; }

    public String getSubject() { return subjectName; }

    public String getTeacher() { return teacher; }

    public void setTeacher(String teacher) { this.teacher = teacher; }

    public int getTeacherId() { return teacherId; }

    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getUser() { return user; }

    public LocalDateTime getDateTime() { return dateTime; }

    public void setDateTime(LocalDateTime dateTime){this.dateTime = dateTime;}

    public  boolean isConfirmed() { return  status == 30;}

    public boolean isCancelled() { return  status == 20;}

    public boolean isInit() { return status == 10;}

    public String getStatus(){
        switch (status){
            case 10:
                return "Attiva";
            case 20:
                return "Cancellata";
            case 30:
                return "Confermata";
            default:
                return null;
        }
    }
}
