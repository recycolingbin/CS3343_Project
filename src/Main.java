package main;

import java.util.Scanner;

import administrator.Administrator;
import baseUser.BaseUser;
import employee.Employee;
import employeeFunction.EmployeeFunction;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=============== Welcome to the Employee Management System ===============");
            System.out.println("1. Login: Employee");
            System.out.println("2. Login: Administrator");
            System.out.println("3. Exit");
            System.out.print("Please select an option(1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Employee login
                    System.out.print("Enter Employee Username: ");
                    String Username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String Password = scanner.nextLine();

                    // not sure if here will work cuz baseUser doesnt return userID
                    Employee employee = new Employee(1001, Username, Password);
                    if (baseUser.login(Username, Password)) {
                        System.out.println("Employee login successful.");
                        employee.Login_page(employee.userId);
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

                    // Simulating administrator data
                    Administrator admin = new Administrator(2001, AdminUsername, AdminPassword);
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
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }

        scanner.close();
    }

//================================================================================================================
//===============================Admin menu=======================================================================
//================================================================================================================

    private static void adminMenu(Administrator admin, Scanner scanner) {
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\n=============== Administrator Menu ===============");
            System.out.println("1. Staff Management");
            System.out.println("2. Request Management");
            System.out.println("3. Shift Management");
            System.out.println("4. Logout");
            System.out.print("Please select a category (1-4): ");

            int adminChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (adminChoice) {
                case 1:
                    // Staff Management Sub-Menu
                    staffManagementMenu(admin, scanner);
                    break;

                case 2:
                    // Request Management Sub-Menu
                    requestManagementMenu(admin, scanner);
                    break;

                case 3:
                    // Shift Management Sub-Menu
                    shiftManagementMenu(admin, scanner);
                    break;

                case 4:
                    // Logout
                    System.out.println("Logging out...");
                    adminRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

//================================================================================================================
//================Admin sub menu: Staff management================================================================
//================================================================================================================

    private static void staffManagementMenu(Administrator admin, Scanner scanner) {
        boolean staffManagementRunning = true;

        while (staffManagementRunning) {
            System.out.println("\n=============== Staff Management Menu ===============");
            System.out.println("1. Add Staff Profile");
            System.out.println("2. Edit Staff Profile");
            System.out.println("3. View All Staff Profiles");
            System.out.println("4. Search Staff Profile By ID");
            System.out.println("5. Search Staff Profile By Name");
            System.out.println("6. Search Staff Profile By Department");
            System.out.println("7. Delete Staff Profile");
            System.out.println("8. Back to Main Menu");
            System.out.print("Please select an option (1-8): ");

            int staffChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    System.out.print("Enter Department: ");
                    String department = scanner.nextLine();
                    System.out.print("Enter Salary: ");
                    double salary = scanner.nextDouble();
                    scanner.nextLine();

                    admin.addStaffProfile(staffId, staffName, role, department, salary);
                    break;

                case 2:
                    // Edit staff profile
                    System.out.print("Enter Staff ID to Edit: ");
                    int editStaffId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Field to Edit (name/role/department/salary): ");
                    String field = scanner.nextLine();
                    System.out.print("Enter New Value: ");
                    String newValue = scanner.nextLine();

                    admin.editStaffProfile(editStaffId, field, newValue);
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
                    // Search staff by name
                    System.out.print("Enter Name to Search: ");
                    String searchName = scanner.nextLine();
                    var resultsByName = admin.searchStaffByName(searchName);
                    for (var staff : resultsByName) {
                        System.out.println("ID: " + staff.getStaffId() + ", Name: " + staff.getName());
                    }
                    break;

                case 6:
                    // Search staff by department
                    System.out.print("Enter Department to Search: ");
                    String staffDepartment = scanner.nextLine();
                    var resultsByDept = admin.getStaffByDepartment(staffDepartment);
                    for (var staff : resultsByDept) {
                        System.out.println("ID: " + staff.getStaffId() + ", Name: " + staff.getName());
                    }
                    break;

                case 7:
                    // Delete staff profile
                    System.out.print("Enter Staff ID to Delete: ");
                    int deleteStaffId = scanner.nextInt();
                    admin.deleteStaffProfile(deleteStaffId);
                    break;

                case 8:
                    // Back to main menu
                    staffManagementRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

//================================================================================================================
//========Admin sub menu: request management======================================================================
//================================================================================================================

    private static void requestManagementMenu(Administrator admin, Scanner scanner) {
        boolean requestManagementRunning = true;

        while (requestManagementRunning) {
            System.out.println("\n=============== Request Management Menu ===============");
            System.out.println("1. Duty Management");
            System.out.println("2. Leave Request Management");
            System.out.println("3. Reject Leave Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option (1-4): ");

            int requestChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (requestChoice) {
                case 1:
                    // View pending leave requests
                    admin.viewPendingRequests();
                    break;

                case 2:
                    // Approve leave request
                    System.out.print("Enter Request ID to Approve: ");
                    int approveRequestId = scanner.nextInt();
                    admin.approveRequest(approveRequestId);
                    break;

                case 3:
                    // Reject leave request
                    System.out.print("Enter Request ID to Reject: ");
                    int rejectRequestId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Reason for Rejection: ");
                    String rejectionReason = scanner.nextLine();
                    admin.rejectRequest(rejectRequestId, rejectionReason);
                    break;

                case 4:
                    // Back to main menu
                    requestManagementRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

//================================================================================================================
//====================Request management sub menu: Duty request===================================================
//================================================================================================================

    private static void DutyRequestMenu(Administrator admin, Scanner scanner) {
        boolean dutyManagementRunning = true;

        while (requestManagementRunning) {
            System.out.println("\n=============== Request Management Menu ===============");
            System.out.println("1. View All Duty Requests");
            System.out.println("2. Approve Duty Request");
            System.out.println("3. Remove Duty Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option (1-4): ");

            int requestChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (requestChoice) {
                case 1:
                    // View all duty requests
                    admin.viewPendingRequests();
                    break;

                case 2:
                    // Approve duty request
                    System.out.print("Enter Employee ID to Approve: ");
                    int dutyEmployeeID = scanner.nextInt();
                    System.out.print("Enter Date (YYYY-MM-DD) to Approve: ");
                    int dutyApproveDate = scanner.nextInt();
                    System.out.print("Enter Session to Approve: ");
                    int dutyApproveSession = scanner.nextInt();

                    admin.approveDutyRequest(dutyEmployeeID, dutyApproveDate, dutyApproveSession);
                    break;

                case 3:
                    // Remove duty request
                    System.out.print("Enter Employee ID to Remove: ");
                    int dutyId = scanner.nextInt();
                    System.out.print("Enter Date (YYYY-MM-DD) to Remove: ");
                    int dutyDate = scanner.nextInt();
                    System.out.print("Enter Session to Remove: ");
                    int dutySession = scanner.nextInt();

                    admin.removeDutyRequest(dutyId, dutyDate, dutySession);
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

//================================================================================================================
//====================Request management sub menu: Leave request===================================================
//================================================================================================================

    private static void LeaveRequestMenu(Administrator admin, Scanner scanner) {
        boolean LeaveManagementRunning = true;

        while (requestManagementRunning) {
            System.out.println("\n=============== Request Management Menu ===============");
            System.out.println("1. View Pending Leave Requests");
            System.out.println("2. Approve Leave Request");
            System.out.println("3. Reject Leave Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option (1-4): ");

            int requestChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (requestChoice) {
                case 1:
                    // View pending leave requests
                    admin.viewPendingRequests();
                    break;

                case 2:
                    // Approve leave request
                    System.out.print("Enter Request ID to Approve: ");
                    int approveRequestId = scanner.nextInt();
                    admin.approveRequest(approveRequestId);
                    break;

                case 3:
                    // Reject leave request
                    System.out.print("Enter Request ID to Reject: ");
                    int rejectRequestId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Reason for Rejection: ");
                    String rejectionReason = scanner.nextLine();
                    admin.rejectRequest(rejectRequestId, rejectionReason);
                    break;

                case 4:
                    // Back to main menu
                    LeaveManagementRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again :(");
            }
        }
    }

//================================================================================================================
//====================Request management sub menu: Shift request===================================================
//================================================================================================================

    private static void shiftManagementMenu(Administrator admin, Scanner scanner) {
        boolean shiftManagementRunning = true;

        while (shiftManagementRunning) {
            System.out.println("\n=============== Shift Management Menu ===============");
            System.out.println("1. View Shift Schedule");
            System.out.println("2. Search Shift Schedule By Session");
            System.out.println("3. Assign Shift Schedule");
            System.out.println("4. Edit Shift Schedule");
            System.out.println("5. Delete/Cancel Shift Schedule");
            System.out.println("6. Back to Main Menu");
            System.out.print("Please select an option (1-2): ");

            int shiftChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (shiftChoice) {
                case 1:
                    // View shift schedule
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    admin.viewShiftSchedule(date);
                    break;

                case 2:
                    // View shift schedule by session
                    System.out.print("Enter Session: ");
                    String viewSession = scanner.nextLine();
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String viewDate = scanner.nextLine();
                    admin.viewShiftSchedule(viewSession, viewDate);
                    break;

                case 3:
                    // Assign shift schedule
                    System.out.print("Enter Staff ID: ");
                    int staffId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String shiftDate = scanner.nextLine();
                    System.out.print("Enter Session: ");
                    String session = scanner.nextLine();
                    System.out.print("Enter Notes: ");
                    String note = scanner.nextLine();
                    scanner.nextLine();

                    administrator.assignShift(staffId, shiftDate, session, note);
                    break;

                case 4:
                    // Edit shift schedule
                    System.out.print("Enter Shift ID: ");
                    int shiftId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Field: ");
                    String field = scanner.nextLine();
                    System.out.print("Enter Changes: ");
                    String changes = scanner.nextLine();
                    scanner.nextLine();

                    administrator.editShift(shiftId, field, changes);
                    break;

                case 5:
                    // Delete/Cancel shift schedule
                    System.out.print("Enter Shift ID: ");
                    String sID = scanner.nextLine();
                    admin.deleteShift(sID);
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