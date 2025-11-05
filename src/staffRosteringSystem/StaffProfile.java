package staffRosteringSystem;

public class StaffProfile {
    private int staffId;
    private String name;
    private String role;

    // Constructor (ID, name, role)
    public StaffProfile(int staffId, String name, String role) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
    }

    // Getters
    public int getStaffId() { return staffId; }
    public String getName() { return name; }
    public String getRole() { return role; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
}
