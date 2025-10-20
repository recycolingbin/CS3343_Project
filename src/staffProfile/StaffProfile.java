package staffProfile;

public class StaffProfile {
    private int staffId;
    private String name;
    private String role;
    private String department;
    private double salary;

    // Full constructor (for backward compatibility)
    public StaffProfile(int staffId, String name, String role, String department, double salary) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.department = department;
        this.salary = salary;
    }

    // Simplified constructor (ID, name, role only)
    public StaffProfile(int staffId, String name, String role) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.department = "N/A";  // Default value
        this.salary = 0.0;        // Default value
    }

    // Getters
    public int getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
