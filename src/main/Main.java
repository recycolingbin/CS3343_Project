package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import adminFunction.AdminFunction;
import baseFunction.BaseFunction;
import employeeFunction.EmployeeFunction;
import staffProfile.StaffProfile;

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

                        // Create employee function and handle login
                        EmployeeFunction employeeFunction = new EmployeeFunction();
                        if (employeeFunction.login(Username, Password)) {
                            System.out.println("Employee login successful.");
                            employeeFunction.loginPage("1001");
                        } else {
                            System.out.println("Invalid. Please try again :(");
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
                            adminMenu(admin, scanner);
                        } else {
                            System.out.println("Invalid. Please try again.");
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

    // ================================================================================================================
    // ===============================Admin
    // menu=======================================================================
    // ================================================================================================================

    private static void adminMenu(AdminFunction admin, Scanner scanner) {
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\n=============== Administrator Menu ===============");
            System.out.println("1. Staff Management");
            System.out.println("2. Duty Request Management");
            System.out.println("3. Leave Request Management");
            System.out.println("4. Shift Management");
            System.out.println("5. Logout");
            System.out.print("Please select a category (1-5): ");

            int adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    // Staff Management Sub-Menu
                    staffManagementMenu(admin, scanner);
                    break;

                case 2:
                    // Duty Request Management Sub-Menu
                    dutyRequestMenu(admin, scanner);
                    break;

                case 3:
                    // Leave Request Management Sub-Menu
                    leaveRequestMenu(admin, scanner);
                    break;

                case 4:
                    // Shift Management Sub-Menu
                    shiftManagementMenu(admin, scanner);
                    break;

                case 5:
                    // Logout
                    System.out.println("Logging out...");
                    adminRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

    // ================================================================================================================
    // ================Admin sub menu: Staff
    // management================================================================
    // ================================================================================================================

    private static void staffManagementMenu(AdminFunction admin, Scanner scanner) {
        boolean staffManagementRunning = true;

        while (staffManagementRunning) {
            System.out.println("\n=============== Staff Management Menu ===============");
            System.out.println("1. Add Staff Profile");
            System.out.println("2. Edit Staff Profile");
            System.out.println("3. View All Staff Profiles");
            System.out.println("4. Search Staff Profile By ID");
            System.out.println("5. Delete Staff Profile");
            System.out.println("6. Back to Main Menu");
            System.out.print("Please select an option (1-6): ");

            int staffChoice = scanner.nextInt();
            scanner.nextLine();

            switch (staffChoice) {
                case 1:
                    // Add staff profile
                    System.out.print("Enter Staff ID: ");
                    int staffId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Staff Name: ");
                    String staffName = scanner.nextLine();
                    System.out.print("Enter Role: ");
                    String role = scanner.nextLine();

                    admin.addStaffProfile(staffId, staffName, role);
                    break;

                case 2:
                    // Edit staff profile
                    admin.editStaffProfileMenu(scanner);
                    break;

                case 3:
                    // View all staff profiles
                    admin.viewAllStaffProfiles();
                    break;

                case 4:
                    // Search staff by ID
                    System.out.print("Enter Staff ID to Search: ");
                    int viewStaffId = scanner.nextInt();
                    admin.viewStaffProfile(viewStaffId);
                    break;

                case 5:
                    // Delete staff profile
                    System.out.print("Enter Staff ID to Delete: ");
                    int deleteStaffId = scanner.nextInt();
                    admin.deleteStaffProfile(deleteStaffId);
                    break;

                case 6:
                    // Back to main menu
                    staffManagementRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

    // ================================================================================================================
    // ====================Request management sub menu: Duty
    // request===================================================
    // ================================================================================================================

    private static void dutyRequestMenu(AdminFunction admin, Scanner scanner) {
        boolean dutyManagementRunning = true;

        while (dutyManagementRunning) {
            System.out.println("\n=============== Duty Request Management Menu ===============");
            System.out.println("1. View Pending Duty Requests");
            System.out.println("2. Approve Duty Request");
            System.out.println("3. Reject Duty Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option (1-4): ");

            int requestChoice = scanner.nextInt();
            scanner.nextLine();

            switch (requestChoice) {
                case 1:
                    // View all duty requests
                    admin.viewDutyRequestsWithCaseNumbers();
                    break;

                case 2:
                    // Approve duty request by case number
                    admin.viewDutyRequestsWithCaseNumbers();
                    System.out.print("Enter Case Number to Approve: ");
                    int approveDutyCaseNumber = scanner.nextInt();
                    scanner.nextLine();

                    admin.approveDutyRequestByCaseNumber(approveDutyCaseNumber);
                    break;

                case 3:
                    // Reject duty request by case number
                    admin.viewDutyRequestsWithCaseNumbers();
                    System.out.print("Enter Case Number to Reject: ");
                    int rejectDutyCaseNumber = scanner.nextInt();
                    scanner.nextLine();

                    admin.rejectDutyRequestByCaseNumber(rejectDutyCaseNumber);
                    break;

                case 4:
                    // Back to main menu
                    dutyManagementRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

    // ================================================================================================================
    // ====================Request management sub menu: Leave
    // request===================================================
    // ================================================================================================================

    private static void leaveRequestMenu(AdminFunction admin, Scanner scanner) {
        boolean leaveManagementRunning = true;

        while (leaveManagementRunning) {
            System.out.println("\n=============== Leave Request Management Menu ===============");
            System.out.println("1. Request Leave");
            System.out.println("2. Approve Leave Request");
            System.out.println("3. Reject Leave Request");
            System.out.println("4. View All Leave Requests");
            System.out.println("5. Back to Main Menu");
            System.out.print("Please select an option (1-5): ");

            int requestChoice = scanner.nextInt();
            scanner.nextLine();

            switch (requestChoice) {
                case 1:
                    // Request leave with date validation
                    System.out.print("Enter Employee ID: ");
                    int employeeId = scanner.nextInt();
                    scanner.nextLine();

                    String startDate = admin.getValidDateInput(scanner, "Enter Start Date (YYYY-MM-DD): ");
                    String endDate = admin.getValidDateInput(scanner, "Enter End Date (YYYY-MM-DD): ");

                    System.out.print("Enter Reason: ");
                    String reason = scanner.nextLine();

                    admin.requestLeave(employeeId, startDate, endDate, reason);
                    break;

                case 2:
                    // Approve leave request by case number
                    admin.viewLeaveRequestsWithCaseNumbers();
                    System.out.print("Enter Case Number to Approve: ");
                    int approveCaseNumber = scanner.nextInt();
                    scanner.nextLine();

                    admin.approveLeaveRequestByCaseNumber(approveCaseNumber);
                    break;

                case 3:
                    // Reject leave request by case number
                    admin.viewLeaveRequestsWithCaseNumbers();
                    System.out.print("Enter Case Number to Reject: ");
                    int rejectCaseNumber = scanner.nextInt();
                    scanner.nextLine();

                    admin.rejectLeaveRequestByCaseNumber(rejectCaseNumber);
                    break;

                case 4:
                    // View all leave requests
                    admin.viewAllLeaveRequests();
                    break;

                case 5:
                    // Back to main menu
                    leaveManagementRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

    // ================================================================================================================
    // ====================Shift management sub
    // menu===================================================
    // ================================================================================================================

    private static void shiftManagementMenu(AdminFunction admin, Scanner scanner) {
        boolean shiftManagementRunning = true;

        while (shiftManagementRunning) {
            System.out.println("\n=============== Shift Management Menu ===============");
            System.out.println("1. View All Shift Schedules");
            System.out.println("2. View Shift Schedule By Date");
            System.out.println("3. Search Shift Schedule By Session");
            System.out.println("4. Assign Shift Schedule");
            System.out.println("5. Delete/Cancel Shift Schedule");
            System.out.println("6. Back to Main Menu");
            System.out.print("Please select an option (1-6): ");

            int shiftChoice;
            try {
                shiftChoice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number between 1-6.");
                scanner.nextLine(); // clear invalid input
                continue; // re-display shift management menu
            }

            switch (shiftChoice) {
                case 1:
                    // View all shift schedules
                    admin.viewAllShiftSchedules();
                    break;

                case 2:
                    // View shift schedule with date validation
                    String date = admin.getValidDateInput(scanner, "Enter Date (YYYY-MM-DD): ");
                    admin.viewShiftSchedule(date);
                    break;

                case 3:
                    // View shift schedule by session with date validation
                    System.out.print("Enter Session (MORNING/AFTERNOON/NIGHT): ");
                    String viewSession = scanner.nextLine();
                    System.out.print("Enter Date (YYYY-MM-DD) or press Enter for all dates: ");
                    String viewDate = scanner.nextLine();
                    if (viewDate.trim().isEmpty()) {
                        viewDate = null;
                    } else {
                        // Validate the entered date
                        while (!admin.isValidDate(viewDate)) {
                            System.out.println(
                                    "Invalid date format! Please enter date in YYYY-MM-DD format (e.g., 2025-10-25)");
                            System.out.print("Enter Date (YYYY-MM-DD) or press Enter for all dates: ");
                            viewDate = scanner.nextLine();
                            if (viewDate.trim().isEmpty()) {
                                viewDate = null;
                                break;
                            }
                        }
                    }
                    admin.viewShiftsBySession(viewSession, viewDate);
                    break;

                case 4:
                    // Assign shift schedule with date validation
                    System.out.print("Enter Staff ID: ");
                    int staffId = scanner.nextInt();
                    scanner.nextLine();

                    String shiftDate = admin.getValidDateInput(scanner, "Enter Date (YYYY-MM-DD): ");

                    System.out.print("Enter Session (MORNING/AFTERNOON/NIGHT): ");
                    String session = scanner.nextLine();
                    System.out.print("Enter Notes: ");
                    String note = scanner.nextLine();

                    admin.assignShift(staffId, shiftDate, session, note);
                    break;

                case 5:
                    // Delete/Cancel shift schedule
                    System.out.print("Enter Shift ID: ");
                    int shiftId = scanner.nextInt();
                    admin.deleteShift(shiftId);
                    break;

                case 6:
                    // Back to main menu
                    shiftManagementRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }
}