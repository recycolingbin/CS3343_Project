package adminFunction;

import java.util.*;

/**
 * MenuManager handles all menu display and navigation for administrators.
 * Responsibilities:
 * - Display menu options and handle user input
 * - Navigate between different menu screens
 * - Coordinate with manager classes for operations
 */
public class MenuManager {
    private StaffManager staffManager;
    private RequestManager requestManager;
    private ShiftManager shiftManager;
    private Scanner scanner;

    public MenuManager(StaffManager staffManager, RequestManager requestManager, 
                      ShiftManager shiftManager, Scanner scanner) {
        this.staffManager = staffManager;
        this.requestManager = requestManager;
        this.shiftManager = shiftManager;
        this.scanner = scanner;
    }

    // Main login page for administrators
    public void loginPage() {
        while (true) {
            System.out.println("\n=============== Administrator Main Menu ===============");
            System.out.println("1. Staff Management");
            System.out.println("2. Session Management");
            System.out.println("3. Leave Request Management");
            System.out.println("4. Duty Request Management");
            System.out.println("5. Roster Preparation");
            System.out.println("6. Logout");
            System.out.print("Please select an option(1-6): ");

            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();
            
            switch (input) {
                case "1":
                    staffManagementMenu();
                    break;
                case "2":
                    sessionManagementMenu();
                    break;
                case "3":
                    leaveRequestManagementMenu();
                    break;
                case "4":
                    dutyRequestManagementMenu();
                    break;
                case "5":
                    rosterPreparationMenu();
                    break;
                case "6":
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Staff management menu
    private void staffManagementMenu() {
        while (true) {
            System.out.println("\n=============== Staff Management ===============");
            System.out.println("1. Add Staff Profile");
            System.out.println("2. Edit Staff Profile");
            System.out.println("3. Delete Staff Profile");
            System.out.println("4. View Staff Profile");
            System.out.println("5. View All Staff Profiles");
            System.out.println("6. Back to Main Menu");
            System.out.print("Please select an option(1-6): ");

            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.print("Enter Staff ID: ");
                    try {
                        int staffId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter Role: ");
                        String role = scanner.nextLine().trim();
                        
                        if (staffManager.addStaffProfile(staffId, name, role)) {
                            System.out.println("Staff profile added successfully!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "2":
                    System.out.print("Enter Staff ID to edit: ");
                    try {
                        int staffId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Edit Name (press Enter to skip): ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Edit Role (press Enter to skip): ");
                        String role = scanner.nextLine().trim();
                        
                        if (!name.isEmpty()) {
                            staffManager.editStaffProfile(staffId, "name", name);
                        }
                        if (!role.isEmpty()) {
                            staffManager.editStaffProfile(staffId, "role", role);
                        }
                        if (!name.isEmpty() || !role.isEmpty()) {
                            System.out.println("Staff profile updated successfully!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "3":
                    System.out.print("Enter Staff ID to delete: ");
                    try {
                        int staffId = Integer.parseInt(scanner.nextLine().trim());
                        if (staffManager.deleteStaffProfile(staffId)) {
                            System.out.println("Staff profile deleted successfully!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "4":
                    System.out.print("Enter Staff ID to view: ");
                    try {
                        int staffId = Integer.parseInt(scanner.nextLine().trim());
                        staffManager.viewStaffProfile(staffId);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "5":
                    staffManager.viewAllStaffProfiles();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Leave request management menu
    private void leaveRequestManagementMenu() {
        while (true) {
            System.out.println("\n=============== Leave Request Management ===============");
            System.out.println("1. View Leave Requests");
            System.out.println("2. Approve Leave Request");
            System.out.println("3. Reject Leave Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option(1-4): ");

            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
                    break;
                case "2":
                    System.out.print("Enter case number to approve: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.approveLeaveRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "3":
                    System.out.print("Enter case number to reject: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.rejectLeaveRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Duty request management menu
    private void dutyRequestManagementMenu() {
        while (true) {
            System.out.println("\n=============== Duty Request Management ===============");
            System.out.println("1. View Duty Requests");
            System.out.println("2. Approve Duty Request");
            System.out.println("3. Reject Duty Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option(1-4): ");

            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
                    break;
                case "2":
                    System.out.print("Enter case number to approve: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.approveDutyRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "3":
                    System.out.print("Enter case number to reject: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.rejectDutyRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Session management menu
    private void sessionManagementMenu() {
        while (true) {
            System.out.println("\n=============== Session Management ===============");
            System.out.println("1. View Available Sessions");
            System.out.println("2. Back to Main Menu");
            System.out.print("Please select an option(1-2): ");

            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("\nAvailable Sessions:");
                    System.out.println("  MORNING: 06:00 - 14:00");
                    System.out.println("  AFTERNOON: 14:00 - 22:00");
                    System.out.println("  NIGHT: 22:00 - 06:00");
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Roster preparation menu
    private void rosterPreparationMenu() {
        while (true) {
            System.out.println("\n=============== Roster Preparation ===============");
            System.out.println("1. Approve Leave Request");
            System.out.println("2. Reject Leave Request");
            System.out.println("3. Approve Duty Request");
            System.out.println("4. Reject Duty Request");
            System.out.println("5. View Leave Requests");
            System.out.println("6. View Duty Requests");
            System.out.println("7. View All Shifts");
            System.out.println("8. Assign Shift");
            System.out.println("9. Delete Shift");
            System.out.println("10. Back to Main Menu");
            System.out.print("Please select an option(1-10): ");

            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
                    System.out.print("Enter case number to approve: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.approveLeaveRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "2":
                    requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
                    System.out.print("Enter case number to reject: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.rejectLeaveRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "3":
                    requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
                    System.out.print("Enter case number to approve: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.approveDutyRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "4":
                    requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
                    System.out.print("Enter case number to reject: ");
                    try {
                        int caseNum = Integer.parseInt(scanner.nextLine().trim());
                        requestManager.rejectDutyRequestByCaseNumber(caseNum);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "5":
                    requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
                    break;
                case "6":
                    requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
                    break;
                case "7":
                    shiftManager.viewAllShiftSchedules();
                    break;
                case "8":
                    assignShiftMenu();
                    break;
                case "9":
                    System.out.print("Enter Shift ID to delete: ");
                    try {
                        int shiftId = Integer.parseInt(scanner.nextLine().trim());
                        shiftManager.deleteShift(shiftId, staffManager);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input!");
                    }
                    break;
                case "10":
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Assign shift helper menu
    private void assignShiftMenu() {
        System.out.print("Enter Employee ID: ");
        try {
            int employeeId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();
            System.out.print("Enter Session (MORNING/AFTERNOON/NIGHT): ");
            String session = scanner.nextLine().trim();
            System.out.print("Enter Notes (optional): ");
            String notes = scanner.nextLine().trim();
            
            shiftManager.assignShift(employeeId, date, session, notes, staffManager);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
}
