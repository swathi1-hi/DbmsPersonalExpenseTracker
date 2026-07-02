# Expense Tracker System

## Introduction

The Expense Tracker is a Java Swing desktop application developed to help users manage their daily expenses efficiently. The application provides a modern graphical user interface using the FlatLaf library and stores expense data in a MySQL database through JDBC. Users can add expenses, view expense records, delete unwanted records, and analyze their spending through graphical charts.

---

# Software Requirements

- Java JDK 17 or above
- Visual Studio Code
- MySQL Server
- MySQL Workbench
- FlatLaf JAR
- MySQL Connector/J JAR

---

# Project Structure

ExpenseTracker/

├── LoginFrame.java

├── HomeFrame.java

├── AddExpenseFrame.java

├── ViewExpenseFrame.java

├── ChartFrame.java

├── DBConnection.java

├── TestConnection.java

└── lib/

    ├── flatlaf-3.x.jar
    
    └── mysql-connector-j-9.x.x.jar

---

# Modules

## 1. Login Module

The Login Module is the starting point of the application. It authenticates users using predefined credentials.

**Functions**
- Accepts username and password.
- Validates user credentials.
- Opens the Home Dashboard after successful login.
- Displays an error message for invalid login.

---

## 2. Home Dashboard

The Home Dashboard acts as the main menu of the application.

**Functions**
- Open Add Expense page.
- Open View Expense page.
- Open Expense Chart page.

---

## 3. Add Expense Module

This module allows users to add new expense records.

**Input Fields**
- Amount
- Category
- Date
- Description

**Database Operation**

```sql
INSERT INTO expenses
(amount, category, expense_date, description)
VALUES (?, ?, ?, ?);
```

After saving successfully, the expense is stored in the MySQL database.

---

## 4. View Expense Module

This module displays all expense records stored in the database.

**Features**
- View all expenses
- Display total expenses
- Delete selected expense
- Refresh table automatically

**Database Operation**

```sql
SELECT * FROM expenses;
```

Delete Operation

```sql
DELETE FROM expenses
WHERE expense_id = ?;
```

---

## 5. Expense Chart Module

The Expense Chart module displays expenses category-wise using a Pie Chart.

**Functions**
- Retrieve expense data
- Group expenses by category
- Calculate percentage
- Display graphical analysis

**Database Query**

```sql
SELECT category,
SUM(amount)
FROM expenses
GROUP BY category;
```

---

## 6. Database Connection Module

The DBConnection class creates the connection between Java and MySQL.

```java
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/expense_tracker",
    "root",
    "12345678"
);
```

This connection is used by all other modules.

---

## 7. Test Connection Module

This module checks whether the database connection is established successfully.

Output

```
Connected to MySQL
```

or

```
Connection Failed
```

---

# FlatLaf Library

FlatLaf is a modern Look and Feel library for Java Swing applications. It provides a clean, professional, and responsive interface compared to the default Swing appearance.

### Purpose

- Modern UI
- Rounded Buttons
- Better Text Fields
- Improved Fonts
- Professional Appearance

### Import

```java
import com.formdev.flatlaf.FlatIntelliJLaf;
```

### Apply Theme

```java
FlatIntelliJLaf.setup();
```

### UI Customization

```java
UIManager.put("Button.arc", 8);
UIManager.put("Component.arc", 8);
UIManager.put("TextComponent.arc", 8);
UIManager.put("defaultFont",
new Font("Segoe UI", Font.PLAIN, 13));
```

FlatLaf is applied once in the LoginFrame and automatically affects all application windows.

---

# MySQL Connector/J Library

MySQL Connector/J is the JDBC driver that enables Java applications to communicate with MySQL databases.

### Purpose

- Establish database connection
- Execute SQL queries
- Retrieve records
- Insert records
- Delete records

### Import

```java
import java.sql.*;
```

### Connection

```java
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/expense_tracker",
    "root",
    "12345678"
);
```

---

# Database

Database Name

```
expense_tracker
```

Table Name

```
expenses
```

| Column | Data Type |
|----------|------------|
| expense_id | INT (Primary Key, Auto Increment) |
| amount | DOUBLE |
| category | VARCHAR(100) |
| expense_date | DATE |
| description | VARCHAR(255) |

---

# System Workflow

```
Application Starts
        │
        ▼
   LoginFrame
        │
        ▼
   HomeFrame
   ├───────────────┐
   │               │
   ▼               ▼
AddExpense    ViewExpense
   │               │
   └──────┐   ┌────┘
          ▼   ▼
      ChartFrame
          │
          ▼
     DBConnection
          │
          ▼
 MySQL Connector/J
          │
          ▼
 MySQL Database
```

---

# Conclusion

The Expense Tracker application provides a simple and efficient solution for recording and managing daily expenses. The system uses Java Swing with FlatLaf for an attractive graphical interface and MySQL with JDBC for reliable data storage. Its modular design improves maintainability while offering features such as expense management, data visualization, and secure database connectivity.