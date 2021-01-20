package Model;

import Dao.SubjectDao;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static Dao.BookingDao.getWeeklyBookings;
import static Time.TimeUtility.getFirstDayOfWeek;

public class Slot {
    private LocalDateTime date;
    private ArrayList<Teacher> teachers;
    private String subjectname;
    private int subjectId;
    private boolean isAvailable;

    public Slot(LocalDateTime date, Subject subject) {

        this.date = date;
        this.subjectId = subject.getId();
        this.teachers = new ArrayList<>(subject.getTeachers());
        this.subjectname = subject.getName();
        this.isAvailable = true;
    }

    public ArrayList<Teacher> getTeachers() { return teachers; }
    public LocalDateTime getDate() { return date; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public static class Dao{

        private static Map<LocalDate, ArrayList<Slot>> createNewWeeklyCalendar(int subjectId) throws SQLException, NamingException {
            Instant start = Instant.now();
            Subject subject = SubjectDao.getSubject(subjectId);
            Map<LocalDate, ArrayList<Slot>> newCalendar = new TreeMap<>();
            for (int i = 0; i<5; i++){
                ArrayList<Slot> slots = new ArrayList<>();
                for (int j = 0; j<5 ; j++){
                    Slot slot =new Slot(getFirstDayOfWeek().plusDays(i).plusHours(15+j),subject);
                    if (subject.getTeachers().isEmpty())
                        slot.setAvailable(false);
                    slots.add(slot);
                }
                newCalendar.put(getFirstDayOfWeek().plusDays(i).toLocalDate(),slots);
            }
            Instant end = Instant.now();
            System.out.print("createNewWeeklyCalendar: duration : ");
            System.out.println(Duration.between(start,end));

            return newCalendar;
        }

        public static Map<LocalDate, ArrayList<Slot>> weeklySubjectCalendar(int subjectId) throws SQLException, NamingException {

            ArrayList<Booking> bookings = getWeeklyBookings();

            Map<LocalDate,ArrayList<Slot>> weeklyCalendar = createNewWeeklyCalendar(subjectId);
            Instant start = Instant.now();
            for (Map.Entry<LocalDate,ArrayList<Slot>> entry : weeklyCalendar.entrySet()){
                for (Slot slot: entry.getValue()) {
                    for (Booking booking: bookings){
                        if (slot.getDate().isEqual(booking.getDate())){
                            slot.teachers.removeIf(t -> t.getId() == booking.getTeacherId());
                        }
                        if (slot.teachers.isEmpty())
                            slot.setAvailable(false);
                    }
                }
            }
            Instant end = Instant.now();
            System.out.print("weeklyCalendar: duration : ");
            System.out.println(Duration.between(start,end));
            return weeklyCalendar;
        }
    }

}
