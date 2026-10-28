import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager("expenses.csv");

        while (true) {
            System.out.println("\n===== Expense Tracker =====");
            System.out.println("1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. Delete an expense");
            System.out.println("4. Show total spent");
            System.out.println("5. Show total per category");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addExpense(manager);
                case "2" -> viewAll(manager);
                case "3" -> deleteExpense(manager);
                case "4" -> System.out.printf("Total spent: Rs.%.2f%n", manager.getTotal());
                case "5" -> showCategoryTotals(manager);
                case "6" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option, try 1-6.");
            }
        }
    }

    private static void addExpense(ExpenseManager manager) {
        double amount = readAmount();
        System.out.print("Category (e.g. Food, Travel, Bills): ");
        String category = sc.nextLine().trim().replace(",", " ");
        if (category.isEmpty()) category = "Other";
        LocalDate date = readDate();
        System.out.print("Description: ");
        String description = sc.nextLine().trim().replace(",", " ");

        manager.add(amount, category, date, description);
        System.out.println("Expense added.");
    }

    private static double readAmount() {
        while (true) {
            System.out.print("Amount: ");
            try {
                double amount = Double.parseDouble(sc.nextLine().trim());
                if (amount > 0) return amount;
                System.out.println("Amount must be more than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static LocalDate readDate() {
        while (true) {
            System.out.print("Date (YYYY-MM-DD, press Enter for today): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) return LocalDate.now();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use the format YYYY-MM-DD.");
            }
        }
    }

    private static void viewAll(ExpenseManager manager) {
        if (manager.getAll().isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        System.out.printf("%-4s %-12s %-12s %-13s %s%n", "ID", "Date", "Category", "Amount", "Description");
        for (Expense e : manager.getAll()) System.out.println(e);
    }

    private static void deleteExpense(ExpenseManager manager) {
        System.out.print("Enter the ID to delete: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.println(manager.delete(id) ? "Deleted." : "No expense with that ID.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID number.");
        }
    }

    private static void showCategoryTotals(ExpenseManager manager) {
        Map<String, Double> totals = manager.getCategoryTotals();
        if (totals.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.printf("%-12s Rs.%.2f%n", entry.getKey(), entry.getValue());
        }
    }
}
