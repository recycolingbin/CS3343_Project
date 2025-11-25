package staffRosteringSystem;

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
    
    // Helper method to safely read integer input
    public int readIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return -1;
        }
    }
    
    // Helper method to safely read string input
    public String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    // Helper method to check if scanner has input
    public boolean hasInput() {
        return scanner.hasNextLine();
    }

    // Main login page for administrators
    public void loginPage() {
        while (true) {
            System.out.println("\n=============== Administrator Main Menu ===============");
            System.out.println("1. Staff Management");
            System.out.println("2. Shift Management");
            System.out.println("3. Duty Request Management");
            System.out.println("4. Leave Request Management");
            System.out.println("5. Logout");
            System.out.print("Please select an option(1-5): ");

            if (!hasInput()) break;
            String input = scanner.nextLine().trim();
            
            if (!handleMainMenuChoice(input)) {
                return; // Logout
            }
        }
    }
    
    // Handle main menu choice - extracted for testability
    public boolean handleMainMenuChoice(String input) {
        switch (input) {
            case "1":
            	staffManagementMenu();
                return true;
            case "2":
                shiftManagementMenu();
                return true;
            case "3":
            	dutyRequestManagementMenu();
                return true;
            case "4":
            	leaveRequestManagementMenu();
                return true;

            case "5":
                System.out.println("Logged out successfully.");
                return false;
            default:
                System.out.println("Invalid option! Please try again.");
                return true;
        }
    }

    //1. Staff management menu
    public void staffManagementMenu() {
        while (true) {
            System.out.println("\n=============== Staff Management ===============");
            System.out.println("1. Add Staff Profile");
            System.out.println("2. Edit Staff Profile");
            System.out.println("3. Delete Staff Profile");
            System.out.println("4. View Staff Profile");
            System.out.println("5. View All Staff Profiles");
            System.out.println("6. Back to Main Menu");
            System.out.print("Please select an option(1-6): ");

            if (!hasInput()) break;
            String input = scanner.nextLine().trim();

            if (!handleStaffManagementChoice(input)) {
                return; // Back to main menu
            }
        }
    }
    
    // Handle staff management choice - extracted for testability
    public boolean handleStaffManagementChoice(String input) {
        switch (input) {
            case "1":
                handleAddStaff();
                return true;
            case "2":
                handleEditStaff();
                return true;
            case "3":
                handleDeleteStaff();
                return true;
            case "4":
                handleViewStaff();
                return true;
            case "5":
                handleViewAllStaff();
                return true;
            case "6":
                return false;
            default:
                System.out.println("Invalid option! Please try again.");
                return true;
        }
    }
    
    // Extract add staff logic
    public void handleAddStaff() {
        int staffId = readIntInput("Enter Staff ID: ");
        if (staffId == -1) return;
        
        String name = readStringInput("Enter Name: ");
        String role = readStringInput("Enter Role: ");
        
        if (staffManager.addStaffProfile(staffId, name, role)) {
            System.out.println("Staff profile added successfully!");
        }
    }
    
    // Extract edit staff logic
    public void handleEditStaff() {
        int staffId = readIntInput("Enter Staff ID to edit: ");
        if (staffId == -1) return;
        
        String name = readStringInput("Edit Name (press Enter to skip): ");
        String role = readStringInput("Edit Role (press Enter to skip): ");
        
        boolean updated = false;
        if (!name.isEmpty()) {
            staffManager.editStaffProfile(staffId, "name", name);
            updated = true;
        }
        if (!role.isEmpty()) {
            staffManager.editStaffProfile(staffId, "role", role);
            updated = true;
        }
        if (updated) {
            System.out.println("Staff profile updated successfully!");
        }
    }
    
    // Extract delete staff logic
    public void handleDeleteStaff() {
        int staffId = readIntInput("Enter Staff ID to delete: ");
        if (staffId == -1) return;
        
        if (staffManager.deleteStaffProfile(staffId)) {
            System.out.println("Staff profile deleted successfully!");
        }
    }
    
    // Extract view staff logic
    public void handleViewStaff() {
        int staffId = readIntInput("Enter Staff ID to view: ");
        if (staffId == -1) return;
        
        System.out.println(staffManager.viewStaffProfile(staffId));
    }
    
    // Extract view all staff logic
    public void handleViewAllStaff() {
        System.out.println(staffManager.viewAllStaffProfiles(null));
    }

    //2. Shift management menu
    public void shiftManagementMenu() {
        while (true) {
            System.out.println("\n=============== Shift Management ===============");
            System.out.println("1. View Available Sessions");
            System.out.println("2. View All Shifts");
            System.out.println("3. Assign Shift");
            System.out.println("4. Delete Shift");
            System.out.println("5. Back to Main Menu");
            System.out.print("Please select an option(1-5): ");

            if (!hasInput()) break;
            String input = scanner.nextLine().trim();

            if (!handleShiftManagementChoice(input)) {
                return; // Back to main menu
            }
        }
    }
    
    // Handle session management choice - extracted for testability
    public boolean handleShiftManagementChoice(String input) {
        switch (input) {
            case "1":
                displayAvailableSessions();
                return true;
            case "2":
                shiftManager.viewAllShiftSchedules();
                return true;
            case "3":
            	handleAssignShift();
				return true;
			case "4":
				handleDeleteShift();
				return true;
			case "5":
                return false;
            default:
                System.out.println("Invalid option! Please try again.");
                return true;
        }
    }
    
    // Extract session display logic
    public void displayAvailableSessions() {
        System.out.println("\nAvailable Sessions:");
        System.out.println("  MORNING: 06:00 - 14:00");
        System.out.println("  AFTERNOON: 14:00 - 22:00");
        System.out.println("  NIGHT: 22:00 - 06:00");
    }
    
    public void handleDeleteShift() {
        int shiftId = readIntInput("Enter Shift ID to delete: ");
        if (shiftId != -1) {
            shiftManager.deleteShift(shiftId);
        }
    }

    // Assign shift helper menu
    public void handleAssignShift() {
        int employeeId = readIntInput("Enter Employee ID: ");
        if (employeeId == -1) return;
        
        String date = readStringInput("Enter Date (YYYY-MM-DD): ");
        String session = readStringInput("Enter Session (MORNING/AFTERNOON/NIGHT): ");
        String notes = readStringInput("Enter Notes (optional): ");
        
        shiftManager.assignShift(employeeId, date, session, notes);
    }
    //3. Duty request management menu
    public void dutyRequestManagementMenu() {
        while (true) {
            System.out.println("\n=============== Duty Request Management ===============");
            System.out.println("1. View Duty Requests");
            System.out.println("2. Approve Duty Request");
            System.out.println("3. Reject Duty Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option(1-4): ");

            if (!hasInput()) break;
            String input = scanner.nextLine().trim();

            if (!handleDutyRequestChoice(input)) {
                return; // Back to main menu
            }
        }
    }
    
    // Handle duty request choice - extracted for testability
    public boolean handleDutyRequestChoice(String input) {
        switch (input) {
            case "1":
                requestManager.viewDutyRequestsWithCaseNumbers();
                return true;
            case "2":
                handleApproveDutyRequest();
                return true;
            case "3":
                handleRejectDutyRequest();
                return true;
            case "4":
                return false;
            default:
                System.out.println("Invalid option! Please try again.");
                return true;
        }
    }
    
    // Extract approve duty request logic
    public void handleApproveDutyRequest() {
        int caseNum = readIntInput("Enter case number to approve: ");
        if (caseNum != -1) {
            requestManager.approveDutyRequestByCaseNumber(caseNum);
        }
    }
    
    // Extract reject duty request logic
    public void handleRejectDutyRequest() {
        int caseNum = readIntInput("Enter case number to reject: ");
        if (caseNum != -1) {
            requestManager.rejectDutyRequestByCaseNumber(caseNum);
        }
    }
    
    //4. Leave request management menu
    public void leaveRequestManagementMenu() {
        while (true) {
            System.out.println("\n=============== Leave Request Management ===============");
            System.out.println("1. View Leave Requests");
            System.out.println("2. Approve Leave Request");
            System.out.println("3. Reject Leave Request");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please select an option(1-4): ");

            if (!hasInput()) break;
            String input = scanner.nextLine().trim();

            if (!handleLeaveRequestChoice(input)) {
                return; // Back to main menu
            }
        }
    }
    
    // Handle leave request choice - extracted for testability
    public boolean handleLeaveRequestChoice(String input) {
        switch (input) {
            case "1":
                requestManager.viewLeaveRequestsWithCaseNumbers();
                return true;
            case "2":
                handleApproveLeaveRequest();
                return true;
            case "3":
                handleRejectLeaveRequest();
                return true;
            case "4":
                return false;
            default:
                System.out.println("Invalid option! Please try again.");
                return true;
        }
    }
    
    // Extract approve leave request logic
    public void handleApproveLeaveRequest() {
        int caseNum = readIntInput("Enter case number to approve: ");
        if (caseNum != -1) {
            requestManager.approveLeaveRequestByCaseNumber(caseNum);
        }
    }
    
    // Extract reject leave request logic
    public void handleRejectLeaveRequest() {
        int caseNum = readIntInput("Enter case number to reject: ");
        if (caseNum != -1) {
            requestManager.rejectLeaveRequestByCaseNumber(caseNum);
        }
    }
}
