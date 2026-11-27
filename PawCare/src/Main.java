import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Collections;
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
    private final EditProfileScreen editProfile = new EditProfileScreen(this);

    public Main() {
        root.add(centered(new LoginScreen(this).getPanel(), 380, 320), "login");
        root.add(centered(new SignUpScreen(this).getPanel(), 380, 470), "signup");
        root.add(centered(ownerHome.getPanel(), 380, 440), "ownerHome");
        root.add(centered(vetHome.getPanel(), 380, 400), "vetHome");
        root.add(centered(deskHome.getPanel(), 380, 440), "deskHome");
        root.add(centered(editProfile.getPanel(), 400, 520), "editProfile");

        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(root, BorderLayout.CENTER);

        setTitle("PawCare - Veterinary Clinic System");
        setSize(730, 600);
        setMinimumSize(new Dimension(700, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showScreen("login");
    }

    // styles a screen and centers it at a fixed size on the mint background
    private JPanel centered(JPanel screen, int width, int height) {
        Theme.styleScreen(screen);
        screen.setPreferredSize(new Dimension(width, height));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Theme.BACKGROUND);
        wrapper.add(screen);
        return wrapper;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setBackground(Theme.PRIMARY);
        header.setBorder(new MatteBorder(0, 0, 4, 0, Theme.ACCENT));

        JLabel title = new JLabel("PawCare  |  Veterinary Clinic System");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        header.add(title);
        return header;
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

    public void showEditProfile() {
        editProfile.refresh();
        showScreen("editProfile");
    }

    public void logout() {
        currentUser = null;
        showScreen("login");
    }

    public void comingSoon(String feature) {
        JOptionPane.showMessageDialog(this, feature + " (coming soon)");
    }

    private static void setGlobalFont(Font font) {
        for (Object key : Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(key) instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        setGlobalFont(new Font("Segoe UI", Font.PLAIN, 14));

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}