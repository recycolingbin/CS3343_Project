package staffRosteringSystem;

import java.util.*;

/**
 * AdminFunction (Refactored) is a lightweight coordinator class.
 * Responsibilities:
 * - Initialize and coordinate manager classes
 * - Delegate to appropriate manager for operations
 * - Serve as the main entry point for administrative functions
 * 
 * This class demonstrates Single Responsibility Principle (SRP) compliance
 * by delegating actual operations to specialized manager classes:
 * - StaffManager: handles staff profile operations
 * - RequestManager: handles leave and duty request operations
 * - ShiftManager: handles shift scheduling operations
 * - MenuManager: handles menu display and user interaction
 */
public class AdminFunction extends BaseFunction {
    private StaffManager staffManager;
    private RequestManager requestManager;
    private ShiftManager shiftManager;
    private MenuManager menuManager;
    private Scanner scanner;

    public AdminFunction(int userId, String username, String password) {
        super(userId, username, password);
        this.scanner = new Scanner(System.in);
        
        // Initialize all manager classes
        initializeManagers();
    }

    /**
     * Initialize all manager classes
     */
    private void initializeManagers() {
        this.staffManager = new StaffManager();
        this.requestManager = new RequestManager();
        this.shiftManager = new ShiftManager();
        
        // Initialize request files if they don't exist
        this.requestManager.initializeRequestFiles(null);
        
        this.menuManager = new MenuManager(staffManager, requestManager, shiftManager, scanner);
    }

    /**
     * Main entry point for administrator login
     */
    public void login() {
        menuManager.loginPage();
    }

    // ==================== DELEGATION METHODS ====================
    // These methods delegate to appropriate managers for backward compatibility

    // Staff Management delegations
    public boolean addStaffProfile(int staffId, String staffName, String role) {
        return staffManager.addStaffProfile(staffId, staffName, role);
    }

    public boolean editStaffProfile(int staffId, String field, String newValue) {
        return staffManager.editStaffProfile(staffId, field, newValue);
    }

    public boolean deleteStaffProfile(int staffId) {
        return staffManager.deleteStaffProfile(staffId);
    }

    public void viewStaffProfile(int staffId) {
        System.out.println(staffManager.viewStaffProfile(staffId));
    	//staffManager.viewStaffProfile(staffId);
    }

    public void viewAllStaffProfiles() {
    	System.out.println( staffManager.viewAllStaffProfiles(null));
    	//staffManager.viewAllStaffProfiles(staffId);
    }

    // Request Management delegations
    public void viewLeaveRequests() {
        requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
    }

    public void viewDutyRequests() {
        requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
    }

    public boolean approveLeaveRequest(int caseNumber) {
        return requestManager.approveLeaveRequestByCaseNumber(caseNumber);
    }

    public boolean rejectLeaveRequest(int caseNumber) {
        return requestManager.rejectLeaveRequestByCaseNumber(caseNumber);
    }

    public boolean approveDutyRequest(int caseNumber) {
        return requestManager.approveDutyRequestByCaseNumber(caseNumber);
    }

    public boolean rejectDutyRequest(int caseNumber) {
        return requestManager.rejectDutyRequestByCaseNumber(caseNumber);
    }

    // Shift Management delegations
    public boolean assignShift(int employeeId, String date, String session, String notes) {
        return shiftManager.assignShift(employeeId, date, session, notes, staffManager);
    }

    public boolean deleteShift(int shiftId) {
        return shiftManager.deleteShift(shiftId, staffManager);
    }

    public void viewAllShiftSchedules() {
        shiftManager.viewAllShiftSchedules();
    }

    /**
     * Get the menu manager for direct menu operations if needed
     */
    public MenuManager getMenuManager() {
        return menuManager;
    }

    /**
     * Get the staff manager for direct staff operations if needed
     */
    public StaffManager getStaffManager() {
        return staffManager;
    }

    /**
     * Get the request manager for direct request operations if needed
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * Get the shift manager for direct shift operations if needed
     */
    public ShiftManager getShiftManager() {
        return shiftManager;
    }
}
