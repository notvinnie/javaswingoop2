import javax.swing.*;

public class LoginScreen {
    private JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginScreen(Main app) {
        loginButton.addActionListener(e -> {
            String user = textField1.getText().trim();
            String pass = new String(passwordField1.getPassword());

            if (!app.accounts.containsKey(user)) {
                JOptionPane.showMessageDialog(mainPanel, "No account found. Please sign up.");
                app.showScreen("signup");
            } else if (!app.accounts.get(user).equals(pass)) {
                JOptionPane.showMessageDialog(mainPanel, "Wrong password.");
            } else {
                app.currentUser = user;
                textField1.setText("");
                passwordField1.setText("");
                app.showScreen("home");
            }
        });

        signUpButton.addActionListener(e -> app.showScreen("signup"));
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}