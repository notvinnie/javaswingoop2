import javax.swing.*;

public class LoginScreen {
    private JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginScreen(Main app) {
        loginButton.addActionListener(e -> {
            String name = textField1.getText().trim();
            String pass = new String(passwordField1.getPassword());
            User user = app.accounts.get(name);

            if (user == null) {                                   // Check Account Availability
                JOptionPane.showMessageDialog(mainPanel, "No account found. Please sign up.");
                app.showScreen("signup");
            } else if (!user.password.equals(pass)) {             // Authenticate Password
                JOptionPane.showMessageDialog(mainPanel, "Wrong password.");
            } else {
                textField1.setText("");
                passwordField1.setText("");
                app.currentUser = user;
                app.goHome();
            }
        });

        signUpButton.addActionListener(e -> app.showScreen("signup"));
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}