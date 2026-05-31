#-- Update column domains from enum to varchar

#-- companydetailstable
ALTER TABLE `pagibig`.`companydetailstable` 
CHANGE COLUMN `Office_Assignment` `Office_Assignment` VARCHAR(11) NOT NULL;

#-- currentemprecordtable
ALTER TABLE `pagibig`.`currentemprecordtable` 
CHANGE COLUMN `Employment_Status` `Employment_Status` VARCHAR(17) NULL DEFAULT NULL,
CHANGE COLUMN `TypeOfWork` `TypeOfWork` VARCHAR(10) NULL DEFAULT NULL,
CHANGE COLUMN `Country_Of_Assignment` `Country_Of_Assignment` VARCHAR(25) NULL;

#-- membertable
ALTER TABLE `pagibig`.`membertable` 
CHANGE COLUMN `Occupational_Status` `Occupational_Status` VARCHAR(21) NOT NULL,
CHANGE COLUMN `Membership_Type` `Membership_Type` VARCHAR(24) NOT NULL,
CHANGE COLUMN `Membership_Category` `Membership_Category` VARCHAR(30) NOT NULL,
CHANGE COLUMN `Marital_Status` `Marital_Status` VARCHAR(17) NOT NULL,
CHANGE COLUMN `Sex` `Sex` VARCHAR(6) NOT NULL;