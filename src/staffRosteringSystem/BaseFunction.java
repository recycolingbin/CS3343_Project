package staffRosteringSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public abstract class BaseFunction {
    protected int userId;
    protected String username;
    protected String password;
    
    protected ShiftManager shiftManager;
    protected StaffManager staffManager;
    
    protected static final String SHIFT_FILE = "Data/Shift.txt";
    protected static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";
    
    public static final String MORNING_SESSION = "MORNING";
    public static final String AFTERNOON_SESSION = "AFTERNOON";
    public static final String NIGHT_SESSION = "NIGHT";

    public BaseFunction(int userId, String username, String password, ShiftManager shiftManager, StaffManager staffManager) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.shiftManager = shiftManager;
        this.staffManager = staffManager;
    }
    
    protected BaseFunction(int userId, String username, String password) {
        this(userId, username, password, new ShiftManager(), new StaffManager());
    }
    
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
//    public void viewFunction() {
//        System.out.println("Viewing available functions for user: " + username);
//    }
    
//    public void loginPage(String userId) {
//        System.out.println("Login page for user: " + userId);
//    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }

    // Load shifts from Shift.txt
    /*
    protected static List<Shift> loadShifts() {
        List<Shift> shifts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        int shiftId = Integer.parseInt(parts[0].trim());
                        int employeeId = Integer.parseInt(parts[1].trim());
                        String date = parts[2].trim();
                        ShiftSession session = ShiftSession.fromString(parts[3].trim());
                        String startTime = parts[4].trim();
                        String endTime = parts[5].trim();
                        String notes = parts[6].trim();
                        shifts.add(Shift.create(shiftId, employeeId, date, session, notes));
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
    */

    public StaffProfile getUserInfo(int staffId) {
    	return staffManager.getStaffInfo(staffId);

    }
    
    // View shift schedule for a specific date
    public void viewShiftSchedule(String date) {
        List<Shift> shifts = shiftManager.loadShifts();
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
        System.out.printf("%-8s %-12s %-20s %-10s %-15s%n",
                "Shift ID", "Session", "Employee", "Time", "Notes");
        System.out.println("------------------------------------------------------------------------");

        for (Shift shift : dayShifts) {
            StaffProfile staff = getUserInfo(shift.getEmployeeId());
            String employeeName = (staff != null) ? staff.getName() : "Unknown";
            String timeRange = shift.getStartTime() + "-" + shift.getEndTime();

            System.out.printf("%-8d %-12s %-20s %-10s %-15s%n",
                    shift.getShiftId(), shift.getSession(), employeeName,
                    timeRange, shift.getNotes());
        }
        System.out.println("========================================================================");
    }

    // View shifts by session
    /*public void viewShiftsBySession(String session, String date) {
        if (!isValidSession(session)) {
            System.out.println("Error: Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
            return;
        }

        List<Shift> shifts = shiftManager.loadShifts();
        List<Shift> sessionShifts = new ArrayList<>();

        for (Shift shift : shifts) {
        	ShiftSession sessionEnum = ShiftSession.fromString(session);
            if (sessionEnum != null && shift.getSession() == sessionEnum && (date == null || shift.getDate().equals(date))) {
                sessionShifts.add(shift);
            }
        }

        if (sessionShifts.isEmpty()) {
            String dateStr = (date != null) ? " on " + date : "";
            System.out.println("No " + session.toUpperCase() + " shifts found" + dateStr);
            return;
        }

        String title = session.toUpperCase() + " SHIFTS" + (date != null ? " - " + date : "");
        System.out.println("==================== " + title + " ====================");
        System.out.printf("%-8s %-12s %-20s %-10s %-15s%n",
                "Shift ID", "Date", "Employee", "Time", "Notes");
        System.out.println("------------------------------------------------------------");

        for (Shift shift : sessionShifts) {
            StaffProfile staff = getUserInfo(shift.getEmployeeId());
            String employeeName = (staff != null) ? staff.getName() : "Unknown";
            String timeRange = shift.getStartTime() + "-" + shift.getEndTime();

            System.out.printf("%-8d %-12s %-20s %-10s %-15s%n",
                    shift.getShiftId(), shift.getDate(), employeeName,
                    timeRange, shift.getNotes());
        }
        System.out.println("============================================================");
    }
    */
    // View personal roster for employees
    /*
    public void viewMyRoster() {
        List<Shift> shifts = shiftManager.loadShifts();
        List<Shift> myShifts = new ArrayList<>();

        for (Shift shift : shifts) {
            if (shift.getEmployeeId() == userId) {
                myShifts.add(shift);
            }
        }

        if (myShifts.isEmpty()) {
            System.out.println("No shifts assigned to you yet.");
            return;
        }

        // Sort by date then session order
        myShifts.sort((s1, s2) -> {
            int dateCompare = s1.getDate().compareTo(s2.getDate());
            if (dateCompare != 0) return dateCompare;
            return Integer.compare(getSessionOrder(s1.getSession()), getSessionOrder(s2.getSession()));
        });

        System.out.println("\n========== MY SHIFT SCHEDULE ==========");
        System.out.printf("%-12s %-10s %-12s%n", "Date", "Session", "Time");
        System.out.println("----------------------------------------");
        
        for (Shift shift : myShifts) {
            String timeRange = shift.getStartTime() + "-" + shift.getEndTime();
            System.out.printf("%-12s %-10s %-12s%n",
                    shift.getDate(),
                    shift.getSession(),
                    timeRange);
        }
        System.out.println("========================================");
    }*/

    public boolean isValidSession(String session) {
        return ShiftSession.fromString(session) != null;
    }

    public int getSessionOrder(ShiftSession session) {
        switch (session) {
            case MORNING:
                return 1;
            case AFTERNOON:
                return 2;
            case NIGHT:
                return 3;
            default:
                return 4;
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
        int attempts = 0;
        final int MAX_ATTEMPTS = 10; // Prevent infinite loops
        
        while (attempts < MAX_ATTEMPTS) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                System.out.println("Input stream ended. Returning to menu.");
                return null;
            }
            
            date = scanner.nextLine().trim();
            if (isValidDate(date)) {
                return date;
            } else {
                System.out.println("Invalid date format! Please enter date in YYYY-MM-DD format (e.g., 2025-10-25)");
                attempts++;
            }
        }
        
        System.out.println("Too many invalid attempts. Returning to menu.");
        return null;
    }

}
