package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import models.MemberTable;

public class MemberDAO {

    // ─── CREATE ──────────────────────────────────────────────────────────────
    public boolean insertMember(MemberTable member) {
        String sql = "INSERT INTO membertable ("
                + "PagIbig_MID_No, Occupational_Status, Membership_Type, "
                + "Membership_Category, Member_Name, Father_Name, "
                + "Mother_Name, Spouse_Name, Birthdate, Marital_Status, Birthplace, Citizenship, "
                + "Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS, Employee_Number, "
                + "Present_Home_Address, Permanent_Home_Address, Preferred_Mailing_Address, "
                + "Home_TelNum, Cellphone_Num, Bus_DirectLine, Bus_TrunkLine, Local, "
                + "Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  member.getPagIbigMIDNo());
            ps.setString(2,  member.getOccupationalStatus());
            ps.setString(3,  member.getMembershipType());
            ps.setString(4,  member.getMembershipCategory());
            ps.setString(5,  member.getMemberName());
            ps.setString(6,  member.getFatherName());
            ps.setString(7,  member.getMotherName());
            ps.setString(8,  member.getSpouseName());
            ps.setDate(9,    member.getBirthdate());
            ps.setString(10, member.getMaritalStatus());
            ps.setString(11, member.getBirthplace());
            ps.setString(12, member.getCitizenship());
            ps.setString(13, member.getSex());
            ps.setString(14, member.getCrn());
            ps.setString(15, member.getFrequencyOfMembershipSavings());
            ps.setString(16, member.getTin());
            ps.setString(17, member.getSss());
            ps.setObject(18, member.getEmployeeNumber());
            ps.setString(19, member.getPresentHomeAddress());
            ps.setString(20, member.getPermanentHomeAddress());
            ps.setString(21, member.getPreferredMailingAddress());
            ps.setString(22, member.getHomeTelNum());
            ps.setString(23, member.getCellphoneNum());
            ps.setString(24, member.getBusDirectLine());
            ps.setString(25, member.getBusTrunkLine());
            ps.setString(26, member.getLocal());
            ps.setString(27, member.getEmailAddress());
            ps.setBigDecimal(28, member.getAllowBasic());
            ps.setBigDecimal(29, member.getAllowOtherSources());
            ps.setBigDecimal(30, member.getTotalMoIncome());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MemberDAO] insertMember error: " + e.getMessage());
            return false;
        }
    }

    // ─── READ (single) ────────────────────────────────────────────────────────
    public MemberTable getMemberById(String pagIbigMIDNo) {
        String sql = "SELECT * FROM membertable WHERE PagIbig_MID_No = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagIbigMIDNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[MemberDAO] getMemberById error: " + e.getMessage());
        }
        return null;
    }

    // ─── READ (all) ───────────────────────────────────────────────────────────
    public List<MemberTable> getAllMembers() {
        List<MemberTable> members = new ArrayList<>();
        String sql = "SELECT * FROM membertable";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) members.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[MemberDAO] getAllMembers error: " + e.getMessage());
        }
        return members;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────
    public boolean updateMember(MemberTable member) {
        String sql = "UPDATE membertable SET "
                + "Occupational_Status = ?, Membership_Type = ?, "
                + "Membership_Category = ?, Member_Name = ?, "
                + "Father_Name = ?, Mother_Name = ?, Spouse_Name = ?, Birthdate = ?, "
                + "Marital_Status = ?, Birthplace = ?, Citizenship = ?, Sex = ?, CRN = ?, "
                + "Frequency_Of_Membership_Savings = ?, TIN = ?, SSS = ?, Employee_Number = ?, "
                + "Present_Home_Address = ?, Permanent_Home_Address = ?, "
                + "Preferred_Mailing_Address = ?, Home_TelNum = ?, Cellphone_Num = ?, "
                + "Bus_DirectLine = ?, Bus_TrunkLine = ?, Local = ?, Email_Address = ?, "
                + "Allow_Basic = ?, Allow_Other_Sources = ?, Total_Mo_Income = ? "
                + "WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  member.getOccupationalStatus());
            ps.setString(2,  member.getMembershipType());
            ps.setString(3,  member.getMembershipCategory());
            ps.setString(4,  member.getMemberName());
            ps.setString(5,  member.getFatherName());
            ps.setString(6,  member.getMotherName());
            ps.setString(7,  member.getSpouseName());
            ps.setDate(8,    member.getBirthdate());
            ps.setString(9,  member.getMaritalStatus());
            ps.setString(10, member.getBirthplace());
            ps.setString(11, member.getCitizenship());
            ps.setString(12, member.getSex());
            ps.setString(13, member.getCrn());
            ps.setString(14, member.getFrequencyOfMembershipSavings());
            ps.setString(15, member.getTin());
            ps.setString(16, member.getSss());
            ps.setObject(17, member.getEmployeeNumber());
            ps.setString(18, member.getPresentHomeAddress());
            ps.setString(19, member.getPermanentHomeAddress());
            ps.setString(20, member.getPreferredMailingAddress());
            ps.setString(21, member.getHomeTelNum());
            ps.setString(22, member.getCellphoneNum());
            ps.setString(23, member.getBusDirectLine());
            ps.setString(24, member.getBusTrunkLine());
            ps.setString(25, member.getLocal());
            ps.setString(26, member.getEmailAddress());
            ps.setBigDecimal(27, member.getAllowBasic());
            ps.setBigDecimal(28, member.getAllowOtherSources());
            ps.setBigDecimal(29, member.getTotalMoIncome());
            ps.setString(30, member.getPagIbigMIDNo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MemberDAO] updateMember error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────
    public boolean deleteMember(String pagIbigMIDNo) {
        String sql = "DELETE FROM membertable WHERE PagIbig_MID_No = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagIbigMIDNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MemberDAO] deleteMember error: " + e.getMessage());
            return false;
        }
    }

    // ─── MAPPER ──────────────────────────────────────────────────────────────
    private MemberTable mapRow(ResultSet rs) throws SQLException {
        MemberTable m = new MemberTable();
        m.setPagIbigMIDNo(rs.getString("PagIbig_MID_No"));
        m.setOccupationalStatus(rs.getString("Occupational_Status"));
        m.setMembershipType(rs.getString("Membership_Type"));
        m.setMembershipCategory(rs.getString("Membership_Category"));
        m.setMemberName(rs.getString("Member_Name"));
        m.setFatherName(rs.getString("Father_Name"));
        m.setMotherName(rs.getString("Mother_Name"));
        m.setSpouseName(rs.getString("Spouse_Name"));
        m.setBirthdate(rs.getDate("Birthdate"));
        m.setMaritalStatus(rs.getString("Marital_Status"));
        m.setBirthplace(rs.getString("Birthplace"));
        m.setCitizenship(rs.getString("Citizenship"));
        m.setSex(rs.getString("Sex"));
        m.setCrn(rs.getString("CRN"));
        m.setFrequencyOfMembershipSavings(rs.getString("Frequency_Of_Membership_Savings"));
        m.setTin(rs.getString("TIN"));
        m.setSss(rs.getString("SSS"));
        m.setEmployeeNumber((Integer) rs.getObject("Employee_Number"));
        m.setPresentHomeAddress(rs.getString("Present_Home_Address"));
        m.setPermanentHomeAddress(rs.getString("Permanent_Home_Address"));
        m.setPreferredMailingAddress(rs.getString("Preferred_Mailing_Address"));
        m.setHomeTelNum(rs.getString("Home_TelNum"));
        m.setCellphoneNum(rs.getString("Cellphone_Num"));
        m.setBusDirectLine(rs.getString("Bus_DirectLine"));
        m.setBusTrunkLine(rs.getString("Bus_TrunkLine"));
        m.setLocal(rs.getString("Local"));
        m.setEmailAddress(rs.getString("Email_Address"));
        m.setAllowBasic(rs.getBigDecimal("Allow_Basic"));
        m.setAllowOtherSources(rs.getBigDecimal("Allow_Other_Sources"));
        m.setTotalMoIncome(rs.getBigDecimal("Total_Mo_Income"));
        return m;
    }

    // ─── GENERATE NEXT MID ───────────────────────────────────────────────────
    public String generateNextMID() {
        String sql = "SELECT PagIbig_MID_No FROM membertable ORDER BY PagIbig_MID_No DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastMID = rs.getString("PagIbig_MID_No");
                String[] parts = lastMID.split("-");
                int lastSegment = Integer.parseInt(parts[2]);
                lastSegment++;
                return parts[0] + "-" + parts[1] + "-" + String.format("%04d", lastSegment);
            }
        } catch (SQLException e) {
            System.err.println("[MemberDAO] generateNextMID error: " + e.getMessage());
        }
        return null;
    }
}