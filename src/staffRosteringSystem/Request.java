package staffRosteringSystem;

public abstract class Request {
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
