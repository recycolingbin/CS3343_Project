package staffRosteringSystem;

import java.io.*;
import java.util.*;

/**
 * ShiftManager handles all shift operations.
 * Responsibilities:
 * - Load/save shifts from/to file
 * - Assign and delete shifts
 * - View shift schedules
 * - Shift validation and generation
 */
public class ShiftManager {
    private static final String SHIFT_FILE = "Data/Shift.txt";
    private static final int INITIAL_SHIFT_ID = 3000;
    
    // Session constants
    protected static final String MORNING_SESSION = "MORNING";
    protected static final String AFTERNOON_SESSION = "AFTERNOON";
    protected static final String NIGHT_SESSION = "NIGHT";

    // Load shifts from file
    public List<Shift> loadShifts() {
        List<Shift> shifts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        int shiftId = Integer.parseInt(parts[0].trim());
                        int employeeId = Integer.parseInt(parts[1].trim());
                        String date = parts[2].trim();
                        String session = parts[3].trim();
                        String startTime = parts[4].trim();
                        String endTime = parts[5].trim();
                        String notes = parts[6].trim();
                        shifts.add(new Shift(shiftId, employeeId, date, session, startTime, endTime, notes));
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        } catch (NumberFormatException e) {
            System.out.println("Error parsing shift data: " + e.getMessage());
        }
        return shifts;
    }

    // Save shifts to file
    public void saveShifts(List<Shift> shifts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SHIFT_FILE))) {
            for (Shift shift : shifts) {
                writer.println(shift.getShiftId() + "," + shift.getEmployeeId() + "," +
                        shift.getDate() + "," + shift.getSession() + "," +
                        shift.getStartTime() + "," + shift.getEndTime() + "," +
                        shift.getNotes());
            }
        } catch (IOException e) {
            System.out.println("Error saving shifts: " + e.getMessage());
        }
    }

    // Assign shift to employee
    public boolean assignShift(int employeeId, String date, String session, String notes, StaffManager staffManager) {
        if (!staffManager.staffExists(employeeId)) {
            System.out.println("Error: Employee ID " + employeeId + " not found!");
            return false;
        }

        String upperSession = session.toUpperCase();
        if (!isValidSession(upperSession)) {
            System.out.println("Error: Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
            return false;
        }

        List<Shift> shifts = loadShifts();
        for (Shift shift : shifts) {
            if (shift.getEmployeeId() == employeeId && shift.getDate().equals(date) && shift.getSession().equals(upperSession)) {
                System.out.println("Error: Employee already assigned to " + upperSession + " session on " + date);
                return false;
            }
        }

        int newShiftId = generateShiftId(shifts);
        String[] times = getSessionTimes(upperSession);
        String startTime = times[0];
        String endTime = times[1];

        Shift newShift = new Shift(newShiftId, employeeId, date, upperSession, startTime, endTime, notes);
        shifts.add(newShift);
        saveShifts(shifts);

        StaffProfile staff = staffManager.getStaffInfo(employeeId);
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

    // Delete shift
    public boolean deleteShift(int shiftId, StaffManager staffManager) {
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

        StaffProfile staff = staffManager.getStaffInfo(targetShift.getEmployeeId());
        String employeeName = (staff != null) ? staff.getName() : "Unknown";

        System.out.println("Shift deleted successfully:");
        System.out.println("  Shift ID: " + shiftId);
        System.out.println("  Employee: " + employeeName);
        System.out.println("  Date: " + targetShift.getDate());
        System.out.println("  Session: " + targetShift.getSession());

        return true;
    }

    // View all shift schedules
    public void viewAllShiftSchedules() {
        List<Shift> shifts = loadShifts();
        if (shifts.isEmpty()) {
            System.out.println("No shifts scheduled.");
            return;
        }

        System.out.println("\n=============== All Shift Schedules ===============");
        System.out.printf("%-8s %-10s %-12s %-15s %-20s%n", "Shift ID", "Staff ID", "Date", "Session", "Notes");
        System.out.println("================================================================");
        
        for (Shift shift : shifts) {
            String truncatedNotes = shift.getNotes().length() > 20 ? 
                                   shift.getNotes().substring(0, 17) + "..." : shift.getNotes();
            
            System.out.printf("%-8d %-10d %-12s %-15s %-20s%n", 
                            shift.getShiftId(), shift.getEmployeeId(), shift.getDate(), 
                            shift.getSession(), truncatedNotes);
        }
        System.out.println("================================================================");
    }

    // Remove shifts for a specific employee and date range
    public void removeShiftsForLeave(int employeeId) {
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
        int maxId = INITIAL_SHIFT_ID;
        for (Shift shift : shifts) {
            if (shift.getShiftId() > maxId) {
                maxId = shift.getShiftId();
            }
        }
        return maxId + 1;
    }
}
