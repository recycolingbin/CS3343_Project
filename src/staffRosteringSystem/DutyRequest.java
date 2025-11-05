package staffRosteringSystem;

public class DutyRequest extends Request {
    private String dutyType;
    private String dutyDescription;

    public DutyRequest(int employeeId, int requestId, String requestDate, String dutyType, String dutyDescription) {
        super(employeeId, requestId, requestDate);
        this.dutyType = dutyType;
        this.dutyDescription = dutyDescription;
    }

    public String getDutyType() { return dutyType; }
    public String getDutyDescription() { return dutyDescription; }
}
