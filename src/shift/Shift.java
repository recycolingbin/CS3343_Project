package shift;

public class Shift {
    private int shiftId;
    private int employeeId;
    private String date;
    private String session; // MORNING, AFTERNOON, NIGHT
    private String startTime;
    private String endTime;
    private String notes;

    public Shift(int shiftId, int employeeId, String date, String session,
            String startTime, String endTime, String notes) {
        this.shiftId = shiftId;
        this.employeeId = employeeId;
        this.date = date;
        this.session = session;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
