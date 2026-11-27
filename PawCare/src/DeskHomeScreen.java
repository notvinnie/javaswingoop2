import javax.swing.*;
import java.awt.*;

public class DeskHomeScreen {
    private JPanel mainPanel;
    private JLabel greetingLabel;
    private JButton appointmentsButton, ownersButton, billingButton, remindersButton, profileButton, logoutButton;
    private final Main app;

    public DeskHomeScreen(Main app) {
        this.app = app;
        greetingLabel.setFont(greetingLabel.getFont().deriveFont(Font.BOLD, 20f));

        appointmentsButton.addActionListener(e -> app.showAppointments());
        ownersButton.addActionListener(e -> app.comingSoon("Pet Owners"));
        billingButton.addActionListener(e -> app.comingSoon("Billing"));
        remindersButton.addActionListener(e -> app.sendReminders());
        profileButton.addActionListener(e -> app.showEditProfile());
        logoutButton.addActionListener(e -> app.logout());
    }

    public void refresh() {
        greetingLabel.setText("Hello, " + app.currentUser.fullName + "!");
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}