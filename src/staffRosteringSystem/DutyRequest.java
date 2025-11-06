package staffRosteringSystem;

public class DutyRequest extends Request {
    private String dutyType;
    private String dutyDescription;
    private String Section;

    public DutyRequest(int employeeId, int requestId, String requestDate, String Section, String dutyType, String dutyDescription) {
        super(employeeId, requestId, requestDate);
        this.dutyType = dutyType;
        this.dutyDescription = dutyDescription;
        this.Section = Section;
    }

    public String getDutyType() { return dutyType; }
    public String getDutyDescription() { return dutyDescription; }
    public String getSection() { return Section; }
}
