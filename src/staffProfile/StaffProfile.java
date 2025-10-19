package staffProfile;

public class StaffProfile {
    private int staffId;
    private String name;
    private String role; // e.g., "Manager", "Staff"
    private String employeeType; // e.g., "FullTime" or "PartTime"
    private double salary;

    public StaffProfile(int staffId, String name, String role, String employeeType, double salary) {
            this.staffId = staffId;
            this.name = name;
            this.role = role;
            this.employeeType = employeeType;
            this.salary = salary;
    }

    public int getEmployeeId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
    
	public String getEmployeeType() {
		return employeeType;
	}

    public double getSalary() {
        return salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

    public void setSalary(double salary) {
        this.salary = salary;
    }

}
