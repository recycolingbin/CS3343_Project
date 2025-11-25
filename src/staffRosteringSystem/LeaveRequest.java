package staffRosteringSystem;

/**
 * LeaveRequest with business logic and file format handling
 */
public class LeaveRequest extends Request {
    private String leaveType;
    private String reason;
    private String endDate;
    
    public LeaveRequest(int employeeId, int requestId, String startDate, String endDate, String leaveType, String reason) {
        super(employeeId, requestId, startDate);
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.reason = reason;
    }
    
    
    public String getLeaveType() { return leaveType; }
    public String getReason() { return reason; }
    public String getStartDate() { return getRequestDate(); } 
    public String getEndDate() { return endDate; }

    public int getLeaveDuration() {
            java.time.LocalDate start = java.time.LocalDate.parse(getStartDate());
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);
            return (int) java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
  
    }

    @Override
    public String toFileFormat() {
        // Format: employeeId|requestId|startDate|endDate|leaveType|reason
        return String.format("%d|%d|%s|%s|%s|%s",
            getEmployeeId(), getRequestId(), getRequestDate(), endDate, leaveType, reason);
    }

    /**
     * Factory method - parse from file line
     */
    public static LeaveRequest fromFileFormat(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|");
        

        
        // Handle new format (6 fields): employeeId|requestId|startDate|endDate|leaveType|reason
        if (parts.length >= 6) {

                int employeeId = Integer.parseInt(parts[0].trim());
                int requestId = Integer.parseInt(parts[1].trim());
                String startDate = parts[2].trim();
                String endDate = parts[3].trim();
                String leaveType = parts[4].trim();
                String reason = parts[5].trim();
                
                return new LeaveRequest(employeeId, requestId, startDate, endDate, 
                                       leaveType, reason);
           
        }
        
        System.out.println("Warning: Invalid leave request format, skipping line");
        return null;
    }
}