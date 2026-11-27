import javax.swing.*;
import java.awt.*;

public class PetFormScreen {
    private JPanel mainPanel;
    private JLabel headingLabel;
    private JTextField nameField;
    private JComboBox<String> speciesBox;
    private JTextField breedField;
    private JTextField ageField;
    private JTextField notesField;
    private JButton saveButton;
    private JButton cancelButton;

    private final Main app;
    private Pet editing;   // null = adding a new pet

    public PetFormScreen(Main app) {
        this.app = app;
        headingLabel.setFont(headingLabel.getFont().deriveFont(Font.BOLD, 18f));
        speciesBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"Dog", "Cat", "Bird", "Rabbit", "Other"}));

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> app.showPets());
    }

    // pet == null means "add new"; otherwise edit that pet
    public void refresh(Pet pet) {
        editing = pet;
        if (pet == null) {
            headingLabel.setText("Add Pet");
            nameField.setText("");
            speciesBox.setSelectedIndex(0);
            breedField.setText("");
            ageField.setText("");
            notesField.setText("");
        } else {
            headingLabel.setText("Edit " + pet.name);
            nameField.setText(pet.name);
            speciesBox.setSelectedItem(pet.species);
            breedField.setText(pet.breed);
            ageField.setText(String.valueOf(pet.age));
            notesField.setText(pet.notes);
        }
    }

    private void save() {
        String name = nameField.getText().trim();
        String species = (String) speciesBox.getSelectedItem();
        String breed = breedField.getText().trim();
        String notes = notesField.getText().trim();
        int age;

        if (name.isEmpty()) {
            error("Pet name can't be empty.");
            return;
        }
        try {
            age = Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException ex) {
            error("Age must be a whole number.");
            return;
        }
        if (age < 0 || age > 50) {
            error("Please enter a realistic age (0-50).");
            return;
        }

        if (editing == null) {
            app.pets.add(new Pet(app.currentUser.username, name, species, breed, age, notes));
        } else {
            editing.name = name;
            editing.species = species;
            editing.breed = breed;
            editing.age = age;
            editing.notes = notes;
        }
        app.showPets();
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(mainPanel, message);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}