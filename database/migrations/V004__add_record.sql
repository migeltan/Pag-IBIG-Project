#-- SQL RECORD ADDING SYNTAX:

INSERT IGNORE INTO companydetailstable
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES
  ('HWI', 'Huawei Philippines', 'Taguig City, PH', 'BRANCH', 'Quezon City');
   
#-- ADDING MEMBER:
INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Type_Others, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5674', 'FIRST TIME JOBSEEKERS', 'SELF-EMPLOYED', 'REMOTE WORKER/FREELANCER' , 'OTHER EARNING GROUPS' ,
   'Catty Hemady', 'Roberto Reyes', 'Catriona Reyes', 'Leon Hemady', '1998-10-12', 'MARRIED',
   'Lubao, Pampanga', 'Filipino', 'FEMALE', '222492939945', 'Monthly', '123-456-239-22', '13-2341118-7',
   52421, '12 B Liwayway St., Marulas, Valenzuela City', '12 B Liwayway St., Marulas, Valenzuela City',
   'Present Home Address', '(02) 8123-4571', '0917-033-0351', NULL,
   NULL, NULL, 'cattyhem32@hotmail.com', 56000.11, 636687, 1020426.00);
   
#-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5674', 'HWI' , 'UNEMPLOYED', 'CASUAL', NULL,
   '', '1999-01-01');
   
#-- ADDING PREVIOUS EMPLOYMENT
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5674', 'HWI' , '1999-10-12', '2007-10-12');
  
--  ALTER TABLE heirstable DROP INDEX PagIbig_MID_No_UNIQUE;
  
#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5674', 'Roberto Reyes', 'Father', '1976-01-09'),
  ('1212-3434-5674', 'Catriona Reyes', 'Mother', '1980-02-01'),
  ('1212-3434-5674', 'Sevvy Hemady', 'Son', '2010-11-28'),
  ('1212-3434-5674', 'Maria Hemady', 'Daughter', '2012-05-13');