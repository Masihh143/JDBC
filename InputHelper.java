import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class InputHelper {

    // ----- EXISTING FUNCTIONS -----

    // 1. Get a valid integer choice within a range (like menu options)
    public static int getValidatedChoice(Scanner sc, int min, int max) {
        while (true) {
            System.out.print("option: ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return -1;
            }
            if (input.equalsIgnoreCase("exit")) exitProgram();


            try {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Please enter a valid option (" + min + " - " + max + ")");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or type 'cancel' to go back.");
            }
        }
    }

    // 2. Get a text input like name/email with cancel support
    public static String getValidatedText(Scanner sc, String fieldName) {
        while (true) {
            System.out.print(fieldName + ": ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return "cancel";
            }
            if (input.equalsIgnoreCase("exit")) exitProgram();


            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Try again or type 'cancel'.");
            }
        }
    }

    // 3. Confirm yes/no (for delete or exit confirmation)
    public static boolean getConfirmation(Scanner sc, String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();

            if (input.equals("cancel")) {
                return false;
            }
            if (input.equalsIgnoreCase("exit")) exitProgram();


            if (input.equals("y")) return true;
            if (input.equals("n")) return false;

            System.out.println("Please enter 'y' or 'n'. Or type 'cancel' to abort.");
        }
    }

    // ----- NEW INSERT-RELATED HELPERS -----

    // 4. Non-empty string with exit support
    public static String getNonEmptyString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return null;
            }
            if (input.equalsIgnoreCase("exit")) exitProgram();


            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Try again or type 'cancel'.");
            }
        }
    }

    // 5. Validated date in yyyy-mm-dd format
    public static LocalDate getValidatedDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return null;
            }
            if (input.equalsIgnoreCase("exit")) exitProgram();


            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please use format yyyy-mm-dd or type 'cancel'.");
            }
        }
    }

    // 6. Simple validated int (with cancel support)
    public static int getValidatedInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) return -1;
            if (input.equalsIgnoreCase("exit")) exitProgram();


            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again or type 'cancel'.");
            }
        }
    }

    // validated phone number allowing 10 digits
    public static String getValidatedPhone(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
    
            if (input.equalsIgnoreCase("cancel")) return null;
            if (input.equalsIgnoreCase("exit")) exitProgram();
    
            // Basic validation
            if (input.matches("\\d{10}")) {
                return input;
            } else {
                System.out.println("Invalid phone number. Enter a 10-digit number or type 'cancel'.");
            }
        }
    }
    

    // 7. Enum validation: Project status
    public static String getProjectStatus(Scanner sc) {
        Map <String , String> validStatus = Map.of(
            "NOT STARTED" , "Not Started",
            "IN PROGRESS" , "In Progress",
            "COMPLETED" , "Completed",
            "ARCHIVED" , "Archived"
        );
        while (true) {
            System.out.print("Enter project status (Not Started / In Progress / Completed / Archived): ");
            String input = sc.nextLine().trim().toUpperCase();

            if (input.equalsIgnoreCase("cancel")) return null;
            if (input.equalsIgnoreCase("exit")) exitProgram();


            if(validStatus.containsKey(input)){
                return validStatus.get(input);
            }

            System.out.println("Invalid status. Please choose a vaild");
        }
    }

    // 8. Enum validation: Employee designation
    public static String getEmployeeDesig(Scanner sc) {
        Map<String, String> validDesignations = Map.of(
            "MANAGER", "Manager",
            "SDE-1", "SDE-1",
            "SDE-2", "SDE-2",
            "SDE-3", "SDE-3",
            "SDE-4", "SDE-4",
            "TESTER", "Tester",
            "DESIGNER", "Designer"
        );

        while (true) {
            System.out.print("Enter designation (Manager / SDE-1 / SDE-2 / SDE-3 / SDE-4 / Tester / Designer): ");
            String input = sc.nextLine().trim().toUpperCase();

            if (input.equalsIgnoreCase("cancel")) return null;
            if (input.equalsIgnoreCase("exit")) exitProgram();

            if (validDesignations.containsKey(input)) {
                return validDesignations.get(input); // Return with proper case
            }

            System.out.println("Invalid designation. Please choose a valid one.");
        }
    }


    // 9. Enum validation: Task status
    public static String getTaskStatus(Scanner sc) {
        Map<String , String> validTask = Map.of(
            "NOT ASSIGNED" , "Not Assigned",
            "BLOCKED" , "Blocked",
            "NOT STARTED" , "Not Started",
            "IN PROGRESS" , "In Progress",
            "COMPLETED" , "Completed",
            "OVERDUE" , "Overdue"
        );
        while (true) {
            System.out.print("Enter task status (Not Assigned / Blocked / Not Started / In Progress / Completed / Overdue): ");
            String input = sc.nextLine().trim().toUpperCase();

            if (input.equalsIgnoreCase("cancel")) return null;
            if (input.equalsIgnoreCase("exit")) exitProgram();


            if (validTask.containsKey(input)) {
                return validTask.get(input);
            }

            System.out.println("Invalid task status. Please choose a valid one.");
        }
    }

    // function to exit the program
    private static void exitProgram() {
        System.out.println("👋 Exiting program...");
        System.exit(0);
    }
} 