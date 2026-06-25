package utils;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Value must be between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Value must be between %.2f and %.2f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static void pressEnterToContinue() {
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();
    }
}
