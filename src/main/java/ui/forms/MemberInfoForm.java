package ui.forms;

import dao.MemberDAO;
import main.RegistrationSession;
import models.MemberTable;
import ui.frames.SignUpFrame;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;

public class MemberInfoForm extends JFrame {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color textWhite   = Color.WHITE;

    // ── Fields ───────────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField;
    public JComboBox<String> occupationalStatusBox, membershipTypeBox, membershipCategoryBox,
            maritalStatusBox, sexBox, frequencyOfMembershipSavingsBox,
            preferredMailingAddressBox, citizenshipBox;

    public JTextField membershipTypeOthersField;
    public JTextField membershipCategoryOthersField;
    public JTextField memberNameField, fatherNameField, motherNameField, spouseNameField;
    public JTextField birthdateField, birthplaceField, crnField;
    public JTextField tinField, sssField, employeeNumberField;
    public JTextField presentHomeAddressField, permanentHomeAddressField;
    public JTextField homeTelNumField, cellphoneNumField;
    public JTextField busDirectLineField, busTrunkLineField, localField, emailAddressField;
    public JTextField allowBasicField, allowOtherSourcesField, totalMoIncomeField;

    // ── "Others" wrapper panels (shown/hidden) ───────────────────────────────
    private JPanel membershipTypeOthersPanel;
    private JPanel membershipCategoryOthersPanel;

    // ADDED: Flag to track whether a DB record already exists for this MID.
    // Used to decide between INSERT (first save) and UPDATE (subsequent saves),
    // and to skip the "unsaved changes" warning on Back when data is already stored.
    private boolean recordExists = false; // ADDED

    public MemberInfoForm() {

        setTitle("Pag-CONNECT — Member Information");
        setSize(1100, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        setContentPane(bg);

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
                g2.drawLine(16, 0, getWidth() - 16, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(950, 660));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Member Information Form");
        title.setFont(new Font("Arial Black", Font.BOLD, 24));
        title.setForeground(textWhite);

        JLabel sub = new JLabel("Fill out the required information below.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 170));

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(sub);

        // ── Scroll Content ───────────────────────────────────────────────────
        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Bottom Buttons ───────────────────────────────────────────────────
        JButton backBtn   = buildButton("Back",  accentRed);
        JButton submitBtn = buildButton("Save",  accentGreen);

        backBtn.addActionListener(e -> { // CHANGED: listener body replaced — now branches on recordExists
            if (recordExists) { // ADDED: skip confirmation if record is already saved
                dispose(); // ADDED: go back immediately, no data loss risk
                SwingUtilities.invokeLater(() -> new SignUpFrame()); // ADDED
            } else { // ADDED: only show warning when no record has been saved yet
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to go back?\nUnsaved changes will be lost.",
                        "Return to Sign Up",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    SwingUtilities.invokeLater(() -> new SignUpFrame());
                }
            } // ADDED: end of recordExists else-branch
        });

        submitBtn.addActionListener(e -> handleSave());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
        bottom.add(backBtn);
        bottom.add(submitBtn);

        // ── Assemble ─────────────────────────────────────────────────────────
        card.add(header, BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(bottom,  BorderLayout.SOUTH);
        bg.add(card);

        prefillFromDb(); // ADDED: populate fields from DB/session before showing the form

        setVisible(true);
    }

    // ── ADDED: prefillFromDb() ────────────────────────────────────────────────
    // Queries the DB for an existing record matching the session MID.
    // Falls back to the session cache if the DB returns null.
    // Sets recordExists = true and populates all fields/combos when found.
    private void prefillFromDb() { // ADDED
        String mid = RegistrationSession.getInstance().getTempMID(); // ADDED
        if (mid == null || mid.isEmpty()) return; // ADDED: skip if no MID in session

        MemberDAO dao = new MemberDAO(); // ADDED
        MemberTable existing = dao.getMemberById(mid); // ADDED: query DB for existing record

        if (existing == null) { // ADDED: DB had no record — try session cache
            existing = RegistrationSession.getInstance().getMemberData(); // ADDED: fall back to cached data
        } // ADDED

        if (existing == null) return; // ADDED: nothing found anywhere — leave form blank

        recordExists = true; // ADDED: mark so handleSave() calls UPDATE, not INSERT

        // ADDED: Populate all text fields with stored values (null-safe via setText())
        setText(memberNameField,           existing.getMemberName()); // ADDED
        setText(fatherNameField,           existing.getFatherName()); // ADDED
        setText(motherNameField,           existing.getMotherName()); // ADDED
        setText(spouseNameField,           existing.getSpouseName()); // ADDED
        setText(birthdateField,            existing.getBirthdate() != null // ADDED
                                               ? existing.getBirthdate().toString() : ""); // ADDED
        setText(birthplaceField,           existing.getBirthplace()); // ADDED
        setText(crnField,                  existing.getCrn()); // ADDED
        setText(tinField,                  existing.getTin()); // ADDED
        setText(sssField,                  existing.getSss()); // ADDED
        setText(employeeNumberField,       existing.getEmployeeNumber() != null // ADDED
                                               ? String.valueOf(existing.getEmployeeNumber()) : ""); // ADDED
        setText(presentHomeAddressField,   existing.getPresentHomeAddress()); // ADDED
        setText(permanentHomeAddressField, existing.getPermanentHomeAddress()); // ADDED
        setText(homeTelNumField,           existing.getHomeTelNum()); // ADDED
        setText(cellphoneNumField,         existing.getCellphoneNum()); // ADDED
        setText(busDirectLineField,        existing.getBusDirectLine()); // ADDED
        setText(busTrunkLineField,         existing.getBusTrunkLine()); // ADDED
        setText(localField,                existing.getLocal()); // ADDED
        setText(emailAddressField,         existing.getEmailAddress()); // ADDED
        setText(allowBasicField,           existing.getAllowBasic() != null // ADDED
                                               ? existing.getAllowBasic().toPlainString() : ""); // ADDED
        setText(allowOtherSourcesField,    existing.getAllowOtherSources() != null // ADDED
                                               ? existing.getAllowOtherSources().toPlainString() : ""); // ADDED
        setText(totalMoIncomeField,        existing.getTotalMoIncome() != null // ADDED
                                               ? existing.getTotalMoIncome().toPlainString() : ""); // ADDED

        // ADDED: Restore combo selections using reverse-mapper methods (DB enum → UI display value)
        setCombo(occupationalStatusBox,           fromDbOccupational(existing.getOccupationalStatus())); // ADDED
        setCombo(membershipTypeBox,               fromDbMembershipType(existing.getMembershipType())); // ADDED
        setCombo(membershipCategoryBox,           fromDbMembershipCategory(existing.getMembershipCategory())); // ADDED
        setCombo(maritalStatusBox,                fromDbMarital(existing.getMaritalStatus())); // ADDED
        setCombo(sexBox,                          toTitleCase(existing.getSex())); // ADDED
        setCombo(citizenshipBox,                  existing.getCitizenship() != null // ADDED
                                                      ? existing.getCitizenship() : "Select"); // ADDED
        setCombo(frequencyOfMembershipSavingsBox, existing.getFrequencyOfMembershipSavings()); // ADDED
        setCombo(preferredMailingAddressBox,      existing.getPreferredMailingAddress()); // ADDED

        // ADDED: Reveal "Others" specify panels and fill their fields if applicable
        if ("Others".equals(membershipTypeBox.getSelectedItem())) { // ADDED
            setText(membershipTypeOthersField, existing.getMembershipTypeOthers()); // ADDED
            membershipTypeOthersPanel.setVisible(true); // ADDED
        } // ADDED
        if ("Others".equals(membershipCategoryBox.getSelectedItem())) { // ADDED
            setText(membershipCategoryOthersField, existing.getMembershipCategoryOthers()); // ADDED
            membershipCategoryOthersPanel.setVisible(true); // ADDED
        } // ADDED

        // ADDED: Sync session so other frames can access member data without another DB hit
        RegistrationSession session = RegistrationSession.getInstance(); // ADDED
        session.setMemberData(existing); // ADDED
        session.setMemberInfoDone(true); // ADDED
    } // ADDED

    // ── ADDED: setText() — null-safe helper for populating text fields ────────
    private void setText(JTextField field, String value) { // ADDED
        if (field != null) field.setText(value != null ? value : ""); // ADDED: guards against null field and null value
    } // ADDED

    // ── ADDED: setCombo() — selects the matching item in a combo box ──────────
    // Loops through items and does a case-insensitive match.
    // Leaves the combo on its current selection if no match is found.
    private void setCombo(JComboBox<String> box, String value) { // ADDED
        if (box == null || value == null) return; // ADDED: null guard
        for (int i = 0; i < box.getItemCount(); i++) { // ADDED
            if (value.equalsIgnoreCase(box.getItemAt(i))) { // ADDED: case-insensitive match
                box.setSelectedIndex(i); // ADDED: select matched item
                return; // ADDED
            } // ADDED
        } // ADDED
        // ADDED: no match found — leave combo as-is ("Select")
    } // ADDED

    // ── ADDED: Reverse mappers — DB enum strings → UI display values ──────────
    // These are the inverse of toDbEnum/toMembershipTypeEnum/etc.
    // Required so prefillFromDb() can restore combo selections from stored values.

    private String fromDbOccupational(String db) { // ADDED
        if (db == null) return "Select"; // ADDED
        switch (db) { // ADDED
            case "EMPLOYED":              return "Employed"; // ADDED
            case "UNEMPLOYED":            return "Unemployed"; // ADDED
            case "FIRST TIME JOBSEEKERS": return "First Time Jobseeker"; // ADDED
            default:                      return "Select"; // ADDED
        } // ADDED
    } // ADDED

    private String fromDbMembershipType(String db) { // ADDED
        if (db == null) return "Select"; // ADDED
        switch (db) { // ADDED
            case "EMPLOYED":                 return "Employed"; // ADDED
            case "OVERSEAS FILIPINO WORKER": return "Overseas Filipino Worker"; // ADDED
            case "SELF-EMPLOYED":            return "Self-Employed"; // ADDED
            default:                         return "Others"; // ADDED: unrecognized value maps to "Others"
        } // ADDED
    } // ADDED

    private String fromDbMembershipCategory(String db) { // ADDED
        if (db == null) return "Select"; // ADDED
        switch (db) { // ADDED
            case "PRIVATE":                     return "Private"; // ADDED
            case "GOVERNMENT":                  return "Government"; // ADDED
            case "PRIVATE HOUSEHOLD":           return "Private Household"; // ADDED
            case "OVERSEAS FILIPINO WORKER":    return "Overseas Filipino Worker"; // ADDED
            case "PROFESSIONAL/BUSINESS OWNER": return "Professional/Business Owner"; // ADDED
            case "JOB ORDER PERSONNEL":         return "Job Order Personnel"; // ADDED
            case "OTHER EARNING GROUPS":        return "Other Earning Groups"; // ADDED
            default:                            return "Others"; // ADDED
        } // ADDED
    } // ADDED

    private String fromDbMarital(String db) { // ADDED
        if (db == null) return "Select"; // ADDED
        switch (db) { // ADDED
            case "SINGLE":            return "Single"; // ADDED
            case "MARRIED":           return "Married"; // ADDED
            case "WIDOWED":           return "Widowed"; // ADDED
            case "LEGALLY SEPARATED": return "Legally Separated"; // ADDED
            case "ANNULED":           return "Annulled"; // ADDED: DB has typo "ANNULED"; UI shows correct spelling
            default:                  return "Select"; // ADDED
        } // ADDED
    } // ADDED

    // ── ADDED: toTitleCase() — converts "MALE"/"FEMALE" to "Male"/"Female" ────
    // Needed so setCombo() can match sex values stored as uppercase in the DB.
    private String toTitleCase(String s) { // ADDED
        if (s == null || s.isEmpty()) return "Select"; // ADDED
        return s.charAt(0) + s.substring(1).toLowerCase(); // ADDED: uppercase first char, lowercase the rest
    } // ADDED

    // ── CHANGED: handleSave() — now does INSERT or UPDATE based on recordExists ─
    private void handleSave() {

        // ── Validate required fields ─────────────────────────────────────────
        if (isBlank(memberNameField)
                || isBlank(birthdateField)
                || isBlank(birthplaceField)
                || isBlank(cellphoneNumField)
                || isBlank(presentHomeAddressField)
                || isBlank(permanentHomeAddressField)
                || isBlank(allowBasicField)
                || isComboDefault(occupationalStatusBox)
                || isComboDefault(membershipTypeBox)
                || isComboDefault(membershipCategoryBox)
                || isComboDefault(maritalStatusBox)
                || isComboDefault(sexBox)
                || isComboDefault(citizenshipBox)
                || isComboDefault(frequencyOfMembershipSavingsBox)
                || isComboDefault(preferredMailingAddressBox)) {
            showError("Please fill in all required (*) fields.");
            return;
        }

        // ── Validate birthdate format ────────────────────────────────────────
        Date birthdate;
        try {
            birthdate = Date.valueOf(birthdateField.getText().trim());
        } catch (IllegalArgumentException ex) {
            showError("Birthdate must be in YYYY-MM-DD format.");
            return;
        }

        // ── Build MemberTable object ─────────────────────────────────────────
        String mid = RegistrationSession.getInstance().getTempMID();

        String membershipType = (String) membershipTypeBox.getSelectedItem();
        String membershipTypeOthers = membershipType.equals("Others")
                ? membershipTypeOthersField.getText().trim()
                : null;

        String membershipCategory = (String) membershipCategoryBox.getSelectedItem();
        String membershipCategoryOthers = membershipCategory.equals("Others")
                ? membershipCategoryOthersField.getText().trim()
                : null;

        String occupationalStatus = toDbEnum((String) occupationalStatusBox.getSelectedItem());
        String membershipTypeDb   = toMembershipTypeEnum(membershipType);
        String membershipCatDb    = toMembershipCategoryEnum(membershipCategory);
        String maritalStatus      = toMaritalEnum((String) maritalStatusBox.getSelectedItem());
        String sex                = ((String) sexBox.getSelectedItem()).toUpperCase();
        String citizenship        = citizenshipBox.getSelectedItem().equals("Filipino") ? "Filipino" : citizenshipBox.getSelectedItem().toString();
        String frequency          = (String) frequencyOfMembershipSavingsBox.getSelectedItem();
        String mailingAddress     = toMailingEnum((String) preferredMailingAddressBox.getSelectedItem());

        BigDecimal allowBasic  = parseBigDecimal(allowBasicField.getText());
        BigDecimal allowOther  = parseBigDecimal(allowOtherSourcesField.getText());
        BigDecimal totalIncome = allowBasic.add(allowOther);

        Integer empNumber = null;
        String empNumText = employeeNumberField.getText().trim();
        if (!empNumText.isEmpty()) {
            try { empNumber = Integer.parseInt(empNumText); }
            catch (NumberFormatException ex) {
                showError("Employee Number must be a valid number.");
                return;
            }
        }

        MemberTable member = new MemberTable(
                mid,
                occupationalStatus,
                membershipTypeDb,
                membershipTypeOthers,
                membershipCatDb,
                membershipCategoryOthers,
                memberNameField.getText().trim(),
                fatherNameField.getText().trim(),
                motherNameField.getText().trim(),
                spouseNameField.getText().trim(),
                birthdate,
                maritalStatus,
                birthplaceField.getText().trim(),
                citizenship,
                sex,
                crnField.getText().trim(),
                frequency,
                tinField.getText().trim(),
                sssField.getText().trim(),
                empNumber,
                presentHomeAddressField.getText().trim(),
                permanentHomeAddressField.getText().trim(),
                mailingAddress,
                homeTelNumField.getText().trim(),
                cellphoneNumField.getText().trim(),
                busDirectLineField.getText().trim(),
                busTrunkLineField.getText().trim(),
                localField.getText().trim(),
                emailAddressField.getText().trim(),
                allowBasic,
                allowOther,
                totalIncome
        );

        // CHANGED: was always dao.insertMember(); now branches on recordExists
        MemberDAO dao = new MemberDAO();
        boolean saved; // CHANGED: was assigned directly from insertMember()

        if (recordExists) { // ADDED: record already in DB — run UPDATE
            saved = dao.updateMember(member); // ADDED: call updateMember() instead of insertMember()
        } else { // ADDED: no record yet — run INSERT
            saved = dao.insertMember(member); // CHANGED: same call as before, but now inside the else-branch
            if (saved) recordExists = true; // ADDED: flip flag so next Save in same session calls UPDATE
        } // ADDED

        if (!saved) {
            showError("Failed to save. Please check your connection and try again.");
            return;
        }

        // ── Sync session ─────────────────────────────────────────────────────
        RegistrationSession session = RegistrationSession.getInstance();
        session.setMemberData(member);
        session.setMemberInfoDone(true);

        // CHANGED: success message now says "updated" vs "saved" based on which operation ran
        JOptionPane.showMessageDialog(this,
                recordExists ? "Member information updated successfully!" // CHANGED: "updated" branch
                             : "Member information saved successfully!",  // CHANGED: "saved" branch (first insert)
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        SwingUtilities.invokeLater(() -> new SignUpFrame());
    }

    // ── Build Form Content (unchanged) ───────────────────────────────────────
    private JPanel buildContent() {

        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 0, 20, 0));

        c.add(sectionHeader("Membership Information"));
        c.add(vgap(14));

        JPanel r1 = row(3);
        pagIbigMidNoField = tf(14);
        pagIbigMidNoField.setText(RegistrationSession.getInstance().getTempMID());
        pagIbigMidNoField.setEditable(false);
        pagIbigMidNoField.setForeground(accentAmber);
        r1.add(lf("Pag-IBIG MID No. (Temporary)", pagIbigMidNoField));

        membershipTypeBox = cb(new String[]{
                "Select", "Employed", "Overseas Filipino Worker", "Self-Employed", "Others"
        });
        r1.add(lf("Membership Type *", membershipTypeBox));

        membershipCategoryBox = cb(new String[]{
                "Select", "Private", "Government", "Private Household",
                "Overseas Filipino Worker", "Professional/Business Owner",
                "Job Order Personnel", "Other Earning Groups", "Others"
        });
        r1.add(lf("Membership Category *", membershipCategoryBox));
        c.add(r1);
        c.add(vgap(10));

        membershipTypeOthersField = tf(100);
        membershipTypeOthersPanel = othersPanel("Membership Type — please specify", membershipTypeOthersField);
        membershipTypeOthersPanel.setVisible(false);
        c.add(membershipTypeOthersPanel);

        membershipCategoryOthersField = tf(100);
        membershipCategoryOthersPanel = othersPanel("Membership Category — please specify", membershipCategoryOthersField);
        membershipCategoryOthersPanel.setVisible(false);
        c.add(membershipCategoryOthersPanel);

        membershipTypeBox.addActionListener(e -> {
            boolean show = "Others".equals(membershipTypeBox.getSelectedItem());
            membershipTypeOthersPanel.setVisible(show);
            c.revalidate();
            c.repaint();
        });

        membershipCategoryBox.addActionListener(e -> {
            boolean show = "Others".equals(membershipCategoryBox.getSelectedItem());
            membershipCategoryOthersPanel.setVisible(show);
            c.revalidate();
            c.repaint();
        });

        c.add(vgap(10));

        JPanel r2 = row(2);
        r2.add(lf("Occupational Status *",
                occupationalStatusBox = cb(new String[]{
                        "Select", "Employed", "Unemployed", "First Time Jobseeker"
                })));
        r2.add(lf("Frequency of Membership Savings *",
                frequencyOfMembershipSavingsBox = cb(new String[]{
                        "Select", "Monthly", "Quarterly", "Semi-Annual", "Annual"
                })));
        c.add(r2);
        c.add(vgap(26));

        c.add(sectionHeader("Personal Information"));
        c.add(vgap(14));

        JPanel r3 = row(1);
        r3.add(lf("Member Name *", memberNameField = tf(50)));
        c.add(r3);
        c.add(vgap(16));

        JPanel r4 = row(3);
        r4.add(lf("Father's Name",  fatherNameField = tf(50)));
        r4.add(lf("Mother's Name",  motherNameField = tf(50)));
        r4.add(lf("Spouse's Name",  spouseNameField = tf(50)));
        c.add(r4);
        c.add(vgap(16));

        JPanel r5 = row(3);
        r5.add(lf("Birthdate (YYYY-MM-DD) *", birthdateField  = tf(10)));
        r5.add(lf("Birthplace *",             birthplaceField = tf(45)));
        r5.add(lf("Marital Status *",
                maritalStatusBox = cb(new String[]{
                        "Select", "Single", "Married", "Widowed", "Legally Separated", "Annulled"
                })));
        c.add(r5);
        c.add(vgap(16));

        JPanel r6 = row(3);
        r6.add(lf("Sex *",         sexBox         = cb(new String[]{"Select", "Male", "Female"})));
        r6.add(lf("Citizenship *", citizenshipBox = cb(new String[]{"Select", "Filipino", "Other"})));
        r6.add(lf("CRN",           crnField       = tf(12)));
        c.add(r6);
        c.add(vgap(16));

        JPanel r7 = row(3);
        r7.add(lf("TIN",           tinField            = tf(14)));
        r7.add(lf("SSS No.",       sssField            = tf(12)));
        r7.add(lf("Employee No.",  employeeNumberField = tf(14)));
        c.add(r7);
        c.add(vgap(26));

        c.add(sectionHeader("Address Information"));
        c.add(vgap(14));

        JPanel r8 = row(1);
        r8.add(lf("Present Home Address *", presentHomeAddressField = tf(255)));
        c.add(r8);
        c.add(vgap(16));

        JPanel r9 = row(1);
        r9.add(lf("Permanent Home Address *", permanentHomeAddressField = tf(255)));
        c.add(r9);
        c.add(vgap(16));

        JPanel r10 = row(1);
        r10.add(lf("Preferred Mailing Address *",
                preferredMailingAddressBox = cb(new String[]{
                        "Select", "Present Home Address",
                        "Permanent Home Address", "Employer/Business Address"
                })));
        c.add(r10);
        c.add(vgap(26));

        c.add(sectionHeader("Contact Information"));
        c.add(vgap(14));

        JPanel r11 = row(3);
        r11.add(lf("Cellphone No. * (+63...)", cellphoneNumField = tf(13)));
        r11.add(lf("Home Telephone No.",       homeTelNumField   = tf(20)));
        r11.add(lf("Email Address",            emailAddressField = tf(255)));
        c.add(r11);
        c.add(vgap(16));

        JPanel r12 = row(3);
        r12.add(lf("Business Direct Line", busDirectLineField = tf(20)));
        r12.add(lf("Business Trunk Line",  busTrunkLineField  = tf(20)));
        r12.add(lf("Local/Extension",      localField         = tf(6)));
        c.add(r12);
        c.add(vgap(26));

        c.add(sectionHeader("Income Information"));
        c.add(vgap(14));

        JPanel r13 = row(3);
        r13.add(lf("Basic Allowance *",    allowBasicField        = tf(20)));
        r13.add(lf("Other Sources",        allowOtherSourcesField = tf(20)));
        r13.add(lf("Total Monthly Income", totalMoIncomeField     = tf(20)));
        c.add(r13);

        totalMoIncomeField.setEditable(false);
        totalMoIncomeField.setForeground(accentGreen);

        FocusAdapter income = new FocusAdapter() {
            public void focusLost(FocusEvent e) { computeTotalIncome(); }
        };
        allowBasicField.addFocusListener(income);
        allowOtherSourcesField.addFocusListener(income);

        return c;
    }

    // ── Others Panel (unchanged) ──────────────────────────────────────────────
    private JPanel othersPanel(String label, JTextField field) {
        JPanel row = row(1);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.add(lf(label, field));
        return row;
    }

    private void computeTotalIncome() {
        try {
            double basic = parseDouble(allowBasicField.getText());
            double other = parseDouble(allowOtherSourcesField.getText());
            totalMoIncomeField.setText(String.format("%.2f", basic + other));
        } catch (Exception ignored) {}
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s.replace(",", "")); }
        catch (Exception e) { return 0; }
    }

    private BigDecimal parseBigDecimal(String s) {
        try { return new BigDecimal(s.replace(",", "").trim()); }
        catch (Exception e) { return BigDecimal.ZERO; }
    }

    // ── DB Enum Mappers UI → DB (unchanged) ──────────────────────────────────
    private String toDbEnum(String occupational) {
        switch (occupational) {
            case "Employed":             return "EMPLOYED";
            case "Unemployed":           return "UNEMPLOYED";
            case "First Time Jobseeker": return "FIRST TIME JOBSEEKERS";
            default:                     return occupational.toUpperCase();
        }
    }

    private String toMembershipTypeEnum(String type) {
        switch (type) {
            case "Employed":                 return "EMPLOYED";
            case "Overseas Filipino Worker": return "OVERSEAS FILIPINO WORKER";
            case "Self-Employed":            return "SELF-EMPLOYED";
            default:                         return "EMPLOYED";
        }
    }

    private String toMembershipCategoryEnum(String cat) {
        switch (cat) {
            case "Private":                     return "PRIVATE";
            case "Government":                  return "GOVERNMENT";
            case "Private Household":           return "PRIVATE HOUSEHOLD";
            case "Overseas Filipino Worker":    return "OVERSEAS FILIPINO WORKER";
            case "Professional/Business Owner": return "PROFESSIONAL/BUSINESS OWNER";
            case "Job Order Personnel":         return "JOB ORDER PERSONNEL";
            case "Other Earning Groups":        return "OTHER EARNING GROUPS";
            default:                            return "PRIVATE";
        }
    }

    private String toMaritalEnum(String status) {
        switch (status) {
            case "Single":            return "SINGLE";
            case "Married":           return "MARRIED";
            case "Widowed":           return "WIDOWED";
            case "Legally Separated": return "LEGALLY SEPARATED";
            case "Annulled":          return "ANNULED";   // matches DB spelling
            default:                  return "SINGLE";
        }
    }

    private String toMailingEnum(String mailing) {
        switch (mailing) {
            case "Present Home Address":      return "Present Home Address";
            case "Permanent Home Address":    return "Permanent Home Address";
            case "Employer/Business Address": return "Employer/Business Address";
            default:                          return "Present Home Address";
        }
    }

    // ── Validation Helpers (unchanged) ───────────────────────────────────────
    private boolean isBlank(JTextField f)          { return f.getText().trim().isEmpty(); }
    private boolean isComboDefault(JComboBox<?> b) { return "Select".equals(b.getSelectedItem()); }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    // ── Section Header (unchanged) ────────────────────────────────────────────
    private JPanel sectionHeader(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbl = new JLabel(text);
        lbl.setForeground(accentGreen);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 15));
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        return p;
    }

    private JPanel lf(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(255, 255, 255, 180));
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField tf(int maxLen) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                        ? new Color(96, 216, 164, 200)
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

        ((javax.swing.text.AbstractDocument) field.getDocument())
                .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string,
                                             javax.swing.text.AttributeSet attr)
                            throws javax.swing.text.BadLocationException {
                        if (fb.getDocument().getLength() + string.length() <= maxLen)
                            super.insertString(fb, offset, string, attr);
                    }
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text,
                                        javax.swing.text.AttributeSet attrs)
                            throws javax.swing.text.BadLocationException {
                        int newLen = fb.getDocument().getLength() - length + (text == null ? 0 : text.length());
                        if (newLen <= maxLen)
                            super.replace(fb, offset, length, text, attrs);
                    }
                });

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> cb(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 40, 65));
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
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 48));
        return btn;
    }

    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemberInfoForm::new);
    }
}