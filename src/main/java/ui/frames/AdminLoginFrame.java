package ui.frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dao.AdminDAO;

public class AdminLoginFrame extends JFrame {

    private final Color darkBg1    = new Color(10, 22, 40);
    private final Color darkBg2    = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color textDark   = new Color(30, 30, 30);
    private final Color textMuted  = new Color(117, 117, 117);
    private final Color fieldBg    = new Color(245, 245, 245);
    private final Color primaryBlue = new Color(21, 101, 192);
    private final Color darkBlue   = new Color(13, 71, 161);

    private JTextField     usernameField;
    private JPasswordField passwordField;

    public AdminLoginFrame() {
        setTitle("Pag-CONNECT — Admin Portal");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── Background ────────────────────────────────────────────────────────
        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        GridBagConstraints gbc = new GridBagConstraints();

        // ── Header ────────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        ImageIcon logoIcon = loadIcon("/ui/assets/logo.png", 80, 80);
        JLabel logoLabel = new JLabel(logoIcon != null ? logoIcon : new ImageIcon());
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mainTitle = new JLabel("Pag-CONNECT");
        mainTitle.setFont(new Font("Arial Black", Font.BOLD, 28));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("A Digital Registration System for Pag-IBIG Fund Applicants");
        subTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subTitle.setForeground(new Color(220, 235, 255));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel portalTitle = new JLabel("Admin Management Portal");
        portalTitle.setFont(new Font("Arial", Font.BOLD, 20));
        portalTitle.setForeground(accentGreen);
        portalTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(mainTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        headerPanel.add(portalTitle);

        // ── Card ──────────────────────────────────────────────────────────────
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setPreferredSize(new Dimension(480, 380));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel signInLabel = new JLabel("Admin Sign In");
        signInLabel.setFont(new Font("Arial Black", Font.BOLD, 24));
        signInLabel.setForeground(textDark);
        signInLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel adminBadge = new JLabel("🔐  Restricted Access");
        adminBadge.setFont(new Font("Arial", Font.BOLD, 11));
        adminBadge.setForeground(new Color(150, 80, 80));
        adminBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setFont(new Font("Arial", Font.BOLD, 11));
        userLabel.setForeground(textMuted);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        styleField(usernameField);
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Arial", Font.BOLD, 11));
        passLabel.setForeground(textMuted);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        styleField(passwordField);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton loginBtn = createStyledButton("Sign In as Admin", primaryBlue, Color.WHITE);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.addActionListener(e -> handleLogin());

        // Allow Enter key to submit
        passwordField.addActionListener(e -> handleLogin());

        JButton backBtn = new JButton("← Back to Member Login");
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setForeground(primaryBlue);
        backBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        card.add(signInLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(adminBadge);
        card.add(Box.createRigidArea(new Dimension(0, 28)));
        card.add(userLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(usernameField);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(passLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 24)));
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(backBtn);

        // ── Layout ────────────────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(headerPanel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        bg.add(card, gbc);

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter username and password.",
                "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AdminDAO dao = new AdminDAO();
        String adminName = dao.verifyAdmin(username, password);

        if (adminName != null) {
            new AdminDashboard(adminName);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void styleField(JTextField field) {
        field.setOpaque(true);
        field.setBackground(fieldBg);
        field.setForeground(Color.DARK_GRAY);
        field.setCaretColor(Color.DARK_GRAY);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(21, 101, 192), 2, true),
                    new EmptyBorder(9, 13, 9, 13)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    new EmptyBorder(10, 14, 10, 14)
                ));
            }
        });
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) return null;
            return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    class RoundedPanel extends JPanel {
        private final int radius; private final Color bg;
        RoundedPanel(int r, Color bg) { this.radius = r; this.bg = bg; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0,0,0,20));
            g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, radius, radius);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, radius, radius);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLoginFrame::new);
    }
}