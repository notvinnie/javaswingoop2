import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    public enum Status { PENDING, CONFIRMED, CANCELLED }

    private static int nextId = 1;

    public final int id;
    public final String ownerUsername;
    public final Pet pet;
    public LocalDate date;
    public LocalTime time;
    public String reason;
    public Status status = Status.PENDING;

    public Appointment(String ownerUsername, Pet pet, LocalDate date,
                       LocalTime time, String reason) {
        this.id = nextId++;
        this.ownerUsername = ownerUsername;
        this.pet = pet;
        this.date = date;
        this.time = time;
        this.reason = reason;
    }
}