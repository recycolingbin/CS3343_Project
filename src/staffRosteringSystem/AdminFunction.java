package staffRosteringSystem;

import java.util.*;

/**
 * AdminFunction (Refactored) is a lightweight coordinator class.
 * Responsibilities:
 * - Initialize and coordinate manager classes
 * - Delegate to appropriate manager for operations
 * - Serve as the main entry point for administrative functions
 */
public class AdminFunction extends BaseFunction {
    //private StaffManager staffManager;
    private RequestManager requestManager;
    //private ShiftManager shiftManager;
    private MenuManager menuManager;
    private Scanner scanner;

	public AdminFunction(int userId, String username, String password) {
        super(userId, username, password);
        this.scanner = new Scanner(System.in);
        this.requestManager = new RequestManager();
        this.menuManager = new MenuManager(super.staffManager, requestManager, super.shiftManager, scanner);
    }
	
	public AdminFunction(int userId, String username, String password,
            StaffManager staffManager, RequestManager requestManager,
            ShiftManager shiftManager, Scanner scanner) {
		super(userId, username, password, shiftManager, staffManager);
		this.requestManager = requestManager;
		this.scanner = scanner;
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

    public String viewStaffProfile(int staffId) {
        //System.out.println(staffManager.viewStaffProfile(staffId));
    	return staffManager.viewStaffProfile(staffId);
    }

    public String viewAllStaffProfiles() {
    	//System.out.println( staffManager.viewAllStaffProfiles(null));
    	return staffManager.viewAllStaffProfiles(null);
    }

    // Request Management delegations
    public void viewLeaveRequests() {
        requestManager.viewLeaveRequestsWithCaseNumbers();
    }

    public void viewDutyRequests() {
        requestManager.viewDutyRequestsWithCaseNumbers();
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
        return shiftManager.assignShift(employeeId, date, session, notes);
    }

    public boolean deleteShift(int shiftId) {
        return shiftManager.deleteShift(shiftId);
    }

    public void viewAllShiftSchedules() {
        shiftManager.viewAllShiftSchedules();
    }

//    /**
//     * Get the menu manager for direct menu operations if needed
//     */
//    public MenuManager getMenuManager() {
//        return menuManager;
//    }
//
//    /**
//     * Get the staff manager for direct staff operations if needed
//     */
//    public StaffManager getStaffManager() {
//        return staffManager;
//    }
//
//    /**
//     * Get the request manager for direct request operations if needed
//     */
//    public RequestManager getRequestManager() {
//        return requestManager;
//    }
//
//    /**
//     * Get the shift manager for direct shift operations if needed
//     */
//    public ShiftManager getShiftManager() {
//        return shiftManager;
//    }
}
