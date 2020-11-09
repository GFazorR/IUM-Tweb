package Model;

public class Booking {
    private int id;
    private String subject;
    private String teacher;
    private String user;
    private int status;
    private String date;

    public Booking(int id, String course, String teacher, String user, int status, String date) {
        this.id = id;
        this.subject = course;
        this.teacher = teacher;
        this.user = user;
        this.status = status;
        this.date = date;
    }
}
