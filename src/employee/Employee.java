package employee;

public class Employee {
    private int employeeId;
    private String name;
    private String role; // e.g., "Manager", "Staff"
    private String employeeType; // e.g., "FullTime" or "PartTime"

    public Employee(int employeeId, String name, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.employeeType = "FullTime";
    }

    public int getEmployeeId() {
        return employeeId;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
}