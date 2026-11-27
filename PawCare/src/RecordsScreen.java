import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordsScreen {
    private JPanel mainPanel;
    private JTable recordTable;
    private JButton newButton;
    private JButton editButton;
    private JButton backButton;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    private final Main app;
    private final DefaultTableModel model;
    private List<MedicalRecord> shown = new ArrayList<>();

    public RecordsScreen(Main app) {
        this.app = app;

        model = new DefaultTableModel(
                new String[]{"Date", "Pet", "Owner", "Diagnosis", "Treatment", "Vet"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // view-only; editing happens in the form
            }
        };
        recordTable.setModel(model);
        recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        newButton.addActionListener(e -> app.showRecordForm(null, null));

        editButton.addActionListener(e -> {
            int row = recordTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(mainPanel, "Select a record first.");
                return;
            }
            app.showRecordForm(shown.get(row), null);
        });

        backButton.addActionListener(e -> app.goHome());
    }

    // called every time the screen is opened
    public void refresh() {
        shown = new ArrayList<>(app.records);
        shown.sort(Comparator.comparing((MedicalRecord r) -> r.date).reversed()); // newest first

        model.setRowCount(0);
        for (MedicalRecord r : shown) {
            model.addRow(new Object[]{
                    r.date.format(DATE_FMT), r.pet.name,
                    app.ownerName(r.pet.ownerUsername),
                    r.diagnosis, r.treatment, r.vetName});
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}