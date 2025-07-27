import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
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

    public static boolean validDateAssign(int projectId, LocalDate taskStartTime) throws SQLException {
        try (Connection conn = DBconnector.getConnection()){
            String sql = "SELECT start_date FROM project WHERE p_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate projectStart = rs.getObject("start_date", LocalDate.class);
    
                if (projectStart == null) {
                    System.out.println("Project has no start_date set.");
                    return false; 
                }
    
                if (!taskStartTime.isBefore(projectStart)) {
                    return true; // Valid
                } else {
                    System.out.println("Task cannot start before the project begins!");
                    System.out.println("Parent project start date: " + projectStart);
                    return false; // Invalid
                }
            } else {
                System.out.println("Project not found!");
                return false; 
            }
        } catch (SQLException e) {
            System.out.println("Error checking parent project timestamp: " + e.getMessage());
            return false;
        }
        
    }








     public static Map<Integer, List<Integer>> buildGraphFromDB() {            // circular dependency
        Map<Integer, List<Integer>> graph = new HashMap<>();

        try (Connection conn = DBconnector.getConnection()) {
            String sql = "SELECT task_id, d_task_id FROM task_dependency";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int from = rs.getInt("task_id");
                int to = rs.getInt("d_task_id");

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static boolean createsCycle(Map<Integer, List<Integer>> graph, int u, int v) {
        // Add temporary edge u → v
        graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);

        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();

        boolean hasCycle = dfsCycleCheck(graph, v, u, visited, recStack);

        // Remove temporary edge
        graph.get(u).remove(Integer.valueOf(v));

        return hasCycle;
    }


    private static boolean dfsCycleCheck(Map<Integer, List<Integer>> graph, int current, int target, Set<Integer> visited, Set<Integer> recStack) {
        if (current == target) return true;
        if (recStack.contains(current)) return false;

        visited.add(current);
        recStack.add(current);

        for (int neighbor : graph.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                if (dfsCycleCheck(graph, neighbor, target, visited, recStack)) {
                    return true;
                }
            }
        }

        recStack.remove(current);
        return false;
    }


}
