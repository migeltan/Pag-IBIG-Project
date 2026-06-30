# Pag-CONNECT

**Pag-CONNECT** is a desktop-based Pag-IBIG membership management system built using Java (Swing) and MySQL. This project demonstrates the application of core OOP principles, database connectivity using JDBC, exception handling, and CRUD operations through a GUI-driven interface.

---

## Project Overview

This system is developed using Java and MySQL, fulfilling the requirements for OOP course.

It allows users to perform basic system transactions such as:

- Add Records
- View Records
- Update Records
- Delete Records

Across the following membership form modules:

- Member Information
- Current Employment Record
- Previous Employment Record
- Heirs / Dependents
- Company Details
- Admin Dashboard
- Report Generation

The project demonstrates the use of:

- Classes and Objects
- Encapsulation
- Inheritance
- Polymorphism
- Abstraction
- Constructors and Methods
- Exception Handling
- Database Connectivity (JDBC)


---

## Technologies Used

| Technology         | Purpose                    |
| ------------------ | -------------------------- |
| Java 8             | Main Programming Language  |
| Java Swing         | GUI / Desktop Interface    |
| MySQL 9.7          | Database                   |
| Eclipse / VSCode   | IDE                        |
| GitHub             | Version Control            |

---

## Project Structure

```
Pag-CONNECT-Project/
└── src/main/java/
    ├── main/
    │   ├── Pagibig.java                    ← Entry point
    │   └── RegistrationSession.java        ← In-memory session holder for signup flow
    ├── ui/
    │   ├── assets/                         ← Image assets (logos, icons)
    │   │   ├── currentEmp.png
    │   │   ├── heirs.png
    │   │   ├── logo.png
    │   │   ├── memberinfo.png
    │   │   └── prevEmp.png
    │   ├── forms/                          ← Signup module forms
    │   │   ├── CurrentEmpForm.java
    │   │   ├── HeirsForm.java
    │   │   ├── MemberInfoForm.java
    │   │   ├── MemberRecordForm.java       ← Combined edit form (member dashboard)
    │   │   └── PrevEmpForm.java
    │   ├── frames/                         ← Top-level windows
    │   │   ├── AdminDashboard.java         ← Admin CRUD management panel
    │   │   ├── AdminLoginFrame.java        ← Admin login screen
    │   │   ├── LoginFrame.java             ← Member login screen (Run this first for the whole flow)
    │   │   ├── SignInFrame.java            ← Member dashboard / main menu
    │   │   └── SignUpFrame.java            ← Registration module selector
    │   ├── utils/                          ← Shared utility frames
    │   │   ├── FontShowcase.java
    │   │   ├── NewPassword.java
    │   │   └── SetUpPassword.java          ← Password + security question setup
    │   └── views/                          ← Read-only / editable record views
    │       ├── CurrentEmpFormView.java
    │       ├── HeirsFormView.java
    │       ├── MemberInfoFormView.java
    │       └── PrevEmpFormView.java
    └── database/
        ├── dao/
        │   ├── AdminDAO.java                ← Auth for admincredentials
        │   ├── CompanyDAO.java              ← CRUD for companydetailstable
        │   ├── CurrentEmpDAO.java           ← CRUD for currentemprecordtable
        │   ├── HeirsDAO.java                ← CRUD for heirstable
        │   ├── MemberDAO.java               ← CRUD for membertable
        │   ├── PrevEmpDAO.java              ← CRUD for prevemptable
        │   └── UserCredentialsDAO.java      ← Auth for usercredentials
        ├── database/
        │   ├── DatabaseConnection.java      ← MySQL connection manager
        │   └── dumpSeedRunThis.sql          ← Seed data script (Run this first before all the migration files)
        ├── models/
        │   ├── CompanyDetailsTable.java
        │   ├── CurrentEmpRecordTable.java
        │   ├── HeirsTable.java
        │   ├── MemberTable.java
        │   ├── PrevEmpTable.java
        │   └── UserCredentials.java
        └── migrations/
            ├── V001__001to013.sql           ← Run this first then sequentially, last will be the completion tracking logic
            ├── V014_add20_records.sql
            ├── V015_add10_records.sql
            ├── V016_add30_records.sql
            ├── V017_alteration.sql
            ├── V018_admin.sql
            └── Completion tracking logic.sql
```

---

## Package Descriptions & Team Responsibilities

---

### Frontend / UI — `ui/`


Contains all GUI screens built with Java Swing, split into four subpackages. The UI layer never accesses the database directly — it always calls a DAO method to perform any data operation.

| Subpackage | Description |
| ---------- | ------------ |
| `ui/frames/` | Top-level windows — login, signup, member dashboard, admin dashboard |
| `ui/forms/` | Signup module forms (Member Info, Employment, Heirs) and the combined member edit form |
| `ui/views/` | Read-only / editable record views shown after login |
| `ui/utils/` | Shared utility frames such as password setup |
| `ui/assets/` | Image assets used across the UI |

| File                       | Description                                       |
| --------------------------- | -------------------------------------------------- |
| `LoginFrame.java`          | Member login screen shown on startup               |
| `SignInFrame.java`         | Member dashboard / main menu after login           |
| `SignUpFrame.java`         | Registration module selector                       |
| `AdminLoginFrame.java`     | Admin login screen                                  |
| `AdminDashboard.java`      | Admin CRUD management panel with search/filter      |
| `MemberInfoForm.java`      | Member personal details form (signup)               |
| `MemberRecordForm.java`    | Combined edit form accessible from member dashboard |
| `...Form.java` / `...View.java` | Remaining modules follow the same Form/View split documented in the source code section |

---

## Application Architecture

```
UI Layer (ui/)
     ↓  calls
DAO Layer (database/dao/)
     ↓  uses
DatabaseConnection (database/database/)
     ↓  connects to
MySQL Database
```

Models (`database/models/`) serve as data containers passed between the UI and DAO layers.

---

## Database Schema

The system uses the `pagibig` MySQL database with the following tables:

| Table                    | Description                                   |
| -------------------------- | ---------------------------------------------- |
| `membertable`             | Core member personal and contact information |
| `companydetailstable`     | Company records linked to members             |
| `currentemprecordtable`   | Each member's current employment details      |
| `prevemptable`            | Previous employment history per member        |
| `heirstable`              | Heirs and dependents per member               |
| `usercredentials`         | Member MID, password, and security questions  |
| `admincredentials`        | Admin login credentials                       |

---

## Required Software Installation

### 1. Java JDK

Download and install **Java 8 (JavaSE-1.8)**:

- [Eclipse Temurin JDK](https://adoptium.net/)

Verify installation:

```bash
java -version
```

### 2. IDE

- [Eclipse IDE](https://www.eclipse.org/downloads/) — recommended
- or [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### 3. MySQL

- Download [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
- Download [MySQL Workbench](https://dev.mysql.com/downloads/workbench/) for GUI management

### 4. GitHub

- Set up Git inside your IDE or install [Git](https://git-scm.com/downloads) separately

---

## Importing the Project into Eclipse

1. Copy the repository URL from GitHub (green **Code** button → HTTPS link).
2. Open Eclipse → **File → Import...**
3. Select **Git → Projects from Git** → click **Next**.
4. Choose **Clone URI** → paste the copied repo link → click **Next**.
5. Select the branch (usually `main`) → click **Next** → choose a local destination folder → **Next**.
6. When prompted to import, select **Import as general project** (or **Existing Maven Projects** if a `pom.xml` is detected) → **Finish**.
7. Once imported, right-click the project → **Configure → Convert to Maven Project** (if not already converted) to ensure dependencies resolve correctly.
8. Wait for Eclipse to finish building the workspace, then proceed to the Database Setup section below.

---

## Database Setup

1. Open MySQL Workbench or any MySQL client
2. Create the database:

```sql
CREATE DATABASE pagibig;
```

3. Import the schema:

```bash
mysql -u root -p pagibig < database/pagibig.sql
```

4. (Optional) Import seed data:

```bash
mysql -u root -p pagibig < database/seed.sql
```

5. Run adding_records, all migrations

---

## Configuration

Open `DatabaseConnection.java` and update the credentials to match your local MySQL setup:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/pagibig";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password_here";
```

---

## Collaborating on this Repository

### Create Your Branch

- In Eclipse, right click then navigate to team, then there should be a push branch option.

### Pull Latest Changes Before Working

```bash
git pull origin main
```

### Push Your Branch

```bash
git push origin feature-yourname
```

### Commit Message Format

```bash
git commit -m "Added member CRUD functionality"
```

---

## Contributors

| Name                 |
| -------------------- |
| Yeshua Arjona        |
| James Escanillas     |
| James Patrick Isidro |
| Raven Rayo           |
| Migel Tan            |

---

## License

Academic use only. Developed as a course requirement for OOP and Information Management.