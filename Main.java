import java.io.*;
import java.util.*;

public class Main {
    private static final String FILENAME = "budget_data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load budget data if available
        Map<String, Double> expenses = new HashMap<>();
        double income = 0;
        double savingsGoal = 0;
        try {
            loadBudgetData(expenses);
            income = expenses.getOrDefault("Income", 0.0);
            savingsGoal = expenses.getOrDefault("Savings Goal", 0.0);
            expenses.remove("Income");
            expenses.remove("Savings Goal");
        } catch (IOException e) {
            System.out.println("No budget data found.");
        }

        // Input income
        System.out.print("Enter your monthly income: ");
        income = scanner.nextDouble();

        // Input expenses
        System.out.println("Enter your monthly expenses (enter 'done' to finish): ");
        inputExpenses(scanner, expenses);

        // Input savings goal
        System.out.print("Enter your monthly savings goal: ");
        savingsGoal = scanner.nextDouble();

        // Calculate remaining budget
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        double remainingBudget = income - totalExpenses;

        // Check if savings goal is met
        boolean isSavingsGoalMet = remainingBudget >= savingsGoal;

        // Display budget summary
        System.out.println("\nBudget Summary:");
        System.out.println("------------------------------");
        System.out.printf("Income: $%.2f%n", income);
        System.out.println("Expenses:");
        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            System.out.printf("- %s: $%.2f%n", entry.getKey(), entry.getValue());
        }
        System.out.printf("Total Expenses: $%.2f%n", totalExpenses);
        System.out.printf("Savings Goal: $%.2f%n", savingsGoal);
        System.out.printf("Remaining Budget: $%.2f%n", remainingBudget);
        if (isSavingsGoalMet) {
            System.out.println("Congratulations! You've met your savings goal.");
        } else {
            System.out.println("You have not met your savings goal yet.");
        }

        // Save budget data
        try {
            saveBudgetData(income, savingsGoal, expenses);
            System.out.println("Budget data saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save budget data.");
        }

        scanner.close();
    }

    private static void inputExpenses(Scanner scanner, Map<String, Double> expenses) {
        while (true) {
            System.out.print("Expense category: ");
            String category = scanner.next();
            if (category.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("Amount: $");
            double amount = scanner.nextDouble();
            expenses.put(category, amount);
        }
    }

    private static void loadBudgetData(Map<String, Double> expenses) throws IOException {
        File file = new File(FILENAME);
        if (!file.exists()) {
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            expenses.put(parts[0], Double.parseDouble(parts[1]));
        }
        reader.close();
    }

    private static void saveBudgetData(double income, double savingsGoal, Map<String, Double> expenses) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
        writer.write("Income:" + income + "\n");
        writer.write("Savings Goal:" + savingsGoal + "\n");
        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
        }
        writer.close();
    }
}
