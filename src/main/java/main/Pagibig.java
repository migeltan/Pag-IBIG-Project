package main;

import dao.*;
import models.*;

import java.sql.Date;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entry point for the Pag-IBIG system.
 * Demonstrates basic CRUD usage through the DAO layer.
 */
public class Pagibig {

    // DAO instances
    static MemberDAO memberDAO = new MemberDAO();
    static CompanyDAO companyDAO = new CompanyDAO();
    static CurrentEmpDAO currentEmpDAO = new CurrentEmpDAO();
    static HeirsDAO heirsDAO = new HeirsDAO();
    static PrevEmpDAO prevEmpDAO = new PrevEmpDAO();

    public static void main(String[] args) {

        System.out.println("=== Pag-IBIG System Started ===\n");

        // ── 1. List all members ──────────────────────────────────────────────────
        System.out.println("--- All Members ---");
        List<MemberTable> members = memberDAO.getAllMembers();
        for (MemberTable m : members) {
            System.out.println(m.getPagIbigMIDNo() + " | " + m.getMemberName()
                    + " | " + m.getMembershipCategory());
        }

        // ── 2. Get a single member ───────────────────────────────────────────────
        System.out.println("\n--- Lookup Member: 1212-3434-5656 ---");
        MemberTable member = memberDAO.getMemberById("1212-3434-5656");
        if (member != null) {
            System.out.println("Name    : " + member.getMemberName());
            System.out.println("Email   : " + member.getEmailAddress());
            System.out.println("Income  : " + member.getTotalMoIncome());
        }

        // ── 3. List all companies ────────────────────────────────────────────────
        System.out.println("\n--- All Companies ---");
        List<CompanyDetailsTable> companies = companyDAO.getAllCompanies();
        for (CompanyDetailsTable c : companies) {
            System.out.println(c.getCompanyCode() + " | " + c.getCompanyName()
                    + " | " + c.getOfficeAssignment());
        }

        // ── 4. Current employment of a member ───────────────────────────────────
        System.out.println("\n--- Current Job: 1212-3434-5657 ---");
        CurrentEmpRecordTable job = currentEmpDAO.getCurrentEmpByMID("1212-3434-5657");
        if (job != null) {
            System.out.println("Company  : " + job.getCompanyCode());
            System.out.println("Position : " + job.getOccupation());
            System.out.println("Status   : " + job.getEmploymentStatus());
        }

        // ── 5. Previous employment history ──────────────────────────────────────
        System.out.println("\n--- Prev Jobs: 1212-3434-5656 ---");
        List<PrevEmpTable> prevJobs = prevEmpDAO.getPrevEmpByMID("1212-3434-5656");
        for (PrevEmpTable p : prevJobs) {
            System.out.println("Company: " + p.getCompanyCode()
                    + " | From: " + p.getFromDate() + " → To: " + p.getToDate());
        }

        // ── 6. Insert a new member (example) ────────────────────────────────────
        /*
        MemberTable newMember = new MemberTable();
        newMember.setPagIbigMIDNo("1212-3434-9999");
        newMember.setOccupationalStatus("EMPLOYED");
        newMember.setMembershipType("EMPLOYED");
        newMember.setMembershipCategory("PRIVATE");
        newMember.setMemberName("Juan Dela Cruz");
        newMember.setBirthdate(Date.valueOf("1995-06-15"));
        newMember.setMaritalStatus("SINGLE");
        newMember.setBirthplace("Manila City");
        newMember.setCitizenship("Filipino");
        newMember.setSex("MALE");
        newMember.setFrequencyOfMembershipSavings("Monthly");
        newMember.setPresentHomeAddress("123 Sample St., Manila");
        newMember.setPermanentHomeAddress("123 Sample St., Manila");
        newMember.setPreferredMailingAddress("Present Home Address");
        newMember.setCellphoneNum("0917-000-0000");
        newMember.setEmailAddress("juan@example.com");
        newMember.setAllowBasic(new BigDecimal("30000.00"));
        newMember.setTotalMoIncome(new BigDecimal("30000.00"));

        boolean inserted = memberDAO.insertMember(newMember);
        System.out.println("\nInsert new member: " + (inserted ? "SUCCESS" : "FAILED"));
        */

        System.out.println("\n=== Done ===");
    }
}
