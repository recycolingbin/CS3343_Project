package staffRosteringSystem;

public class LeaveRequest extends Request {
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
