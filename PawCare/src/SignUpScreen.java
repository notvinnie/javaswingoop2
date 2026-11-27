import javax.swing.*;

public class SignUpScreen {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton createButton;
    private JButton backButton;

    public SignUpScreen(Main app) {
        createButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please fill in all fields.");
            } else if (app.accounts.containsKey(user)) {
                JOptionPane.showMessageDialog(mainPanel, "Username already taken.");
            } else if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(mainPanel, "Passwords don't match.");
            } else {
                app.accounts.put(user, pass);
                JOptionPane.showMessageDialog(mainPanel, "Account created! You can log in now.");
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