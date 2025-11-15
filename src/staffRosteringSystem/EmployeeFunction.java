package staffRosteringSystem;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * EmployeeFunction - Employee menu and operations
 * Delegates request management to RequestManager
 * Focuses on UI/UX and input validation
 */
public class EmployeeFunction extends BaseFunction {
    private static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";
    private static final String DUTY_REQUEST_FILE = "Data/Duty_Request.txt";
    
    private final RequestManager requestManager;

    public EmployeeFunction(int userId, String username, String password, 
                          ShiftManager shiftManager, StaffManager staffManager) {
        super(userId, username, password, shiftManager, staffManager);
        this.requestManager = new RequestManager(staffManager);
    }
    
    public EmployeeFunction() {
        this(0, "", "", new ShiftManager(), new StaffManager());
    }


    // ==================== Login ====================

    public boolean login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && isEmployeeMatch(parts, username)) {
                    System.out.println("Employee login successful.");
                    loginPage(parts[0].trim());
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading staff profiles: " + e.getMessage());
        }
        System.out.println("Invalid. Please try again :(");
        return false;
    }

    private boolean isEmployeeMatch(String[] parts, String username) {
        String staffName = parts[1].trim();
        String role = parts[2].trim();
        return staffName.equals(username) && role.equals("Employee");
    }

    // ==================== Menu Navigation ====================

    public void loginPage(String userid) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try {
                displayMenu();
                int action = scanner.nextInt();
                scanner.nextLine();

                if (!handleMenuAction(action, userid)) {
                    return; // Logout
                }
                
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 4.");
                scanner.nextLine();
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=============== Employee Menu ===============");
        System.out.println("1. View Shift Schedule");
        System.out.println("2. Request Duty");
        System.out.println("3. Request Leave");
        System.out.println("4. Logout");
        System.out.print("Please select an option (1-4): ");
    }

    private boolean handleMenuAction(int action, String userid) {
        switch (action) {
            case 1:
                viewShiftSchedule(LocalDate.now().toString());
                return true;
            case 2:
                requestDuty(userid);
                return true;
            case 3:
                requestLeave(userid);
                return true;
            case 4:
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
                return true;
        }
    }

    // ==================== Duty Request ====================

    public void requestDuty(String userid) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            String date = getValidDateInput(scanner, 
                "Please enter the date you want to request a duty (YYYY-MM-DD): ");
            if (date == null) return;

            String session = getValidSession(scanner);
            if (session == null) return;
            
            if (addDuty(userid, date, session)) {
                System.out.println("Duty request submitted successfully.");
            }
            
        } catch (Exception e) {
            System.err.println("Error processing duty request: " + e.getMessage());
        }
    }

    private String getValidSession(Scanner scanner) {
        System.out.print("Please enter the session (MORNING/AFTERNOON/NIGHT): ");
        String session = scanner.nextLine().trim().toUpperCase();

        if (!isValidSession(session)) {
            System.out.println("Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
            return null;
        }
        return session;
    }

    public boolean addDuty(String userid, String date, String session) throws FileNotFoundException {
        if (hasDuplicateDuty(userid, date, session)) {
            System.out.println("You already have duty on " + date + " at section " + session + 
                             ". Cannot request another duty.");
            return false;
        }
        
        return writeDutyToFile(userid, date, session);
    }

    private boolean hasDuplicateDuty(String userid, String date, String session) 
            throws FileNotFoundException {
        File duty = new File(DUTY_REQUEST_FILE);
        
        try (Scanner fileScanner = new Scanner(duty)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] info = line.split(",");
                if (isDuplicateDutyEntry(info, userid, date, session)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDuplicateDutyEntry(String[] info, String userid, String date, String session) {
        return info.length >= 4 && 
               info[1].trim().equals(userid) && 
               info[2].trim().equals(date) && 
               info[3].trim().equals(session);
    }

    private boolean writeDutyToFile(String userid, String date, String session) {
        int newRequestId = generateDutyRequestId();
        
        try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE, true)) {
            writer.write(String.format("%d,%s,%s,%s%n", newRequestId, userid, date, session));
            return true;
        } catch (IOException e) {
            System.err.println("Error writing duty request: " + e.getMessage());
            return false;
        }
    }

    private int generateDutyRequestId() {
        try {
            List<String> lines = java.nio.file.Files.readAllLines(
                new File(DUTY_REQUEST_FILE).toPath());
            return lines.size() + 1;
        } catch (IOException e) {
            return 1; // File doesn't exist or is empty
        }
    }

    // ==================== Leave Request ====================

    public void requestLeave(String userid) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            String startDate = getValidDateInput(scanner, 
                "Please enter the start date for leave (YYYY-MM-DD): ");
            if (startDate == null) return;
            
            String endDate = getValidEndDate(scanner, startDate);
            if (endDate == null) return;
            
            if (!checkDuty(userid, startDate, endDate)) {
                return;
            }
            
            String leaveType = getNonEmptyInput(scanner, 
                "Please enter the type of leave (e.g., Sick, Annual, Emergency): ");
            String reason = getNonEmptyInput(scanner, 
                "Please enter the reason for leave: ");
            
            saveLeaveRequest(userid, startDate, endDate, leaveType, reason);
            
        } catch (Exception e) {
            System.err.println("Error processing leave request: " + e.getMessage());
        }
    }

    private String getValidEndDate(Scanner scanner, String startDate) {
        String endDate = getValidDateInput(scanner, 
            "Please enter the end date for leave (YYYY-MM-DD): ");
        if (endDate == null) return null;
        
        while (!isDateAfterOrEqual(endDate, startDate)) {
            System.out.println("Error: End date cannot be earlier than start date! Please try again.");
            endDate = getValidDateInput(scanner, 
                "Please enter the end date for leave (YYYY-MM-DD): ");
            if (endDate == null) return null;
        }
        
        return endDate;
    }

    private String getNonEmptyInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        
        while (input.isEmpty()) {
            System.out.println("Input cannot be empty!");
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        
        return input;
    }

    public boolean checkDuty(String userid, String startDate, String endDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        
        Date sd = dateFormat.parse(startDate);
        Date ed = dateFormat.parse(endDate);
        
        try (Scanner fileScanner = new Scanner(new File(DUTY_REQUEST_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] info = line.split(",");
                
                if (hasDutyInRange(info, userid, sd, ed, dateFormat)) {
                    return true;
                }
            }
            
            System.out.println("You do not have a duty during " + startDate + 
                             " to " + endDate + ". Cannot request leave.");
            return false;
            
        } catch (FileNotFoundException e) {
            System.out.println("No duty requests found. Cannot request leave.");
            return false;
        }
    }

    private boolean hasDutyInRange(String[] info, String userid, Date sd, Date ed, 
                                  SimpleDateFormat dateFormat) throws Exception {
        if (info.length < 4 || !info[1].trim().equals(userid)) {
            return false;
        }
        
        Date dutyDate = dateFormat.parse(info[2].trim());
        return isDutyInDateRange(dutyDate, sd, ed);
    }

    private boolean isDutyInDateRange(Date dutyDate, Date startDate, Date endDate) {
        return (dutyDate.after(startDate) && dutyDate.before(endDate)) || 
               dutyDate.equals(startDate) || 
               dutyDate.equals(endDate);
    }

    private void saveLeaveRequest(String userid, String startDate, String endDate,
                                 String leaveType, String reason) {
        int newRequestId = generateLeaveRequestId();
        
        try (FileWriter writer = new FileWriter("Data/Leave_Request.txt", true)) {
            String record = String.format("%s|%d|%s|%s|%s|%s%n", 
                userid, newRequestId, startDate, endDate, leaveType, reason);
            writer.write(record);
            System.out.println("Leave request submitted successfully.");
        } catch (IOException e) {
            System.err.println("Error writing leave request: " + e.getMessage());
        }
    }

    private int generateLeaveRequestId() {
        try {
            List<String> lines = java.nio.file.Files.readAllLines(
                new File("Data/Leave_Request.txt").toPath());
            return lines.size() + 1;
        } catch (IOException e) {
            return 1;
        }
    }

    // ==================== Utility Methods ====================

    private boolean isDateAfterOrEqual(String date1, String date2) {
        try {
            LocalDate d1 = LocalDate.parse(date1);
            LocalDate d2 = LocalDate.parse(date2);
            return !d1.isBefore(d2);
        } catch (Exception e) {
            return false;
        }
    }
}
//package staffRosteringSystem;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeParseException;
//import java.util.*;
//
///**
// * EmployeeFunction - Employee menu and operations
// * NO DUPLICATION VERSION - delegates to RequestManager and BaseFunction
// */
//
//public class EmployeeFunction extends BaseFunction {
//    private static final String DUTY_REQUEST_FILE = "Data/Duty_Request.txt";
//    private static final String LEAVE_REQUEST_FILE = "Data/Leave_Request.txt";
//    private static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";
//
//    public EmployeeFunction(int userId, String username, String password, ShiftManager shiftManager,StaffManager staffManager) {
//        super(userId, username, password, shiftManager,staffManager);
//    }
//    
//    public boolean login(String username, String password) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 3) {
//                    String staffName = parts[1].trim();
//                    String role = parts[2].trim();
//                    if (staffName.equals(username) && role.equals("Employee")) {
//                        System.out.println("Employee login successful.");
//                        loginPage(parts[0].trim());
//                        return true;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading staff profiles: " + e.getMessage());
//        }
//        System.out.println("Invalid. Please try again :(");
//        return false;
//    }
//    
//    public void loginPage(String userid) {
//        Scanner scanner = new Scanner(System.in);
//        
//        while (true) {
//            try {
//                System.out.println("\n=============== Employee Menu ===============");
//                System.out.println("1. View Shift Schedule");
//                System.out.println("2. Request Duty");
//                System.out.println("3. Request Leave");
//                System.out.println("4. Logout");
//                System.out.print("Please select an option (1-4): ");
//
//                int action = scanner.nextInt();
//                scanner.nextLine();
//
//                switch (action) {
//                    case 1:
//                        String todayDate = java.time.LocalDate.now().toString();
//                        viewShiftSchedule(todayDate);
//                        break;
//                    case 2:
//                        requestDuty(userid);
//                        break;
//                    case 3:
//                        requestLeave(userid);
//                        break;
//                    case 4:
//                        System.out.println("Logging out...");
//                        return;
//                    default:
//                        System.out.println("Invalid choice. Please try again.");
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input! Please enter a number between 1 and 4.");
//                scanner.nextLine();
//            } 
//        }
//    }
//    
//    public boolean addDuty(String userid, String date, String session) throws FileNotFoundException{
//        File duty = new File(DUTY_REQUEST_FILE);
//        
//        // Check for existing duty on same date/session
//        try (Scanner fileScanner = new Scanner(duty)) {
//            while (fileScanner.hasNextLine()) {
//                String line = fileScanner.nextLine();
//                String[] info = line.split(",");
//                if (info.length >= 4 && 
//                    info[1].trim().equals(userid) && 
//                    info[2].trim().equals(date) && 
//                    info[3].trim().equals(session)) {
//                    System.out.println("You already have duty on " + date + " at section " + session + ". Cannot request another duty.");
//                    return false;
//                }
//            }
//        
//        }
//        
//        // Generate new request ID
//        int newRequestId = 1;
//        try {
//            List<String> lines = Files.readAllLines(duty.toPath());
//            newRequestId = lines.size() + 1;
//        } catch (IOException e) {
//            // File doesn't exist or is empty, use ID 1
//        }
//        
//        try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE, true)) {
//            writer.write(newRequestId + "," + userid + "," + date + "," + session + "\n");
//            return true;
//        } catch (IOException e) {
//            System.err.println("Error writing duty request: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public void requestDuty(String userid) {
//        Scanner scanner = new Scanner(System.in);
//        
//        try {
//            String date = getValidDateInput(scanner, "Please enter the date you want to request a duty (YYYY-MM-DD): ");
//            if (date == null) return;
//
//            System.out.print("Please enter the session (MORNING/AFTERNOON/NIGHT): ");
//            String session = scanner.nextLine().trim().toUpperCase();
//
//            if (!isValidSession(session)) {
//                System.out.println("Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
//                return;
//            }
//            
//            if (addDuty(userid, date, session)) {
//                System.out.println("Duty request submitted successfully.");
//            }
//            
//        } catch (Exception e) {
//            System.err.println("Error processing duty request: " + e.getMessage());
//        }
//    }
//
//    public boolean checkDuty(String userid, String startDate, String endDate) throws Exception {
//        File duty = new File(DUTY_REQUEST_FILE);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        simpleDateFormat.setLenient(false);
//        
//        Date sd = simpleDateFormat.parse(startDate);
//        Date ed = simpleDateFormat.parse(endDate);
//        
//        try (Scanner fileScanner = new Scanner(duty)) {
//            boolean found = false;
//            while (fileScanner.hasNextLine()) {
//                String line = fileScanner.nextLine();
//                String[] info = line.split(",");
//                if (info.length >= 4) {
//                    if (info[1].trim().equals(userid)) {
//                        Date dutyDate = simpleDateFormat.parse(info[2].trim());
//                        if ((dutyDate.after(sd) && dutyDate.before(ed)) || 
//                            dutyDate.equals(sd) || 
//                            dutyDate.equals(ed)) {
//                            found = true;
//                            break;
//                        }
//                    }
//                }
//            }
//            if (!found) {
//                System.out.println("You do not have a duty during " + startDate + " to " + endDate + ". Cannot request leave.");
//                return false;
//            }
//            return true;
//
//        } catch (FileNotFoundException e) {
//            System.out.println("No duty requests found. Cannot request leave.");
//            return false;
//        }
//           
//    }
//    
//    public void requestLeave(String userid) {
//        Scanner scanner = new Scanner(System.in);
//        
//        try {
//            String startDate = getValidDateInput(scanner, "Please enter the start date for leave (YYYY-MM-DD): ");
//            if (startDate == null) return;
//            
//            String endDate = getValidDateInput(scanner, "Please enter the end date for leave (YYYY-MM-DD): ");
//            if (endDate == null) return;
//            
//            while (!isDateAfterOrEqual(endDate, startDate)) {
//                System.out.println("Error: End date cannot be earlier than start date! Please try again.");
//                endDate = getValidDateInput(scanner, "Please enter the end date for leave (YYYY-MM-DD): ");
//                if (endDate == null) return;
//            }
//            
//            if (checkDuty(userid, startDate, endDate)) {
//                System.out.print("Please enter the type of leave (e.g., Sick, Annual, Emergency): ");
//                String leaveType = scanner.nextLine().trim();
//                
//                while (leaveType.isEmpty()) {
//                    System.out.println("Leave type cannot be empty!");
//                    System.out.print("Please enter the type of leave: ");
//                    leaveType = scanner.nextLine().trim();
//                }
//                
//                String reason = "";
//                while (reason.isEmpty()) {
//                    System.out.print("Please enter the reason for leave: ");
//                    reason = scanner.nextLine().trim();
//                    
//                    if (reason.isEmpty()) {
//                        System.out.println("Reason cannot be empty!");
//                    }
//                }
//                
//                // Generate request ID
//                int newRequestId = 1;
//                File leaveFile = new File(LEAVE_REQUEST_FILE);
//                try {
//                    List<String> lines = Files.readAllLines(leaveFile.toPath());
//                    newRequestId = lines.size() + 1;
//                } catch (IOException e) {
//                    // File doesn't exist or is empty, use ID 1
//                }
//
//                try (FileWriter writer = new FileWriter(LEAVE_REQUEST_FILE, true)) {
//                    writer.write(userid + "|" + newRequestId + "|" + startDate + "|" + leaveType + "|" + reason + "\n");
//                    System.out.println("Leave request submitted successfully.");
//                } catch (IOException e) {
//                    System.err.println("Error writing leave request: " + e.getMessage());
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error processing leave request: " + e.getMessage());
//        }
//    }
//    
//    
//    private boolean isDateAfterOrEqual(String date1, String date2) {
//        try {
//            String[] parts1 = date1.split("-");
//            String[] parts2 = date2.split("-");
//            
//            int year1 = Integer.parseInt(parts1[0]);
//            int month1 = Integer.parseInt(parts1[1]);
//            int day1 = Integer.parseInt(parts1[2]);
//            
//            int year2 = Integer.parseInt(parts2[0]);
//            int month2 = Integer.parseInt(parts2[1]);
//            int day2 = Integer.parseInt(parts2[2]);
//            
//            if (year1 > year2) return true;
//            if (year1 < year2) return false;
//            
//            if (month1 > month2) return true;
//            if (month1 < month2) return false;
//            
//            return day1 >= day2;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//} 