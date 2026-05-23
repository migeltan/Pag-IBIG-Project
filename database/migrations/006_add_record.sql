#-- SQL RECORD ADDING SYNTAX:

#-- ADDING MEMBER:
INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5694', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Diana M. Villanueva', 'Father Villanueva', 'Mother Villanueva', NULL, '2000-11-18', 'SINGLE',
   'Marikina City', 'Filipino', 'FEMALE', '226000400128', 'Monthly', '123-456-589-21', '13-2343168-9',
   123428227, 'Marikina City', 'Marikina City',
   'Present Home Address', '(02) 8123-4573', '0917-000-0003', NULL,
   NULL, NULL, 'dianav@gmail.com', 52000.00, NULL, 52000.00);
   
#-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5694', 'SPL', 'Database Administrator', 'PERMANENT/REGULAR', NULL,
   'Philippines', '2025-04-15');
   
#-- ADDING PREVIOUS EMPLOYMENT
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5694', 'ORACLE', '2025-03-31', '2020-09-01');
  
#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5694', 'Mother Villanueva', 'Mother', '1972-04-30'),
  ('1212-3434-5694', 'Father Villanueva', 'Father', '1970-07-15');