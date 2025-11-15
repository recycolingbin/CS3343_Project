package staffRosteringSystem;

public class Shift {
    private final int shiftId;
    private final int employeeId;
    private final String date;
    private final ShiftSession session;   // <-- enum
    private final String startTime;
    private final String endTime;
    private final String notes;

    private Shift(int shiftId, int employeeId, String date, ShiftSession session,
                  String startTime, String endTime, String notes) {
                    this.shiftId = shiftId;
        this.employeeId = employeeId;
        this.date = date;
        this.session = session;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
    }
     // ----- static factory -------------------------------------------------
    public static Shift create(int shiftId, int employeeId, String date,
                               ShiftSession session, String notes) {
        return new Shift(shiftId, employeeId, date, session,
                         session.getStartTime(), session.getEndTime(), notes);
    }
    // ----- file format helpers -------------------------------------------
    public String toFileFormat() {
        return String.format("%d,%d,%s,%s,%s,%s,%s",
                shiftId, employeeId, date, session.name(),
                startTime, endTime, notes);
    }
    public static Shift fromFileFormat(String line) {
        String[] p = line.split(",", 7);
        int shiftId = Integer.parseInt(p[0].trim());
        int empId   = Integer.parseInt(p[1].trim());
        String date = p[2].trim();
        ShiftSession sess = ShiftSession.fromString(p[3].trim());
        return new Shift(shiftId, empId, date, sess, p[4].trim(), p[5].trim(), p[6].trim());
    }
      // ----- getters -------------------------------------------------------
    public int getShiftId()          { return shiftId; }
    public int getEmployeeId()       { return employeeId; }
    public String getDate()          { return date; }
    public ShiftSession getSession() { return session; }
    public String getStartTime()     { return startTime; }
    public String getEndTime()       { return endTime; }
    public String getNotes()         { return notes; }
}