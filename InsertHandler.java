import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class InsertHandler {

    public static void handle(Scanner sc) {
        while (true) {
            System.out.println("\nSelect a table to insert into:");
            System.out.println("1. Employee");
            System.out.println("2. Project");
            System.out.println("3. Task");
            System.out.println("4. Task-Assign");
            System.out.println("5. task-dependendency");
            System.out.println("6. Comment");
            System.out.println("0. Back to Main Menu");
            String choice = sc.nextLine().trim();
            System.out.println("-------------------------------------------------------------------------------");


            switch (choice) {
                case "1":
                    insertEmployee(sc);
                    break;
                case "2":
                    insertProject(sc);
                    break;
                case "3":
                    insertTask(sc);
                    break;
                case "4":
                    insertAssignment(sc);
                    break;
                case "5":
                    insertDependency(sc);
                    break;
                case "6":
                    insertComment(sc);
                    break;
                case "0":
                    return; // go back to main menu
                default:
                    System.out.println("❌ Invalid option. Please select from 1-6 or 0.");
            }
        }
    }




    public static void insertEmployee(Scanner sc) {
        System.out.println("--- operation: ADDING ---");
        System.out.println("--- Insert New Employee ---");

        String name = InputHelper.getNonEmptyString(sc, "employee name: ");
        if (name == null) return;

        String email = InputHelper.getNonEmptyString(sc, "employee email: ");
        if (email == null) return;

        String phone = InputHelper.getValidatedPhone(sc, "employee phone: ");
        if (phone == null) return;

        String designation = InputHelper.getEmployeeDesig(sc);
        if (designation == null) return;

        try (Connection conn = DBconnector.getConnection()) {
            if (conn == null) {
                System.out.println("❌ Could not connect to database.");
                return;
            }

            String sql = "INSERT INTO employee (emp_name, emp_email, emp_desig, emp_phone_no) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, designation);
            stmt.setString(4, phone);

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




    public static void insertProject(Scanner sc) {
        System.out.println("--- operation: ADDING ---");
        System.out.println("\n--- Insert New Project ---");

        String name = InputHelper.getNonEmptyString(sc, "Enter project name: ");
        if (name == null) return;

        String description = InputHelper.getNonEmptyString(sc, "Enter project description: ");
        if (description == null) return;

        LocalDate startDate = InputHelper.getValidatedDate(sc, "Enter start date (yyyy-mm-dd): ");
        if (startDate == null) return;

        LocalDate archive_at = InputHelper.getValidatedDate(sc, "Enter archive date (yyyy-mm-dd) or null: ");
        if (archive_at == null) return;

        if (archive_at.isBefore(startDate)) {
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

            String sql = "INSERT INTO project (p_name, p_descrip, start_date, archive_at, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDate(3, java.sql.Date.valueOf(startDate));
            stmt.setDate(4, java.sql.Date.valueOf(archive_at));
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







    public static void insertTask(Scanner sc) {
        System.out.println("--- Operation: ADDING ---");
        System.out.println("--- Insert New Task ---");
    
        int p_id = InputHelper.getValidatedInt(sc, "project id: ");
        if (p_id == -1) return;
    
        // FK Check for project existence
        boolean isValid = FkValidator.validateWithNavigation(
            sc,
            "Project",
            p_id,
            FkValidator::projectExists,   // ✅ Function passed correctly
            () -> insertTask(sc)          // retry operation
        );
        if (!isValid) return;
    
        String title = InputHelper.getNonEmptyString(sc, "task title: ");
        if (title == null) return;
    
        String description = InputHelper.getNonEmptyString(sc, "task description: ");
        if (description == null) return;
    
        LocalDate dueDate = InputHelper.getValidatedDate(sc, "due date (yyyy-mm-dd): ");
        if (dueDate == null) return;
    
        LocalDate archive_at = InputHelper.getValidatedDate(sc, "Enter archive date (yyyy-mm-dd) or null: ");
        if (archive_at == null) return;

        String status = InputHelper.getTaskStatus(sc);
        if (status == null) return;

        LocalDate start_time = InputHelper.getValidatedDate(sc, "Enter start date (yyyy-mm-dd): ");
        if (start_time == null) return;
    
        try (Connection conn = DBconnector.getConnection()) {
            String sql = "INSERT INTO tasks (p_id, task_title, task_descrip, due_date, archive_at ,status ,start_time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p_id);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setDate(4, java.sql.Date.valueOf(dueDate));
            stmt.setDate(5, java.sql.Date.valueOf(archive_at));
            stmt.setString(6, status);
            stmt.setDate(7, java.sql.Date.valueOf(start_time));

            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Project inserted successfully.");
            } else {
                System.out.println("⚠️ Insert failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static void insertAssignment(Scanner sc) {
        System.out.println("--- Operation: ADDING ---");
        System.out.println("--- Insert New Assignment ---");
    
        int employeeId = InputHelper.getValidatedInt(sc, "employee_id: ");
        if (employeeId == -1) return;
    
        // Validate Employee existence
        boolean empValid = FkValidator.validateWithNavigation(
            sc,
            "Employee",
            employeeId,
            FkValidator::employeeExists,        // You need to define this function in FkValidator
            () -> insertAssignment(sc)
        );
        if (!empValid) return;
    
        int taskId = InputHelper.getValidatedInt(sc, "task_id: ");
        if (taskId == -1) return;
    
        // Validate Task existence
        boolean taskValid = FkValidator.validateWithNavigation(
            sc,
            "Task",
            taskId,
            FkValidator::taskExists,           // You need to define this function in FkValidator
            () -> insertAssignment(sc)
        );
        if (!taskValid) return;
    
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
    

    public static void insertComment(Scanner sc) {
        System.out.println("operation: ADDING");
        System.out.println("--- Insert New Comment ---");
    
        int employeeId = InputHelper.getValidatedInt(sc, "employee_id: ");
        if (employeeId == -1) return;
    
        // ✅ Validate Employee
        boolean isValidEmployee = FkValidator.validateWithNavigation(
            sc,
            "Employee",
            employeeId,
            FkValidator::employeeExists,
            () -> insertComment(sc)  // retry logic
        );
        if (!isValidEmployee) return;
    
        int taskId = InputHelper.getValidatedInt(sc, "task_id: ");
        if (taskId == -1) return;
    
        // ✅ Validate Task
        boolean isValidTask = FkValidator.validateWithNavigation(
            sc,
            "Task",
            taskId,
            FkValidator::taskExists,
            () -> insertComment(sc)  // retry logic
        );
        if (!isValidTask) return;
    
        LocalDate created_at = InputHelper.getValidatedDate(sc, "date (yyyy-mm-dd): ");
        if (created_at == null) return;
    
        String body = InputHelper.getNonEmptyString(sc , "body: ");
        if (body == null) return;
    
        try (Connection conn = DBconnector.getConnection()) {
            String sql = "INSERT INTO comment (task_id, emp_id, body, created_at) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, taskId);
            stmt.setInt(2, employeeId);
            stmt.setString(3, body);
            stmt.setDate(4, java.sql.Date.valueOf(created_at));
    
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Comment inserted successfully.");
            else System.out.println("⚠️ Insert failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void insertDependency(Scanner sc) {
        System.out.println("operation: ADDING");
        System.out.println("--- Insert Task Dependency ---");
    
        int taskId = InputHelper.getValidatedInt(sc, "task_id: ");
        boolean isValidTask = FkValidator.validateWithNavigation(
            sc,
            "Task",
            taskId,
            FkValidator::taskExists,
            () -> insertDependency(sc)
        );
        if (!isValidTask) return;
    
        int dependentTaskId = InputHelper.getValidatedInt(sc, "dependent_task_id (d_task_id): ");
        boolean isValidDependent = FkValidator.validateWithNavigation(
            sc,
            "Dependent Task",
            dependentTaskId,
            FkValidator::taskExists,
            () -> insertDependency(sc)
        );
        if (!isValidDependent) return;
    
        if (taskId == dependentTaskId) {
            System.out.println("❌ A task cannot depend on itself.");
            return;
        }
    
        try (Connection conn = DBconnector.getConnection()) {
            String sql = "INSERT INTO task_dependency (task_id, d_task_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, taskId);
            stmt.setInt(2, dependentTaskId);
            int rows = stmt.executeUpdate();
    
            if (rows > 0)
                System.out.println("✅ Task dependency inserted successfully.");
            else
                System.out.println("⚠️ Insert failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
}