CREATE TABLE usercredentials (
    PagIbig_MID_No  char(14)     NOT NULL,
    Password        varchar(255) NOT NULL,
    Security_Q1     varchar(100) NOT NULL,
    Security_A1     varchar(100) NOT NULL,
    Security_Q2     varchar(100) NOT NULL,
    Security_A2     varchar(100) NOT NULL,
    Security_Q3     varchar(100) NOT NULL,
    Security_A3     varchar(100) NOT NULL,
    PRIMARY KEY (PagIbig_MID_No),
    FOREIGN KEY (PagIbig_MID_No) REFERENCES membertable(PagIbig_MID_No)
        ON DELETE CASCADE ON UPDATE CASCADE
);