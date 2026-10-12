/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sweetcupcakeshop;

import java.util.List;
import java.util.Scanner;
/**
 *
 * @author dinuk
 */
public class SweetCupcakeShop {
    
    private static final String DATA_DIR = "data";
    private static final String CUPCAKE_FILE = DATA_DIR + "/cupcakes.csv";
    private static final String USER_FILE = DATA_DIR + "/users.csv";

    public static void main(String[] args) {
        Inventory inventory = new Inventory(CUPCAKE_FILE);
        UserManager userManager = new UserManager(USER_FILE);
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Welcome to The Sweet Cupcake Shop ===");

        User currentUser = null;
        while (currentUser == null) {
            System.out.print("Username: ");
            String user = sc.nextLine().trim();
            System.out.print("Password: ");
            String pass = sc.nextLine().trim();
            currentUser = userManager.login(user, pass);
            if (currentUser == null) System.out.println("Login failed. Try again.");
        }

        System.out.println("Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");

        boolean exit = false;
        while (!exit) {
            if ("MANAGER".equalsIgnoreCase(currentUser.getRole())) {
                printManagerMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1": listAll(inventory); break;
                    case "2": addCupcake(sc, inventory); break;
                    case "3": searchCupcake(sc, inventory); break;
                    case "4": createCashier(sc, userManager); break;
                    case "0": exit = true; break;
                    default: System.out.println("Invalid choice."); break;
                }
            } else { // CASHIER
                printCashierMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1": listAll(inventory); break;
                    case "2": addCupcake(sc, inventory); break;
                    case "3": searchCupcake(sc, inventory); break;
                    case "0": exit = true; break;
                    default: System.out.println("Invalid choice."); break;
                }
            }
        }

        System.out.println("Goodbye!");
        sc.close();
    }

    private static void printManagerMenu() {
        System.out.println("\nManager Menu:");
        System.out.println("1. View all cupcakes");
        System.out.println("2. Add new cupcake");
        System.out.println("3. Search cupcakes by category/name");
        System.out.println("4. Create cashier account");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static void printCashierMenu() {
        System.out.println("\nCashier Menu:");
        System.out.println("1. View all cupcakes");
        System.out.println("2. Add new cupcake");
        System.out.println("3. Search cupcakes by category/name");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static void listAll(Inventory inventory) {
        System.out.println("\n--- All Cupcakes ---");
        List<Cupcake> all = inventory.getAll();
        if (all.isEmpty()) System.out.println("No cupcakes found.");
        else for (Cupcake c : all) System.out.println(c);
    }

    private static void addCupcake(Scanner sc, Inventory inventory) {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Category (Classic/Seasonal/Gluten-free/Custom): ");
        String category = sc.nextLine().trim();
        System.out.print("Price: ");
        double price;
        try {
            price = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price.");
            return;
        }
        inventory.addCupcake(name, category, price);
        System.out.println("Cupcake added.");
    }

    private static void searchCupcake(Scanner sc, Inventory inventory) {
        System.out.print("Enter category or name to search: ");
        String q = sc.nextLine().trim();
        List<Cupcake> res = inventory.searchByCategory(q);
        System.out.println("--- Search results ---");
        if (res.isEmpty()) System.out.println("No results.");
        else for (Cupcake c : res) System.out.println(c);
    }

    private static void createCashier(Scanner sc, UserManager um) {
        System.out.print("New cashier username: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        boolean ok = um.createCashier(u, p);
        if (ok) System.out.println("Cashier account created.");
        else System.out.println("Account creation failed (maybe username exists).");
    }
}




