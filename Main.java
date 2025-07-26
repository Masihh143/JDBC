

// /JDBCProject
// │
// ├── Main.java                      <-- Main Menu + Entry Point
// ├── InsertHandler.java            <-- Handles Insert logic
// ├── UpdateHandler.java            <-- Handles Update logic
// ├── DeleteHandler.java            <-- Handles Delete logic
// ├── DisplayHandler.java           <-- Handles Display logic
// └── InputHelper.java              <-- Handles input logic
// └── DBconnector.java


import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n========================== Main Menu ==========================");
            System.out.println("Select one operation from below:");
            System.out.println("1. Insert");
            System.out.println("2. Update");
            System.out.println("3. Delete");
            System.out.println("4. Display");
            System.out.println("0. Exit");
            String choice = sc.nextLine().trim();
            System.out.println("-------------------------------------------------------------------------------");

            switch (choice) {
                case "1":
                    System.out.println("operation: ADDING");
                    InsertHandler.handle(sc);
                    break;
                case "2":
                    System.out.println("operation: UPDATING");
                    // UpdateHandler.handle(sc);
                    break;
                case "3":
                    System.out.println("operation: DELETING");
                    // DeleteHandler.handle(sc);
                    break;
                case "4":
                    System.out.println("operation: DISPLAYING");
                    // DisplayHandler.handle(sc);
                    break;
                case "0":
                    System.out.println("Exiting the program. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("❌ Invalid option. Please select between 1-5.");
            }
        }
    }
}
