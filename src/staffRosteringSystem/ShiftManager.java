package staffRosteringSystem;
import java.util.*;
public class ShiftManager {
    private static final String SHIFT_FILE = "Data/Shift.txt";
    private static final int INITIAL_SHIFT_ID = 3000;

    private final FileOperations fileOps;
    private final StaffManager staffManager;
	private String shiftFilePath;
	private List<Shift> shifts;

    public ShiftManager(String shiftFilePath, StaffManager staffManager, FileOperations fileOps) {
        this.shiftFilePath = shiftFilePath;
        this.staffManager = Objects.requireNonNull(staffManager, "StaffManager cannot be null");
        this.fileOps = fileOps;
        this.shifts = loadShifts();
    }

    public ShiftManager(StaffManager staffManager) {
        this(SHIFT_FILE, staffManager, new FileOperations());
    }
    public ShiftManager() {
    	this(SHIFT_FILE, new StaffManager(), new FileOperations());
	}

	public List<Shift> loadShifts() {
        return fileOps.loadData(shiftFilePath, Shift::fromFileFormat); 
    }

    public boolean saveShifts(List<Shift> shifts) {
        if (shifts == null) return false;
        return fileOps.saveData(shiftFilePath, shifts, Shift::toFileFormat);
    }

    // Assign shift with full console feedback
    public boolean assignShift(int employeeId, String date, String sessionInput, String notes) {
        if (!staffManager.staffExists(employeeId)) {
            System.out.println("Error: Employee ID " + employeeId + " not found!");
            return false;
        }

        ShiftSession session = ShiftSession.fromString(sessionInput);
        if (session == null) {
            System.out.println("Error: Invalid session! Valid: MORNING, AFTERNOON, NIGHT");
            return false;
        }

        boolean conflict = shifts.stream().anyMatch(s ->
                s.getEmployeeId() == employeeId &&
                s.getDate().equals(date) &&
                s.getSession() == session);

        if (conflict) {
            System.out.println("Error: Employee already assigned to " + session + " on " + date);
            return false;
        }
        int newShiftId = generateShiftId(shifts);
        Shift newShift = Shift.create(newShiftId, employeeId, date, session, notes);
        shifts.add(newShift);
        boolean saved = saveShifts(shifts);  // Add this line
        StaffProfile staff = staffManager.getStaffInfo(employeeId);
        String name = staff.getName();

        if (saved) {
            System.out.println("Shift assigned successfully:");
            System.out.println("  Shift ID : " + newShiftId);
            System.out.println("  Employee : " + name + " (ID: " + employeeId + ")");
            System.out.println("  Date     : " + date);
            System.out.println("  Session  : " + session + " (" + session.getStartTime() + " - " + session.getEndTime() + ")");
            if (!notes.isEmpty()) {
                System.out.println("  Notes    : " + notes);
            }
        } else {
            System.out.println("Failed to save shift.");
        }
        return saved;
    }
    // Delete shift with feedback
    public boolean deleteShift(int shiftId) {
        List<Shift> shifts = loadShifts();
        Shift target = shifts.stream()
                .filter(s -> s.getShiftId() == shiftId)
                .findFirst()
                .orElse(null);

        if (target == null) {
            System.out.println("Error: Shift ID " + shiftId + " not found!");
            return false;
        }
        shifts.remove(target);
        boolean saved = saveShifts(shifts);
        StaffProfile staff = staffManager.getStaffInfo(target.getEmployeeId());
        String name = staff != null ? staff.getName() : "ID:" + target.getEmployeeId();

//        if (saved) {
            System.out.println("Shift deleted successfully:");
            System.out.println("  Shift ID : " + shiftId);
            System.out.println("  Employee : " + name);
            System.out.println("  Date     : " + target.getDate());
            System.out.println("  Session  : " + target.getSession());
//        } else {
//            System.out.println("Failed to delete shift.");
//        }
        return saved;
    }

    // View all shifts with nice table
    public void viewAllShiftSchedules() {
        List<Shift> shifts = loadShifts();
        if (shifts.isEmpty()) {
            System.out.println("No shifts scheduled.");
            return;
        }
        System.out.println("\n=============== All Shift Schedules ==========================");
        System.out.printf("%-8s %-12s %-12s %-15s %-20s%n",
                "Shift ID", "Staff ID", "Name", "Date", "Session");
        System.out.println("================================================================");
        for (Shift s : shifts) {
            StaffProfile staff = staffManager.getStaffInfo(s.getEmployeeId());
            String name = staff != null ? staff.getName() : "ID:" + s.getEmployeeId();
            String truncatedNotes = s.getNotes().length() > 20
                    ? s.getNotes().substring(0, 17) + "..."
                    : s.getNotes();

            System.out.printf("%-8d %-12d %-12s %-12s %-15s %-20s%n",
                    s.getShiftId(),
                    s.getEmployeeId(),
                    name.length() > 10 ? name.substring(0, 10) + "." : name,
                    s.getDate(),
                    s.getSession() + " (" + s.getStartTime() + "-" + s.getEndTime() + ")",
                    truncatedNotes);
        }
        System.out.println("================================================================");
    }
    //update
    int generateShiftId(List<Shift> shifts) {return shifts.stream()
                .mapToInt(Shift::getShiftId)
                .max()
                .orElse(INITIAL_SHIFT_ID - 1) + 1;
    }
}