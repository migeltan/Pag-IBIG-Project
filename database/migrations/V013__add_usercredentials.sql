-- V013__add_usercredentials.sql

-- ADDING/UPDATING USER CREDENTIALS:
INSERT INTO pagibig.usercredentials (PagIbig_MID_No, Password, Security_Q1, Security_A1, Security_Q2, Security_A2, Security_Q3, Security_A3)
VALUES
  ('1212-3434-5656', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5657', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5658', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5659', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5672', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5673', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5674', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5675', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5676', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5677', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5678', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5689', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5694', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5695', 'adminuser', '', '', '', '', '', '')
ON DUPLICATE KEY UPDATE
  Password = VALUES(Password),
  Security_Q1 = VALUES(Security_Q1),
  Security_A1 = VALUES(Security_A1),
  Security_Q2 = VALUES(Security_Q2),
  Security_A2 = VALUES(Security_A2),
  Security_Q3 = VALUES(Security_Q3),
  Security_A3 = VALUES(Security_A3);