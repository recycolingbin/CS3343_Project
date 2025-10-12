package employeeFunction;

import java.io.*;
import java.util.*;

public class Administrator extends BaseUser {
    private static final String STAFF_PROFILE_FILE = "CS3343 Project/Data/Staff_Profile.txt";

    public Administrator(int userId, String username, String password) {
        super(userId, username, password);
        initializeStaffFile();
    }

    // Initialize staff profile file if it doesn't exist
    private void initializeStaffFile() {
        File file = new File(STAFF_PROFILE_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Staff profile file created: " + STAFF_PROFILE_FILE);
            } catch (IOException e) {
                System.out.println("Error creating staff profile file: " + e.getMessage());
            }
        }
    }

    // Load staff profiles from file
    private List<StaffProfile> loadStaffProfiles() {
        List<StaffProfile> profiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        int staffId = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String role = parts[2].trim();
                        String department = parts[3].trim();
                        double salary = Double.parseDouble(parts[4].trim());
                        profiles.add(new StaffProfile(staffId, name, role, department, salary));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading staff profiles: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing staff data: " + e.getMessage());
        }
        return profiles;
    }

    // Save staff profiles to file
    private void saveStaffProfiles(List<StaffProfile> profiles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STAFF_PROFILE_FILE))) {
            for (StaffProfile profile : profiles) {
                writer.println(profile.getStaffId() + "|" + profile.getName() + "|" +
                        profile.getRole() + "|" + profile.getDepartment() + "|" + profile.getSalary());
            }
        } catch (IOException e) {
            System.out.println("Error saving staff profiles: " + e.getMessage());
        }
    }

    // Get user info by staff ID
    public StaffProfile getUserInfo(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                return profile;
            }
        }
        return null;
    }

    // Add staff profile with validation
    public boolean addStaffProfile(int staffId, String staffName, String role, String department, double salary) {
        List<StaffProfile> profiles = loadStaffProfiles();

        // Check if staff ID already exists
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                System.out.println("Error: Staff ID " + staffId + " already exists!");
                return false;
            }
        }

        StaffProfile newStaff = new StaffProfile(staffId, staffName, role, department, salary);
        profiles.add(newStaff);
        saveStaffProfiles(profiles);

        System.out.println("✓ Staff profile added successfully:");
        System.out.println("  ID: " + staffId + ", Name: " + staffName + ", Role: " + role);
        System.out.println("  Department: " + department + ", Salary: $" + salary);
        return true;
    }

    // Edit staff profile with specific field updates
    public boolean editStaffProfile(int staffId, String field, String newValue) {
        List<StaffProfile> profiles = loadStaffProfiles();
        StaffProfile targetStaff = null;

        // Find the staff to edit
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                targetStaff = profile;
                break;
            }
        }

        if (targetStaff == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return false;
        }

        boolean updated = false;

        switch (field.toLowerCase()) {
            case "name":
                targetStaff.setName(newValue);
                updated = true;
                break;
            case "role":
                targetStaff.setRole(newValue);
                updated = true;
                break;
            case "department":
                targetStaff.setDepartment(newValue);
                updated = true;
                break;
            case "salary":
                try {
                    targetStaff.setSalary(Double.parseDouble(newValue));
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
            saveStaffProfiles(profiles);
            System.out.println("✓ Staff profile " + staffId + " updated successfully.");
            System.out.println("  " + field + " changed to: " + newValue);
        }
        return updated;
    }

    // View staff profile with detailed information
    public void viewStaffProfile(int staffId) {
        StaffProfile staff = getUserInfo(staffId);
        if (staff == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return;
        }

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
        List<StaffProfile> profiles = loadStaffProfiles();
        if (profiles.isEmpty()) {
            System.out.println("No staff profiles found.");
            return;
        }

        System.out.println("===================== ALL STAFF PROFILES =====================");
        System.out.printf("%-8s %-20s %-15s %-15s %-10s%n", "ID", "Name", "Role", "Department", "Salary");
        System.out.println("----------------------------------------------------------------");

        for (StaffProfile staff : profiles) {
            System.out.printf("%-8d %-20s %-15s %-15s $%-9.2f%n",
                    staff.getStaffId(), staff.getName(), staff.getRole(),
                    staff.getDepartment(), staff.getSalary());
        }
        System.out.println("================================================================");
    }

    // Delete staff profile with confirmation
    public boolean deleteStaffProfile(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        StaffProfile toRemove = null;

        // Find the staff to remove
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                toRemove = profile;
                break;
            }
        }

        if (toRemove == null) {
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return false;
        }

        profiles.remove(toRemove);
        saveStaffProfiles(profiles);
        System.out.println("✓ Staff profile deleted successfully:");
        System.out.println("  Removed: " + toRemove.getName() + " (ID: " + staffId + ")");
        return true;
    }

    // Helper method to check if staff exists
    public boolean staffExists(int staffId) {
        return getUserInfo(staffId) != null;
    }

    // Get staff count
    public int getStaffCount() {
        return loadStaffProfiles().size();
    }

    // Search staff by name (partial match)
    public List<StaffProfile> searchStaffByName(String searchName) {
        List<StaffProfile> profiles = loadStaffProfiles();
        List<StaffProfile> results = new ArrayList<>();

        for (StaffProfile profile : profiles) {
            if (profile.getName().toLowerCase().contains(searchName.toLowerCase())) {
                results.add(profile);
            }
        }
        return results;
    }

    // Get staff by department
    public List<StaffProfile> getStaffByDepartment(String department) {
        List<StaffProfile> profiles = loadStaffProfiles();
        List<StaffProfile> results = new ArrayList<>();

        for (StaffProfile profile : profiles) {
            if (profile.getDepartment().equalsIgnoreCase(department)) {
                results.add(profile);
            }
        }
        return results;
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
