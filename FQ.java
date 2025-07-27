import java.sql.*;
import java.util.Scanner;

public class FQ {

    static boolean flag = false;

    public static void handle(Scanner sc) {
        while (true) {
            System.out.println("\nSelect a Query:");
            System.out.println("1. List all tasks for a project with their statuses");
            System.out.println("2. Retrieve overdue tasks assigned to an employee");
            System.out.println("3. Find tasks blocked by dependencies");
            System.out.println("4. Show comment history for a task");
            System.out.println("5. Generate project progress summary ");
            System.out.println("0. Back to Main Menu");
            String choice = sc.nextLine().trim();
            System.out.println("-------------------------------------------------------------------------------");


            if (choice.equals("exit")) {
                System.out.println("👋 Exiting the program. Goodbye!");
                System.exit(0);
                sc.close();
            }


            switch (choice) {
                case "1":
                    listProjecttask(sc);
                    if (flag == true){
                        return;
                    } else {
                        break;
                    }

                case "2":
                    getEmployeeOverdueTask(sc);
                    if (flag == true){
                        return;
                    } else {
                        break;
                    }

                case "3":
                    getBlockedTasks();
                    if (flag == true){
                        return;
                    } else {
                        break;
                    }

                case "4":
                    getCommentHistory(sc);
                    if (flag == true){
                        return;
                    } else {
                        break;
                    }

                case "5":
                    getProjectProgressSummary(sc);
                    if (flag == true){
                        return;
                    } else {
                        break;
                    }

                case "0":
                    return; // go back to Main Menu
                default:
                    System.out.println("❌ Invalid option. Please select from 1-5 or 0.");
            }
        }
    }







    // 1. List all tasks for a project with their statuses
    public static void listProjecttask(Scanner sc) {
        int p_id = InputHelper.getValidatedInt(sc, "Enter project ID: ");
        if (p_id == -1) return;

        boolean projValid = FkValidator.validateWithNavigation(
            sc,
            "Project",
            p_id,
            FkValidator::projectExists,
            () -> listProjecttask(sc)
        );
        if (!projValid) return;

        String sql = "SELECT task_id, task_title, status FROM task WHERE p_id = ?";
        try (Connection conn = DBconnector_SQLite.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            System.out.println("Tasks for Project ID " + p_id + ":");
            while (rs.next()) {
                found = true;
                System.out.printf("Task ID: %d | Title: %s | Status: %s%n",
                        rs.getInt("task_id"),
                        rs.getString("task_title"),
                        rs.getString("status"));
            }

            if (!found) {
                System.out.println("No tasks found for this project.");
            }

            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 2. Retrieve overdue tasks assigned to an employee
    public static void getEmployeeOverdueTask(Scanner sc) {
        int emp_id = InputHelper.getValidatedInt(sc, "employee ID: ");
        if (emp_id == -1) return;

        // Validate Employee existence
        boolean empValid = FkValidator.validateWithNavigation(
            sc,
            "Employee",
            emp_id,
            FkValidator::employeeExists,        // You need to define this function in FkValidator
            () -> getEmployeeOverdueTask(sc)
        );
        if (!empValid) return;

        String sql = """
                SELECT t.task_id, t.task_title, t.due_date
                FROM task t
                JOIN task_employee te ON t.task_id = te.task_id
                WHERE te.emp_id = ? AND t.status = 'Overdue'
                """;

        try (Connection conn = DBconnector_SQLite.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emp_id);
            ResultSet rs = stmt.executeQuery();

            boolean found = false; 

            System.out.println("Overdue Tasks for Employee ID " + emp_id + ":");
            while (rs.next()) {
                found = true;
                System.out.printf("Task ID: %d | Title: %s | Due: %s%n",
                        rs.getInt("task_id"),
                        rs.getString("task_title"),
                        rs.getDate("due_date"));
            }

            if (!found) {
                System.out.println("There are no overdue tasks");
            }

            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 3. Find tasks blocked by dependencies
    public static void getBlockedTasks() {
        String sql = """
                SELECT t.task_id, t.task_title
                FROM task t
                WHERE t.task_id IN (
                    SELECT d_task_id
                    FROM task_dependency
                )
                """;

        try (Connection conn = DBconnector_SQLite.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            boolean found = false;
            System.out.println("Blocked Tasks:");
            while (rs.next()) {
                found = true;
                System.out.printf("Task ID: %d | Title: %s%n",
                        rs.getInt("task_id"),
                        rs.getString("task_title"));
            }

            if (!found) {
                System.out.println("No blocked tasks found.");
            }

            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 4. Show comment history for a task
    public static void getCommentHistory(Scanner sc) {
        int task_id = InputHelper.getValidatedInt(sc, "Enter task ID: ");
        if (task_id == -1) return;

        boolean taskValid = FkValidator.validateWithNavigation(
            sc,
            "Task",
            task_id,
            FkValidator::taskExists,
            () -> getCommentHistory(sc)
        );
        if (!taskValid) return;

        String sql = """
                SELECT c.body, c.created_at, e.emp_name
                FROM comment c
                JOIN employee e ON c.emp_id = e.emp_id
                WHERE c.task_id = ?
                ORDER BY c.created_at ASC
                """;

        try (Connection conn = DBconnector_SQLite.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, task_id);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            System.out.println("Comment History for Task ID " + task_id + ":");
            while (rs.next()) {
                found = true;
                System.out.printf("[%s] %s: %s%n",
                        rs.getTimestamp("created_at"),
                        rs.getString("emp_name"),
                        rs.getString("body"));
            }

            if (!found) {
                System.out.println("No comments found for this task.");
            }

            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 5. Generate project progress summary (% tasks done)
    public static void getProjectProgressSummary(Scanner sc) {
        int p_id = InputHelper.getValidatedInt(sc, "Enter project ID: ");
        if (p_id == -1) return;

        boolean projValid = FkValidator.validateWithNavigation(
            sc,
            "Project",
            p_id,
            FkValidator::projectExists,
            () -> getProjectProgressSummary(sc)
        );
        if (!projValid) return;

        String sql = """
                SELECT
                    COUNT(*) AS total,
                    SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) AS done
                FROM task
                WHERE p_id = ?
                """;

        try (Connection conn = DBconnector_SQLite.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                int done = rs.getInt("done");
                double percent = (total > 0) ? (done * 100.0 / total) : 0.0;

                System.out.printf("Project %d Progress: %.2f%% tasks done (%d/%d)%n", p_id, percent, done, total);
            } else {
                System.out.println("No tasks found for project " + p_id);
            }

            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
