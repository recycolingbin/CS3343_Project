package staffRosteringSystem;

import java.util.*;
import java.util.function.Function;

/**
 * StaffManager handles all staff profile operations.
 * Responsibilities:
 * - Load/save staff profiles from/to file
 * - Add, edit, view, delete staff profiles
 * - Staff validation and lookup
 */
public class StaffManager {
    private static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";
	private String staffFilePath;
	private FileOperations fileOps;
    
    public StaffManager(String staffFilePath, FileOperations fileOps) {
        this.staffFilePath = staffFilePath;
        this.fileOps = fileOps;
    }
    
    // Default constructor (for production)
    public StaffManager() {
        this(STAFF_PROFILE_FILE, new FileOperations());
    }
    
    public boolean initializeStaffProfileFile(String filePath) {
        //FileOperations fOps = new FileOperations();
        //return fOps.initializeFile(filePath);    
        return this.fileOps.initializeFile(filePath); 
    }
    
    // Load staff profiles from file
    public List<StaffProfile> loadStaffProfiles() {
        FileOperations fOps = new FileOperations();
        Function<String, StaffProfile> parser = line -> {
            
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    int staffId = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String role = parts[2].trim();
                    return new StaffProfile(staffId, name, role);
                }
                return null;
            
        };
        return fOps.loadData(staffFilePath, parser);
    }
    
    // Save staff profiles to file
    public boolean saveStaffProfiles(List<StaffProfile> profiles) {
        FileOperations fOps = new FileOperations();
        Function<StaffProfile, String> formatter = profile ->
            profile.getStaffId() + "," + profile.getName() + "," + profile.getRole();
        return fOps.saveData(staffFilePath, profiles, formatter);
    }
    
    // Add staff profile with validation
    public boolean addStaffProfile(int staffId, String staffName, String role) {
        List<StaffProfile> profiles = loadStaffProfiles();

        // Check if staff ID already exists
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                System.out.println("Error: Staff ID " + staffId + " already exists!");
                return false;
            }
        }

        StaffProfile newStaff = new StaffProfile(staffId, staffName, role);
        profiles.add(newStaff);
        saveStaffProfiles(profiles);

        System.out.println("Staff profile added successfully:");
        System.out.println("  ID: " + staffId + ", Name: " + staffName + ", Role: " + role);
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
            default:
                System.out.println("Error: Invalid field '" + field + "'! Only 'name' and 'role' are allowed.");
                return false;
        }

        if (updated) {
            saveStaffProfiles(profiles);
            System.out.println("Staff profile " + staffId + " updated successfully.");
            System.out.println("  " + field + " changed to: " + newValue);
        }
        return updated;
    }

    // View staff profile with detailed information
    public String viewStaffProfile(int staffId) { //return String for testability
        List<StaffProfile> profiles = loadStaffProfiles();
        StaffProfile staff = null;
        
        // Find the staff profile
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                staff = profile;
                break;
            }
        }
        
        if (staff == null) {
            return "Error: Staff ID " + staffId + " not found!";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("==================== STAFF PROFILE ====================\n");
        sb.append("Staff ID: ").append(staff.getStaffId()).append("\n");
        sb.append("Name: ").append(staff.getName()).append("\n");
        sb.append("Role: ").append(staff.getRole()).append("\n");
        sb.append("======================================================");
        
        return sb.toString();
    }
    
	public String viewAllStaffProfiles(List<StaffProfile> profiles) { //return String for testability
		if (profiles == null) {
			profiles = loadStaffProfiles();
		}
		if (profiles.isEmpty()) {
			return "No staff profiles found.";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("===================== ALL STAFF PROFILES =====================\n");
		sb.append(String.format("%-8s %-25s %-20s%n", "ID", "Name", "Role"));
		sb.append("----------------------------------------------------------\n");

		for (StaffProfile staff : profiles) {
			String truncatedName = staff.getName().length() > 25 ? staff.getName().substring(0, 22) + "..."
					: staff.getName();
			String truncatedRole = staff.getRole().length() > 20 ? staff.getRole().substring(0, 17) + "..."
					: staff.getRole();

			sb.append(String.format("%-8d %-25s %-20s%n", staff.getStaffId(), truncatedName, truncatedRole));
		}
		sb.append("==========================================================\n");
		return sb.toString();
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
        System.out.println("Staff profile deleted successfully:");
        System.out.println("  Removed: " + toRemove.getName() + " (ID: " + staffId + ")");
        return true;
    }

    // Helper method to check if staff exists
    public boolean staffExists(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                return true;
            }
        }
        return false;
    }

    // Get staff info by ID
    public StaffProfile getStaffInfo(int staffId) {
        List<StaffProfile> profiles = loadStaffProfiles();
        for (StaffProfile profile : profiles) {
            if (profile.getStaffId() == staffId) {
                return profile;
            }
        }
        return null;
    }

    // Get staff count
    public int getStaffCount() {
        return loadStaffProfiles().size();
    }
}
