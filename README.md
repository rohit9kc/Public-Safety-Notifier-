# 🚨 Public Safety Notifier (PSN)

A **Java-based application** for reporting and managing public safety incidents with a **user-friendly GUI**.

---

## 📖 Overview

The **Public Safety Notifier (PSN)** is a desktop application built in Java, designed to enhance public safety by enabling:

- 🧑 Citizens to report incidents  
- 👮 Authorities to post and manage alerts  
- 🛠️ Admins to oversee the system  

> Built with **Java Swing** for the GUI, **MySQL** for data storage, and **object-oriented principles**, PSN ensures efficient incident reporting and alert dissemination with a pastel gradient theme.

---

## 🎯 Objectives

- Empower citizens to report safety incidents quickly  
- Enable authorities to post and manage public alerts  
- Provide admins with tools to approve reports and manage users  
- Deliver a visually appealing, user-friendly interface  

---

## ✨ Features

### 🧑‍🤝‍🧑 User Roles

| Role      | Capabilities |
|-----------|--------------|
| **Citizen**   | Register, log in, submit incident reports, view approved reports & alerts |
| **Authority** | Log in, post alerts, convert reports to alerts, view reports & alerts |
| **Admin**     | Log in, approve/reject reports, manage users, view all reports/alerts |

### 🎨 GUI

- Pastel purple-to-lavender **gradient background** with diagonal line patterns  
- Built using **Java Swing** for a modern and responsive interface  

### 💾 Data Management

- Stores reports, alerts, and user data in a **MySQL** database

### 🛡️ Error Handling

- Custom exceptions like `AuthenticationException` and `ReportValidationException` for robust error management

### 🔐 Security

- User authentication with blocked user checks

---

## 🛠️ Technologies Used

| Technology         | Purpose                               |
|--------------------|----------------------------------------|
| Java               | Core programming language              |
| Java Swing         | GUI framework                          |
| MySQL              | Database for data storage              |
| JDBC               | Database connectivity                  |
| PlantUML           | Class and use case diagrams            |

---

## ⚙️ Setup Instructions

### 📋 Prerequisites

- Java Development Kit (JDK): Version 8 or higher  
- MySQL Server: Version 5.7 or higher  
- MySQL Connector/J JAR: e.g., `mysql-connector-java-8.0.28.jar`  
- Java IDE: IntelliJ IDEA, Eclipse, or command-line environment  

### 🗄️ Set Up the MySQL Database

1. Create the database:

    ```sql
    CREATE DATABASE psn_db;
    ```

2. Create tables for users, reports, and alerts (refer to SQL scripts in the project)

3. Update the `DatabaseConnection` class in `PSNGUI.java` with your credentials:

    ```java
    String url = "jdbc:mysql://localhost:3306/psn_db";
    String user = "your_username";
    String password = "your_password";
    ```

### 📦 Add MySQL Connector/J

- Download from [MySQL's official site](https://dev.mysql.com/downloads/connector/j/)
- Add the JAR to your project classpath:

  - **IDE**: Add as a library  
  - **Command Line**: Place the JAR in your project directory  

---

### 🚀 Compile and Run

**Using an IDE:**

- Open the project  
- Set `PSNGUI.java` as the main class  
- Run the project  

**Using Command Line:**

```bash
javac -cp .;mysql-connector-java-8.0.28.jar PSNGUI.java
java -cp .;mysql-connector-java-8.0.28.jar PSNGUI
```
## 📱 Usage

### 🧑 For Citizens
- **Register**: Create an account  
- **Log In**: Use your credentials  
- **Submit Report**: Enter incident details  
- **View Updates**: See approved reports and alerts  

### 👮 For Authorities
- **Log In**: Predefined credentials (e.g., `auth@psn.com/auth123`)  
- **Post Alerts**: Notify the public  
- **Convert Reports**: Turn approved reports into alerts  
- **View**: Monitor incident reports and active alerts  

### 🛠️ For Admins
- **Log In**: Admin credentials (e.g., `admin@psn.com/admin123`)  
- **Approve/Reject Reports**: Manage report validity  
- **Manage Users**: Block, unblock, or delete accounts  
- **View All Data**: Full system access  

---

## 🧬 Class Structure

| Class                    | Description                                     |
|--------------------------|-------------------------------------------------|
| `PSNGUI`                 | Main GUI class, extends `JFrame`                |
| `PatternedPanel`         | Custom `JPanel` with gradient background        |
| `User`                   | Abstract class for user roles                   |
| `Admin`                  | Manages reports and users                       |
| `Authority`              | Posts alerts and converts reports               |
| `Citizen`                | Submits reports and views updates               |
| `Reportable`             | Interface for reports and alerts                |
| `IncidentReport`         | Represents a report from a citizen              |
| `Alert`                  | Represents an alert posted by an authority      |
| `ReportService`          | Manages report and alert operations             |
| `UserService`            | Handles registration and user management        |
| `DatabaseConnection`     | Manages connection to MySQL database            |
| `AuthenticationException`| Custom exception for login issues               |
| `ReportValidationException` | Custom exception for invalid reports       |

---

## 📊 Use Cases

| Actor      | Actions                                                              |
|------------|----------------------------------------------------------------------|
| **Admin**      | Login, Approve/Reject Reports, Manage Users, View Reports/Alerts     |
| **Authority**  | Login, Post Alerts, Convert Reports, View Reports/Alerts             |
| **Citizen**    | Register, Login, Submit Reports, View Approved Reports/Alerts        |
| **System**     | Store and Retrieve data in the database                              |

---

## 📈 Use Case Diagram (Simplified)

- **Admin** → Approves reports, manages users, views all data  
- **Authority** → Posts alerts, converts reports, views updates  
- **Citizen** → Submits reports, views alerts and approved reports  
- **System** → Handles database operations  

---

## 📧 Contact

**Name**: Rohit Kumar Chaudhary  
**Email**: [rohitkc52005@gmail.com](mailto:rohitkc52005@gmail.com)

