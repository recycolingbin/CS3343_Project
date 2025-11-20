package staffRosteringSystem;

/**
 * Base class for all requests with encapsulated business logic
 */
public abstract class Request {
    private  int employeeId;
    private  int requestId;
    private  String requestDate;

    protected Request(int employeeId, int requestId, String requestDate) {
        this.employeeId = employeeId;
        this.requestId = requestId;
        this.requestDate = requestDate;
    }

    // Getters
    public int getEmployeeId() { return employeeId; }
    public int getRequestId() { return requestId; }
    public String getRequestDate() { return requestDate; }
    
    // For subclasses to implement
    public abstract String toFileFormat();


}
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (!(obj instanceof Request)) return false;
//        Request other = (Request) obj;
//        return this.requestId == other.requestId;
//    }

//    @Override
//    public int hashCode() {
//        return Integer.hashCode(requestId);
//    }