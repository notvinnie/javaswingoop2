import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    public final Map<String, String> accounts = new HashMap<>();
    public String currentUser;

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final HomeScreen homeScreen = new HomeScreen(this);

    public Main() {
        root.add(new LoginScreen(this).getPanel(), "login");
        root.add(new SignUpScreen(this).getPanel(), "signup");
        root.add(homeScreen.getPanel(), "home");

        setContentPane(root);
        setTitle("PawCare");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showScreen("login");
    }

    public void showScreen(String name) {
        if (name.equals("home")) {
            homeScreen.setUser(currentUser);
        }
        cards.show(root, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}