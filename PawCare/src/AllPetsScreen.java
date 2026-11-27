import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AllPetsScreen {
    private JPanel mainPanel;
    private JTable allPetsTable;
    private JButton addRecordButton;
    private JButton backButton;

    private final Main app;
    private final DefaultTableModel model;
    private List<Pet> shown = new ArrayList<>();

    public AllPetsScreen(Main app) {
        this.app = app;

        model = new DefaultTableModel(
                new String[]{"Name", "Species", "Breed", "Age", "Owner", "Notes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // view-only
            }
        };
        allPetsTable.setModel(model);
        allPetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addRecordButton.addActionListener(e -> {
            int row = allPetsTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(mainPanel, "Select a pet first.");
                return;
            }
            app.showRecordForm(null, shown.get(row));
        });

        backButton.addActionListener(e -> app.goHome());
    }

    // called every time the screen is opened
    public void refresh() {
        shown = new ArrayList<>(app.pets);
        shown.sort(Comparator.comparing((Pet p) -> p.name.toLowerCase()));

        model.setRowCount(0);
        for (Pet p : shown) {
            model.addRow(new Object[]{
                    p.name, p.species, p.breed, p.age,
                    app.ownerName(p.ownerUsername),
                    p.notes.isEmpty() ? "-" : p.notes});
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}