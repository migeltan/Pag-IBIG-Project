INSERT INTO companydetailstable 
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES 
  ('SCPH', 'Sta. Clara International Corporation', 'Muntinlupa City, PH', 'HEAD OFFICE', NULL);

INSERT INTO companydetailstable 
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES 
  ('AECO', 'AECOM Philippines', 'Pasig City, PH', 'BRANCH', 'Ortigas Center, Pasig City');


INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5675', 'EMPLOYED', 'SELF-EMPLOYED', 'PROFESSIONAL/BUSINESS OWNER',
   'Jasmine Shaine B. Arjona', 'Benjamin R. Arjona', 'Rosario B. Arjona',
   'Yeshua Adrielle S. Arjona', '2004-08-22', 'MARRIED',
   'Quezon City', 'Filipino', 'FEMALE', '226000000003', 'Monthly',
   '987-654-321-02', '33-4567890-1',
   111223344, 'Block 1 Lot 1 Phase B, Brgy. Mulawin Francisco Homes 1 City of San Jose del Monte, Bulacan', 'Taguig City, PH',
   'Present Home Address', '(02) 8765-4321', '0918-000-0002', '(02) 8800-1234',
   NULL, '102', 'jasmine.arjona@gmail.com', 45000.00, 5000.00, 50000.00);
   
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5675', 'SCPH', 'Civil Engineer', 'CONTRACTUAL', NULL,
   'Philippines', '2024-03-15');

#-- ADDING PREVIOUS EMPLOYMENT:
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5675', 'AECO', '2024-02-28', '2021-07-01');
  
#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5675', 'Yeshua Adrielle S. Arjona', 'Husband', '2005-04-15');

INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5675', 'Rosario B. Arjona', 'Mother', '1968-11-05');

INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5675', 'Benjamin R. Arjona', 'Father', '1965-03-17');