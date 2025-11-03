package adminFunction;

import java.io.*;
import java.util.*;
import staffProfile.StaffProfile;

/**
 * StaffManager handles all staff profile operations.
 * Responsibilities:
 * - Load/save staff profiles from/to file
 * - Add, edit, view, delete staff profiles
 * - Staff validation and lookup
 */
public class StaffManager {
    private static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";

    // Load staff profiles from file
    public List<StaffProfile> loadStaffProfiles() {
        List<StaffProfile> profiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        int staffId = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String role = parts[2].trim();
                        profiles.add(new StaffProfile(staffId, name, role));
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
    public void saveStaffProfiles(List<StaffProfile> profiles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STAFF_PROFILE_FILE))) {
            for (StaffProfile profile : profiles) {
                writer.println(profile.getStaffId() + "," + profile.getName() + "," + profile.getRole());
            }
        } catch (IOException e) {
            System.out.println("Error saving staff profiles: " + e.getMessage());
        }
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
    public void viewStaffProfile(int staffId) {
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
            System.out.println("Error: Staff ID " + staffId + " not found!");
            return;
        }

        System.out.println("==================== STAFF PROFILE ====================");
        System.out.println("Staff ID: " + staff.getStaffId());
        System.out.println("Name: " + staff.getName());
        System.out.println("Role: " + staff.getRole());
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
        System.out.printf("%-8s %-25s %-20s%n", "ID", "Name", "Role");
        System.out.println("----------------------------------------------------------");

        for (StaffProfile staff : profiles) {
            String truncatedName = staff.getName().length() > 25 ? 
                                  staff.getName().substring(0, 22) + "..." : staff.getName();
            String truncatedRole = staff.getRole().length() > 20 ? 
                                  staff.getRole().substring(0, 17) + "..." : staff.getRole();
                                  
            System.out.printf("%-8d %-25s %-20s%n",
                    staff.getStaffId(), truncatedName, truncatedRole);
        }
        System.out.println("==========================================================");
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

    // Initialize staff file if it doesn't exist
    public void initializeStaffFile() {
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
}
