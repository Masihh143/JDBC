import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InsertHandler {

    public static void insertProject(Scanner sc) {
        System.out.println("--- operation: ADDING ---");
        System.out.println("\n--- Insert New Project ---");

        String name = InputHelper.getNonEmptyString(sc, "Enter project name: ");
        if (name == null) return;

        String description = InputHelper.getNonEmptyString(sc, "Enter project description: ");
        if (description == null) return;

        LocalDate startDate = InputHelper.getValidatedDate(sc, "Enter start date (yyyy-mm-dd): ");
        if (startDate == null) return;

        LocalDate archived_at = InputHelper.getValidatedDate(sc, "Enter archive date (yyyy-mm-dd): ");
        if (archived_at == null) return;

        if (archived_at.isBefore(startDate)) {
            System.out.println("❌ End date cannot be before start date. Insert operation cancelled.");
            return;
        }

        String status = InputHelper.getProjectStatus(sc);
        if (status == null) return;

        // Insert into DB
        try (Connection conn = DBconnector.getConnection()) {
            if (conn == null) {
                System.out.println("❌ Could not connect to database.");
                return;
            }

            String sql = "INSERT INTO projects (name, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDate(3, java.sql.Date.valueOf(startDate));
            stmt.setDate(4, java.sql.Date.valueOf(archived_at));
            stmt.setString(5, status);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Project inserted successfully.");
            } else {
                System.out.println("⚠️ Insert failed.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Database error occurred:");
            e.printStackTrace();
        }
    }


    public static void insertEmployee(Scanner sc) {
        System.out.println("--- operation: ADDING ---");
        System.out.println("--- Insert New Employee ---");

        String name = InputHelper.getNonEmptyString(sc, "employee name: ");
        if (name == null) return;

        String email = InputHelper.getNonEmptyString(sc, "employee email: ");
        if (email == null) return;

        String phone = InputHelper.getNonEmptyString(sc, "employee phone: ");
        if (phone == null) return;

        String designation = InputHelper.getEmployeeDesig(sc);
        if (designation == null) return;

        try (Connection conn = DBconnector.getConnection()) {
            if (conn == null) {
                System.out.println("❌ Could not connect to database.");
                return;
            }

            String sql = "INSERT INTO employees (name, email, phone, designation) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, designation);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Employee inserted successfully.");
            } else {
                System.out.println("⚠️ Insert failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTask(Scanner sc) {
        System.out.println("--- peration: ADDING ---");
        System.out.println("--- Insert New Task ---");

        int projectId = InputHelper.getValidatedInt(sc, "project id: ");
        if (projectId == -1) return;

        String title = InputHelper.getNonEmptyString(sc, "task title: ");
        if (title == null) return;

        String description = InputHelper.getNonEmptyString(sc, "task description: ");
        if (description == null) return;

        LocalDate dueDate = InputHelper.getValidatedDate(sc, "due date (yyyy-mm-dd): ");
        if (dueDate == null) return;

        String status = InputHelper.getTaskStatus(sc);
        if (status == null) return;

        try (Connection conn = DBconnector.getConnection()) {
            String sql = "INSERT INTO tasks (project_id, title, description, due_date, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, projectId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setDate(4, java.sql.Date.valueOf(dueDate));
            stmt.setString(5, status);
            int rows = stmt.executeUpdate();

            if (rows > 0) System.out.println("✅ Task inserted successfully.");
            else System.out.println("⚠️ Insert failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertAssignment(Scanner sc) {
        System.out.println("operation: ADDING");
        System.out.println("--- Insert New Assignment ---");

        int employeeId = InputHelper.getValidatedInt(sc, "employee_id: ");
        if (employeeId == -1) return;

        int taskId = InputHelper.getValidatedInt(sc, "task_id: ");
        if (taskId == -1) return;

        try (Connection conn = DBconnector.getConnection()) {
            String sql = "INSERT INTO assignments (employee_id, task_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, employeeId);
            stmt.setInt(2, taskId);
            int rows = stmt.executeUpdate();

            if (rows > 0) System.out.println("✅ Assignment inserted successfully.");
            else System.out.println("⚠️ Insert failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTimesheet(Scanner sc) {
        System.out.println("operation: ADDING");
        System.out.println("--- Insert New Timesheet Entry ---");

        int employeeId = InputHelper.getValidatedInt(sc, "employee_id: ");
        if (employeeId == -1) return;

        int taskId = InputHelper.getValidatedInt(sc, "task_id: ");
        if (taskId == -1) return;

        LocalDate date = InputHelper.getValidatedDate(sc, "date (yyyy-mm-dd): ");
        if (date == null) return;

        double hours = InputHelper.getValidatedDouble(sc, "hours_worked: ");
        if (hours < 0) return;

        try (Connection conn = DBconnector.getConnection()) {
            String sql = "INSERT INTO timesheets (employee_id, task_id, date, hours_worked) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, employeeId);
            stmt.setInt(2, taskId);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            stmt.setDouble(4, hours);
            int rows = stmt.executeUpdate();

            if (rows > 0) System.out.println("✅ Timesheet entry inserted successfully.");
            else System.out.println("⚠️ Insert failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

}
