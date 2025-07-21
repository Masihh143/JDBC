import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateHandler {

    public static void handle(Scanner sc) {
        System.out.println("\n==================== Update Project ====================");
        
        int projectId = InputHelper.getIntInput(sc, "Enter project ID to update (or -1 to cancel): ");
        if (projectId == -1) {
            System.out.println("❌ Update cancelled.");
            return;
        }

        String newName = InputHelper.getStringInput(sc, "Enter new project name: ");
        String newDescription = InputHelper.getStringInput(sc, "Enter new project description: ");

        try (Connection conn = DBconnector.getConnection()) {
            String sql = "UPDATE projects SET name = ?, description = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setInt(3, projectId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Project updated successfully.");
            } else {
                System.out.println("⚠️ No project found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error updating project: " + e.getMessage());
        }

        System.out.println("---------------------------------------------------------");
    }
}
