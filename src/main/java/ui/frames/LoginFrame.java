package ui.frames;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dao.UserCredentialsDAO;

public class LoginFrame extends JFrame {

    private final Color primaryBlue = new Color(21, 101, 192);
    private final Color darkBlue    = new Color(13, 71, 161);
    private final Color textDark    = new Color(50, 50, 50);
    private final Color textMuted   = new Color(117, 117, 117);
    private final Color fieldBg     = new Color(245, 245, 245);

    private ModernTextField     midField;
    private ModernPasswordField passField;
    private JCheckBox           rememberBox;

    public LoginFrame() {
        setTitle("Pag-CONNECT Member Portal");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientPanel backgroundPanel = new GradientPanel(new Color(10, 25, 47), new Color(21, 101, 192));
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        ImageIcon logoIcon = loadAndScaleIcon("/ui/assets/logo.png", 80, 80);
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

        JLabel portalTitle = new JLabel("Pag-IBIG Member Portal");
        portalTitle.setFont(new Font("Arial", Font.BOLD, 20));
        portalTitle.setForeground(Color.WHITE);
        portalTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(mainTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        headerPanel.add(portalTitle);

        RoundedPanel mainCard = new RoundedPanel(20, Color.WHITE);
        mainCard.setPreferredSize(new Dimension(800, 450));
        mainCard.setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel signInLabel = new JLabel("Sign In");
        signInLabel.setFont(new Font("Arial Black", Font.BOLD, 26));
        signInLabel.setForeground(textDark);
        signInLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel midLabel = new JLabel("PAG-IBIG 12-DIGIT MID NO.");
        midLabel.setFont(new Font("Arial", Font.BOLD, 11));
        midLabel.setForeground(textMuted);
        midLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        midField = new ModernTextField("1234-5678-9012", fieldBg);
        midField.setAlignmentX(Component.LEFT_ALIGNMENT);
        midField.setMaximumSize(new Dimension(400, 40));

        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Arial", Font.BOLD, 11));
        passLabel.setForeground(textMuted);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Password field wrapped in a container with eye-toggle button ──────
        passField = new ModernPasswordField("", fieldBg);

        JPanel passWrapper = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fieldBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        passWrapper.setOpaque(false);
        passWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        passWrapper.setMaximumSize(new Dimension(400, 40));
        passWrapper.setPreferredSize(new Dimension(400, 40));

        // Eye toggle button
        JButton eyeBtn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                boolean isVisible = passField.getEchoChar() == (char) 0
                        || (passField.getEchoChar() != '•' && passField.getEchoChar() != '*');

                if (isVisible) {
                    // Eye-open icon (drawn manually)
                    drawEyeOpen(g2, cx, cy);
                } else {
                    // Eye-closed icon (drawn manually)
                    drawEyeClosed(g2, cx, cy);
                }
                g2.dispose();
            }

            private void drawEyeOpen(Graphics2D g2, int cx, int cy) {
                g2.setColor(new Color(117, 117, 117));
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Outer eye arc
                g2.drawArc(cx - 8, cy - 5, 16, 10, 0, 180);
                g2.drawArc(cx - 8, cy - 5, 16, 10, 180, 180);
                // Pupil
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawOval(cx - 3, cy - 3, 6, 6);
                // Pupil fill
                g2.setColor(new Color(117, 117, 117, 180));
                g2.fillOval(cx - 2, cy - 2, 4, 4);
            }

            private void drawEyeClosed(Graphics2D g2, int cx, int cy) {
                g2.setColor(new Color(117, 117, 117));
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Upper arc only
                g2.drawArc(cx - 8, cy - 5, 16, 10, 0, 180);
                // Slash line through
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx - 6, cy + 4, cx + 6, cy - 4);
            }
        };

        eyeBtn.setPreferredSize(new Dimension(36, 40));
        eyeBtn.setContentAreaFilled(false);
        eyeBtn.setBorderPainted(false);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setOpaque(false);
        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeBtn.setToolTipText("Show/Hide Password");

        // Track whether password is being shown
        final boolean[] passwordVisible = {false};

        eyeBtn.addActionListener(e -> {
            passwordVisible[0] = !passwordVisible[0];

            // Only toggle if it's not showing placeholder
            boolean isPlaceholder = passField.getForeground().equals(Color.GRAY);
            if (!isPlaceholder) {
                if (passwordVisible[0]) {
                    passField.setEchoChar((char) 0);   // show password
                } else {
                    passField.setEchoChar('•');         // hide password
                }
            }
            eyeBtn.repaint();
        });

        // Sync eye button state when field gains/loses focus
        passField.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {
                // Reset eye to hidden when user starts typing
                if (passField.getForeground().equals(Color.GRAY)) {
                    passwordVisible[0] = false;
                    eyeBtn.repaint();
                }
            }
            @Override public void focusLost(FocusEvent e) {
                // If field is empty and goes back to placeholder, reset
                if (String.valueOf(passField.getPassword()).isEmpty()) {
                    passwordVisible[0] = false;
                    eyeBtn.repaint();
                }
            }
        });

        passWrapper.add(passField, BorderLayout.CENTER);
        passWrapper.add(eyeBtn,   BorderLayout.EAST);
        // ─────────────────────────────────────────────────────────────────────

        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);
        optionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsRow.setMaximumSize(new Dimension(400, 30));

        rememberBox = new JCheckBox("Remember Me");
        rememberBox.setOpaque(false);
        rememberBox.setFont(new Font("Arial", Font.PLAIN, 12));
        rememberBox.setForeground(textMuted);
        rememberBox.setFocusPainted(false);

        JButton resetPassBtn = new JButton("Reset Password");
        resetPassBtn.setContentAreaFilled(false);
        resetPassBtn.setBorderPainted(false);
        resetPassBtn.setFocusPainted(false);
        resetPassBtn.setForeground(primaryBlue);
        resetPassBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        resetPassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        optionsRow.add(rememberBox,   BorderLayout.WEST);
        optionsRow.add(resetPassBtn,  BorderLayout.EAST);

        JButton signInBtn = createStyledButton("Sign In", primaryBlue, Color.WHITE);
        signInBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInBtn.setMaximumSize(new Dimension(400, 45));
        signInBtn.addActionListener(e -> {
            String mid  = midField.getText().trim();
            String pass = String.valueOf(passField.getPassword()).trim();

            if (mid.isEmpty() || mid.equals("1234-5678-9012") || pass.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your MID Number and Password.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserCredentialsDAO dao = new UserCredentialsDAO();
            boolean valid = dao.verifyLogin(mid, pass);

            if (valid) {
                new SignInFrame(mid);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid MID Number or Password. Please try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        resetPassBtn.addActionListener(e -> {
            new PasswordRecoveryFrame().setVisible(true);
            dispose();
        });

        leftPanel.add(signInLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(midLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(midField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(passLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(passWrapper);   // ← use wrapper instead of passField directly
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(optionsRow);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(signInBtn);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton adminAccessBtn = new JButton("Admin Access →");
        adminAccessBtn.setContentAreaFilled(false);
        adminAccessBtn.setBorderPainted(false);
        adminAccessBtn.setFocusPainted(false);
        adminAccessBtn.setForeground(textMuted);
        adminAccessBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        adminAccessBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminAccessBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminAccessBtn.addActionListener(e -> {
            new AdminLoginFrame();
            dispose();
        });

        leftPanel.add(adminAccessBtn);

        GradientPanel rightPanel = new GradientPanel(primaryBlue, darkBlue) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, primaryBlue, getWidth(), getHeight(), darkBlue));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.fillRect(0, 0, 20, getHeight());
                g2.dispose();
            }
        };
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel rightTitle = new JLabel("Sign up");
        rightTitle.setFont(new Font("Arial Black", Font.BOLD, 36));
        rightTitle.setForeground(Color.WHITE);
        rightTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rightSub = new JLabel("<html><div style='text-align:center'>Create your account to<br>access the portal.</div></html>");
        rightSub.setFont(new Font("Arial", Font.PLAIN, 14));
        rightSub.setForeground(new Color(200, 225, 255));
        rightSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSub.setHorizontalAlignment(SwingConstants.CENTER);

        JButton registerBtn = createOutlinedButton("Sign Up");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(200, 45));
        registerBtn.addActionListener(e -> {
            new SignUpFrame();
            dispose();
        });

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(rightTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(rightSub);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        rightPanel.add(registerBtn);
        rightPanel.add(Box.createVerticalGlue());

        mainCard.add(leftPanel);
        mainCard.add(rightPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(headerPanel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        backgroundPanel.add(mainCard, gbc);

        add(backgroundPanel);
        setVisible(true);
    }

    private ImageIcon loadAndScaleIcon(String resourceName, int width, int height) {
        try {
            java.net.URL imgUrl = getClass().getResource(resourceName);
            if (imgUrl == null) { System.err.println("Resource not found: " + resourceName); return null; }
            Image scaled = new ImageIcon(imgUrl).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception ex) {
            System.err.println("Could not load " + resourceName + ": " + ex.getMessage());
            return null;
        }
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
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createOutlinedButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                }
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Inner classes ────────────────────────────────────────────────────────

    class GradientPanel extends JPanel {
        private final Color color1, color2;
        public GradientPanel(Color c1, Color c2) { this.color1 = c1; this.color2 = c2; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius; this.bgColor = bgColor; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, radius, radius);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, radius, radius);
        }
    }

    class ModernTextField extends JTextField {
        public ModernTextField(String placeholder, Color bgColor) {
            setOpaque(false);
            setBorder(new EmptyBorder(10, 15, 10, 15));
            setFont(new Font("Arial", Font.PLAIN, 14));
            setForeground(Color.GRAY);
            setText(placeholder);
            addFocusListener(new FocusListener() {
                @Override public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) { setText(""); setForeground(Color.BLACK); }
                }
                @Override public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) { setText(placeholder); setForeground(Color.GRAY); }
                }
            });
            addKeyListener(new java.awt.event.KeyAdapter() {
                @Override public void keyReleased(java.awt.event.KeyEvent e) {
                    if (getForeground().equals(Color.GRAY)) return;
                    String text = getText().replaceAll("[^0-9]", "");
                    if (text.length() > 12) text = text.substring(0, 12);
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < text.length(); i++) {
                        if (i > 0 && i % 4 == 0) formatted.append("-");
                        formatted.append(text.charAt(i));
                    }
                    setText(formatted.toString());
                }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(245, 245, 245));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    /**
     * Password field with placeholder support.
     * The eye-toggle button lives OUTSIDE this class (in the passWrapper panel).
     * Echo char is managed by the toggle button's ActionListener.
     */
    class ModernPasswordField extends JPasswordField {
        private final String placeholder = "";

        public ModernPasswordField(String placeholder, Color bgColor) {
            setOpaque(false);
            setBorder(new EmptyBorder(10, 15, 10, 5));  // reduced right padding; eye btn takes the space
            setFont(new Font("Arial", Font.PLAIN, 14));
            setForeground(Color.GRAY);
            setText(placeholder);
            setEchoChar((char) 0);  // show placeholder text as plain

            addFocusListener(new FocusListener() {
                @Override public void focusGained(FocusEvent e) {
                    if (String.valueOf(getPassword()).equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                        setEchoChar('•');  // hide by default when user starts typing
                    }
                }
                @Override public void focusLost(FocusEvent e) {
                    if (String.valueOf(getPassword()).isEmpty()) {
                        setEchoChar((char) 0);
                        setText(placeholder);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(245, 245, 245));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}