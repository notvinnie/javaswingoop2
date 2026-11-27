import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class Theme {
    public static final Color PRIMARY      = new Color(0x3A8F85);
    public static final Color PRIMARY_DARK = new Color(0x2C746B);
    public static final Color ACCENT       = new Color(0xF4A261);
    public static final Color BACKGROUND   = new Color(0xEAF4F1);
    public static final Color CARD         = Color.WHITE;
    public static final Color TEXT         = new Color(0x2F3E3B);
    public static final Color MUTED        = new Color(0x6B7F7B);
    public static final Color BORDER       = new Color(0xC9DCD7);

    // buttons with these texts get the white outlined style
    private static final Set<String> SECONDARY =
            Set.of("back", "log out", "cancel", "sign up", "back to login");

    // call this on every screen's panel
    public static void styleScreen(JPanel screen) {
        screen.setBackground(CARD);
        screen.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)));
        styleTree(screen, false);
    }

    private static void styleTree(Container container, boolean insideTitled) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton b) {
                styleButton(b);
            } else if (c instanceof JTextComponent f) {
                styleField(f, insideTitled);
            } else if (c instanceof JComboBox<?> box) {
                box.setBackground(Color.WHITE);
                box.setForeground(TEXT);
            } else if (c instanceof JLabel l) {
                l.setForeground(l.getFont().isBold() ? PRIMARY_DARK : TEXT);
            } else if (c instanceof JPanel p) {
                p.setBackground(CARD);
                boolean titled = p.getBorder() instanceof TitledBorder;
                if (titled) {
                    TitledBorder tb = (TitledBorder) p.getBorder();
                    tb.setTitleColor(MUTED);
                    tb.setBorder(new LineBorder(BORDER, 1, true));
                }
                styleTree(p, titled);
            }
        }
    }

    private static void styleButton(JButton b) {
        boolean secondary = SECONDARY.contains(b.getText().trim().toLowerCase());
        Color base  = secondary ? Color.WHITE : PRIMARY;
        Color hover = secondary ? BACKGROUND : PRIMARY_DARK;
        Color text  = secondary ? PRIMARY_DARK : Color.WHITE;

        b.setUI(new BasicButtonUI());          // flat look, respects our colors
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBackground(base);
        b.setForeground(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        b.setBorder(new CompoundBorder(
                new LineBorder(PRIMARY, 1, true),
                new EmptyBorder(8, 16, 8, 16)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(base); }
        });
    }

    private static void styleField(JTextComponent f, boolean insideTitled) {
        f.setBackground(Color.WHITE);
        f.setForeground(TEXT);
        f.setCaretColor(PRIMARY_DARK);
        // titled panels already draw a box, so only pad the field in that case
        f.setBorder(insideTitled
                ? new EmptyBorder(4, 6, 4, 6)
                : new CompoundBorder(new LineBorder(BORDER, 1, true),
                new EmptyBorder(4, 8, 4, 8)));
    }
}