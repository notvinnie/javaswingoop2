import javax.swing.*;

public class SignUpScreen {
    private JPanel mainPanel;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JComboBox<Role> roleBox;
    private JButton createButton;
    private JButton backButton;

    public SignUpScreen(Main app) {
        roleBox.setModel(new DefaultComboBoxModel<>(Role.values()));

        createButton.addActionListener(e -> {
            String name = fullNameField.getText().trim();
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());
            Role role = (Role) roleBox.getSelectedItem();

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please fill in all fields.");
            } else if (app.accounts.containsKey(user)) {          // Check Account Availability
                JOptionPane.showMessageDialog(mainPanel, "Username already taken.");
            } else if (pass.length() < 6) {                       // Validate Password
                JOptionPane.showMessageDialog(mainPanel, "Password must be at least 6 characters.");
            } else if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(mainPanel, "Passwords don't match.");
            } else {
                app.accounts.put(user, new User(user, pass, role, name));
                JOptionPane.showMessageDialog(mainPanel, "Account created! You can log in now.");
                fullNameField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                confirmField.setText("");
                app.showScreen("login");
            }
        });

        backButton.addActionListener(e -> app.showScreen("login"));
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}