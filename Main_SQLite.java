import java.util.Scanner;

public class Main_SQLite {
    public static void main(String[] args) {
        DBconnector_SQLite.initializeDatabase();
        Scanner sc = new Scanner(System.in);
        System.out.println("\n🌐 SQLite Project Management System");

        while (true) {
            System.out.println("\n========================== Main Menu ==========================");
            System.out.println("Select one operation from below:");
            System.out.println("1. Insert");
            System.out.println("2. Frequent query");
            System.out.println("0. Exit");
            String choice = sc.nextLine().trim();

            if (choice.equals("0") || choice.equals("exit")) {
                System.out.println("👋 Exiting the program. Goodbye!");
                break;
            }


            switch (choice) {
                case "1":
                    System.out.println("operation: ADDING");
                    InsertHandler.handle(sc);
                    break;
                case "5":
                    System.out.println("operation: FREQUENT QUERY");
                    FQ.handle(sc);
                    break;
                default:
                    System.out.println("❌ Invalid option. Please select one from above.");
            }
        }
    }
}
