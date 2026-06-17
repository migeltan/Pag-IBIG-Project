package models;

import java.math.BigDecimal;
import java.sql.Date;

public class MemberTable {

    private String pagIbigMIDNo;
    private String occupationalStatus;
    private String membershipType;
    private String membershipCategory;
    private String memberName;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private Date birthdate;
    private String maritalStatus;
    private String birthplace;
    private String citizenship;
    private String sex;
    private String crn;
    private String frequencyOfMembershipSavings;
    private String tin;
    private String sss;
    private Integer employeeNumber;
    private String presentHomeAddress;
    private String permanentHomeAddress;
    private String preferredMailingAddress;
    private String homeTelNum;
    private String cellphoneNum;
    private String busDirectLine;
    private String busTrunkLine;
    private String local;
    private String emailAddress;
    private BigDecimal allowBasic;
    private BigDecimal allowOtherSources;
    private BigDecimal totalMoIncome;

    public MemberTable() {}

    public MemberTable(String pagIbigMIDNo, String occupationalStatus, String membershipType,
                       String membershipCategory, String memberName,
                       String fatherName, String motherName, String spouseName,
                       Date birthdate, String maritalStatus, String birthplace,
                       String citizenship, String sex, String crn,
                       String frequencyOfMembershipSavings, String tin, String sss,
                       Integer employeeNumber, String presentHomeAddress,
                       String permanentHomeAddress, String preferredMailingAddress,
                       String homeTelNum, String cellphoneNum, String busDirectLine,
                       String busTrunkLine, String local, String emailAddress,
                       BigDecimal allowBasic, BigDecimal allowOtherSources,
                       BigDecimal totalMoIncome) {
        this.pagIbigMIDNo = pagIbigMIDNo;
        this.occupationalStatus = occupationalStatus;
        this.membershipType = membershipType;
        this.membershipCategory = membershipCategory;
        this.memberName = memberName;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.spouseName = spouseName;
        this.birthdate = birthdate;
        this.maritalStatus = maritalStatus;
        this.birthplace = birthplace;
        this.citizenship = citizenship;
        this.sex = sex;
        this.crn = crn;
        this.frequencyOfMembershipSavings = frequencyOfMembershipSavings;
        this.tin = tin;
        this.sss = sss;
        this.employeeNumber = employeeNumber;
        this.presentHomeAddress = presentHomeAddress;
        this.permanentHomeAddress = permanentHomeAddress;
        this.preferredMailingAddress = preferredMailingAddress;
        this.homeTelNum = homeTelNum;
        this.cellphoneNum = cellphoneNum;
        this.busDirectLine = busDirectLine;
        this.busTrunkLine = busTrunkLine;
        this.local = local;
        this.emailAddress = emailAddress;
        this.allowBasic = allowBasic;
        this.allowOtherSources = allowOtherSources;
        this.totalMoIncome = totalMoIncome;
    }

    public String getPagIbigMIDNo()                       { return pagIbigMIDNo; }
    public void setPagIbigMIDNo(String v)                 { this.pagIbigMIDNo = v; }
    public String getOccupationalStatus()                 { return occupationalStatus; }
    public void setOccupationalStatus(String v)           { this.occupationalStatus = v; }
    public String getMembershipType()                     { return membershipType; }
    public void setMembershipType(String v)               { this.membershipType = v; }
    public String getMembershipCategory()                 { return membershipCategory; }
    public void setMembershipCategory(String v)           { this.membershipCategory = v; }
    public String getMemberName()                         { return memberName; }
    public void setMemberName(String v)                   { this.memberName = v; }
    public String getFatherName()                         { return fatherName; }
    public void setFatherName(String v)                   { this.fatherName = v; }
    public String getMotherName()                         { return motherName; }
    public void setMotherName(String v)                   { this.motherName = v; }
    public String getSpouseName()                         { return spouseName; }
    public void setSpouseName(String v)                   { this.spouseName = v; }
    public Date getBirthdate()                            { return birthdate; }
    public void setBirthdate(Date v)                      { this.birthdate = v; }
    public String getMaritalStatus()                      { return maritalStatus; }
    public void setMaritalStatus(String v)                { this.maritalStatus = v; }
    public String getBirthplace()                         { return birthplace; }
    public void setBirthplace(String v)                   { this.birthplace = v; }
    public String getCitizenship()                        { return citizenship; }
    public void setCitizenship(String v)                  { this.citizenship = v; }
    public String getSex()                                { return sex; }
    public void setSex(String v)                          { this.sex = v; }
    public String getCrn()                                { return crn; }
    public void setCrn(String v)                          { this.crn = v; }
    public String getFrequencyOfMembershipSavings()       { return frequencyOfMembershipSavings; }
    public void setFrequencyOfMembershipSavings(String v) { this.frequencyOfMembershipSavings = v; }
    public String getTin()                                { return tin; }
    public void setTin(String v)                          { this.tin = v; }
    public String getSss()                                { return sss; }
    public void setSss(String v)                          { this.sss = v; }
    public Integer getEmployeeNumber()                    { return employeeNumber; }
    public void setEmployeeNumber(Integer v)              { this.employeeNumber = v; }
    public String getPresentHomeAddress()                 { return presentHomeAddress; }
    public void setPresentHomeAddress(String v)           { this.presentHomeAddress = v; }
    public String getPermanentHomeAddress()               { return permanentHomeAddress; }
    public void setPermanentHomeAddress(String v)         { this.permanentHomeAddress = v; }
    public String getPreferredMailingAddress()            { return preferredMailingAddress; }
    public void setPreferredMailingAddress(String v)      { this.preferredMailingAddress = v; }
    public String getHomeTelNum()                         { return homeTelNum; }
    public void setHomeTelNum(String v)                   { this.homeTelNum = v; }
    public String getCellphoneNum()                       { return cellphoneNum; }
    public void setCellphoneNum(String v)                 { this.cellphoneNum = v; }
    public String getBusDirectLine()                      { return busDirectLine; }
    public void setBusDirectLine(String v)                { this.busDirectLine = v; }
    public String getBusTrunkLine()                       { return busTrunkLine; }
    public void setBusTrunkLine(String v)                 { this.busTrunkLine = v; }
    public String getLocal()                              { return local; }
    public void setLocal(String v)                        { this.local = v; }
    public String getEmailAddress()                       { return emailAddress; }
    public void setEmailAddress(String v)                 { this.emailAddress = v; }
    public BigDecimal getAllowBasic()                      { return allowBasic; }
    public void setAllowBasic(BigDecimal v)               { this.allowBasic = v; }
    public BigDecimal getAllowOtherSources()               { return allowOtherSources; }
    public void setAllowOtherSources(BigDecimal v)        { this.allowOtherSources = v; }
    public BigDecimal getTotalMoIncome()                  { return totalMoIncome; }
    public void setTotalMoIncome(BigDecimal v)            { this.totalMoIncome = v; }

    @Override
    public String toString() {
        return "MemberTable{pagIbigMIDNo='" + pagIbigMIDNo + "', memberName='" + memberName + "'}";
    }
}