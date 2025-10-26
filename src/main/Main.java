package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import adminFunction.AdminFunction;
import employeeFunction.EmployeeFunction;

public class Main {
    public static void main(String[] args) {
        // Initialize data directories and files
        initializeDataFiles();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=============== Welcome to the Roster Management System ===============");
            System.out.println("1. Login: Employee");
            System.out.println("2. Login: Administrator");
            System.out.println("3. Exit");
            System.out.print("Please select an option(1-3): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        // Employee login
                        System.out.print("Enter Employee Username: ");
                        String Username = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String Password = scanner.nextLine();

                        EmployeeFunction employeeFunction = new EmployeeFunction();
                        if (employeeFunction.login(Username, Password)) {
                            System.out.println("Employee login successful.");
                            employeeFunction.loginPage("1001");
                        } else {
                            System.out.println("Invalid username or password. Please try again :(");
                        }
                        break;

                    case 2:
                        // Administrator login
                        System.out.print("Enter Administrator Username: ");
                        String AdminUsername = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String AdminPassword = scanner.nextLine();

                        AdminFunction admin = new AdminFunction(2001, AdminUsername, AdminPassword);
                        if (admin.login(AdminUsername, AdminPassword)) {
                            System.out.println("Administrator login successful.");
                            admin.loginPage(); 
                        } else {
                            System.out.println("Invalid username or password. Please try again :(");
                        }
                        break;

                    case 3:
                        // Exit the program
                        System.out.println("See you next time :)");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again :(");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number between 1-3.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static void initializeDataFiles() {
        // Create Data directory if it doesn't exist
        File dataDir = new File("Data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            System.out.println("Created Data directory.");
        }

        // Create data files if they don't exist
        String[] files = {
                "Data/Staff_Profile.txt",
                "Data/Duty_Request.txt",
                "Data/Leave_Request.txt",
                "Data/Shift.txt"
        };

        for (String fileName : files) {
            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    System.out.println("Created " + fileName);

                    // Add sample data for Staff_Profile.txt if it's newly created
                    if (fileName.equals("Data/Staff_Profile.txt")) {
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write("1001,John Doe,Employee,IT,50000.0\n");
                            writer.write("2001,admin,Administrator,Management,80000.0\n");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error creating " + fileName + ": " + e.getMessage());
                }
            }
        }
    }
}