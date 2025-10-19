package administrator;

import java.io.*;
import java.util.*;

import baseFunction.BaseFunction;;

public class Administrator extends BaseFunction {
    private static final String STAFF_PROFILE_FILE = "CS3343 Project/Data/Staff_Profile.txt";
    private static final String LEAVE_REQUEST_FILE = "CS3343 Project/Data/Leave_Request.txt";
    private static final String DUTY_REQUEST_FILE = "CS3343 Project/Data/Duty_Request.txt";
    private static final String SHIFT_FILE = "CS3343 Project/Data/Shift.txt";

    // Session time constants
    public static final String MORNING_SESSION = "MORNING";
    public static final String AFTERNOON_SESSION = "AFTERNOON";
    public static final String NIGHT_SESSION = "NIGHT";

    public Administrator(int userId, String username, String password) {
        super(userId, username, password);
        initializeStaffFile();
    }

    // Initialize staff profile file if it doesn't exist
    private void initializeStaffFile() {
        File file = new File(STAFF_PROFILE_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Staff profile file created: " + STAFF_PROFILE_FILE);
            } catch (IOException e) {
                System.out.println("Error creating staff profile file: " + e.getMessage());
            }
        }
    }

    // Load staff profiles from file
    private List<StaffProfile> loadStaffProfiles() {
        List<StaffProfile> profiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        int staffId = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String role = parts[2].trim();
                        String department = parts[3].trim();
                        double salary = Double.parseDouble(parts[4].trim());
                        profiles.add(new StaffProfile(staffId, name, role, department, salary));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading staff profiles: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing staff data: " + e.getMessage());
        }
        return profiles;
    }

    // Save staff profiles to file
    private void saveStaffProfiles(List<StaffProfile> profiles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STAFF_PROFILE_FILE))) {
            for (StaffProfile profile : profiles) {
                writer.println(profile.getStaffId() + "|" + profile.getName() + "|" +
                        profile.getRole() + "|" + profile.getDepartment() + "|" + profile.getSalary());
            }
        } catch (IOException e) {
            System.out.println("Error saving staff profiles: " + e.getMessage());
        }
    }

    // Get user info by staff ID
    public StaffProfile getUserInfo(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                return profile;
            }
        }
        return null;
    }

    // Add staff profile with validation
    public boolean addStaffProfile(int staffId, String staffName, String role, String department, double salary) {
        List<StaffProfile> profiles = loadStaffProfiles();

        // Check if staff ID already exists
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                System.out.println("Error: Staff ID " + staffId + " already exists!");
                return false;
            }
        }

        StaffProfile newStaff = new StaffProfile(staffId, staffName, role, department, salary);
        profiles.add(newStaff);
        saveStaffProfiles(profiles);

        System.out.println("✓ Staff profile added successfully:");
        System.out.println("  ID: " + staffId + ", Name: " + staffName + ", Role: " + role);
        System.out.println("  Department: " + department + ", Salary: $" + salary);
        return true;
    }

    // Edit staff profile with specific field updates
    public boolean editStaffProfile(int staffId, String field, String newValue) {
        List<StaffProfile> profiles = loadStaffProfiles();
        StaffProfile targetStaff = null;

        // Find the staff to edit
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                targetStaff = profile;
                break;
            }
        }

        if (targetStaff == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return false;
        }

        boolean updated = false;

        switch (field.toLowerCase()) {
            case "name":
                targetStaff.setName(newValue);
                updated = true;
                break;
            case "role":
                targetStaff.setRole(newValue);
                updated = true;
                break;
            case "department":
                targetStaff.setDepartment(newValue);
                updated = true;
                break;
            case "salary":
                try {
                    targetStaff.setSalary(Double.parseDouble(newValue));
                    updated = true;
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid salary format!");
                    return false;
                }
                break;
            default:
                System.out.println("Error: Invalid field '" + field + "'!");
                return false;
        }

        if (updated) {
            saveStaffProfiles(profiles);
            System.out.println("✓ Staff profile " + staffId + " updated successfully.");
            System.out.println("  " + field + " changed to: " + newValue);
        }
        return updated;
    }

    // View staff profile with detailed information
    public void viewStaffProfile(int staffId) {
        StaffProfile staff = getUserInfo(staffId);
        if (staff == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return;
        }

        System.out.println("==================== STAFF PROFILE ====================");
        System.out.println("Staff ID: " + staff.getStaffId());
        System.out.println("Name: " + staff.getName());
        System.out.println("Role: " + staff.getRole());
        System.out.println("Department: " + staff.getDepartment());
        System.out.println("Salary: $" + staff.getSalary());
        System.out.println("======================================================");
    }

    // View all staff profiles
    public void viewAllStaffProfiles() {
        List<StaffProfile> profiles = loadStaffProfiles();
        if (profiles.isEmpty()) {
            System.out.println("No staff profiles found.");
            return;
        }

        System.out.println("===================== ALL STAFF PROFILES =====================");
        System.out.printf("%-8s %-20s %-15s %-15s %-10s%n", "ID", "Name", "Role", "Department", "Salary");
        System.out.println("----------------------------------------------------------------");

        for (StaffProfile staff : profiles) {
            System.out.printf("%-8d %-20s %-15s %-15s $%-9.2f%n",
                    staff.getStaffId(), staff.getName(), staff.getRole(),
                    staff.getDepartment(), staff.getSalary());
        }
        System.out.println("================================================================");
    }

    // Delete staff profile with confirmation
    public boolean deleteStaffProfile(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        StaffProfile toRemove = null;

        // Find the staff to remove
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                toRemove = profile;
                break;
            }
        }

        if (toRemove == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return false;
        }

        profiles.remove(toRemove);
        saveStaffProfiles(profiles);
        System.out.println("✓ Staff profile deleted successfully:");
        System.out.println("  Removed: " + toRemove.getName() + " (ID: " + staffId + ")");
        return true;
    }

    // Helper method to check if staff exists
    public boolean staffExists(int staffId) {
        return getUserInfo(staffId) != null;
    }

    // Get staff count
    public int getStaffCount() {
        return loadStaffProfiles().size();
    }

    // Search staff by name (partial match)
    public List<StaffProfile> searchStaffByName(String searchName) {
        List<StaffProfile> profiles = loadStaffProfiles();
        List<StaffProfile> results = new ArrayList<>();

        for (StaffProfile profile : profiles) {
            if (profile.getName().toLowerCase().contains(searchName.toLowerCase())) {
                results.add(profile);
            }
        }
        return results;
    }

    // Get staff by department
    public List<StaffProfile> getStaffByDepartment(String department) {
        List<StaffProfile> profiles = loadStaffProfiles();
        List<StaffProfile> results = new ArrayList<>();

        for (StaffProfile profile : profiles) {
            if (profile.getDepartment().equalsIgnoreCase(department)) {
                results.add(profile);
            }
        }
        return results;
    }

    // ================== ROSTER PREPARATION FUNCTIONS ==================

    // Request inner class for managing employee requests
    public static class Request {
        private int requestId;
        private int employeeId;
        private String requestType; // "LEAVE" or "DUTY"
        private String startDate;
        private String endDate;
        private String reason;
        private String status; // "PENDING", "APPROVED", "REJECTED"
        private String requestDate;

        public Request(int requestId, int employeeId, String requestType, String startDate,
                String endDate, String reason, String status, String requestDate) {
            this.requestId = requestId;
            this.employeeId = employeeId;
            this.requestType = requestType;
            this.startDate = startDate;
            this.endDate = endDate;
            this.reason = reason;
            this.status = status;
            this.requestDate = requestDate;
        }

        // Getters
        public int getRequestId() {
            return requestId;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public String getRequestType() {
            return requestType;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getReason() {
            return reason;
        }

        public String getStatus() {
            return status;
        }

        public String getRequestDate() {
            return requestDate;
        }

        // Setters
        public void setStatus(String status) {
            this.status = status;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    // Load all requests from both files
    private List<Request> loadAllRequests() {
        List<Request> requests = new ArrayList<>();
        requests.addAll(loadLeaveRequests());
        requests.addAll(loadDutyRequests());
        return requests;
    }

    // Load leave requests from file
    private List<Request> loadLeaveRequests() {
        return loadRequestsFromFile(LEAVE_REQUEST_FILE, "LEAVE");
    }

    // Load duty requests from file
    private List<Request> loadDutyRequests() {
        return loadRequestsFromFile(DUTY_REQUEST_FILE, "DUTY");
    }

    // Generic method to load requests from file
    private List<Request> loadRequestsFromFile(String filename, String requestType) {
        List<Request> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 7) {
                        int requestId = Integer.parseInt(parts[0].trim());
                        int employeeId = Integer.parseInt(parts[1].trim());
                        String startDate = parts[2].trim();
                        String endDate = parts[3].trim();
                        String reason = parts[4].trim();
                        String status = parts[5].trim();
                        String requestDate = parts[6].trim();
                        requests.add(new Request(requestId, employeeId, requestType, startDate,
                                endDate, reason, status, requestDate));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + requestType.toLowerCase() + " requests: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing " + requestType.toLowerCase() + " request data: " + e.getMessage());
        }
        return requests;
    }

    // Save requests back to appropriate files
    private void saveRequestsToFile(List<Request> requests, String filename, String requestType) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("# " + requestType + " Request Data File");
            writer.println("# Format: RequestID|EmployeeID|StartDate|EndDate|Reason|Status|RequestDate");

            for (Request request : requests) {
                if (request.getRequestType().equals(requestType)) {
                    writer.println(request.getRequestId() + "|" + request.getEmployeeId() + "|" +
                            request.getStartDate() + "|" + request.getEndDate() + "|" +
                            request.getReason() + "|" + request.getStatus() + "|" +
                            request.getRequestDate());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving " + requestType.toLowerCase() + " requests: " + e.getMessage());
        }
    }

    // ================== SIMPLIFIED DUTY REQUEST FUNCTIONS ==================

    // Simplified DutyRequest class for employee duty requests
    public static class DutyRequest {
        private int employeeId;
        private String date;
        private String session;

        public DutyRequest(int employeeId, String date, String session) {
            this.employeeId = employeeId;
            this.date = date;
            this.session = session;
        }

        // Getters
        public int getEmployeeId() {
            return employeeId;
        }

        public String getDate() {
            return date;
        }

        public String getSession() {
            return session;
        }
    }

    // Load simplified duty requests from file
    private List<DutyRequest> loadSimpleDutyRequests() {
        List<DutyRequest> dutyRequests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        int employeeId = Integer.parseInt(parts[0].trim());
                        String date = parts[1].trim();
                        String session = parts[2].trim();
                        dutyRequests.add(new DutyRequest(employeeId, date, session));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading duty requests: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing duty request data: " + e.getMessage());
        }
        return dutyRequests;
    }

    // Save duty requests to file
    private void saveSimpleDutyRequests(List<DutyRequest> dutyRequests) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DUTY_REQUEST_FILE))) {
            writer.println("# Duty Request Data File");
            writer.println("# Format: EmployeeID|Date|Session|");

            for (DutyRequest request : dutyRequests) {
                writer.println(request.getEmployeeId() + "|" + request.getDate() + "|" + request.getSession() + "|");
            }
        } catch (IOException e) {
            System.out.println("Error saving duty requests: " + e.getMessage());
        }
    }

    // View all duty requests (no status needed)
    public void viewDutyRequests() {
        List<DutyRequest> dutyRequests = loadSimpleDutyRequests();

        if (dutyRequests.isEmpty()) {
            System.out.println("No duty requests found.");
            return;
        }

        System.out.println("==================== DUTY REQUESTS ====================");
        System.out.printf("%-10s %-20s %-12s %-12s%n", "Emp ID", "Employee Name", "Date", "Session");
        System.out.println("--------------------------------------------------------");

        for (DutyRequest request : dutyRequests) {
            StaffProfile staff = getUserInfo(request.getEmployeeId());
            String employeeName = (staff != null) ? staff.getName() : "Unknown";

            System.out.printf("%-10d %-20s %-12s %-12s%n",
                    request.getEmployeeId(), employeeName,
                    request.getDate(), request.getSession());
        }
        System.out.println("========================================================");
    }

    // Approve duty request - automatically assign shift and remove from requests
    public boolean approveDutyRequest(int employeeId, String date, String session) {
        List<DutyRequest> dutyRequests = loadSimpleDutyRequests();
        DutyRequest targetRequest = null;

        // Find the specific duty request
        for (DutyRequest request : dutyRequests) {
            if (request.getEmployeeId() == employeeId &&
                    request.getDate().equals(date) &&
                    request.getSession().equals(session)) {
                targetRequest = request;
                break;
            }
        }

        if (targetRequest == null) {
            System.out.println("Error: Duty request not found for Employee ID " + employeeId +
                    " on " + date + " for " + session + " session!");
            return false;
        }

        // Assign the shift automatically
        boolean shiftAssigned = assignShift(employeeId, date, session, "Approved duty request");

        if (shiftAssigned) {
            // Remove the request from duty requests file
            dutyRequests.remove(targetRequest);
            saveSimpleDutyRequests(dutyRequests);

            StaffProfile staff = getUserInfo(employeeId);
            String employeeName = (staff != null) ? staff.getName() : "Unknown";

            System.out.println("✓ Duty request approved and shift assigned:");
            System.out.println("  Employee: " + employeeName + " (ID: " + employeeId + ")");
            System.out.println("  Date: " + date);
            System.out.println("  Session: " + session);
            System.out.println("  → Employee can now check their assigned duties in the shift schedule.");

            return true;
        }

        return false;
    }

    // Remove duty request without approving
    public boolean removeDutyRequest(int employeeId, String date, String session) {
        List<DutyRequest> dutyRequests = loadSimpleDutyRequests();
        DutyRequest targetRequest = null;

        // Find the specific duty request
        for (DutyRequest request : dutyRequests) {
            if (request.getEmployeeId() == employeeId &&
                    request.getDate().equals(date) &&
                    request.getSession().equals(session)) {
                targetRequest = request;
                break;
            }
        }

        if (targetRequest == null) {
            System.out.println("Error: Duty request not found!");
            return false;
        }

        dutyRequests.remove(targetRequest);
        saveSimpleDutyRequests(dutyRequests);

        StaffProfile staff = getUserInfo(employeeId);
        String employeeName = (staff != null) ? staff.getName() : "Unknown";

        System.out.println("✓ Duty request removed:");
        System.out.println("  Employee: " + employeeName + " (ID: " + employeeId + ")");
        System.out.println("  Date: " + date + ", Session: " + session);

        return true;
    }

    // ================== LEAVE REQUEST FUNCTIONS (keeping status system)
    // ==================

    // View all pending requests (only leave requests now)
    public void viewPendingRequests() {
        List<Request> leaveRequests = loadLeaveRequests();
        List<Request> pendingRequests = new ArrayList<>();

        for (Request request : leaveRequests) {
            if ("PENDING".equals(request.getStatus())) {
                pendingRequests.add(request);
            }
        }

        if (pendingRequests.isEmpty()) {
            System.out.println("No pending leave requests found.");
            return;
        }

        System.out.println("==================== PENDING LEAVE REQUESTS ====================");
        System.out.printf("%-8s %-10s %-8s %-12s %-12s %-30s%n",
                "Req ID", "Type", "Emp ID", "Start Date", "End Date", "Reason");
        System.out.println("------------------------------------------------------------------");

        for (Request request : pendingRequests) {
            StaffProfile staff = getUserInfo(request.getEmployeeId());
            String employeeName = (staff != null) ? staff.getName() : "Unknown";

            System.out.printf("%-8d %-10s %-8d %-12s %-12s %-30s%n",
                    request.getRequestId(), request.getRequestType(),
                    request.getEmployeeId(), request.getStartDate(),
                    request.getEndDate(), request.getReason());
            System.out.println("         Employee: " + employeeName);
        }
        System.out.println("==================================================================");
    }

    // Approve a leave request (duty requests are handled separately)
    public boolean approveRequest(int requestId) {
        return updateRequestStatus(requestId, "APPROVED");
    }

    // Reject a request
    public boolean rejectRequest(int requestId, String rejectionReason) {
        List<Request> allRequests = loadAllRequests();
        Request targetRequest = null;

        for (Request request : allRequests) {
            if (request.getRequestId() == requestId) {
                targetRequest = request;
                break;
            }
        }

        if (targetRequest == null) {
            System.out.println("Error: Request ID " + requestId + " not found!");
            return false;
        }

        if (!"PENDING".equals(targetRequest.getStatus())) {
            System.out.println("Error: Request " + requestId + " is not pending!");
            return false;
        }

        // Update status and reason
        targetRequest.setStatus("REJECTED");
        if (rejectionReason != null && !rejectionReason.trim().isEmpty()) {
            targetRequest.setReason(targetRequest.getReason() + " [REJECTED: " + rejectionReason + "]");
        }

        // Save back to appropriate file
        if ("LEAVE".equals(targetRequest.getRequestType())) {
            List<Request> leaveRequests = loadLeaveRequests();
            for (Request req : leaveRequests) {
                if (req.getRequestId() == requestId) {
                    req.setStatus("REJECTED");
                    req.setReason(targetRequest.getReason());
                    break;
                }
            }
            saveRequestsToFile(leaveRequests, LEAVE_REQUEST_FILE, "LEAVE");
        } else {
            List<Request> dutyRequests = loadDutyRequests();
            for (Request req : dutyRequests) {
                if (req.getRequestId() == requestId) {
                    req.setStatus("REJECTED");
                    req.setReason(targetRequest.getReason());
                    break;
                }
            }
            saveRequestsToFile(dutyRequests, DUTY_REQUEST_FILE, "DUTY");
        }

        StaffProfile staff = getUserInfo(targetRequest.getEmployeeId());
        String employeeName = (staff != null) ? staff.getName() : "Unknown";

        System.out.println("✓ Request rejected successfully:");
        System.out.println("  Request ID: " + requestId);
        System.out.println("  Employee: " + employeeName + " (ID: " + targetRequest.getEmployeeId() + ")");
        System.out.println("  Type: " + targetRequest.getRequestType());
        System.out.println("  Period: " + targetRequest.getStartDate() + " to " + targetRequest.getEndDate());
        if (rejectionReason != null && !rejectionReason.trim().isEmpty()) {
            System.out.println("  Rejection Reason: " + rejectionReason);
        }

        return true;
    }

    // Update request status (helper method) - only for leave requests now
    private boolean updateRequestStatus(int requestId, String newStatus) {
        List<Request> leaveRequests = loadLeaveRequests();
        Request targetRequest = null;

        for (Request request : leaveRequests) {
            if (request.getRequestId() == requestId) {
                targetRequest = request;
                break;
            }
        }

        if (targetRequest == null) {
            System.out.println("Error: Leave request ID " + requestId + " not found!");
            return false;
        }

        if (!"PENDING".equals(targetRequest.getStatus())) {
            System.out.println("Error: Leave request " + requestId + " is not pending!");
            return false;
        }

        // Update the request
        targetRequest.setStatus(newStatus);

        // Save back to leave request file
        List<Request> allLeaveRequests = loadLeaveRequests();
        for (Request req : allLeaveRequests) {
            if (req.getRequestId() == requestId) {
                req.setStatus(newStatus);
                break;
            }
        }
        saveRequestsToFile(allLeaveRequests, LEAVE_REQUEST_FILE, "LEAVE");

        StaffProfile staff = getUserInfo(targetRequest.getEmployeeId());
        String employeeName = (staff != null) ? staff.getName() : "Unknown";

        System.out.println("✓ Leave request " + newStatus.toLowerCase() + " successfully:");
        System.out.println("  Request ID: " + requestId);
        System.out.println("  Employee: " + employeeName + " (ID: " + targetRequest.getEmployeeId() + ")");
        System.out.println("  Period: " + targetRequest.getStartDate() + " to " + targetRequest.getEndDate());

        return true;
    }

    // View leave requests by status
    public void viewRequestsByStatus(String status) {
        List<Request> leaveRequests = loadLeaveRequests();
        List<Request> filteredRequests = new ArrayList<>();

        for (Request request : leaveRequests) {
            if (status.equalsIgnoreCase(request.getStatus())) {
                filteredRequests.add(request);
            }
        }

        if (filteredRequests.isEmpty()) {
            System.out.println("No " + status.toLowerCase() + " leave requests found.");
            return;
        }

        System.out.println("================== " + status.toUpperCase() + " LEAVE REQUESTS ==================");
        System.out.printf("%-8s %-8s %-12s %-12s %-15s%n",
                "Req ID", "Emp ID", "Start Date", "End Date", "Employee Name");
        System.out.println("-----------------------------------------------------------");

        for (Request request : filteredRequests) {
            StaffProfile staff = getUserInfo(request.getEmployeeId());
            String employeeName = (staff != null) ? staff.getName() : "Unknown";

            System.out.printf("%-8d %-8d %-12s %-12s %-15s%n",
                    request.getRequestId(), request.getEmployeeId(),
                    request.getStartDate(), request.getEndDate(), employeeName);
        }
        System.out.println("===========================================================");
    } // ================== SESSION MANAGEMENT FUNCTIONS ==================

    // Shift inner class for managing work sessions
    public static class Shift {
        private int shiftId;
        private int employeeId;
        private String date;
        private String session; // MORNING, AFTERNOON, NIGHT
        private String startTime;
        private String endTime;
        private String status; // SCHEDULED, COMPLETED, CANCELLED
        private String notes;

        public Shift(int shiftId, int employeeId, String date, String session,
                String startTime, String endTime, String status, String notes) {
            this.shiftId = shiftId;
            this.employeeId = employeeId;
            this.date = date;
            this.session = session;
            this.startTime = startTime;
            this.endTime = endTime;
            this.status = status;
            this.notes = notes;
        }

        // Getters
        public int getShiftId() {
            return shiftId;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public String getDate() {
            return date;
        }

        public String getSession() {
            return session;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getStatus() {
            return status;
        }

        public String getNotes() {
            return notes;
        }

        // Setters
        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    // Load shifts from file
    private List<Shift> loadShifts() {
        List<Shift> shifts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 7) {
                        int shiftId = Integer.parseInt(parts[0].trim());
                        int employeeId = Integer.parseInt(parts[1].trim());
                        String date = parts[2].trim();
                        String session = parts[3].trim();
                        String startTime = parts[4].trim();
                        String endTime = parts[5].trim();
                        String status = parts[6].trim();
                        String notes = parts.length > 7 ? parts[7].trim() : "";
                        shifts.add(new Shift(shiftId, employeeId, date, session, startTime, endTime, status, notes));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading shifts: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing shift data: " + e.getMessage());
        }
        return shifts;
    }

    // Save shifts to file
    private void saveShifts(List<Shift> shifts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SHIFT_FILE))) {
            writer.println("# Shift Data File");
            writer.println("# Format: ShiftID|EmployeeID|Date|Session|StartTime|EndTime|Status|Notes");

            for (Shift shift : shifts) {
                writer.println(shift.getShiftId() + "|" + shift.getEmployeeId() + "|" +
                        shift.getDate() + "|" + shift.getSession() + "|" +
                        shift.getStartTime() + "|" + shift.getEndTime() + "|" +
                        shift.getStatus() + "|" + shift.getNotes());
            }
        } catch (IOException e) {
            System.out.println("Error saving shifts: " + e.getMessage());
        }
    }

    // Assign shift to employee
    public boolean assignShift(int employeeId, String date, String session, String notes) {
        // Validate employee exists
        if (!staffExists(employeeId)) {
            System.out.println("Error: Employee ID " + employeeId + " not found!");
            return false;
        }

        // Validate session
        if (!isValidSession(session)) {
            System.out.println("Error: Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
            return false;
        }

        List<Shift> shifts = loadShifts();

        // Check for duplicate shift assignment
        for (Shift shift : shifts) {
            if (shift.getEmployeeId() == employeeId &&
                    shift.getDate().equals(date) &&
                    shift.getSession().equals(session) &&
                    !"CANCELLED".equals(shift.getStatus())) {
                System.out.println("Error: Employee already assigned to " + session + " session on " + date);
                return false;
            }
        }

        // Generate new shift ID
        int newShiftId = generateShiftId(shifts);

        // Determine start and end times based on session
        String[] times = getSessionTimes(session);
        String startTime = times[0];
        String endTime = times[1];

        Shift newShift = new Shift(newShiftId, employeeId, date, session, startTime, endTime, "SCHEDULED", notes);
        shifts.add(newShift);
        saveShifts(shifts);

        StaffProfile staff = getUserInfo(employeeId);
        String employeeName = (staff != null) ? staff.getName() : "Unknown";

        System.out.println("✓ Shift assigned successfully:");
        System.out.println("  Shift ID: " + newShiftId);
        System.out.println("  Employee: " + employeeName + " (ID: " + employeeId + ")");
        System.out.println("  Date: " + date);
        System.out.println("  Session: " + session + " (" + startTime + " - " + endTime + ")");
        if (notes != null && !notes.trim().isEmpty()) {
            System.out.println("  Notes: " + notes);
        }

        return true;
    }

    // Edit shift details
    public boolean editShift(int shiftId, String field, String newValue) {
        List<Shift> shifts = loadShifts();
        Shift targetShift = null;

        for (Shift shift : shifts) {
            if (shift.getShiftId() == shiftId) {
                targetShift = shift;
                break;
            }
        }

        if (targetShift == null) {
            System.out.println("Error: Shift ID " + shiftId + " not found!");
            return false;
        }

        boolean updated = false;

        switch (field.toLowerCase()) {
            case "employee":
            case "employeeid":
                try {
                    int newEmployeeId = Integer.parseInt(newValue);
                    if (staffExists(newEmployeeId)) {
                        targetShift.setEmployeeId(newEmployeeId);
                        updated = true;
                    } else {
                        System.out.println("Error: Employee ID " + newEmployeeId + " not found!");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid employee ID format!");
                    return false;
                }
                break;
            case "session":
                if (isValidSession(newValue)) {
                    targetShift.setSession(newValue);
                    // Update times based on new session
                    String[] times = getSessionTimes(newValue);
                    targetShift.setStartTime(times[0]);
                    targetShift.setEndTime(times[1]);
                    updated = true;
                } else {
                    System.out.println("Error: Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
                    return false;
                }
                break;
            case "starttime":
                targetShift.setStartTime(newValue);
                updated = true;
                break;
            case "endtime":
                targetShift.setEndTime(newValue);
                updated = true;
                break;
            case "status":
                if (isValidStatus(newValue)) {
                    targetShift.setStatus(newValue);
                    updated = true;
                } else {
                    System.out.println("Error: Invalid status! Valid statuses: SCHEDULED, COMPLETED, CANCELLED");
                    return false;
                }
                break;
            case "notes":
                targetShift.setNotes(newValue);
                updated = true;
                break;
            default:
                System.out.println("Error: Invalid field '" + field + "'!");
                return false;
        }

        if (updated) {
            saveShifts(shifts);
            System.out.println("✓ Shift " + shiftId + " updated successfully.");
            System.out.println("  " + field + " changed to: " + newValue);
        }

        return updated;
    }

    // Delete/Cancel shift
    public boolean deleteShift(int shiftId) {
        List<Shift> shifts = loadShifts();
        Shift targetShift = null;

        for (Shift shift : shifts) {
            if (shift.getShiftId() == shiftId) {
                targetShift = shift;
                break;
            }
        }

        if (targetShift == null) {
            System.out.println("Error: Shift ID " + shiftId + " not found!");
            return false;
        }

        shifts.remove(targetShift);
        saveShifts(shifts);

        StaffProfile staff = getUserInfo(targetShift.getEmployeeId());
        String employeeName = (staff != null) ? staff.getName() : "Unknown";

        System.out.println("✓ Shift deleted successfully:");
        System.out.println("  Shift ID: " + shiftId);
        System.out.println("  Employee: " + employeeName);
        System.out.println("  Date: " + targetShift.getDate());
        System.out.println("  Session: " + targetShift.getSession());

        return true;
    }

    // View shift schedule
    public void viewShiftSchedule(String date) {
        List<Shift> shifts = loadShifts();
        List<Shift> dayShifts = new ArrayList<>();

        for (Shift shift : shifts) {
            if (shift.getDate().equals(date)) {
                dayShifts.add(shift);
            }
        }

        if (dayShifts.isEmpty()) {
            System.out.println("No shifts scheduled for " + date);
            return;
        }

        // Sort by session order (Morning, Afternoon, Night)
        dayShifts.sort((s1, s2) -> {
            int order1 = getSessionOrder(s1.getSession());
            int order2 = getSessionOrder(s2.getSession());
            return Integer.compare(order1, order2);
        });

        System.out.println("==================== SHIFT SCHEDULE FOR " + date + " ====================");
        System.out.printf("%-8s %-12s %-20s %-10s %-15s %-10s%n",
                "Shift ID", "Session", "Employee", "Time", "Status", "Notes");
        System.out.println("------------------------------------------------------------------------");

        for (Shift shift : dayShifts) {
            StaffProfile staff = getUserInfo(shift.getEmployeeId());
            String employeeName = (staff != null) ? staff.getName() : "Unknown";
            String timeRange = shift.getStartTime() + "-" + shift.getEndTime();

            System.out.printf("%-8d %-12s %-20s %-10s %-15s %-10s%n",
                    shift.getShiftId(), shift.getSession(), employeeName,
                    timeRange, shift.getStatus(), shift.getNotes());
        }
        System.out.println("========================================================================");
    }

    // View shifts by session
    public void viewShiftsBySession(String session, String date) {
        if (!isValidSession(session)) {
            System.out.println("Error: Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
            return;
        }

        List<Shift> shifts = loadShifts();
        List<Shift> sessionShifts = new ArrayList<>();

        for (Shift shift : shifts) {
            if (shift.getSession().equals(session) && (date == null || shift.getDate().equals(date))) {
                sessionShifts.add(shift);
            }
        }

        if (sessionShifts.isEmpty()) {
            String dateStr = (date != null) ? " on " + date : "";
            System.out.println("No " + session + " shifts found" + dateStr);
            return;
        }

        String title = session + " SHIFTS" + (date != null ? " - " + date : "");
        System.out.println("==================== " + title + " ====================");
        System.out.printf("%-8s %-12s %-20s %-10s %-15s%n",
                "Shift ID", "Date", "Employee", "Time", "Status");
        System.out.println("------------------------------------------------------------");

        for (Shift shift : sessionShifts) {
            StaffProfile staff = getUserInfo(shift.getEmployeeId());
            String employeeName = (staff != null) ? staff.getName() : "Unknown";
            String timeRange = shift.getStartTime() + "-" + shift.getEndTime();

            System.out.printf("%-8d %-12s %-20s %-10s %-15s%n",
                    shift.getShiftId(), shift.getDate(), employeeName,
                    timeRange, shift.getStatus());
        }
        System.out.println("============================================================");
    }

    // Helper methods
    private boolean isValidSession(String session) {
        return MORNING_SESSION.equals(session) || AFTERNOON_SESSION.equals(session) || NIGHT_SESSION.equals(session);
    }

    private boolean isValidStatus(String status) {
        return "SCHEDULED".equals(status) || "COMPLETED".equals(status) || "CANCELLED".equals(status);
    }

    private String[] getSessionTimes(String session) {
        switch (session) {
            case MORNING_SESSION:
                return new String[] { "06:00", "14:00" };
            case AFTERNOON_SESSION:
                return new String[] { "14:00", "22:00" };
            case NIGHT_SESSION:
                return new String[] { "22:00", "06:00" };
            default:
                return new String[] { "00:00", "00:00" };
        }
    }

    private int getSessionOrder(String session) {
        switch (session) {
            case MORNING_SESSION:
                return 1;
            case AFTERNOON_SESSION:
                return 2;
            case NIGHT_SESSION:
                return 3;
            default:
                return 4;
        }
    }

    private int generateShiftId(List<Shift> shifts) {
        int maxId = 3000; // Starting from 3000 for shifts
        for (Shift shift : shifts) {
            if (shift.getShiftId() > maxId) {
                maxId = shift.getShiftId();
            }
        }
        return maxId + 1;
    }

    // Inner class for Staff Profile data structure
    private static class StaffProfile {
        private int staffId;
        private String name;
        private String role;
        private String department;
        private double salary;

        public StaffProfile(int staffId, String name, String role, String department, double salary) {
            this.staffId = staffId;
            this.name = name;
            this.role = role;
            this.department = department;
            this.salary = salary;
        }

        // Getters
        public int getStaffId() {
            return staffId;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public String getDepartment() {
            return department;
        }

        public double getSalary() {
            return salary;
        }

        // Setters
        public void setName(String name) {
            this.name = name;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }
    }
}
