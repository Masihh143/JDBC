# Project Management Console Application (SQLite + Java)

This is a **Java-based console application** for managing projects, employees, and task assignments. It uses **SQLite** as the underlying database via the `sqlite-jdbc` driver.

### ✅ Features:

* Add new employees and projects
* Assign employees to projects
* Display employee, project, and assignment info
* Validates foreign key constraints in-app
* Retryable operations for reliability

---

## 🔧 How to Run

### 1. Prerequisites

* Java installed (JDK 8 or above)
* `sqlite-jdbc-3.50.3.0.jar` downloaded in the project directory

> Download from: [Maven Central - sqlite-jdbc](https://search.maven.org/artifact/org.xerial/sqlite-jdbc/3.50.3.0/jar)

### 2. Project Folder Structure

```
project-folder/
├── Main_SQLite.java
├── FkValidator.java
├── InputHelper.java
├── InsertHandler.java
├── RetryableOperation.java
├── DBConnector_SQLite.java
├── FQ.java
├── sqlite-jdbc-3.50.3.0.jar
└── start.sh
```

### 3. Make Script Executable (first time only)

```bash
chmod +x start.sh
```

### 4. Run the App

```bash
./start.sh
```

---

## 📈 Program Flow (Simple Flowchart)

```
+------------------+
|  Start Program   |
+------------------+
         |
         v
+-------------------------+
|   Show Main Menu        |
|   1. Add Employee       |
|   2. Add Project        |
|   3. Assign Employee    |
|   4. View Info          |
|   5. Exit               |
+-------------------------+
         |
         v
+-------------------------------+
|  Call Relevant Input Method   |
|  → Validate Inputs            |
|  → Perform SQLite Insertion   |
+-------------------------------+
         |
         v
+----------------+
| Show Success   |
+----------------+
         |
         v
+----------------+
|  Back to Menu  |
+----------------+
```

---

## 📁 File Descriptions

| File Name                 | Description                                                                                              |
| ------------------------- | -------------------------------------------------------------------------------------------------------- |
| `Main_SQLite.java`        | The main entry point. Displays the user menu and routes user inputs to handlers.                         |
| `FkValidator.java`        | Validates foreign key constraints before inserts. Prevents orphaned references.                          |
| `InsertHandler.java`      | Handles insertions into Employees, Projects, and Assignments tables. Calls helper and validator classes. |
| `InputHelper.java`        | Reads and sanitizes user input. Offers utility methods for input prompts.                                |
| `RetryableOperation.java` | Provides a generic retry mechanism for code blocks that may fail, adding resilience to DB operations.    |
| `DBConnector_SQLite.java` | Sets up and returns a valid SQLite database connection. Used throughout the app.                         |
| `FQ.java`                 | Stands for "Fetch Queries" — retrieves and prints information from the database tables.                  |
| `start.sh`                | Bash script to compile all `.java` files and run the application with JDBC driver.                       |

---

## 📌 Note on JDBC `.jar` File

The program depends on the `sqlite-jdbc-3.50.3.0.jar` file.

### Do I need to download it?

Yes — place the `.jar` file in the **same directory** as the source code.

> If you'd prefer a single `.jar` that includes all classes and the driver, we can bundle it for you as a fat jar. Ask if interested!
