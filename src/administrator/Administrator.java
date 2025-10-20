package administrator;

import java.io.*;
import java.util.*;

import baseFunction.BaseFunction;
import staffProfile.StaffProfile;

public class Administrator extends BaseFunction {
    private static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";
    private static final String LEAVE_REQUEST_FILE = "Data/Leave_Request.txt";
    private static final String DUTY_REQUEST_FILE = "Data/Duty_Request.txt";
    private static final String SHIFT_FILE = "Data/Shift.txt";

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

    // ================== STAFF MANAGEMENT ==================
    
    // Load staff profiles from file
    private List<StaffProfile> loadStaffProfiles() {
        List<StaffProfile> profiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        int staffId = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String role = parts[2].trim();
                        // Use simplified constructor without department and salary
                        profiles.add(new StaffProfile(staffId, name, role));
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
                writer.println(profile.getStaffId() + "," + profile.getName() + "," + profile.getRole());
            }
        } catch (IOException e) {
            System.out.println("Error saving staff profiles: " + e.getMessage());
        }
    }

    // Add staff profile with validation (simplified without department and salary)
    public boolean addStaffProfile(int staffId, String staffName, String role) {
        List<StaffProfile> profiles = loadStaffProfiles();

        // Check if staff ID already exists
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                System.out.println("Error: Staff ID " + staffId + " already exists!");
                return false;
            }
        }

        StaffProfile newStaff = new StaffProfile(staffId, staffName, role);
        profiles.add(newStaff);
        saveStaffProfiles(profiles);

        System.out.println("Staff profile added successfully:");
        System.out.println("  ID: " + staffId + ", Name: " + staffName + ", Role: " + role);
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
            default:
                System.out.println("Error: Invalid field '" + field + "'! Only 'name' and 'role' are allowed.");
                return false;
        }

        if (updated) {
            saveStaffProfiles(profiles);
            System.out.println("Staff profile " + staffId + " updated successfully.");
            System.out.println("  " + field + " changed to: " + newValue);
        }
        return updated;
    }

    // View staff profile with detailed information
    public void viewStaffProfile(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        StaffProfile staff = null;
        
        // Find the staff profile
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                staff = profile;
                break;
            }
        }
        
        if (staff == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return;
        }

        System.out.println("==================== STAFF PROFILE ====================");
        System.out.println("Staff ID: " + staff.getStaffId());
        System.out.println("Name: " + staff.getName());
        System.out.println("Role: " + staff.getRole());
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
        System.out.println("Staff profile deleted successfully:");
        System.out.println("  Removed: " + toRemove.getName() + " (ID: " + staffId + ")");
        return true;
    }

    // Helper method to check if staff exists
    public boolean staffExists(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                return true;
            }
        }
        return false;
    }

    // Get staff count
    public int getStaffCount() {
        return loadStaffProfiles().size();
    }

    // ================== ROSTER PREPARATION ==================

    // Base Request class
    public static class Request {
        protected int employeeId;
        protected int requestId;
        protected String requestDate;

        public Request(int employeeId, int requestId, String requestDate) {
            this.employeeId = employeeId;
            this.requestId = requestId;
            this.requestDate = requestDate;
        }

        public int getEmployeeId() { return employeeId; }
        public int getRequestId() { return requestId; }
        public String getRequestDate() { return requestDate; }
    }

    // Duty Request extends Request
    public static class DutyRequest extends Request {
        private String session;

        public DutyRequest(int employeeId, int requestId, String requestDate, String session) {
            super(employeeId, requestId, requestDate);
            this.session = session;
        }

        public String getSession() { return session; }
        public void setSession(String session) { this.session = session; }
    }

    // Leave Request extends Request
    public static class LeaveRequest extends Request {
        private String startDate;
        private String endDate;
        private String reason;

        public LeaveRequest(int employeeId, int requestId, String requestDate, 
                           String startDate, String endDate, String reason) {
            super(employeeId, requestId, requestDate);
            this.startDate = startDate;
            this.endDate = endDate;
            this.reason = reason;
        }

        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getReason() { return reason; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public void setReason(String reason) { this.reason = reason; }
    }

    // ================== REQUEST LEAVE FUNCTIONALITY ==================
    
    public boolean requestLeave(int employeeId, String startDate, String endDate, String reason) {
        if (!staffExists(employeeId)) {
            System.out.println("Error: Employee ID " + employeeId + " not found!");
            return false;
        }
        
        // Generate new request ID
        int requestId = generateRequestId();
        String requestDate = java.time.LocalDate.now().toString();
        
        // Save to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEAVE_REQUEST_FILE, true))) {
            writer.println(requestId + "|" + employeeId + "|" + startDate + "|" + endDate + "|" + reason + "|PENDING|" + requestDate);
        } catch (IOException e) {
            System.out.println("Error saving leave request: " + e.getMessage());
            return false;
        }
        
        StaffProfile staff = getUserInfo(employeeId);
        String employeeName = (staff != null) ? staff.getName() : "Unknown";
        
        System.out.println("Leave request submitted successfully:");
        System.out.println("  Request ID: " + requestId);
        System.out.println("  Employee: " + employeeName + " (ID: " + employeeId + ")");
        System.out.println("  Period: " + startDate + " to " + endDate);
        System.out.println("  Reason: " + reason);
        System.out.println("  Status: PENDING");
        
        return true;
    }
    
    private int generateRequestId() {
        int maxId = 2000;
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("\\|");
                    if (parts.length > 0) {
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            if (id > maxId) {
                                maxId = id;
                            }
                        } catch (NumberFormatException e) {
                            // Skip invalid lines
                        }
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist, that's okay
        }
        return maxId + 1;
    }

    // ================== INNER CLASSES FOR SHIFT OPERATIONS ==================
    
    // Inner class for assigning shifts
    public class ShiftAssigner {
        public boolean assignShift(int employeeId, String date, String session, String notes) {
            if (!staffExists(employeeId)) {
                System.out.println("Error: Employee ID " + employeeId + " not found!");
                return false;
            }

            // Convert session to uppercase for consistency
            String upperSession = session.toUpperCase();

            if (!isValidSession(upperSession)) {
                System.out.println("Error: Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
                return false;
            }

            List<Shift> shifts = loadShifts();

            // Check for duplicate shift assignment
            for (Shift shift : shifts) {
                if (shift.getEmployeeId() == employeeId &&
                        shift.getDate().equals(date) &&
                        shift.getSession().equals(upperSession) &&
                        !"CANCELLED".equals(shift.getStatus())) {
                    System.out.println("Error: Employee already assigned to " + upperSession + " session on " + date);
                    return false;
                }
            }

            // Generate new shift ID
            int newShiftId = generateShiftId(shifts);

            // Determine start and end times based on session
            String[] times = getSessionTimes(upperSession);
            String startTime = times[0];
            String endTime = times[1];

            Shift newShift = new Shift(newShiftId, employeeId, date, upperSession, startTime, endTime, "SCHEDULED", notes);
            shifts.add(newShift);
            saveShifts(shifts);

            StaffProfile staff = getUserInfo(employeeId);
            String employeeName = (staff != null) ? staff.getName() : "Unknown";

            System.out.println("Shift assigned successfully:");
            System.out.println("  Shift ID: " + newShiftId);
            System.out.println("  Employee: " + employeeName + " (ID: " + employeeId + ")");
            System.out.println("  Date: " + date);
            System.out.println("  Session: " + session + " (" + startTime + " - " + endTime + ")");
            if (notes != null && !notes.trim().isEmpty()) {
                System.out.println("  Notes: " + notes);
            }

            return true;
        }
    }
    
    // Inner class for deleting shifts
    public class ShiftDeleter {
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

            System.out.println("Shift deleted successfully:");
            System.out.println("  Shift ID: " + shiftId);
            System.out.println("  Employee: " + employeeName);
            System.out.println("  Date: " + targetShift.getDate());
            System.out.println("  Session: " + targetShift.getSession());

            return true;
        }
    }
    
    // Create instances of inner classes for operations
    private ShiftAssigner shiftAssigner = new ShiftAssigner();
    private ShiftDeleter shiftDeleter = new ShiftDeleter();

    // Assign shift to employee - delegates to inner class
    public boolean assignShift(int employeeId, String date, String session, String notes) {
        return shiftAssigner.assignShift(employeeId, date, session, notes);
    }

    // Delete shift - delegates to inner class  
    public boolean deleteShift(int shiftId) {
        return shiftDeleter.deleteShift(shiftId);
    }

    // Save shifts to file
    private void saveShifts(List<Shift> shifts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SHIFT_FILE))) {
            for (Shift shift : shifts) {
                writer.println(shift.getShiftId() + "," + shift.getEmployeeId() + "," +
                        shift.getDate() + "," + shift.getSession() + "," +
                        shift.getStartTime() + "," + shift.getEndTime() + "," +
                        shift.getStatus() + "," + shift.getNotes());
            }
        } catch (IOException e) {
            System.out.println("Error saving shifts: " + e.getMessage());
        }
    }

    // Helper methods
    protected boolean isValidSession(String session) {
        if (session == null) return false;
        String upperSession = session.toUpperCase();
        return MORNING_SESSION.equals(upperSession) || AFTERNOON_SESSION.equals(upperSession) || NIGHT_SESSION.equals(upperSession);
    }

    private String[] getSessionTimes(String session) {
        String upperSession = session.toUpperCase();
        switch (upperSession) {
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

    protected int getSessionOrder(String session) {
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
        int maxId = 3000;
        for (Shift shift : shifts) {
            if (shift.getShiftId() > maxId) {
                maxId = shift.getShiftId();
            }
        }
        return maxId + 1;
    }

    // ================== LOGIN PAGE FUNCTIONS ==================
    
    public void loginPage() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n==================== ADMINISTRATOR LOGIN ====================");
            System.out.println("1. Staff Management");
            System.out.println("2. Roster Preparation"); 
            System.out.println("3. Session Management");
            System.out.println("4. View Shift Schedule");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        staffManagementMenu(scanner);
                        break;
                    case 2:
                        rosterPreparationMenu(scanner);
                        break;
                    case 3:
                        sessionManagementMenu(scanner);
                        break;
                    case 4:
                        String todayDate = java.time.LocalDate.now().toString();
                        viewShiftSchedule(todayDate);
                        break;
                    case 5:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
    
    private void staffManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n================== STAFF MANAGEMENT ==================");
            System.out.println("1. Add Staff Profile");
            System.out.println("2. Edit Staff Profile");
            System.out.println("3. View Staff Profile");
            System.out.println("4. View All Staff Profiles");
            System.out.println("5. Delete Staff Profile");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        addStaffProfileMenu(scanner);
                        break;
                    case 2:
                        editStaffProfileMenu(scanner);
                        break;
                    case 3:
                        viewStaffProfileMenu(scanner);
                        break;
                    case 4:
                        viewAllStaffProfiles();
                        break;
                    case 5:
                        deleteStaffProfileMenu(scanner);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
    
    private void rosterPreparationMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n================== ROSTER PREPARATION ==================");
            System.out.println("1. Request Leave");
            System.out.println("2. Back to Main Menu");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        requestLeaveMenu(scanner);
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
    
    private void sessionManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n================== SESSION MANAGEMENT ==================");
            System.out.println("1. View Shift Schedule by Date");
            System.out.println("2. Delete Shift");
            System.out.println("3. Back to Main Menu");
            System.out.print("Select an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewShiftScheduleByDateMenu(scanner);
                        break;
                    case 2:
                        deleteShiftMenu(scanner);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
    
    // Menu helper methods
    private void addStaffProfileMenu(Scanner scanner) {
        System.out.print("Enter Staff ID: ");
        int staffId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Role: ");
        String role = scanner.nextLine();
        
        addStaffProfile(staffId, name, role);
    }
    
    public void editStaffProfileMenu(Scanner scanner) {
        System.out.print("Enter Staff ID to edit: ");
        int staffId = Integer.parseInt(scanner.nextLine());
        
        // Check if staff exists
        List<StaffProfile> profiles = loadStaffProfiles();
        StaffProfile targetStaff = null;
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                targetStaff = profile;
                break;
            }
        }
        
        if (targetStaff == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return;
        }
        
        // Show current staff information
        System.out.println("\n==================== CURRENT STAFF PROFILE ====================");
        System.out.println("Staff ID: " + targetStaff.getStaffId());
        System.out.println("Name: " + targetStaff.getName());
        System.out.println("Role: " + targetStaff.getRole());
        System.out.println("========================================================");
        
        // Loop for editing multiple fields
        while (true) {
            System.out.print("Enter Field to Edit (name/role): ");
            String field = scanner.nextLine();
            System.out.print("Enter New Value: ");
            String newValue = scanner.nextLine();
            
            boolean success = editStaffProfile(staffId, field, newValue);
            
            if (success) {
                System.out.print("Do you want to edit another field? (y/n): ");
                String continueEdit = scanner.nextLine().trim().toLowerCase();
                if (!continueEdit.equals("y") && !continueEdit.equals("yes")) {
                    break;
                }
            } else {
                System.out.print("Do you want to try editing another field? (y/n): ");
                String continueEdit = scanner.nextLine().trim().toLowerCase();
                if (!continueEdit.equals("y") && !continueEdit.equals("yes")) {
                    break;
                }
            }
        }
        
        System.out.println("Returning to Staff Management Menu...");
    }
    
    private void viewStaffProfileMenu(Scanner scanner) {
        System.out.print("Enter Staff ID to view: ");
        int staffId = Integer.parseInt(scanner.nextLine());
        viewStaffProfile(staffId);
    }
    
    private void deleteStaffProfileMenu(Scanner scanner) {
        System.out.print("Enter Staff ID to delete: ");
        int staffId = Integer.parseInt(scanner.nextLine());
        deleteStaffProfile(staffId);
    }
    
    private void requestLeaveMenu(Scanner scanner) {
        System.out.print("Enter Employee ID: ");
        int employeeId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
        System.out.print("Enter Reason: ");
        String reason = scanner.nextLine();
        
        requestLeave(employeeId, startDate, endDate, reason);
    }
    
    private void viewShiftScheduleByDateMenu(Scanner scanner) {
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        viewShiftSchedule(date);
    }
    
    private void deleteShiftMenu(Scanner scanner) {
        System.out.print("Enter Shift ID to delete: ");
        int shiftId = Integer.parseInt(scanner.nextLine());
        deleteShift(shiftId);
    }
    
    // ================== DUTY REQUEST MANAGEMENT ==================
    
    public void viewAllDutyRequests() {
        System.out.println("\n=============== All Duty Requests ===============");
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            boolean hasRequests = false;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        System.out.println("Employee ID: " + parts[0] + ", Date: " + parts[1] + 
                                         ", Session: " + parts[2] + ", Status: " + parts[3]);
                        hasRequests = true;
                    }
                }
            }
            if (!hasRequests) {
                System.out.println("No duty requests found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading duty requests: " + e.getMessage());
        }
    }
    
    public void approveDutyRequest(int employeeId, String session) {
        // For simplicity, we'll approve based on employee ID and session
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && 
                        parts[0].trim().equals(String.valueOf(employeeId)) && 
                        parts[2].trim().equalsIgnoreCase(session) &&
                        parts[3].trim().equals("PENDING")) {
                        // Approve this request
                        lines.add(parts[0] + "," + parts[1] + "," + parts[2] + ",APPROVED");
                        found = true;
                        
                        // Add to shift schedule
                        try (FileWriter shiftWriter = new FileWriter(SHIFT_FILE, true)) {
                            shiftWriter.write(parts[0] + "," + parts[1] + "," + parts[2] + ",Approved duty request\n");
                        }
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading duty requests: " + e.getMessage());
            return;
        }
        
        if (found) {
            // Write back to file
            try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE)) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error updating duty requests: " + e.getMessage());
            }
            System.out.println("Duty request approved for employee " + employeeId + " session " + session);
        } else {
            System.out.println("No pending duty request found for employee " + employeeId + " session " + session);
        }
    }
    
    public void removeDutyRequest(int employeeId, String session) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && 
                        parts[0].trim().equals(String.valueOf(employeeId)) && 
                        parts[2].trim().equalsIgnoreCase(session)) {
                        found = true;
                        // Skip this line (remove it)
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading duty requests: " + e.getMessage());
            return;
        }
        
        if (found) {
            try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE)) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error updating duty requests: " + e.getMessage());
            }
            System.out.println("Duty request removed for employee " + employeeId + " session " + session);
        } else {
            System.out.println("No duty request found for employee " + employeeId + " session " + session);
        }
    }
    
    // ================== IMPROVED DUTY REQUEST MANAGEMENT ==================
    
    public void viewPendingDutyRequestsWithCaseNumbers() {
        System.out.println("\n=============== Pending Duty Requests ===============");
        List<String> pendingRequests = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            int caseNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && parts[3].trim().equals("PENDING")) {
                        System.out.println("Case #" + caseNumber + " - Employee ID: " + parts[0] + 
                                         ", Date: " + parts[1] + ", Session: " + parts[2] + 
                                         ", Status: " + parts[3]);
                        pendingRequests.add(line);
                        caseNumber++;
                    }
                }
            }
            
            if (pendingRequests.isEmpty()) {
                System.out.println("No pending duty requests found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading duty requests: " + e.getMessage());
        }
    }
    
    public void approveDutyRequestByCaseNumber(int caseNumber) {
        List<String> allLines = new ArrayList<>();
        List<String> pendingRequests = new ArrayList<>();
        
        // Read all lines and identify pending requests
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && parts[3].trim().equals("PENDING")) {
                        pendingRequests.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading duty requests: " + e.getMessage());
            return;
        }
        
        // Check if case number is valid
        if (caseNumber < 1 || caseNumber > pendingRequests.size()) {
            System.out.println("Invalid case number! Please enter a number between 1 and " + pendingRequests.size());
            return;
        }
        
        // Get the specific request to approve
        String requestToApprove = pendingRequests.get(caseNumber - 1);
        String[] parts = requestToApprove.split(",");
        
        // Remove the request from all lines (don't keep approved requests)
        allLines.removeIf(line -> line.equals(requestToApprove));
        
        // Add to shift schedule
        try (FileWriter shiftWriter = new FileWriter(SHIFT_FILE, true)) {
            shiftWriter.write(parts[0] + "," + parts[1] + "," + parts[2] + ",Approved duty request\n");
        } catch (IOException e) {
            System.err.println("Error writing to shift file: " + e.getMessage());
        }
        
        // Write back to file
        try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE)) {
            for (String line : allLines) {
                if (!line.trim().isEmpty()) {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating duty requests: " + e.getMessage());
            return;
        }
        
        System.out.println("Duty request approved and assigned to shift successfully!");
        System.out.println("Employee ID: " + parts[0] + ", Date: " + parts[1] + 
                          ", Session: " + parts[2]);
    }
    
    public void rejectDutyRequestByCaseNumber(int caseNumber) {
        List<String> allLines = new ArrayList<>();
        List<String> pendingRequests = new ArrayList<>();
        
        // Read all lines and identify pending requests
        try (BufferedReader reader = new BufferedReader(new FileReader(DUTY_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && parts[3].trim().equals("PENDING")) {
                        pendingRequests.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading duty requests: " + e.getMessage());
            return;
        }
        
        // Check if case number is valid
        if (caseNumber < 1 || caseNumber > pendingRequests.size()) {
            System.out.println("Invalid case number! Please enter a number between 1 and " + pendingRequests.size());
            return;
        }
        
        // Get the specific request to reject and remove it
        String requestToReject = pendingRequests.get(caseNumber - 1);
        String[] parts = requestToReject.split(",");
        
        // Remove the request from all lines (don't keep rejected requests)
        allLines.removeIf(line -> line.equals(requestToReject));
        
        // Write back to file
        try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE)) {
            for (String line : allLines) {
                if (!line.trim().isEmpty()) {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating duty requests: " + e.getMessage());
            return;
        }
        
        System.out.println("Duty request rejected and removed successfully!");
        System.out.println("Employee ID: " + parts[0] + ", Date: " + parts[1] + 
                          ", Session: " + parts[2]);
    }
    
    // ================== LEAVE REQUEST MANAGEMENT ==================
    
    public void viewAllLeaveRequests() {
        System.out.println("\n=============== All Leave Requests ===============");
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            boolean hasRequests = false;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        System.out.println("Employee ID: " + parts[0] + ", From: " + parts[1] + 
                                         " To: " + parts[2] + ", Reason: " + parts[3] + ", Status: " + parts[4]);
                        hasRequests = true;
                    }
                }
            }
            if (!hasRequests) {
                System.out.println("No leave requests found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading leave requests: " + e.getMessage());
        }
    }
    
    public void approveLeaveRequest(int employeeId, String startDate) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && 
                        parts[0].trim().equals(String.valueOf(employeeId)) && 
                        parts[1].trim().equals(startDate) &&
                        parts[4].trim().equals("PENDING")) {
                        lines.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + ",APPROVED");
                        found = true;
                        
                        // Remove shifts for the leave period (simplified - just remove for start date)
                        removeShiftsForLeave(employeeId, parts[1], parts[2]);
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading leave requests: " + e.getMessage());
            return;
        }
        
        if (found) {
            try (FileWriter writer = new FileWriter(LEAVE_REQUEST_FILE)) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error updating leave requests: " + e.getMessage());
            }
            System.out.println("Leave request approved for employee " + employeeId + " starting " + startDate);
        } else {
            System.out.println("No pending leave request found for employee " + employeeId + " starting " + startDate);
        }
    }
    
    public void rejectLeaveRequest(int employeeId, String startDate) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && 
                        parts[0].trim().equals(String.valueOf(employeeId)) && 
                        parts[1].trim().equals(startDate) &&
                        parts[4].trim().equals("PENDING")) {
                        lines.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + ",REJECTED");
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading leave requests: " + e.getMessage());
            return;
        }
        
        if (found) {
            try (FileWriter writer = new FileWriter(LEAVE_REQUEST_FILE)) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error updating leave requests: " + e.getMessage());
            }
            System.out.println("Leave request rejected for employee " + employeeId + " starting " + startDate);
        } else {
            System.out.println("No pending leave request found for employee " + employeeId + " starting " + startDate);
        }
    }
    
    private void removeShiftsForLeave(int employeeId, String startDate, String endDate) {
        // Simplified implementation - just remove shifts for the employee
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && !parts[0].trim().equals(String.valueOf(employeeId))) {
                        lines.add(line);
                    }
                    // For simplicity, removing all shifts for this employee
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading shifts: " + e.getMessage());
            return;
        }
        
        try (FileWriter writer = new FileWriter(SHIFT_FILE)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error updating shifts: " + e.getMessage());
        }
    }
    
    // ================== SHIFT MANAGEMENT ==================
    
    // View all shift schedules without date filter
    public void viewAllShiftSchedules() {
        System.out.println("\n=============== All Shift Schedules ===============");
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFT_FILE))) {
            String line;
            boolean hasShifts = false;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 8) {
                        System.out.println("Shift ID: " + parts[0] + ", Staff ID: " + parts[1] + ", Date: " + parts[2] + 
                                         ", Session: " + parts[3] + ", Notes: " + parts[7]);
                        hasShifts = true;
                    }
                }
            }
            if (!hasShifts) {
                System.out.println("No shifts scheduled.");
            }
        } catch (IOException e) {
            System.err.println("Error reading shift schedule: " + e.getMessage());
        }
        System.out.println("==================================================");
    }
    
    public void viewShiftsBySession(String session, String date) {
        // Convert session to uppercase for consistent comparison
        String upperSession = session.toUpperCase();
        
        System.out.println("\n=============== Shifts for Session: " + upperSession + 
                         (date != null ? " on " + date : " (All Dates)") + " ===============");
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFT_FILE))) {
            String line;
            boolean hasShifts = false;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 8) {
                        String shiftSession = parts[3].trim();
                        String shiftDate = parts[2].trim();
                        
                        if (shiftSession.equals(upperSession) && (date == null || shiftDate.equals(date))) {
                            System.out.println("Shift ID: " + parts[0] + ", Staff ID: " + parts[1] + ", Date: " + parts[2] + 
                                             ", Session: " + parts[3] + ", Notes: " + parts[7]);
                            hasShifts = true;
                        }
                    }
                }
            }
            if (!hasShifts) {
                System.out.println("No shifts found for session " + upperSession + 
                                 (date != null ? " on " + date : ""));
            }
        } catch (IOException e) {
            System.err.println("Error reading shift schedule: " + e.getMessage());
        }
    }
    
    // ================== DATE VALIDATION ==================
    
    public boolean isValidDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }
        
        try {
            String[] parts = dateString.split("-");
            if (parts.length != 3) {
                return false;
            }
            
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            
            // Basic validation
            if (year < 2020 || year > 2030) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            
            // Days in month validation
            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            
            // Check for leap year
            if (month == 2 && isLeapYear(year)) {
                daysInMonth[1] = 29;
            }
            
            if (day > daysInMonth[month - 1]) {
                return false;
            }
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    public String getValidDateInput(Scanner scanner, String prompt) {
        String date;
        while (true) {
            System.out.print(prompt);
            date = scanner.nextLine().trim();
            if (isValidDate(date)) {
                return date;
            } else {
                System.out.println("Invalid date format! Please enter date in YYYY-MM-DD format (e.g., 2025-10-25)");
            }
        }
    }
    
    // ================== IMPROVED LEAVE REQUEST MANAGEMENT ==================
    
    public void viewPendingLeaveRequestsWithCaseNumbers() {
        System.out.println("\n=============== Pending Leave Requests ===============");
        List<String> pendingRequests = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            int caseNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && parts[4].trim().equals("PENDING")) {
                        System.out.println("Case #" + caseNumber + " - Employee ID: " + parts[0] + 
                                         ", From: " + parts[1] + " To: " + parts[2] + 
                                         ", Reason: " + parts[3] + ", Status: " + parts[4]);
                        pendingRequests.add(line);
                        caseNumber++;
                    }
                }
            }
            
            if (pendingRequests.isEmpty()) {
                System.out.println("No pending leave requests found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading leave requests: " + e.getMessage());
        }
    }
    
    public void approveLeaveRequestByCaseNumber(int caseNumber) {
        List<String> allLines = new ArrayList<>();
        List<String> pendingRequests = new ArrayList<>();
        
        // Read all lines and identify pending requests
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && parts[4].trim().equals("PENDING")) {
                        pendingRequests.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading leave requests: " + e.getMessage());
            return;
        }
        
        // Check if case number is valid
        if (caseNumber < 1 || caseNumber > pendingRequests.size()) {
            System.out.println("Invalid case number! Please enter a number between 1 and " + pendingRequests.size());
            return;
        }
        
        // Get the specific request to approve
        String requestToApprove = pendingRequests.get(caseNumber - 1);
        String[] parts = requestToApprove.split(",");
        
        // Remove the approved request from all lines (don't keep approved requests)
        allLines.removeIf(line -> line.equals(requestToApprove));
        
        // Write back to file
        try (FileWriter writer = new FileWriter(LEAVE_REQUEST_FILE)) {
            for (String line : allLines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error updating leave requests: " + e.getMessage());
            return;
        }
        
        // Remove shifts for the leave period
        int employeeId = Integer.parseInt(parts[0]);
        removeShiftsForLeave(employeeId, parts[1], parts[2]);
        
        System.out.println("Leave request approved successfully!");
        System.out.println("Employee ID: " + parts[0] + ", From: " + parts[1] + 
                          " To: " + parts[2] + ", Reason: " + parts[3]);
    }
    
    public void rejectLeaveRequestByCaseNumber(int caseNumber) {
        List<String> allLines = new ArrayList<>();
        List<String> pendingRequests = new ArrayList<>();
        
        // Read all lines and identify pending requests
        try (BufferedReader reader = new BufferedReader(new FileReader(LEAVE_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && parts[4].trim().equals("PENDING")) {
                        pendingRequests.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading leave requests: " + e.getMessage());
            return;
        }
        
        // Check if case number is valid
        if (caseNumber < 1 || caseNumber > pendingRequests.size()) {
            System.out.println("Invalid case number! Please enter a number between 1 and " + pendingRequests.size());
            return;
        }
        
        // Get the specific request to reject
        String requestToReject = pendingRequests.get(caseNumber - 1);
        String[] parts = requestToReject.split(",");
        
        // Remove the rejected request from all lines (don't keep rejected requests)
        allLines.removeIf(line -> line.equals(requestToReject));
        
        // Write back to file
        try (FileWriter writer = new FileWriter(LEAVE_REQUEST_FILE)) {
            for (String line : allLines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error updating leave requests: " + e.getMessage());
            return;
        }
        
        System.out.println("Leave request rejected successfully!");
        System.out.println("Employee ID: " + parts[0] + ", From: " + parts[1] + 
                          " To: " + parts[2] + ", Reason: " + parts[3]);
    }
}
