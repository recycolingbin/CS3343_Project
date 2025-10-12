package employeeFunction;

import java.util.HashMap;
import java.util.Map;

public class Administrator extends BaseUser {
    private Map<Integer, StaffProfile> staffDatabase;

    public Administrator(int userId, String username, String password) {
        super(userId, username, password);
        this.staffDatabase = new HashMap<>();
    }

    // Add staff profile with validation
    public boolean addStaffProfile(int staffId, String staffName, String role, String department, double salary) {
        if (staffDatabase.containsKey(staffId)) {
            System.out.println("Error: Staff ID " + staffId + " already exists!");
            return false;
        }

        StaffProfile newStaff = new StaffProfile(staffId, staffName, role, department, salary);
        staffDatabase.put(staffId, newStaff);
        System.out.println("✓ Staff profile added successfully:");
        System.out.println("  ID: " + staffId + ", Name: " + staffName + ", Role: " + role);
        System.out.println("  Department: " + department + ", Salary: $" + salary);
        return true;
    }

    // Edit staff profile with specific field updates
    public boolean editStaffProfile(int staffId, String field, String newValue) {
        if (!staffDatabase.containsKey(staffId)) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return false;
        }

        StaffProfile staff = staffDatabase.get(staffId);
        boolean updated = false;

        switch (field.toLowerCase()) {
            case "name":
                staff.setName(newValue);
                updated = true;
                break;
            case "role":
                staff.setRole(newValue);
                updated = true;
                break;
            case "department":
                staff.setDepartment(newValue);
                updated = true;
                break;
            case "salary":
                try {
                    staff.setSalary(Double.parseDouble(newValue));
                    updated = true;
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid salary format!");
                    return false;
                }
                break;
            default:
                System.out.println("Error: Invalid field '" + field + "'!");
                return false;
        }

        if (updated) {
            System.out.println("✓ Staff profile " + staffId + " updated successfully.");
            System.out.println("  " + field + " changed to: " + newValue);
        }
        return updated;
    }

    // View staff profile with detailed information
    public void viewStaffProfile(int staffId) {
        if (!staffDatabase.containsKey(staffId)) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return;
        }

        StaffProfile staff = staffDatabase.get(staffId);
        System.out.println("==================== STAFF PROFILE ====================");
        System.out.println("Staff ID: " + staff.getStaffId());
        System.out.println("Name: " + staff.getName());
        System.out.println("Role: " + staff.getRole());
        System.out.println("Department: " + staff.getDepartment());
        System.out.println("Salary: $" + staff.getSalary());
        System.out.println("======================================================");
    }

    // View all staff profiles
    public void viewAllStaffProfiles() {
        if (staffDatabase.isEmpty()) {
            System.out.println("No staff profiles found.");
            return;
        }

        System.out.println("===================== ALL STAFF PROFILES =====================");
        System.out.printf("%-8s %-20s %-15s %-15s %-10s%n", "ID", "Name", "Role", "Department", "Salary");
        System.out.println("----------------------------------------------------------------");

        for (StaffProfile staff : staffDatabase.values()) {
            System.out.printf("%-8d %-20s %-15s %-15s $%-9.2f%n",
                    staff.getStaffId(), staff.getName(), staff.getRole(),
                    staff.getDepartment(), staff.getSalary());
        }
        System.out.println("================================================================");
    }

    // Delete staff profile with confirmation
    public boolean deleteStaffProfile(int staffId) {
        if (!staffDatabase.containsKey(staffId)) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return false;
        }

        StaffProfile removedStaff = staffDatabase.remove(staffId);
        System.out.println("✓ Staff profile deleted successfully:");
        System.out.println("  Removed: " + removedStaff.getName() + " (ID: " + staffId + ")");
        return true;
    }

    // Helper method to check if staff exists
    public boolean staffExists(int staffId) {
        return staffDatabase.containsKey(staffId);
    }

    // Get staff count
    public int getStaffCount() {
        return staffDatabase.size();
    }

    // Inner class for Staff Profile data structure
    private static class StaffProfile {
        private int staffId;
        private String name;
        private String role;
        private String department;
        private double salary;

        public StaffProfile(int staffId, String name, String role, String department, double salary) {
            this.staffId = staffId;
            this.name = name;
            this.role = role;
            this.department = department;
            this.salary = salary;
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
}
