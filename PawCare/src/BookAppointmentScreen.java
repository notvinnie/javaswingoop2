import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookAppointmentScreen {
    private JPanel mainPanel;
    private JLabel headingLabel;
    private JComboBox<String> petBox;
    private JComboBox<String> dateBox;
    private JComboBox<String> timeBox;
    private JTextField reasonField;
    private JButton bookButton;
    private JButton backButton;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a");

    private final Main app;
    private List<Pet> myPets = new ArrayList<>();
    private final List<LocalDate> dates = new ArrayList<>();
    private List<LocalTime> slots = new ArrayList<>();
    private boolean loading = false;   // stops the date listener firing while we fill the boxes

    public BookAppointmentScreen(Main app) {
        this.app = app;
        headingLabel.setFont(headingLabel.getFont().deriveFont(Font.BOLD, 18f));

        // when the date changes, show that day's free slots
        dateBox.addActionListener(e -> {
            if (!loading) loadSlots();
        });

        bookButton.addActionListener(e -> book());
        backButton.addActionListener(e -> app.goHome());
    }

    // called every time the screen is opened
    public void refresh() {
        loading = true;

        myPets = app.petsOf(app.currentUser.username);
        DefaultComboBoxModel<String> petModel = new DefaultComboBoxModel<>();
        for (Pet p : myPets) {
            petModel.addElement(p.name + " (" + p.species + ")");
        }
        petBox.setModel(petModel);

        // the next 14 days, starting today
        dates.clear();
        DefaultComboBoxModel<String> dateModel = new DefaultComboBoxModel<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 14; i++) {
            LocalDate d = today.plusDays(i);
            dates.add(d);
            dateModel.addElement(d.format(DATE_FMT));
        }
        dateBox.setModel(dateModel);

        reasonField.setText("");
        loading = false;
        loadSlots();
    }

    // Check Schedule Availability: only free slots go into the time box
    private void loadSlots() {
        int dateIdx = dateBox.getSelectedIndex();
        if (dateIdx < 0) return;

        slots = app.availableSlots(dates.get(dateIdx));
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (slots.isEmpty()) {
            model.addElement("No slots available");
        } else {
            for (LocalTime t : slots) {
                model.addElement(t.format(TIME_FMT));
            }
        }
        timeBox.setModel(model);
    }

    private void book() {
        int petIdx = petBox.getSelectedIndex();
        int dateIdx = dateBox.getSelectedIndex();
        int timeIdx = timeBox.getSelectedIndex();
        String reason = reasonField.getText().trim();

        if (petIdx < 0) {
            error("Please choose a pet.");
            return;
        }
        if (slots.isEmpty() || timeIdx < 0) {
            error("That day is fully booked. Please pick another date.");
            return;
        }
        if (reason.isEmpty()) {
            error("Please enter a reason for the visit.");
            return;
        }

        Pet pet = myPets.get(petIdx);
        LocalDate date = dates.get(dateIdx);
        LocalTime time = slots.get(timeIdx);

        // check once more right before saving
        if (app.isSlotTaken(date, time)) {
            error("Sorry, that time was just taken. Please pick another.");
            loadSlots();
            return;
        }

        app.appointments.add(new Appointment(app.currentUser.username, pet, date, time, reason));
        JOptionPane.showMessageDialog(mainPanel,
                "Appointment requested for " + pet.name + "\n"
                        + date.format(DATE_FMT) + " at " + time.format(TIME_FMT) + "\n\n"
                        + "Status: Pending. The clinic will confirm it.");
        app.goHome();
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(mainPanel, message);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}