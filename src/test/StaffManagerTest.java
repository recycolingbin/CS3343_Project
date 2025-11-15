package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import staffRosteringSystem.AdminFunction;
import staffRosteringSystem.RequestManager;
import staffRosteringSystem.ShiftManager;
import staffRosteringSystem.StaffManager;
import staffRosteringSystem.StaffProfile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;
import java.util.*;


public class StaffManagerTest {
	private static int uniqueStaffId(StaffManager sm) {
        List<staffRosteringSystem.StaffProfile> profiles = sm.loadStaffProfiles();
        Set<Integer> taken = new HashSet<>();
        for (staffRosteringSystem.StaffProfile p : profiles) taken.add(p.getStaffId());
        int id = 900000; // start high to avoid collisions with seeded data
        while (taken.contains(id)) id++;
        return id;
    }
	
	//Add Staff Profile (3 Tests)
	@Test
    void testAddStaffProfileSuccess() {
		StaffManager staffManager = new StaffManager();
        int id = uniqueStaffId(staffManager);
		AdminFunction adminFunction = new AdminFunction(1, "admin", "password", staffManager, 
				new RequestManager(),new ShiftManager(), new Scanner(""));
        boolean result = adminFunction.addStaffProfile(id, "John Doe", "Employee");
        assertTrue(result);
    }
	
    @Test
    void testAddStaffProfileDuplicate() {
		StaffManager staffManager = new StaffManager();
    	staffManager.addStaffProfile(92001, "John Doe", "Employee");
        boolean result = staffManager.addStaffProfile(92001, "Jane Doe", "Manager");
        assertFalse(result);
    }
    
    @Test
    void testAddMultipleStaffProfiles() {
		StaffManager staffManager = new StaffManager();
    	int id1 = uniqueStaffId(staffManager);
        boolean result1 = staffManager.addStaffProfile(id1, "John", "Employee");
        int id2 = uniqueStaffId(staffManager);
        boolean result2 = staffManager.addStaffProfile(id2, "Jane", "Manager");
        int id3 = uniqueStaffId(staffManager);
        boolean result3 = staffManager.addStaffProfile(id3, "Bob", "Employee");
        
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }
    
    //Edit Staff Profile (4 Tests)
    @Test
    void testEditStaffName() {
		StaffManager staffManager = new StaffManager();
		AdminFunction adminFunction = new AdminFunction(1, "admin", "password", staffManager, 
				new RequestManager(),new ShiftManager(), new Scanner(""));
        int id = uniqueStaffId(staffManager);
        assertTrue(staffManager.addStaffProfile(id, "John", "Employee"));
        
        boolean result = adminFunction.editStaffProfile(id, "name", "Jonathan");
        assertTrue(result);
        StaffProfile profile = staffManager.getStaffInfo(id);
        assertEquals("Jonathan", profile.getName());
        assertEquals("Employee", profile.getRole()); 
    }
    
    @Test
    void testEditStaffRole() {
        StaffManager staffManager = new StaffManager();
        AdminFunction adminFunction = new AdminFunction(1, "admin", "password", staffManager, 
				new RequestManager(),new ShiftManager(), new Scanner(""));
        int id = uniqueStaffId(staffManager);
        assertTrue(staffManager.addStaffProfile(id, "John", "Employee"));
        boolean result = adminFunction.editStaffProfile(id, "role", "Manager");
        assertTrue(result);
        StaffProfile profile = staffManager.getStaffInfo(id);
        assertEquals("John", profile.getName()); 
        assertEquals("Manager", profile.getRole());
    }
    
    @Test
    void testEditStaffInvalidField() {
		StaffManager staffManager = new StaffManager();
		int id = uniqueStaffId(staffManager);
		assertTrue(staffManager.addStaffProfile(id, "John", "Employee"));
		boolean result = staffManager.editStaffProfile(id, "Department", "Sales");
		assertFalse(result);
    }
    
    @Test
    void testEditNonExistentStaff() {
    	StaffManager staffManager = new StaffManager();
        boolean result = staffManager.editStaffProfile(9999, "name", "Peter");
        assertFalse(result);
    }
    
    //Delete Staff Profile (2 Tests)
    @Test
    void testDeleteStaffProfile() {
		StaffManager staffManager = new StaffManager();
		AdminFunction adminFunction = new AdminFunction(1, "admin", "password", staffManager, 
				new RequestManager(),new ShiftManager(), new Scanner(""));
        int id = uniqueStaffId(staffManager);
        assertTrue(adminFunction.addStaffProfile(id, "John", "Employee"));
        boolean result = adminFunction.deleteStaffProfile(id);
        assertTrue(result);
    }
    
    @Test
    void testDeleteNonExistentStaff() {
		StaffManager staffManager = new StaffManager();
        boolean result = staffManager.deleteStaffProfile(9999);
        assertFalse(result);
    }
    
    //View Staff Profiles (2 Tests)
    @Test
	void testViewStaffProfile() {
		StaffManager staffManager = new StaffManager();
		AdminFunction adminFunction = new AdminFunction(1, "admin", "password", staffManager, 
				new RequestManager(),new ShiftManager(), new Scanner(""));
		int id = uniqueStaffId(staffManager);
		adminFunction.addStaffProfile(id, "John Doe", "Employee");
		String result = adminFunction.viewStaffProfile(id);
		assertTrue(result.contains("Staff ID: " + id));
		assertTrue(result.contains("John Doe"));
		assertTrue(result.contains("Employee"));
	}
    
    @Test
    void testNoStaffProfileFound() {
    StaffManager staffManager = new StaffManager();
    int staffId = uniqueStaffId(staffManager);
    String result = staffManager.viewStaffProfile(staffId); 
    assertEquals(result, "Error: Staff ID " + staffId + " not found!");
    }
    
    @Test
    void testViewAllStaffProfiles() {
    	StaffManager staffManager = new StaffManager();
    	AdminFunction adminFunction = new AdminFunction(1, "admin", "password", staffManager, 
				new RequestManager(),new ShiftManager(), new Scanner(""));
    	List<StaffProfile> profiles = new ArrayList<>();
    	profiles.add(new StaffProfile(1, "John", "Employee"));
    	profiles.add(new StaffProfile(2, "Jane", "Manager"));
		String result = adminFunction.viewAllStaffProfiles();
		assertTrue(result.contains("John"));
		assertTrue(result.contains("Employee"));
		assertTrue(result.contains("Jane"));
	    assertTrue(result.contains("Manager"));
    }
    
    @Test
    void testNoStaffProfilesFound() {
        StaffManager staffManager = new StaffManager();
        List<StaffProfile> profiles = new ArrayList<>();
        String result = staffManager.viewAllStaffProfiles(profiles);
        assertEquals("No staff profiles found.", result);
    }
    
    //getStaffInfo (2 Tests)
    @Test
    void testGetStaffInfo() {
    	StaffManager staffManager = new StaffManager();
    	int id = uniqueStaffId(staffManager);
        staffManager.addStaffProfile(id, "John Doe", "Employee");
        StaffProfile info = staffManager.getStaffInfo(id);
        assertNotNull(info);
        assertEquals("John Doe", info.getName());
        assertEquals("Employee", info.getRole());
    }
    
    @Test
    void testGetNonExistentStaffInfo() {
    	StaffManager staffManager = new StaffManager();
        StaffProfile info = staffManager.getStaffInfo(9999);
        assertNull(info);
    }
    
    //initializeStaffProfileFile (3 Tests)
    @Test
	void testinitializeStaffProfileFile1() {
		StaffManager staffManager = new StaffManager();
		String filePath = null;
		boolean result = staffManager.initializeStaffProfileFile(filePath);		
		assertFalse(result);
	}
    
    @Test
	void testinitializeStaffProfileFile2(@TempDir Path tempDir) {
		StaffManager staffManager = new StaffManager();
    	Path file = tempDir.resolve("temp.txt");
        String pathStr = file.toString();
        assertFalse(Files.exists(file));
        boolean result = staffManager.initializeStaffProfileFile(pathStr);
        assertTrue(result);
        assertTrue(Files.exists(file));
    }
	
    
    @Test
    void testinitializeStaffProfileFile3() {
    	StaffManager staffManager = new StaffManager();
    	String filePath = "Data/Staff_Profile.txt";
    	File file = new File(filePath);
    	boolean result = staffManager.initializeStaffProfileFile(filePath);
    	assertTrue(result);
    	assertTrue(file.exists());
    }
    
    @Test
	void testinitializeStaffProfileFile4(@TempDir Path tempDir) {
    	StaffManager staffManager = new StaffManager();
    	Path parentDir = tempDir.resolve("missing-parent");
        Path filePath  = parentDir.resolve("Staff_Profile.txt");
        String pathStr = filePath.toString();
        assertFalse(Files.exists(parentDir));
        boolean result = staffManager.initializeStaffProfileFile(pathStr);
        assertTrue(result);
        assertTrue(Files.exists(filePath));
        assertTrue(Files.exists(parentDir));
    }

}
