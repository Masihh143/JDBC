import java.sql.*;
import java.util.Scanner;
import java.util.function.Function;

public class FkValidator {

    public static boolean validateWithNavigation(
        Scanner sc,
        String entityName,
        int id,
        Function<Integer, Boolean> existsFunc,
        RetryableOperation retryOperation
    ) {
        if (existsFunc.apply(id)) {
            return true;
        }

        System.out.println("Invalid, " + entityName + " with ID " + id + " does not exist.");
        System.out.print("Do you want to retry? (Y/N): ");
            String response = sc.nextLine().trim();
            if (response.equalsIgnoreCase("Y")) {
                retryOperation.execute();
            } else {
                System.out.println("Operation cancelled.");
            }

        return false;
    }

    // ✅ Method to check if a project with given ID exists
    public static boolean projectExists(int projectId) {
        try (Connection conn = DBconnector.getConnection()) {
            String sql = "SELECT 1 FROM project WHERE p_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean employeeExists(int empId) {
        try (Connection conn = DBconnector.getConnection()) {
            String query = "SELECT 1 FROM employee WHERE emp_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking employee existence: " + e.getMessage());
            return false;
        }
    }

    public static boolean taskExists(int taskId) {
        try (Connection conn = DBconnector.getConnection()) {
            String query = "SELECT 1 FROM task WHERE task_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking task existence: " + e.getMessage());
            return false;
        }
    }
}
