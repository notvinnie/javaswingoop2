import javax.swing.*;

public class EditProfileScreen {
    private JPanel mainPanel;
    private JLabel infoLabel;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton saveButton;
    private JButton backButton;
    private final Main app;

    public EditProfileScreen(Main app) {
        this.app = app;
        saveButton.addActionListener(e -> save());
        backButton.addActionListener(e -> app.goHome());
    }

    // called every time the screen is opened
    public void refresh() {
        User u = app.currentUser;
        infoLabel.setText("Username: " + u.username + "   |   Role: " + u.role);
        fullNameField.setText(u.fullName);
        phoneField.setText(u.phone);
        clearPasswords();
    }

    private void save() {
        User u = app.currentUser;
        String name = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String current = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (name.isEmpty()) {
            error("Full name can't be empty.");
            return;
        }
        if (!phone.isEmpty() && !phone.matches("[0-9+ \\-]{7,15}")) {
            error("Please enter a valid phone number.");
            return;
        }

        // only touch the password if they typed a new one
        if (!newPass.isEmpty()) {
            if (!u.password.equals(current)) {
                error("Current password is incorrect.");
                return;
            }
            if (newPass.length() < 6) {
                error("New password must be at least 6 characters.");
                return;
            }
            if (!newPass.equals(confirm)) {
                error("New passwords don't match.");
                return;
            }
            u.password = newPass;
        }

        u.fullName = name;
        u.phone = phone;
        JOptionPane.showMessageDialog(mainPanel, "Profile updated!");
        app.goHome();   // also refreshes the greeting with the new name
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(mainPanel, message);
    }

    private void clearPasswords() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}