import java.io.*;
import java.util.*;

public class ExpenseManager {
    private final List<Expense> expenses = new ArrayList<>();
    private final String fileName;
    private int nextId = 1;

    public ExpenseManager(String fileName) {
        this.fileName = fileName;
        load();
    }

    public void add(double amount, String category, java.time.LocalDate date, String description) {
        expenses.add(new Expense(nextId++, amount, category, date, description));
        save();
    }

    public boolean delete(int id) {
        boolean removed = expenses.removeIf(e -> e.getId() == id);
        if (removed) save();
        return removed;
    }

    public List<Expense> getAll() {
        return expenses;
    }

    public double getTotal() {
        double total = 0;
        for (Expense e : expenses) total += e.getAmount();
        return total;
    }

    // Category name -> total spent in that category (sorted by name)
    public Map<String, Double> getCategoryTotals() {
        Map<String, Double> totals = new TreeMap<>();
        for (Expense e : expenses) {
            totals.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return totals;
    }

    private void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (Expense e : expenses) out.println(e.toFileString());
        } catch (IOException ex) {
            System.out.println("Could not save data: " + ex.getMessage());
        }
    }

    private void load() {
        File file = new File(fileName);
        if (!file.exists()) return;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isBlank()) continue;
                Expense e = Expense.fromFileString(line);
                expenses.add(e);
                if (e.getId() >= nextId) nextId = e.getId() + 1;
            }
        } catch (IOException | RuntimeException ex) {
            System.out.println("Could not read saved data: " + ex.getMessage());
        }
    }
}
