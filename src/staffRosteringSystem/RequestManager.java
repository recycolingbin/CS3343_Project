package staffRosteringSystem;

//import java.io.*;
import java.util.*;
//import java.util.function.Function;

/**
 * RequestManager handles all leave and duty request operations.
 * Responsibilities:
 * - Load/save requests from/to files
 * - View, approve, and reject leave requests
 * - View, approve, and reject duty requests
 * - Submit new leave and duty requests
 * RequestManager handles both business logic and presentation.
 * Uses ArrayList for simple, straightforward data management.
 */
public class RequestManager {
    private static final String LEAVE_REQUEST_FILE = "Data/Leave_Request.txt";
    private static final String DUTY_REQUEST_FILE = "Data/Duty_Request.txt";

    private final FileOperations fileOps;
    private final StaffManager staffManager;
    private final List<LeaveRequest> leaveRequests;
    private final List<DutyRequest> dutyRequests;

    private final String leaveFilePath;
    private final String dutyFilePath;

    /**
     * Default constructor
     */
    public RequestManager() {
        this(new StaffManager());
    }

    /**
     * Constructor with dependency injection
     */
    public RequestManager(StaffManager staffManager) {
        this(LEAVE_REQUEST_FILE, DUTY_REQUEST_FILE, 
             staffManager, new FileOperations());
    }

    /**
     * Constructor for testing
     */
    public RequestManager(String leaveFilePath, String dutyFilePath,
                         StaffManager staffManager, FileOperations fileOps) {
        this.leaveFilePath = leaveFilePath;
        this.dutyFilePath = dutyFilePath;
        this.staffManager = staffManager;
        this.fileOps = fileOps;
        
        this.leaveRequests = new ArrayList<>();
        this.dutyRequests = new ArrayList<>();
        
        initializeFiles();
        //loadAllRequests();
        loadLeaveRequests();
        loadDutyRequests();
    }

    // ==================== Initialization ====================

    private void initializeFiles() {
        fileOps.initializeFile(leaveFilePath);
        fileOps.initializeFile(dutyFilePath);
    }

    private void loadLeaveRequests() {
        List<LeaveRequest> requests = fileOps.loadData(leaveFilePath, LeaveRequest::fromFileFormat);
        leaveRequests.clear();
        leaveRequests.addAll(requests);
    }

    private void loadDutyRequests() {
        List<DutyRequest> requests = fileOps.loadData(dutyFilePath, DutyRequest::fromFileFormat);
        dutyRequests.clear();
        dutyRequests.addAll(requests);
    }

    // ==================== Leave Request Operations ====================

    public boolean submitLeaveRequest(int employeeId, String startDate, String endDate, String leaveType, String reason) {
		try {
		// Validate employee exists
		if (!staffManager.staffExists(employeeId)) {
		throw new IllegalArgumentException("Employee ID " + employeeId + " not found");
		}
		
		int newRequestId = getNextLeaveRequestId();
		LeaveRequest request = new LeaveRequest(employeeId, newRequestId, startDate, endDate, 
		                           leaveType, reason);
		leaveRequests.add(request);
		saveLeaveRequests();
		
		System.out.println("\n Leave request submitted successfully!");
		System.out.println("  Request ID: " + request.getRequestId());
		System.out.println("  Period: " + request.getStartDate() + " to " + request.getEndDate());
		System.out.println("  Duration: " + request.getLeaveDuration() + " day(s)");
		return true;
		
		} catch (IllegalArgumentException e) {
		System.out.println("\n Error: " + e.getMessage());
		return false;
		}
	}

	/**
	* Legacy method - single day leave request
	*/
	public boolean submitLeaveRequest(int employeeId, String leaveType, String reason, String requestDate) {
		return submitLeaveRequest(employeeId, requestDate, requestDate, leaveType, reason);
	}
    
         /**
     * View all pending leave requests with case numbers
     */
    
	public void viewLeaveRequestsWithCaseNumbers() {
        if (leaveRequests.isEmpty()) {
            System.out.println("\nNo pending leave requests found.");
            return;
        }
        
        System.out.println("\n======================= LEAVE REQUESTS =======================");
        System.out.println(String.format("%-5s %-8s %-8s %-12s %-12s %-4s %-15s %-20s", 
                  "Case", "Req ID", "Staff ID", "Start Date", "End Date", "Days", "Type", "Reason"));
        System.out.println("===================================================================");

        for (int i = 0; i < leaveRequests.size(); i++) {
            LeaveRequest req = leaveRequests.get(i);
            String truncatedReason = truncateString(req.getReason(), 20);

            System.out.println(String.format("%-5d %-8d %-8d %-12s %-12s %-4d %-15s %-20s",
                    i + 1, 
                    req.getRequestId(), 
                    req.getEmployeeId(),
                    req.getStartDate(), 
                    req.getEndDate(),
                    req.getLeaveDuration(),
                    req.getLeaveType(), 
                    truncatedReason));
        }
        System.out.println("===================================================================\n");
    }
	
	public boolean approveLeaveRequestByCaseNumber(int caseNumber) {
        if (caseNumber < 1 || caseNumber > leaveRequests.size()) {
            System.out.println("\n Error: Invalid case number!");
            return false;
        }
        LeaveRequest request = leaveRequests.get(caseNumber - 1);        
        // Remove from pending requests
        leaveRequests.remove(caseNumber - 1);
        saveLeaveRequests();
        
        System.out.println("\n Leave request approved successfully!");
        System.out.println("  Request ID: " + request.getRequestId());
        System.out.println("  Employee ID: " + request.getEmployeeId());
        System.out.println("  Date: " + request.getRequestDate());
        
        return true;
    }

        public boolean rejectLeaveRequestByCaseNumber(int caseNumber) {
            if (caseNumber < 1 || caseNumber > leaveRequests.size()) {
            System.out.println("\n Error: Invalid case number!");
            return false;
        }
        LeaveRequest request = leaveRequests.get(caseNumber - 1);
        
        // Remove from pending requests
        leaveRequests.remove(caseNumber - 1);
        saveLeaveRequests();
        
        System.out.println("\n Leave request rejected successfully!");
        System.out.println("  Request ID: " + request.getRequestId());
        System.out.println("  Employee ID: " + request.getEmployeeId());
        
        return true;
    }

    /**
     * Find leave request by request ID
     */
    public LeaveRequest findLeaveRequestById(int requestId) {
        for (LeaveRequest request : leaveRequests) {
            if (request.getRequestId() == requestId) {
                return request;
            }
        }
        return null;
    }

    private void saveLeaveRequests() {
        fileOps.saveData(leaveFilePath, leaveRequests, LeaveRequest::toFileFormat);
    }

    private int getNextLeaveRequestId() {
        int maxId = 1000; // Starting ID for leave requests
        for (LeaveRequest request : leaveRequests) {
            if (request.getRequestId() > maxId) {
                maxId = request.getRequestId();
            }
        }
        return maxId + 1;
    }

    /**
     * Submit a new duty request
     */
    public boolean submitDutyRequest(int employeeId, String section, String requestDate) {
        try {
            // Validate employee exists
            if (!staffManager.staffExists(employeeId)) {
                throw new IllegalArgumentException("Employee ID " + employeeId + " not found");
            }
                   int newRequestId = getNextDutyRequestId();
            DutyRequest request = new DutyRequest(employeeId, newRequestId, 
                                                 requestDate, section);
            
            dutyRequests.add(request);
            saveDutyRequests();
            
            System.out.println("\n Duty request submitted successfully!");
            System.out.println("  Request ID: " + request.getRequestId());
            System.out.println("  Date: " + request.getRequestDate());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println("\n Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * View all pending duty requests with case numbers
     */
    public void viewDutyRequestsWithCaseNumbers() {
        if (dutyRequests.isEmpty()) {
            System.out.println("\nNo pending duty requests found.");
            return;
        }
         System.out.println("\n=============== DUTY REQUESTS ===============");
        System.out.println(String.format("%-5s %-10s %-10s %-12s %-15s",
                  "Case", "Req ID", "Employee", "Date", "Section"));
        System.out.println("=======================================================================");
         for (int i = 0; i < dutyRequests.size(); i++) {
            DutyRequest req = dutyRequests.get(i);
            
            System.out.println(String.format("%-5d %-10d %-10d %-12s %-15s",
                    i + 1, req.getRequestId(), req.getEmployeeId(),
                    req.getRequestDate(), req.getSection()));
        }
        System.out.println("=======================================================================\n");
    }

    /**
     * Approve duty request by case number
     */
    public boolean approveDutyRequestByCaseNumber(int caseNumber) {
        if (caseNumber < 1 || caseNumber > dutyRequests.size()) {
            System.out.println("\n Error: Invalid case number!");
            return false;
        }
        DutyRequest request = dutyRequests.get(caseNumber - 1);
        
        // Remove from pending requests
        dutyRequests.remove(caseNumber - 1);
        saveDutyRequests();
        
        //update approved duty will be automatically added to roster
        ShiftManager sm = new ShiftManager("Data/Shift.txt",staffManager,fileOps);
        List<Shift> shifts = sm.loadShifts();
        int newShiftId = sm.generateShiftId(shifts);
        Shift newShift = Shift.create(newShiftId, request.getEmployeeId(), 
        		request.getRequestDate(), ShiftSession.fromString(request.getSection()),"");
        shifts.add(newShift);
        sm.saveShifts(shifts);
        
        System.out.println("\n Duty request approved successfully!");
        System.out.println("  Request ID: " + request.getRequestId());
        System.out.println("  Employee ID: " + request.getEmployeeId());
        System.out.println("  Date: " + request.getRequestDate());
        
        return true;
    }
     /**
     * Reject duty request by case number
     */
    public boolean rejectDutyRequestByCaseNumber(int caseNumber) {
        if (caseNumber < 1 || caseNumber > dutyRequests.size()) {
            System.out.println("\n Error: Invalid case number!");
            return false;
        }
        DutyRequest request = dutyRequests.get(caseNumber - 1);
        
        // Remove from pending requests
        dutyRequests.remove(caseNumber - 1);
        saveDutyRequests();
        
        System.out.println("\n Duty request rejected successfully!");
        System.out.println("  Request ID: " + request.getRequestId());
        System.out.println("  Employee ID: " + request.getEmployeeId());
     return true;
    }

    /**
     * Find duty request by request ID
     */
    public DutyRequest findDutyRequestById(int requestId) {
        for (DutyRequest request : dutyRequests) {
            if (request.getRequestId() == requestId) {
                return request;
            }
        }
        return null;
    }

    private void saveDutyRequests() {
        fileOps.saveData(dutyFilePath, dutyRequests, DutyRequest::toFileFormat);
    }

    private int getNextDutyRequestId() {
        int maxId = 2000; // Starting ID for duty requests
        for (DutyRequest request : dutyRequests) {
            if (request.getRequestId() > maxId) {
                maxId = request.getRequestId();
            }
        }
        return maxId + 1;
    }


      // ==================== Query Methods ====================

    public int getLeaveRequestCount() {
        return leaveRequests.size();
    }

    public int getDutyRequestCount() {
        return dutyRequests.size();
    }

     /**
     * Get all leave requests for a specific employee
     */
    public List<LeaveRequest> getEmployeeLeaveRequests(int employeeId) {
        List<LeaveRequest> result = new ArrayList<>();
        for (LeaveRequest request : leaveRequests) {
            if (request.getEmployeeId() == employeeId) {
                result.add(request);
            }
        }
        return result;
    }

    /**
     * Get all duty requests for a specific employee
     */
    public List<DutyRequest> getEmployeeDutyRequests(int employeeId) {
        List<DutyRequest> result = new ArrayList<>();
        for (DutyRequest request : dutyRequests) {
            if (request.getEmployeeId() == employeeId) {
                result.add(request);
            }
        }
        return result;
    }

    /**
     * Get all leave requests (returns defensive copy)
     */
    public List<LeaveRequest> getAllLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    /**
     * Get all duty requests (returns defensive copy)
     */
    public List<DutyRequest> getAllDutyRequests() {
        return new ArrayList<>(dutyRequests);
    }

    // ==================== Helper Methods ====================

    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}