package staffRosteringSystem;

import java.io.*;
import java.util.*;

/**
 * RequestManager handles all leave and duty request operations.
 * Responsibilities:
 * - Load/save requests from/to files
 * - View, approve, and reject leave requests
 * - View, approve, and reject duty requests
 * - Submit new leave and duty requests
 */
public class RequestManager {
    private static final String LEAVE_REQUEST_FILE = "Data/Leave_Request.txt";
    private static final String DUTY_REQUEST_FILE = "Data/Duty_Request.txt";
    private static final int INITIAL_REQUEST_ID = 1000;

    // Inner classes for request types
    public static class LeaveRequest extends Request {
        private String leaveType;
        private String reason;

        public LeaveRequest(int employeeId, int requestId, String requestDate, String leaveType, String reason) {
            super(employeeId, requestId, requestDate);
            this.leaveType = leaveType;
            this.reason = reason;
        }

        public String getLeaveType() { return leaveType; }
        public String getReason() { return reason; }
    }

    public static class DutyRequest extends Request {
        private String dutyType;
        private String dutyDescription;

        public DutyRequest(int employeeId, int requestId, String requestDate, String dutyType, String dutyDescription) {
            super(employeeId, requestId, requestDate);
            this.dutyType = dutyType;
            this.dutyDescription = dutyDescription;
        }

        public String getDutyType() { return dutyType; }
        public String getDutyDescription() { return dutyDescription; }
    }

    // Load leave requests
    public List<LeaveRequest> loadLeaveRequests() {
        List<LeaveRequest> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        int employeeId = Integer.parseInt(parts[0].trim());
                        int requestId = Integer.parseInt(parts[1].trim());
                        String requestDate = parts[2].trim();
                        String leaveType = parts[3].trim();
                        String reason = parts[4].trim();
                        requests.add(new LeaveRequest(employeeId, requestId, requestDate, leaveType, reason));
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        } catch (NumberFormatException e) {
            System.out.println("Error parsing leave request data: " + e.getMessage());
        }
        return requests;
    }

    // Load duty requests
    public List<DutyRequest> loadDutyRequests() {
        List<DutyRequest> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        int employeeId = Integer.parseInt(parts[0].trim());
                        int requestId = Integer.parseInt(parts[1].trim());
                        String requestDate = parts[2].trim();
                        String dutyType = parts[3].trim();
                        String dutyDescription = parts[4].trim();
                        requests.add(new DutyRequest(employeeId, requestId, requestDate, dutyType, dutyDescription));
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        } catch (NumberFormatException e) {
            System.out.println("Error parsing duty request data: " + e.getMessage());
        }
        return requests;
    }

    // Save leave requests
    public void saveLeaveRequests(List<LeaveRequest> requests) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEAVE_REQUEST_FILE))) {
            for (LeaveRequest req : requests) {
                writer.println(req.getEmployeeId() + "|" + req.getRequestId() + "|" +
                        req.getRequestDate() + "|" + req.getLeaveType() + "|" + req.getReason());
            }
        } catch (IOException e) {
            System.out.println("Error saving leave requests: " + e.getMessage());
        }
    }

    // Save duty requests
    public void saveDutyRequests(List<DutyRequest> requests) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DUTY_REQUEST_FILE))) {
            for (DutyRequest req : requests) {
                writer.println(req.getEmployeeId() + "," + req.getRequestId() + "," +
                        req.getRequestDate() + "," + req.getDutyType() + "," + req.getDutyDescription());
            }
        } catch (IOException e) {
            System.out.println("Error saving duty requests: " + e.getMessage());
        }
    }

    // View leave requests with case numbers
    public void viewLeaveRequestsWithCaseNumbers(StaffManager staffManager) {
        List<LeaveRequest> requests = loadLeaveRequests();
        if (requests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }

        System.out.println("\n=============== Leave Requests ===============");
        System.out.printf("%-5s %-8s %-8s %-15s %-12s %-20s%n", "Case", "Req ID", "Staff ID", "Type", "Date", "Reason");
        System.out.println("===========================================================================");

        for (int i = 0; i < requests.size(); i++) {
            LeaveRequest req = requests.get(i);
            String truncatedReason = req.getReason().length() > 20 ? 
                                     req.getReason().substring(0, 17) + "..." : req.getReason();
            
            System.out.printf("%-5d %-8d %-8d %-15s %-12s %-20s%n", 
                            i + 1, req.getRequestId(), req.getEmployeeId(), 
                            req.getLeaveType(), req.getRequestDate(), truncatedReason);
        }
        System.out.println("===========================================================================");
    }

    // View duty requests with case numbers
    public void viewDutyRequestsWithCaseNumbers(StaffManager staffManager) {
        List<DutyRequest> requests = loadDutyRequests();
        if (requests.isEmpty()) {
            System.out.println("No duty requests found.");
            return;
        }

        System.out.println("\n=============== Duty Requests ===============");
        System.out.printf("%-5s %-8s %-8s %-12s %-12s %-20s%n", "Case", "Req ID", "Staff ID", "Type", "Date", "Description");
        System.out.println("===========================================================================");

        for (int i = 0; i < requests.size(); i++) {
            DutyRequest req = requests.get(i);
            String truncatedDesc = req.getDutyDescription().length() > 20 ? 
                                   req.getDutyDescription().substring(0, 17) + "..." : req.getDutyDescription();
            
            System.out.printf("%-5d %-8d %-8d %-12s %-12s %-20s%n", 
                            i + 1, req.getRequestId(), req.getEmployeeId(), 
                            req.getDutyType(), req.getRequestDate(), truncatedDesc);
        }
        System.out.println("===========================================================================");
    }

    // Approve leave request by case number
    public boolean approveLeaveRequestByCaseNumber(int caseNumber) {
        List<LeaveRequest> requests = loadLeaveRequests();
        if (caseNumber < 1 || caseNumber > requests.size()) {
            System.out.println("Error: Invalid case number!");
            return false;
        }

        LeaveRequest req = requests.get(caseNumber - 1);
        requests.remove(caseNumber - 1);
        saveLeaveRequests(requests);

        System.out.println("Leave request approved successfully!");
        System.out.println("  Request ID: " + req.getRequestId());
        System.out.println("  Employee ID: " + req.getEmployeeId());
        System.out.println("  Leave Type: " + req.getLeaveType());
        System.out.println("  Date: " + req.getRequestDate());

        return true;
    }

    // Reject leave request by case number
    public boolean rejectLeaveRequestByCaseNumber(int caseNumber) {
        List<LeaveRequest> requests = loadLeaveRequests();
        if (caseNumber < 1 || caseNumber > requests.size()) {
            System.out.println("Error: Invalid case number!");
            return false;
        }

        LeaveRequest req = requests.get(caseNumber - 1);
        requests.remove(caseNumber - 1);
        saveLeaveRequests(requests);

        System.out.println("Leave request rejected successfully!");
        System.out.println("  Request ID: " + req.getRequestId());
        System.out.println("  Employee ID: " + req.getEmployeeId());
        System.out.println("  Leave Type: " + req.getLeaveType());

        return true;
    }

    // Approve duty request by case number
    public boolean approveDutyRequestByCaseNumber(int caseNumber) {
        List<DutyRequest> requests = loadDutyRequests();
        if (caseNumber < 1 || caseNumber > requests.size()) {
            System.out.println("Error: Invalid case number!");
            return false;
        }

        DutyRequest req = requests.get(caseNumber - 1);
        requests.remove(caseNumber - 1);
        saveDutyRequests(requests);

        System.out.println("Duty request approved successfully!");
        System.out.println("  Request ID: " + req.getRequestId());
        System.out.println("  Employee ID: " + req.getEmployeeId());
        System.out.println("  Duty Type: " + req.getDutyType());
        System.out.println("  Date: " + req.getRequestDate());

        return true;
    }

    // Reject duty request by case number
    public boolean rejectDutyRequestByCaseNumber(int caseNumber) {
        List<DutyRequest> requests = loadDutyRequests();
        if (caseNumber < 1 || caseNumber > requests.size()) {
            System.out.println("Error: Invalid case number!");
            return false;
        }

        DutyRequest req = requests.get(caseNumber - 1);
        requests.remove(caseNumber - 1);
        saveDutyRequests(requests);

        System.out.println("Duty request rejected successfully!");
        System.out.println("  Request ID: " + req.getRequestId());
        System.out.println("  Employee ID: " + req.getEmployeeId());
        System.out.println("  Duty Type: " + req.getDutyType());

        return true;
    }

    // Request leave
    public void requestLeave(int employeeId, Scanner scanner, StaffManager staffManager) {
        if (!staffManager.staffExists(employeeId)) {
            System.out.println("Error: Employee ID " + employeeId + " not found!");
            return;
        }

        System.out.print("Enter leave type (e.g., Sick, Annual, Unpaid): ");
        String leaveType = scanner.nextLine().trim();
        System.out.print("Enter reason: ");
        String reason = scanner.nextLine().trim();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        List<LeaveRequest> requests = loadLeaveRequests();
        int newRequestId = generateRequestId(requests);
        
        requests.add(new LeaveRequest(employeeId, newRequestId, date, leaveType, reason));
        saveLeaveRequests(requests);

        System.out.println("Leave request submitted successfully!");
        System.out.println("  Request ID: " + newRequestId);
        System.out.println("  Type: " + leaveType);
        System.out.println("  Date: " + date);
    }

    // Request duty
    public void requestDuty(int employeeId, Scanner scanner, StaffManager staffManager) {
        if (!staffManager.staffExists(employeeId)) {
            System.out.println("Error: Employee ID " + employeeId + " not found!");
            return;
        }

        System.out.print("Enter duty type (e.g., Training, Audit, Meeting): ");
        String dutyType = scanner.nextLine().trim();
        System.out.print("Enter duty description: ");
        String dutyDescription = scanner.nextLine().trim();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        List<DutyRequest> requests = loadDutyRequests();
        int newRequestId = generateRequestId(requests);
        
        requests.add(new DutyRequest(employeeId, newRequestId, date, dutyType, dutyDescription));
        saveDutyRequests(requests);

        System.out.println("Duty request submitted successfully!");
        System.out.println("  Request ID: " + newRequestId);
        System.out.println("  Type: " + dutyType);
        System.out.println("  Date: " + date);
    }

    // Helper method to generate request ID
    private int generateRequestId(List<? extends Request> requests) {
        int maxId = INITIAL_REQUEST_ID;
        for (Request req : requests) {
            if (req.getRequestId() > maxId) {
                maxId = req.getRequestId();
            }
        }
        return maxId + 1;
    }

    // Initialize files if they don't exist
    public void initializeRequestFiles() {
        initializeFile(LEAVE_REQUEST_FILE);
        initializeFile(DUTY_REQUEST_FILE);
    }

    private void initializeFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + filePath);
            }
        }
    }
}
