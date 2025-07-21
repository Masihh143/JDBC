import java.util.Scanner;

public class InputHelper {

    // Get a valid integer choice within a range (like menu options)
    public static int getValidatedChoice(Scanner sc, int min, int max) {
        while (true) {
            System.out.print("option: ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return -1; // special value meaning cancel
            }

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

    // Get a text input like name/email with cancel support
    public static String getValidatedText(Scanner sc, String fieldName) {
        while (true) {
            System.out.print(fieldName + ": ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return "cancel";
            }

            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Try again or type 'cancel'.");
            }
        }
    }

    // Confirm yes/no (for delete or exit confirmation)
    public static boolean getConfirmation(Scanner sc, String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();

            if (input.equals("cancel")) {
                return false;
            }

            if (input.equals("y")) return true;
            if (input.equals("n")) return false;

            System.out.println("Please enter 'y' or 'n'. Or type 'cancel' to abort.");
        }
    }
}
