import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBconnector_SQLite {
    private static final String DB_URL = "jdbc:sqlite:project_management.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employee (
                    emp_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    emp_name TEXT NOT NULL,
                    emp_email TEXT UNIQUE NOT NULL,
                    emp_desig TEXT NOT NULL,
                    emp_phone_no TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS project (
                    p_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    p_name TEXT NOT NULL,
                    p_descrip TEXT,
                    start_date TEXT,
                    archive_at TEXT,
                    status TEXT DEFAULT 'Not Started'
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS task (
                    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    p_id INTEGER,
                    task_title TEXT NOT NULL,
                    task_descrip TEXT,
                    due_date TEXT,
                    archive_at TEXT,
                    status TEXT DEFAULT 'Not Assigned',
                    start_time TEXT,
                    FOREIGN KEY (p_id) REFERENCES project(p_id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS task_employee (
                    task_id INTEGER,
                    emp_id INTEGER,
                    role TEXT,
                    PRIMARY KEY (task_id, emp_id),
                    FOREIGN KEY (task_id) REFERENCES task(task_id),
                    FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS task_dependency (
                    task_id INTEGER,
                    d_task_id INTEGER,
                    PRIMARY KEY (task_id, d_task_id),
                    FOREIGN KEY (task_id) REFERENCES task(task_id),
                    FOREIGN KEY (d_task_id) REFERENCES task(task_id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS comment (
                    c_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    task_id INTEGER,
                    emp_id INTEGER,
                    body TEXT,
                    created_at TEXT DEFAULT (datetime('now')),
                    FOREIGN KEY (task_id) REFERENCES task(task_id),
                    FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
                );
            """);

            System.out.println("✅ SQLite database initialized with schema.");
        } catch (SQLException e) {
            System.out.println("❌ DB init failed:");
            e.printStackTrace();
        }
    }
}
