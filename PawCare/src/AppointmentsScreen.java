import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppointmentsScreen {
    private JPanel mainPanel;
    private JTable appointmentTable;
    private JButton confirmButton;
    private JButton rescheduleButton;
    private JButton cancelButton;
    private JButton backButton;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEE, MMM d");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a");

    private final Main app;
    private final DefaultTableModel model;
    private List<Appointment> shown = new ArrayList<>();

    public AppointmentsScreen(Main app) {
        this.app = app;

        model = new DefaultTableModel(
                new String[]{"Date", "Time", "Pet", "Owner", "Reason", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentTable.setModel(model);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        confirmButton.addActionListener(e -> confirm());
        rescheduleButton.addActionListener(e -> reschedule());
        cancelButton.addActionListener(e -> cancel());
        backButton.addActionListener(e -> app.goHome());
    }

    // called every time the screen is opened
    public void refresh() {
        shown = new ArrayList<>(app.appointments);
        shown.sort(Comparator.comparing((Appointment a) -> a.date).thenComparing(a -> a.time));

        model.setRowCount(0);
        for (Appointment a : shown) {
            User owner = app.accounts.get(a.ownerUsername);
            String ownerName = owner != null ? owner.fullName : a.ownerUsername;
            model.addRow(new Object[]{
                    a.date.format(DATE_FMT), a.time.format(TIME_FMT),
                    a.pet.name, ownerName, a.reason, statusText(a.status)});
        }
    }

    // Confirm Appointment Schedule
    private void confirm() {
        Appointment a = selected();
        if (a == null) return;

        if (a.status == Appointment.Status.CANCELLED) {
            message("That appointment is cancelled.");
        } else if (a.status == Appointment.Status.CONFIRMED) {
            message("That appointment is already confirmed.");
        } else {
            a.status = Appointment.Status.CONFIRMED;
            refresh();
        }
    }

    // Reschedule Appointment
    private void reschedule() {
        Appointment a = selected();
        if (a == null) return;
        if (a.status == Appointment.Status.CANCELLED) {
            message("Cancelled appointments can't be rescheduled.");
            return;
        }

        // small pop-up with a date box and a time box
        List<LocalDate> dates = new ArrayList<>();
        JComboBox<String> dateBox = new JComboBox<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 14; i++) {
            LocalDate d = today.plusDays(i);
            dates.add(d);
            dateBox.addItem(d.format(DATE_FMT));
        }

        JComboBox<String> timeBox = new JComboBox<>();
        List<LocalTime> slots = new ArrayList<>();
        Runnable loadSlots = () -> {
            slots.clear();
            slots.addAll(app.availableSlots(dates.get(dateBox.getSelectedIndex())));
            timeBox.removeAllItems();
            for (LocalTime t : slots) timeBox.addItem(t.format(TIME_FMT));
        };
        dateBox.addActionListener(e -> loadSlots.run());
        loadSlots.run();

        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 6, 6));
        form.add(new JLabel("New date"));
        form.add(dateBox);
        form.add(new JLabel("New time (free slots only)"));
        form.add(timeBox);

        int choice = JOptionPane.showConfirmDialog(mainPanel, form,
                "Reschedule " + a.pet.name, JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) return;

        if (slots.isEmpty() || timeBox.getSelectedIndex() < 0) {
            message("No free slots on that day.");
            return;
        }

        a.date = dates.get(dateBox.getSelectedIndex());
        a.time = slots.get(timeBox.getSelectedIndex());
        a.reminderSent = false;     // the old reminder no longer applies
        a.reminderShown = false;
        refresh();
    }

    // Cancel Appointment (the slot becomes free again)
    private void cancel() {
        Appointment a = selected();
        if (a == null) return;
        if (a.status == Appointment.Status.CANCELLED) {
            message("That appointment is already cancelled.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(mainPanel,
                "Cancel " + a.pet.name + "'s appointment on "
                        + a.date.format(DATE_FMT) + " at " + a.time.format(TIME_FMT) + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            a.status = Appointment.Status.CANCELLED;
            refresh();
        }
    }

    private Appointment selected() {
        int row = appointmentTable.getSelectedRow();
        if (row < 0) {
            message("Select an appointment first.");
            return null;
        }
        return shown.get(row);
    }

    private String statusText(Appointment.Status s) {
        String name = s.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(mainPanel, text);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}