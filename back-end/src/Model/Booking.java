package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Booking {
    private int id;
    private String subject;
    private String teacher;
    private int teacherId;
    private String user;
    private int status;
    private LocalDateTime date;

    public int getTeacherId() { return teacherId; }
    public LocalDateTime getDate() { return date; }

    public Booking(int id, String course, String teacher, String user, int status, Timestamp date) {
        this.id = id;
        this.subject = course;
        this.teacher = teacher;
        this.user = user;
        this.status = status;
        this.date = date.toLocalDateTime();
    }

    public Booking(int id, String subject, String teacher, int teacherId, LocalDateTime date) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.teacherId = teacherId;
        this.date = date;
    }


}
