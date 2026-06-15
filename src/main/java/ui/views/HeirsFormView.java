package ui.views;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import dao.HeirsDAO;
import models.HeirsTable;
import ui.frames.SignInFrame;

public class HeirsFormView extends JPanel {

    private final Color darkBg1          = new Color(10, 22, 40);
    private final Color darkBg2          = new Color(21, 101, 192);
    private final Color accentGreen      = new Color(96, 216, 164);
    private final Color accentRed        = new Color(255, 99, 132);
    private final Color textWhite        = Color.WHITE;

    // Scrollbar palette — matches MemberInfoFormView
    private final Color scrollTrack      = new Color(255, 255, 255, 18);
    private final Color scrollThumb      = new Color(96, 216, 164, 120);
    private final Color scrollThumbHover = new Color(96, 216, 164, 200);

    private JPanel  listPanel;
    private JButton editSaveBtn;
    private boolean editMode  = false;
    private int     heirCount = 0;
    public  List<HeirEntry> entries = new ArrayList<HeirEntry>();

    private final String loggedInMid;

    // ── Constructor ──────────────────────────────────────────────────────────
    public HeirsFormView(String loggedInMid) {
        this.loggedInMid = (loggedInMid != null) ? loggedInMid : "";
        initUI();
    }

    // ── Init ─────────────────────────────────────────────────────────────────
    private void initUI() {
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 8, getWidth() - 8, getHeight() - 8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16, 0, getWidth() - 20, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(1000, 700));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        // ── Header ───────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Heirs & Dependents");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("View and update heirs and dependents information.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── List Panel ───────────────────────────────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.setBorder(new EmptyBorder(0, 0, 0, 12)); // right padding for scrollbar breathing room

        // ── Scroll Pane (custom styled) ───────────────────────────────────────
        JScrollPane scrollPane = buildScrollPane(listPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton deleteBtn = buildButton("Delete All",  new Color(200, 50, 50));
        JButton returnBtn = buildButton("Back",         accentRed);
        JButton addBtn    = buildButton("Add Heir",     accentGreen);
        editSaveBtn       = buildButton("Edit",         new Color(251, 191, 36));

        returnBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(HeirsFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMid);
        });

        addBtn.addActionListener(e -> {
            if (!editMode) {
                JOptionPane.showMessageDialog(HeirsFormView.this,
                    "Click Edit before adding a new heir.",
                    "Edit Mode Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            addEntry();
        });

        editSaveBtn.addActionListener(e -> {
            if (!editMode) {
                editMode = true;
                editSaveBtn.setText("Save Changes");
                unlockAllEntries();
            } else {
                handleSave();
            }
        });

        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(HeirsFormView.this,
                "Delete all heirs and dependents? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;
            new HeirsDAO().deleteAllHeirsByMID(loggedInMid);
            JOptionPane.showMessageDialog(HeirsFormView.this, "All heirs and dependents deleted.");
            entries.clear();
            listPanel.removeAll();
            heirCount = 0;
            listPanel.revalidate();
            listPanel.repaint();
            editMode = false;
            editSaveBtn.setText("Edit");
        });

        buttonPanel.add(deleteBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(editSaveBtn);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Content Panel ────────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(heading);
        content.add(Box.createRigidArea(new Dimension(0, 4)));
        content.add(subHeading);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(scrollPane);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(buttonPanel);

        card.add(content, BorderLayout.CENTER);
        bg.add(card);
        add(bg, BorderLayout.CENTER);

        loadFromDatabase();
    }

    // =========================================================================
    // Custom scroll pane — glass-style, thin accent-green thumb
    // =========================================================================
    private JScrollPane buildScrollPane(JPanel content) {
        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = scrollThumb;
                trackColor = scrollTrack;
            }

            // Thin 6px bar
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(6, super.getPreferredSize(c).height);
            }

            // Remove arrow buttons entirely
            @Override
            protected JButton createDecreaseButton(int orientation) { return invisibleBtn(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return invisibleBtn(); }

            private JButton invisibleBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                b.setVisible(false);
                return b;
            }

            // Semi-transparent pill track
            @Override
            protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(scrollTrack);
                g2.fillRoundRect(trackBounds.x + 1, trackBounds.y,
                        trackBounds.width - 2, trackBounds.height, 6, 6);
                g2.dispose();
            }

            // Accent-green pill thumb
            @Override
            protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle thumbBounds) {
                if (thumbBounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isThumbRollover() ? scrollThumbHover : scrollThumb);
                g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 2,
                        thumbBounds.width - 2, thumbBounds.height - 4, 6, 6);
                g2.dispose();
            }
        });

        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 0));

        return scroll;
    }

    // ── Load heirs from DB ────────────────────────────────────────────────────
    private void loadFromDatabase() {
        HeirsDAO dao = new HeirsDAO();
        List<HeirsTable> saved = dao.getHeirsByMID(loggedInMid);

        if (saved.isEmpty()) {
            JLabel noData = new JLabel("No heirs or dependents on record.");
            noData.setForeground(new Color(255, 255, 255, 150));
            noData.setFont(new Font("Arial", Font.ITALIC, 14));
            noData.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(noData);
            return;
        }

        for (HeirsTable heir : saved) {
            heirCount++;
            HeirEntry entry = new HeirEntry(heirCount, this);
            entry.heirCode = heir.getHeirCode();
            entry.pagIbigMidNoField.setText(loggedInMid);
            entry.heirsNameField.setText(heir.getHeirsName());
            entry.heirsRelationshipBox.setSelectedItem(heir.getHeirsRelationship());
            entry.heirsBirthdateField.setText(
                heir.getHeirsBirthdate() != null ? heir.getHeirsBirthdate().toString() : "");
            entries.add(entry);
            listPanel.add(entry);
            listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        }

        lockAllEntries();
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Add a new blank entry ─────────────────────────────────────────────────
    public void addEntry() {
        heirCount++;
        HeirEntry entry = new HeirEntry(heirCount, this);
        entry.pagIbigMidNoField.setText(loggedInMid);
        entry.pagIbigMidNoField.setEditable(false);
        entry.pagIbigMidNoField.setFocusable(false);
        entry.heirsNameField.setEditable(true);
        entry.heirsNameField.setFocusable(true);
        entry.heirsBirthdateField.setEditable(true);
        entry.heirsBirthdateField.setFocusable(true);
        entry.heirsRelationshipBox.setEnabled(true);
        entry.setRemoveBtnVisible(true);
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Remove entry ──────────────────────────────────────────────────────────
    public void removeEntry(HeirEntry entry) {
        if (entries.size() == 1) {
            JOptionPane.showMessageDialog(this,
                "At least one heir entry is required.",
                "Cannot Remove", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
            "Remove this heir / dependent record?",
            "Confirm Remove", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        if (entry.heirCode > 0) new HeirsDAO().deleteHeir(entry.heirCode);

        entries.remove(entry);
        Component[] comps = listPanel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == entry) {
                listPanel.remove(i);
                if (i < listPanel.getComponentCount()) listPanel.remove(i);
                break;
            }
        }
        for (int i = 0; i < entries.size(); i++) entries.get(i).updateNumber(i + 1);
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Unlock all entries ────────────────────────────────────────────────────
    private void unlockAllEntries() {
        for (HeirEntry e : entries) {
            e.heirsNameField.setEditable(true);
            e.heirsNameField.setFocusable(true);
            e.heirsBirthdateField.setEditable(true);
            e.heirsBirthdateField.setFocusable(true);
            e.heirsRelationshipBox.setEnabled(true);
            e.setRemoveBtnVisible(true);
        }
    }

    // ── Lock all entries ──────────────────────────────────────────────────────
    private void lockAllEntries() {
        for (HeirEntry e : entries) {
            e.heirsNameField.setEditable(false);
            e.heirsNameField.setFocusable(false);
            e.heirsBirthdateField.setEditable(false);
            e.heirsBirthdateField.setFocusable(false);
            e.heirsRelationshipBox.setEnabled(false);
            e.setRemoveBtnVisible(false);
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────
    private void handleSave() {
        for (HeirEntry entry : entries) {
            if (entry.heirsNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name for all heirs.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ("Select".equals(entry.heirsRelationshipBox.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Please select a relationship for all heirs.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String birthdate = entry.heirsBirthdateField.getText().trim();
            if (!birthdate.isEmpty()) {
                try { java.sql.Date.valueOf(birthdate); }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Birthdate must be in YYYY-MM-DD format.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        HeirsDAO dao = new HeirsDAO();
        dao.deleteAllHeirsByMID(loggedInMid);
        for (HeirEntry entry : entries) {
            String name         = entry.heirsNameField.getText().trim();
            String relationship = (String) entry.heirsRelationshipBox.getSelectedItem();
            String birthdate    = entry.heirsBirthdateField.getText().trim();
            java.sql.Date bd    = birthdate.isEmpty() ? null : java.sql.Date.valueOf(birthdate);
            dao.insertHeir(new HeirsTable(loggedInMid, 0, name, relationship, bd));
        }

        JOptionPane.showMessageDialog(this, "Heirs and dependents saved successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        editMode = false;
        editSaveBtn.setText("Edit");
        lockAllEntries();
    }

    // =========================================================================
    // HeirEntry inner class
    // =========================================================================
    public class HeirEntry extends JPanel {

        private JLabel  numberLabel;
        private JButton removeBtn;

        public int heirCode = 0;

        public JTextField        pagIbigMidNoField;
        public JTextField        heirsNameField;
        public JTextField        heirsBirthdateField;
        public JComboBox<String> heirsRelationshipBox;

        public HeirEntry(int number, HeirsFormView parent) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel inner = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            inner.setOpaque(false);
            inner.setLayout(new BorderLayout(0, 15));
            inner.setBorder(new EmptyBorder(18, 20, 18, 20));

            // ── Header ───────────────────────────────────────────────────────
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            numberLabel = new JLabel("Heir / Dependent " + number);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);

            removeBtn = new JButton("\u2715 Remove");
            removeBtn.setForeground(new Color(255, 120, 120));
            removeBtn.setFont(new Font("Arial", Font.BOLD, 12));
            removeBtn.setBorderPainted(false);
            removeBtn.setContentAreaFilled(false);
            removeBtn.setFocusPainted(false);
            removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeBtn.setVisible(false);
            removeBtn.addActionListener(e -> parent.removeEntry(this));

            header.add(numberLabel, BorderLayout.WEST);
            header.add(removeBtn,   BorderLayout.EAST);

            // ── Fields ───────────────────────────────────────────────────────
            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            JPanel r1 = row(2);
            r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField = buildTextField()));
            pagIbigMidNoField.setEditable(false);
            pagIbigMidNoField.setFocusable(false);

            r1.add(fieldPanel("HEIR'S NAME", heirsNameField = buildTextField()));
            heirsNameField.setEditable(false);
            heirsNameField.setFocusable(false);

            JPanel r2 = row(2);
            r2.add(fieldPanel("RELATIONSHIP",
                heirsRelationshipBox = buildComboBox(new String[]{
                    "Select", "Spouse", "Child", "Parent", "Sibling", "Legal Heir", "Other"
                })
            ));
            heirsRelationshipBox.setEnabled(false);

            r2.add(fieldPanel("BIRTHDATE (YYYY-MM-DD)", heirsBirthdateField = buildTextField()));
            heirsBirthdateField.setEditable(false);
            heirsBirthdateField.setFocusable(false);

            fields.add(r1);
            fields.add(Box.createRigidArea(new Dimension(0, 16)));
            fields.add(r2);

            inner.add(header, BorderLayout.NORTH);
            inner.add(fields, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);
        }

        public void setRemoveBtnVisible(boolean visible) { removeBtn.setVisible(visible); }
        public void updateNumber(int n) { numberLabel.setText("Heir / Dependent " + n); }
    }

    // =========================================================================
    // UI Helpers
    // =========================================================================

    // ── Text field ─────────────────────────────────────────────────────────────
    private JTextField buildTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                    ? new Color(96, 216, 164, 180)
                    : new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    // ── Combo box — glass style, locked/editable states, dark popup ────────────
    private JComboBox<String> buildComboBox(String[] items) {

        JComboBox<String> box = new JComboBox<String>(items) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isEnabled()) {
                    // Edit mode: glass fill + solid accent-green border
                    g2.setColor(new Color(255, 255, 255, 14));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.setColor(new Color(96, 216, 164, 160));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                } else {
                    // Read-only: near-invisible fill + dashed subtle border
                    g2.setColor(new Color(255, 255, 255, 6));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1f, new float[]{4f, 3f}, 0f));
                    g2.setColor(new Color(255, 255, 255, 45));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        box.setOpaque(false);
        box.setBackground(new Color(0, 0, 0, 0));
        box.setForeground(new Color(225, 225, 225));
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setBorder(new EmptyBorder(8, 12, 8, 8));
        box.setFocusable(true);

        // Force internal editor text to white on init
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Component editor = box.getEditor().getEditorComponent();
                if (editor instanceof JTextField) {
                    JTextField tf = (JTextField) editor;
                    tf.setForeground(new Color(225, 225, 225));
                    tf.setBackground(new Color(0, 0, 0, 0));
                    tf.setOpaque(false);
                    tf.setBorder(new EmptyBorder(0, 0, 0, 0));
                }
            }
        });

        // Re-apply foreground + arrow visibility on enabled state change
        box.addPropertyChangeListener("enabled", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                boolean enabled = Boolean.TRUE.equals(evt.getNewValue());
                Component editor = box.getEditor().getEditorComponent();
                if (editor instanceof JTextField) {
                    ((JTextField) editor).setForeground(
                        enabled ? new Color(225, 225, 225) : new Color(160, 185, 210));
                }
                for (Component comp : box.getComponents()) {
                    if (comp instanceof JButton) {
                        comp.setVisible(enabled);
                    }
                }
                box.repaint();
            }
        });

        // Transparent arrow button
        for (Component comp : box.getComponents()) {
            if (comp instanceof JButton) {
                JButton arrowBtn = (JButton) comp;
                arrowBtn.setOpaque(false);
                arrowBtn.setContentAreaFilled(false);
                arrowBtn.setBorderPainted(false);
                arrowBtn.setForeground(accentGreen);
            }
        }

        // Renderer: muted steel-blue when locked, bright white when editable
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                boolean enabled = box.isEnabled();

                if (index == -1) {
                    // Value displayed inside the closed box
                    setOpaque(false);
                    setBackground(new Color(0, 0, 0, 0));
                    setForeground(enabled ? new Color(225, 225, 225) : new Color(160, 185, 210));
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(new EmptyBorder(0, 0, 0, 0));
                } else {
                    // Dropdown list rows
                    if (isSelected) {
                        setBackground(new Color(21, 101, 192));
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(new Color(13, 32, 64));
                        setForeground(new Color(210, 220, 235));
                    }
                    setOpaque(true);
                    setFont(new Font("Arial", Font.PLAIN, 14));
                    setBorder(new EmptyBorder(7, 12, 7, 12));
                }

                return this;
            }
        });

        // Dark popup + styled scrollbar inside popup
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Object popup = box.getUI().getAccessibleChild(box, 0);
                if (popup instanceof javax.swing.plaf.basic.ComboPopup) {
                    javax.swing.plaf.basic.ComboPopup cp =
                            (javax.swing.plaf.basic.ComboPopup) popup;
                    JList<?> list = cp.getList();
                    list.setBackground(new Color(13, 32, 64));
                    list.setForeground(new Color(210, 220, 235));
                    list.setBorder(new EmptyBorder(4, 0, 4, 0));

                    Component parent = list.getParent();
                    while (parent != null) {
                        if (parent instanceof JScrollPane) {
                            JScrollPane sp = (JScrollPane) parent;
                            sp.setBorder(javax.swing.BorderFactory.createLineBorder(
                                    new Color(96, 216, 164, 100), 1));
                            sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                                @Override protected void configureScrollBarColors() {
                                    thumbColor = new Color(96, 216, 164, 140);
                                    trackColor = new Color(10, 22, 40, 200);
                                }
                                @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
                                @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
                                private JButton zeroBtn() {
                                    JButton b = new JButton();
                                    b.setPreferredSize(new Dimension(0, 0));
                                    b.setVisible(false);
                                    return b;
                                }
                                @Override protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle r) {
                                    g.setColor(new Color(10, 22, 40, 180));
                                    g.fillRect(r.x, r.y, r.width, r.height);
                                }
                                @Override protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle r) {
                                    if (r.isEmpty()) return;
                                    Graphics2D g2 = (Graphics2D) g.create();
                                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                            RenderingHints.VALUE_ANTIALIAS_ON);
                                    g2.setColor(new Color(96, 216, 164, 160));
                                    g2.fillRoundRect(r.x + 1, r.y + 2, r.width - 2, r.height - 4, 4, 4);
                                    g2.dispose();
                                }
                            });
                            break;
                        }
                        parent = parent.getParent();
                    }
                }
            }
        });

        return box;
    }

    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel fieldPanel(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        p.add(lbl,   BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame("Heirs Form View");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(1200, 850);
                f.setLocationRelativeTo(null);
                f.setContentPane(new HeirsFormView("0000-0000-0000"));
                f.setVisible(true);
            }
        });
    }
}