package staffRosteringSystem;

/**
 * DutyRequest with business logic and file format handling
 */
public class DutyRequest extends Request {
    private String section;

    public DutyRequest(int employeeId, int requestId, String requestDate, String section) {
        super(employeeId, requestId, requestDate);
        this.section = section;
    }

    public String getSection() { return section; }
    
    @Override
    public String toFileFormat() {
        // Format: requestId,employeeId,requestDate,section
        return String.format("%d,%d,%s,%s",
            getRequestId(), getEmployeeId(), getRequestDate(), section);
    }

    /**
     * Factory method - parse from file line
     */
    public static DutyRequest fromFileFormat(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length < 4) {
            System.out.println("Warning: Invalid duty request format, skipping line");
            return null;
        }

            int requestId = Integer.parseInt(parts[0].trim());
            int employeeId = Integer.parseInt(parts[1].trim());
            String requestDate = parts[2].trim();
            String section = parts[3].trim();
            
            return new DutyRequest(employeeId, requestId, requestDate, section);
  
    }

//    @Override
//    public String toString() {
//        return String.format("DutyRequest[ID=%d, Employee=%d, Section=%s, Date=%s]",
//            getRequestId(), getEmployeeId(), section, getRequestDate());
//    }


}