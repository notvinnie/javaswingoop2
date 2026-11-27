import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    public final Map<String, User> accounts = new HashMap<>();
    public User currentUser;

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final OwnerHomeScreen ownerHome = new OwnerHomeScreen(this);
    private final VetHomeScreen vetHome = new VetHomeScreen(this);
    private final DeskHomeScreen deskHome = new DeskHomeScreen(this);

    public Main() {
        root.add(new LoginScreen(this).getPanel(), "login");
        root.add(new SignUpScreen(this).getPanel(), "signup");
        root.add(ownerHome.getPanel(), "ownerHome");
        root.add(vetHome.getPanel(), "vetHome");
        root.add(deskHome.getPanel(), "deskHome");

        setContentPane(root);
        setTitle("PawCare - Veterinary Clinic System");
        setSize(380, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showScreen("login");
    }

    public void showScreen(String name) {
        cards.show(root, name);
    }

    public void goHome() {
        switch (currentUser.role) {
            case PET_OWNER -> { ownerHome.refresh(); showScreen("ownerHome"); }
            case VETERINARIAN -> { vetHome.refresh(); showScreen("vetHome"); }
            case FRONT_DESK -> { deskHome.refresh(); showScreen("deskHome"); }
        }
    }

    public void logout() {
        currentUser = null;
        showScreen("login");
    }

    public void comingSoon(String feature) {