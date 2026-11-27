import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Main extends JFrame {
    // shared "database"
    public final Map<String, User> accounts = new HashMap<>();
    public final List<Pet> pets = new ArrayList<>();
    public User currentUser;

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final OwnerHomeScreen ownerHome = new OwnerHomeScreen(this);
    private final VetHomeScreen vetHome = new VetHomeScreen(this);
    private final DeskHomeScreen deskHome = new DeskHomeScreen(this);
    private final EditProfileScreen editProfile = new EditProfileScreen(this);
    private final PetsScreen petsScreen = new PetsScreen(this);
    private final PetFormScreen petForm = new PetFormScreen(this);
    public final List<Appointment> appointments = new ArrayList<>();
    private final BookAppointmentScreen bookingScreen = new BookAppointmentScreen(this);
    public final List<MedicalRecord> records = new ArrayList<>();
    private final AllPetsScreen allPetsScreen = new AllPetsScreen(this);
    private final RecordsScreen recordsScreen = new RecordsScreen(this);
    private final RecordFormScreen recordForm = new RecordFormScreen(this);

    public Main() {
        root.add(centered(new LoginScreen(this).getPanel(), 380, 320), "login");
        root.add(centered(new SignUpScreen(this).getPanel(), 380, 470), "signup");
        root.add(centered(ownerHome.getPanel(), 380, 440), "ownerHome");
        root.add(centered(vetHome.getPanel(), 380, 400), "vetHome");
        root.add(centered(deskHome.getPanel(), 380, 440), "deskHome");
        root.add(centered(editProfile.getPanel(), 400, 520), "editProfile");
        root.add(centered(petsScreen.getPanel(), 600, 420), "pets");
        root.add(centered(petForm.getPanel(), 400, 500), "petForm");
        root.add(centered(bookingScreen.getPanel(), 400, 480), "booking");
        root.add(centered(appointmentsScreen.getPanel(), 660, 420), "appointments");
        root.add(centered(allPetsScreen.getPanel(), 660, 420), "allPets");
        root.add(centered(recordsScreen.getPanel(), 660, 420), "records");
        root.add(centered(recordForm.getPanel(), 420, 440), "recordForm");

        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(root, BorderLayout.CENTER);

        setTitle("PawCare - Veterinary Clinic System");
        setSize(730, 600);
        setMinimumSize(new Dimension(700, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showScreen("login");
    }

    // styles a screen and centers it at a fixed size on the mint background
    private JPanel centered(JPanel screen, int width, int height) {
        Theme.styleScreen(screen);
        screen.setPreferredSize(new Dimension(width, height));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Theme.BACKGROUND);
        wrapper.add(screen);
        return wrapper;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setBackground(Theme.PRIMARY);
        header.setBorder(new MatteBorder(0, 0, 4, 0, Theme.ACCENT));

        JLabel title = new JLabel("PawCare  |  Veterinary Clinic System");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        header.add(title);
        return header;
    }

    public void showScreen(String name) {
        cards.show(root, name);
    }

    public void goHome() {
        switch (currentUser.role) {
            case PET_OWNER -> { ownerHome.refresh(); showScreen("ownerHome"); showOwnerReminders(); }
            case VETERINARIAN -> { vetHome.refresh(); showScreen("vetHome"); }
            case FRONT_DESK -> { deskHome.refresh(); showScreen("deskHome"); }
        }
    }

    public void showEditProfile() {
        editProfile.refresh();
        showScreen("editProfile");
    }

    public void showPets() {
        petsScreen.refresh();
        showScreen("pets");
    }

    public void showPetForm(Pet pet) {
        petForm.refresh(pet);
        showScreen("petForm");
    }

    public void showBooking() {
        if (petsOf(currentUser.username).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add a pet first, then you can book an appointment.");
            showPets();
            return;
        }
        bookingScreen.refresh();
        showScreen("booking");
    }

    public void showAppointments() {
        appointmentsScreen.refresh();
        showScreen("appointments");
    }

    public void showAllPets() {
        allPetsScreen.refresh();
        showScreen("allPets");
    }

    public void showRecords() {
        recordsScreen.refresh();
        showScreen("records");
    }

    // record == null: new record (pet may be preselected); otherwise edit that record
    public void showRecordForm(MedicalRecord record, Pet pet) {
        if (record == null && pets.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pets are registered yet.");
            return;
        }
        recordForm.refresh(record, pet);
        showScreen("recordForm");
    }

    // full name of an owner, or their username if the account can't be found
    public String ownerName(String username) {
        User u = accounts.get(username);
        return u != null ? u.fullName : username;
    }

    // Front Desk: remind owners of confirmed appointments today or tomorrow
    public void sendReminders() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("EEE, MMM d");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");
        LocalDate today = LocalDate.now();

        StringBuilder sent = new StringBuilder();
        int count = 0;
        for (Appointment a : appointments) {
            boolean soon = !a.date.isBefore(today) && !a.date.isAfter(today.plusDays(1));
            if (a.status == Appointment.Status.CONFIRMED && soon && !a.reminderSent) {
                a.reminderSent = true;
                count++;
                User owner = accounts.get(a.ownerUsername);
                String ownerName = owner != null ? owner.fullName : a.ownerUsername;
                sent.append("- ").append(ownerName).append(": ").append(a.pet.name)
                        .append(", ").append(a.date.format(dateFmt))
                        .append(" at ").append(a.time.format(timeFmt)).append("\n");
            }
        }

        if (count == 0) {
            JOptionPane.showMessageDialog(this,
                    "No reminders to send.\n(Only confirmed appointments today or tomorrow need one.)");
        } else {
            JOptionPane.showMessageDialog(this, count + " reminder(s) sent:\n\n" + sent);
        }
    }

    // Pet Owner: show any reminders they haven't seen yet
    private void showOwnerReminders() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("EEE, MMM d");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

        StringBuilder text = new StringBuilder();
        for (Appointment a : appointments) {
            if (a.ownerUsername.equals(currentUser.username)
                    && a.reminderSent && !a.reminderShown
                    && a.status == Appointment.Status.CONFIRMED) {
                a.reminderShown = true;
                text.append("- ").append(a.pet.name).append(": ")
                        .append(a.date.format(dateFmt)).append(" at ")
                        .append(a.time.format(timeFmt)).append("\n");
            }
        }
        if (!text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appointment reminder:\n\n" + text);
        }
    }



    private final AppointmentsScreen appointmentsScreen = new AppointmentsScreen(this);

    // a slot is taken if a non-cancelled appointment already uses it
    public boolean isSlotTaken(LocalDate date, LocalTime time) {
        for (Appointment a : appointments) {
            if (a.status != Appointment.Status.CANCELLED
                    && a.date.equals(date) && a.time.equals(time)) {
                return true;
            }
        }
        return false;
    }

    // clinic hours: 9:00 AM to 5:00 PM, one slot every 30 minutes
    public List<LocalTime> availableSlots(LocalDate date) {
        List<LocalTime> result = new ArrayList<>();
        LocalTime t = LocalTime.of(9, 0);
        LocalTime last = LocalTime.of(16, 30);
        while (!t.isAfter(last)) {
            boolean inThePast = date.equals(LocalDate.now()) && !t.isAfter(LocalTime.now());
            if (!inThePast && !isSlotTaken(date, t)) {
                result.add(t);
            }
            t = t.plusMinutes(30);
        }
        return result;
    }

    // all pets belonging to one owner
    public List<Pet> petsOf(String username) {
        List<Pet> result = new ArrayList<>();
        for (Pet p : pets) {
            if (p.ownerUsername.equals(username)) result.add(p);
        }
        return result;
    }

    public void logout() {
        currentUser = null;
        showScreen("login");
    }

    public void comingSoon(String feature) {
        JOptionPane.showMessageDialog(this, feature + " (coming soon)");
    }

    private static void setGlobalFont(Font font) {
        for (Object key : Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(key) instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        setGlobalFont(new Font("Segoe UI", Font.PLAIN, 14));

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}