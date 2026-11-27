import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordFormScreen {
    private JPanel mainPanel;
    private JLabel headingLabel;
    private JComboBox<String> petBox;
    private JTextField diagnosisField;
    private JTextField treatmentField;
    private JTextField notesField;
    private JButton saveButton;
    private JButton cancelButton;

    private final Main app;
    private MedicalRecord editing;              // null = creating a new record
    private List<Pet> petList = new ArrayList<>();

    public RecordFormScreen(Main app) {
        this.app = app;
        headingLabel.setFont(headingLabel.getFont().deriveFont(Font.BOLD, 18f));

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> app.showRecords());
    }

    // record == null: new record (optionally for a preselected pet)
    // record != null: edit that record (the pet can't be changed)
    public void refresh(MedicalRecord record, Pet preselected) {
        editing = record;

        petList = new ArrayList<>(app.pets);
        petList.sort(Comparator.comparing((Pet p) -> p.name.toLowerCase()));

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Pet p : petList) {
            model.addElement(p.name + " (" + p.species + ") - " + app.ownerName(p.ownerUsername));
        }
        petBox.setModel(model);

        if (record == null) {
            headingLabel.setText("New Medical Record");
            petBox.setEnabled(true);
            if (preselected != null) petBox.setSelectedIndex(petList.indexOf(preselected));
            diagnosisField.setText("");
            treatmentField.setText("");
            notesField.setText("");
        } else {
            headingLabel.setText("Edit Record: " + record.pet.name);
            petBox.setSelectedIndex(petList.indexOf(record.pet));
            petBox.setEnabled(false);
            diagnosisField.setText(record.diagnosis);
            treatmentField.setText(record.treatment);
            notesField.setText(record.notes);
        }
    }

    private void save() {
        int petIdx = petBox.getSelectedIndex();
        String diagnosis = diagnosisField.getText().trim();
        String treatment = treatmentField.getText().trim();
        String notes = notesField.getText().trim();

        if (petIdx < 0) {
            error("Please choose a pet.");
            return;
        }
        if (diagnosis.isEmpty() || treatment.isEmpty()) {
            error("Diagnosis and treatment can't be empty.");
            return;
        }

        if (editing == null) {
            app.records.add(new MedicalRecord(petList.get(petIdx), LocalDate.now(),
                    diagnosis, treatment, notes, app.currentUser.fullName));
        } else {
            editing.diagnosis = diagnosis;
            editing.treatment = treatment;
            editing.notes = notes;
            editing.vetName = app.currentUser.fullName;   // last vet to edit it
        }
        app.showRecords();
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(mainPanel, message);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}