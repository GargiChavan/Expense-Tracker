import java.time.LocalDate;

public class Expense {
    private final int id;
    private final double amount;
    private final String category;
    private final LocalDate date;
    private final String description;

    public Expense(int id, double amount, String category, LocalDate date, String description) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }

    // One line of the save file: id,amount,category,date,description
    public String toFileString() {
        return id + "," + amount + "," + category + "," + date + "," + description;
    }

    // Rebuilds an Expense from one line of the save file
    public static Expense fromFileString(String line) {
        String[] p = line.split(",", 5);
        return new Expense(Integer.parseInt(p[0]), Double.parseDouble(p[1]),
                p[2], LocalDate.parse(p[3]), p[4]);
    }

    @Override
    public String toString() {
        return String.format("%-4d %-12s %-12s Rs.%-10.2f %s", id, date, category, amount, description);
    }
}
