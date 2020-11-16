package com.example.app_client.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("teacher")
    @Expose
    private String teacher;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("date")
    @Expose
    private String date;


    public Booking(int id, String subject, String teacher, String user, int status, String date) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.user = user;
        this.status = status;
        this.date = date;

    }

    public int getId() { return id; }

    public String getSubject() { return subject; }

    public String getTeacher() { return teacher; }

    public String getUser() { return user; }

    public String getDate() { return date; }

    public Date getDateTime() {
        final String OLD_FORMAT = "yyyy-MM-dd kk:mm:ss";
        try {
            return new SimpleDateFormat(OLD_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

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
