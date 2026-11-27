import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PetsScreen {
    private JPanel mainPanel;
    private JTable petTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;

    private final Main app;
    private final DefaultTableModel model;
    private List<Pet> shown = new ArrayList<>();

    public PetsScreen(Main app) {
        this.app = app;

        model = new DefaultTableModel(new String[]{"Name", "Species", "Breed", "Age"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // view-only; editing happens in the form
            }
        };
        petTable.setModel(model);
        petTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addButton.addActionListener(e -> app.showPetForm(null));

        editButton.addActionListener(e -> {
            Pet p = selectedPet();
            if (p != null) app.showPetForm(p);
        });

        deleteButton.addActionListener(e -> {
            Pet p = selectedPet();
            if (p == null) return;
            int choice = JOptionPane.showConfirmDialog(mainPanel,
                    "Delete " + p.name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                app.pets.remove(p);
                refresh();
            }
        });

        backButton.addActionListener(e -> app.goHome());
    }

    // called every time the screen is opened
    public void refresh() {
        shown = app.petsOf(app.currentUser.username);
        model.setRowCount(0);
        for (Pet p : shown) {
            model.addRow(new Object[]{p.name, p.species, p.breed, p.age});
        }
    }

    private Pet selectedPet() {
        int row = petTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Select a pet first.");
            return null;
        }
        return shown.get(row);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}