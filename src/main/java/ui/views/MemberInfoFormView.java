package ui.views;

import dao.MemberDAO;
import models.MemberTable;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MemberInfoFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private final String loggedInMID;

    // ── Fields ───────────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField;
    public JComboBox<String> occupationalStatusBox, membershipTypeBox,
            membershipCategoryBox, maritalStatusBox, sexBox,
            frequencyOfMembershipSavingsBox,
            preferredMailingAddressBox, citizenshipBox;
    public JTextField membershipTypeOthersField;
    public JTextField memberNameField, fatherNameField, motherNameField, spouseNameField;
    public JTextField birthdateField, birthplaceField, crnField;
    public JTextField tinField, sssField, employeeNumberField;
    public JTextField presentHomeAddressField, permanentHomeAddressField;
    public JTextField homeTelNumField, cellphoneNumField;
    public JTextField busDirectLineField, busTrunkLineField, localField, emailAddressField;
    public JTextField allowBasicField, allowOtherSourcesField, totalMoIncomeField;

    // ── Constructors ─────────────────────────────────────────────────────────
    public MemberInfoFormView(String mid) {
        this.loggedInMID = mid;
        initUI();
    }

    public MemberInfoFormView() {
        this.loggedInMID = null;
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
                g2.fillRoundRect(6, 8, getWidth() - 8, getHeight() - 8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(18, 0, getWidth() - 18, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(980, 650));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Member Information");
        title.setForeground(textWhite);
        title.setFont(new Font("Arial Black", Font.BOLD, 24));
        JLabel sub = new JLabel("View member details and information.");
        sub.setForeground(new Color(255, 255, 255, 170));
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(sub);

        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JButton backBtn = buildButton("Back", accentRed);
        backBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(MemberInfoFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
        bottom.add(backBtn);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(bottom,  BorderLayout.SOUTH);
        bg.add(card);
        add(bg, BorderLayout.CENTER);

        if (loggedInMID != null && !loggedInMID.isEmpty()) {
            loadMemberData(loggedInMID);
        } else {
            loadDummyData();
        }
        lockAllFields();
    }

    // ── Load Real Data from DB ────────────────────────────────────────────────
    private void loadMemberData(String mid) {
        MemberDAO dao = new MemberDAO();
        MemberTable m = dao.getMemberById(mid);

        if (m == null) {
            JOptionPane.showMessageDialog(this,
                "No record found for MID: " + mid,
                "Not Found", JOptionPane.WARNING_MESSAGE);
            loadDummyData();
            return;
        }

        pagIbigMidNoField.setText(safe(m.getPagIbigMIDNo()));
        setCombo(membershipTypeBox,               m.getMembershipType());
        membershipTypeOthersField.setText(        safe(m.getMembershipTypeOthers()));
        setCombo(membershipCategoryBox,           m.getMembershipCategory());
        setCombo(occupationalStatusBox,           m.getOccupationalStatus());
        setCombo(frequencyOfMembershipSavingsBox, m.getFrequencyOfMembershipSavings());
        crnField.setText(safe(m.getCrn()));

        memberNameField.setText(  safe(m.getMemberName()));
        fatherNameField.setText(  safe(m.getFatherName()));
        motherNameField.setText(  safe(m.getMotherName()));
        spouseNameField.setText(  safe(m.getSpouseName()));
        birthdateField.setText(   m.getBirthdate() != null ? m.getBirthdate().toString() : "");
        birthplaceField.setText(  safe(m.getBirthplace()));
        setCombo(maritalStatusBox, m.getMaritalStatus());
        setCombo(sexBox,           m.getSex());
        setCombo(citizenshipBox,   m.getCitizenship());
        employeeNumberField.setText(
            m.getEmployeeNumber() != null ? m.getEmployeeNumber().toString() : "");

        tinField.setText(safe(m.getTin()));
        sssField.setText(safe(m.getSss()));

        presentHomeAddressField.setText(  safe(m.getPresentHomeAddress()));
        permanentHomeAddressField.setText(safe(m.getPermanentHomeAddress()));
        setCombo(preferredMailingAddressBox, m.getPreferredMailingAddress());

        cellphoneNumField.setText( safe(m.getCellphoneNum()));
        homeTelNumField.setText(   safe(m.getHomeTelNum()));
        emailAddressField.setText( safe(m.getEmailAddress()));
        busDirectLineField.setText(safe(m.getBusDirectLine()));
        busTrunkLineField.setText( safe(m.getBusTrunkLine()));
        localField.setText(        safe(m.getLocal()));

        allowBasicField.setText(
            m.getAllowBasic()        != null ? m.getAllowBasic().toPlainString()        : "");
        allowOtherSourcesField.setText(
            m.getAllowOtherSources() != null ? m.getAllowOtherSources().toPlainString() : "");
        totalMoIncomeField.setText(
            m.getTotalMoIncome()     != null ? m.getTotalMoIncome().toPlainString()     : "");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void setCombo(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) {
                box.setSelectedIndex(i);
                return;
            }
        }
        box.addItem(value);
        box.setSelectedItem(value);
    }

    private String safe(String s) { return s != null ? s : ""; }

    // ── Build Form Content ────────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 0, 20, 0));

        c.add(sectionHeader("Membership Information"));
        c.add(vgap(12));
        JPanel r1 = row(3);
        r1.add(lf("Pag-IBIG MID No.",        pagIbigMidNoField        = tf()));
        r1.add(lf("Membership Type",          membershipTypeBox        = cb(new String[]{
                "Select", "Employed", "Overseas Filipino Worker", "Self-Employed", "Others"
        })));
        r1.add(lf("Membership Type (Others)", membershipTypeOthersField = tf()));
        c.add(r1);
        c.add(vgap(14));

        JPanel r2 = row(2);
        r2.add(lf("Membership Category", membershipCategoryBox = cb(new String[]{
                "Select", "Private", "Government", "Private Household",
                "Overseas Filipino Worker", "Professional/Business Owner",
                "Job Order Personnel", "Other Earning Groups", "Others"
        })));
        r2.add(lf("Occupational Status", occupationalStatusBox = cb(new String[]{
                "Select", "Employed", "Unemployed", "First Time Jobseeker"
        })));
        c.add(r2);
        c.add(vgap(14));

        JPanel r3 = row(2);
        r3.add(lf("Frequency of Membership Savings", frequencyOfMembershipSavingsBox = cb(new String[]{
                "Select", "Monthly", "Quarterly", "Semi-Annual", "Annual"
        })));
        r3.add(lf("CRN", crnField = tf()));
        c.add(r3);
        c.add(vgap(24));

        c.add(sectionHeader("Personal Information"));
        c.add(vgap(12));
        JPanel r4 = row(1);
        r4.add(lf("Member Name", memberNameField = tf()));
        c.add(r4);
        c.add(vgap(14));

        JPanel r5 = row(3);
        r5.add(lf("Father's Name", fatherNameField = tf()));
        r5.add(lf("Mother's Name", motherNameField = tf()));
        r5.add(lf("Spouse's Name", spouseNameField = tf()));
        c.add(r5);
        c.add(vgap(14));

        JPanel r6 = row(3);
        r6.add(lf("Birthdate (YYYY-MM-DD)", birthdateField  = tf()));
        r6.add(lf("Birthplace",             birthplaceField = tf()));
        r6.add(lf("Marital Status",         maritalStatusBox = cb(new String[]{
                "Select", "Single", "Married", "Widowed", "Legally Separated", "Annulled"
        })));
        c.add(r6);
        c.add(vgap(14));

        JPanel r7 = row(3);
        r7.add(lf("Sex",          sexBox         = cb(new String[]{"Select", "Male", "Female"})));
        r7.add(lf("Citizenship",  citizenshipBox = cb(new String[]{"Select", "Filipino", "Other"})));
        r7.add(lf("Employee No.", employeeNumberField = tf()));
        c.add(r7);
        c.add(vgap(24));

        c.add(sectionHeader("Government IDs"));
        c.add(vgap(12));
        JPanel r8 = row(2);
        r8.add(lf("TIN",     tinField = tf()));
        r8.add(lf("SSS No.", sssField = tf()));
        c.add(r8);
        c.add(vgap(24));

        c.add(sectionHeader("Address Information"));
        c.add(vgap(12));
        JPanel r9 = row(1);
        r9.add(lf("Present Home Address", presentHomeAddressField = tf()));
        c.add(r9);
        c.add(vgap(14));

        JPanel r10 = row(1);
        r10.add(lf("Permanent Home Address", permanentHomeAddressField = tf()));
        c.add(r10);
        c.add(vgap(14));

        JPanel r11 = row(2);
        r11.add(lf("Preferred Mailing Address", preferredMailingAddressBox = cb(new String[]{
                "Select", "Present Home Address", "Permanent Home Address", "Employer/Business Address"
        })));
        r11.add(lf("", new JLabel()));
        c.add(r11);
        c.add(vgap(24));

        c.add(sectionHeader("Contact Information"));
        c.add(vgap(12));
        JPanel r12 = row(3);
        r12.add(lf("Cellphone No. (+63...)", cellphoneNumField = tf()));
        r12.add(lf("Home Telephone No.",     homeTelNumField   = tf()));
        r12.add(lf("Email Address",          emailAddressField = tf()));
        c.add(r12);
        c.add(vgap(14));

        JPanel r13 = row(3);
        r13.add(lf("Business Direct Line", busDirectLineField = tf()));
        r13.add(lf("Business Trunk Line",  busTrunkLineField  = tf()));
        r13.add(lf("Local/Extension",      localField         = tf()));
        c.add(r13);
        c.add(vgap(24));

        c.add(sectionHeader("Income Information"));
        c.add(vgap(12));
        JPanel r14 = row(3);
        r14.add(lf("Basic Allowance",                       allowBasicField        = tf()));
        r14.add(lf("Other Sources",                         allowOtherSourcesField = tf()));
        r14.add(lf("Total Monthly Income (System-Derived)", totalMoIncomeField     = tf()));
        c.add(r14);

        return c;
    }

    // ── Dummy Data (fallback) ─────────────────────────────────────────────────
    private void loadDummyData() {
        pagIbigMidNoField.setText("1234-5678-9012-00");
        membershipTypeBox.setSelectedItem("Employed");
        membershipTypeOthersField.setText("");
        membershipCategoryBox.setSelectedItem("Private");
        occupationalStatusBox.setSelectedItem("Employed");
        frequencyOfMembershipSavingsBox.setSelectedItem("Monthly");
        crnField.setText("CRN-001234");
        memberNameField.setText("Rizal, Jose Protacio Mercado");
        fatherNameField.setText("Francisco Mercado");
        motherNameField.setText("Teodora Alonso");
        spouseNameField.setText("");
        birthdateField.setText("1861-06-19");
        birthplaceField.setText("Calamba, Laguna");
        maritalStatusBox.setSelectedItem("Single");
        sexBox.setSelectedItem("Male");
        citizenshipBox.setSelectedItem("Filipino");
        employeeNumberField.setText("1001");
        tinField.setText("123-456-789-000");
        sssField.setText("33-1234567-8");
        presentHomeAddressField.setText("123 Sampaguita St., Quezon City, Metro Manila");
        permanentHomeAddressField.setText("45 Mabini St., Calamba, Laguna");
        preferredMailingAddressBox.setSelectedItem("Present Home Address");
        cellphoneNumField.setText("+639171234567");
        homeTelNumField.setText("02-8123-4567");
        emailAddressField.setText("jose.rizal@email.com");
        busDirectLineField.setText("02-8765-4321");
        busTrunkLineField.setText("02-8000-0000");
        localField.setText("101");
        allowBasicField.setText("15000.00");
        allowOtherSourcesField.setText("5000.00");
        totalMoIncomeField.setText("20000.00");
    }

    // ── Lock All Fields ───────────────────────────────────────────────────────
    private void lockAllFields() {
        JTextField[] textFields = {
                pagIbigMidNoField, membershipTypeOthersField,
                memberNameField, fatherNameField, motherNameField, spouseNameField,
                birthdateField, birthplaceField, crnField,
                tinField, sssField, employeeNumberField,
                presentHomeAddressField, permanentHomeAddressField,
                homeTelNumField, cellphoneNumField,
                busDirectLineField, busTrunkLineField, localField, emailAddressField,
                allowBasicField, allowOtherSourcesField, totalMoIncomeField
        };
        for (JTextField f : textFields) {
            f.setEditable(false);
            f.setFocusable(false);
        }
        JComboBox<?>[] combos = {
                occupationalStatusBox, membershipTypeBox, membershipCategoryBox,
                maritalStatusBox, sexBox, frequencyOfMembershipSavingsBox,
                preferredMailingAddressBox, citizenshipBox
        };
        for (JComboBox<?> b : combos) b.setEnabled(false);
    }

    // ── Section Header ────────────────────────────────────────────────────────
    private JPanel sectionHeader(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(text);
        l.setForeground(accentGreen);
        l.setFont(new Font("Arial Black", Font.BOLD, 15));
        p.add(l, BorderLayout.WEST);
        return p;
    }

    // ── Button ────────────────────────────────────────────────────────────────
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
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 46));
        return btn;
    }

    // ── Layout Helpers ────────────────────────────────────────────────────────
    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        return p;
    }

    private JPanel lf(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(new Color(255, 255, 255, 180));
        l.setFont(new Font("Arial", Font.BOLD, 11));
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField tf() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(255, 255, 255, 35));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(new Color(220, 220, 220));
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        return field;
    }

    private JComboBox<String> cb(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 40, 65));
        return box;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Member Info — View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 750);
            f.add(new MemberInfoFormView());
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}