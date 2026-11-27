import java.time.LocalDate;

public class MedicalRecord {
    private static int nextId = 1;

    public final int id;
    public final Pet pet;
    public final LocalDate date;
    public String diagnosis;
    public String treatment;
    public String notes;
    public String vetName;

    public MedicalRecord(Pet pet, LocalDate date, String diagnosis,
                         String treatment, String notes, String vetName) {
        this.id = nextId++;
        this.pet = pet;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.vetName = vetName;
    }
}