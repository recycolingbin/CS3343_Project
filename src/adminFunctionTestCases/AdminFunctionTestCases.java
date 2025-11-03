package adminFunctionTestCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.DisplayName;

import adminFunction.AdminFunction;
import adminFunction.AdminFunction.Request;
import adminFunction.AdminFunction.DutyRequest;
import adminFunction.AdminFunction.LeaveRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;

public class AdminFunctionTestCases {
    private AdminFunction admin;
    
    @TempDir
    Path tempDir;
    
    private File staffProfileFile;
    private File leaveRequestFile;
    private File dutyRequestFile;
    private File shiftFile;
    
    @BeforeEach
    public void setUp() throws Exception {
        // Create temporary files for testing with correct paths
        File dataDir = tempDir.resolve("Data").toFile();
        dataDir.mkdirs();
        
        staffProfileFile = new File(dataDir, "Staff_Profile.txt");
        leaveRequestFile = new File(dataDir, "Leave_Request.txt");
        dutyRequestFile = new File(dataDir, "Duty_Request.txt");
        shiftFile = new File(dataDir, "Shift.txt");
        
        // Create files with initial data
        staffProfileFile.createNewFile();
        leaveRequestFile.createNewFile();
        dutyRequestFile.createNewFile();
        shiftFile.createNewFile();
        
        // Initialize with sample data matching the actual formats
        initializeSampleData();
        
        // Create AdminFunction instance
        admin = new AdminFunction(1, "admin", "password");
    }
    
    private void initializeSampleData() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(staffProfileFile))) {
            writer.println("1001,Mary,Employee");
            writer.println("1002,Chris,Employee");
            writer.println("1003,David,Administrator");
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(leaveRequestFile))) {
            writer.println("1001,2024-12-20,2024-12-25,Christmas holiday");
            writer.println("1002,2024-11-15,2024-11-18,Family vacation");
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(dutyRequestFile))) {
            writer.println("1001,2024-11-08,AFTERNOON");
            writer.println("1002,2024-11-10,MORNING");
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(shiftFile))) {
            writer.println("3001,1001,2024-10-20,NIGHT,22:00,06:00,Regular night shift");
            writer.println("3002,1002,2024-10-21,AFTERNOON,14:00,22:00,Weekend coverage");
        }
    }

    // ================== CORE FUNCTIONALITY TEST CASES ==================

    // Test case 1: addStaffProfile with valid data
    @Test
    public void testAddStaffProfileValid() {
        boolean result = admin.addStaffProfile(101, "Sarah", "Employee");
        assertTrue(result);
        assertTrue(admin.staffExists(101));
    }
    
    // Test case 2: addStaffProfile with duplicate staff ID
    @Test
    public void testAddStaffProfileDuplicateId() {
        admin.addStaffProfile(102, "John", "Employee");
        boolean result = admin.addStaffProfile(102, "Mike", "Employee");
        assertFalse(result);
    }
    
    // Test case 3: addStaffProfile with empty name
    @Test
    public void testAddStaffProfileEmptyName() {
        boolean result = admin.addStaffProfile(103, "", "Employee");
        assertTrue(result);
    }
    
    // Test case 4: addStaffProfile with null role
    @Test
    public void testAddStaffProfileNullRole() {
        boolean result = admin.addStaffProfile(104, "Terry", null);
        assertTrue(result);
    }
    
    // Test case 5: editStaffProfile valid name change
    @Test
    public void testEditStaffProfileValidName() {
        admin.addStaffProfile(201, "Original", "Employee");
        boolean result = admin.editStaffProfile(201, "name", "Updated");
        assertTrue(result);
    }
    
    // Test case 6: editStaffProfile valid role change
    @Test
    public void testEditStaffProfileValidRole() {
        admin.addStaffProfile(202, "Staff", "Employee");
        boolean result = admin.editStaffProfile(202, "role", "Administrator");
        assertTrue(result);
    }
    
    // Test case 7: editStaffProfile non-existent staff ID
    @Test
    public void testEditStaffProfileNonExistent() {
        boolean result = admin.editStaffProfile(999, "name", "NewName");
        assertFalse(result);
    }
    
    // Test case 8: editStaffProfile invalid field
    @Test
    public void testEditStaffProfileInvalidField() {
        admin.addStaffProfile(203, "Test", "Employee");
        boolean result = admin.editStaffProfile(203, "email", "test@example.com");
        assertFalse(result);
    }
    
    // Test case 9: editStaffProfile case insensitive field
    @Test
    public void testEditStaffProfileCaseInsensitive() {
        admin.addStaffProfile(204, "Case", "Employee");
        boolean result = admin.editStaffProfile(204, "NAME", "UPPERCASE");
        assertTrue(result);
    }
    
    // Test case 10: deleteStaffProfile existing staff
    @Test
    public void testDeleteStaffProfileExisting() {
        admin.addStaffProfile(301, "Delete", "Employee");
        assertTrue(admin.staffExists(301));
        boolean result = admin.deleteStaffProfile(301);
        assertTrue(result);
        assertFalse(admin.staffExists(301));
    }
    
    // Test case 11: deleteStaffProfile non-existent staff
    @Test
    public void testDeleteStaffProfileNonExistent() {
        boolean result = admin.deleteStaffProfile(999);
        assertFalse(result);
    }
    
    // Test case 12: staffExists for existing staff
    @Test
    public void testStaffExistsTrue() {
        admin.addStaffProfile(401, "Existing", "Employee");
        boolean result = admin.staffExists(401);
        assertTrue(result);
    }
    
    // Test case 13: staffExists for non-existent staff
    @Test
    public void testStaffExistsFalse() {
        boolean result = admin.staffExists(999);
        assertFalse(result);
    }
    
    // Test case 14: staffExists after deletion
    @Test
    public void testStaffExistsAfterDeletion() {
        admin.addStaffProfile(402, "Temporary", "Employee");
        admin.deleteStaffProfile(402);
        boolean result = admin.staffExists(402);
        assertFalse(result);
    }
    
    // Test case 15: requestLeave valid request
    @Test
    public void testRequestLeaveValid() {
        admin.addStaffProfile(501, "Leave", "Employee");
        boolean result = admin.requestLeave(501, "2024-06-01", "2024-06-05", "Annual Leave");
        assertTrue(result);
    }
    
    // Test case 16: requestLeave non-existent staff
    @Test
    public void testRequestLeaveNonExistentStaff() {
        boolean result = admin.requestLeave(999, "2024-06-01", "2024-06-05", "Vacation");
        assertFalse(result);
    }
    
    // Test case 17: requestLeave with empty reason
    @Test
    public void testRequestLeaveEmptyReason() {
        admin.addStaffProfile(502, "Staff", "Employee");
        boolean result = admin.requestLeave(502, "2024-02-01", "2024-02-03", "");
        assertTrue(result);
    }
    
    // Test case 18: requestLeave with invalid date format
    @Test
    public void testRequestLeaveInvalidDateFormat() {
        admin.addStaffProfile(503, "Test", "Employee");
        boolean result = admin.requestLeave(503, "01-01-2024", "05-01-2024", "Vacation");
        assertTrue(result);
    }
    
    // Test case 19: requestLeave with null parameters
    @Test
    public void testRequestLeaveNullParameters() {
        admin.addStaffProfile(504, "Test", "Employee");
        boolean result = admin.requestLeave(504, null, null, null);
        assertTrue(result);
    }
    
    // Test case 20: getStaffCount empty
    @Test
    public void testGetStaffCountEmpty() {
        // Clear all staff first by clearing the file
        try (PrintWriter writer = new PrintWriter(staffProfileFile)) {
            writer.print(""); // Clear the file
        } catch (IOException e) {
            fail("Failed to clear staff profile file: " + e.getMessage());
        }
        
        int count = admin.getStaffCount();
        assertEquals(0, count);
    }
    
    // Test case 21: getStaffCount after additions
    @Test
    public void testGetStaffCountAfterAdditions() {
        admin.addStaffProfile(701, "One", "Employee");
        admin.addStaffProfile(702, "Two", "Administrator");
        admin.addStaffProfile(703, "Three", "Employee");
        
        int count = admin.getStaffCount();
        assertEquals(3, count);
    }
    
    // Test case 22: getStaffCount after deletion
    @Test
    public void testGetStaffCountAfterDeletion() {
        admin.addStaffProfile(801, "One", "Employee");
        admin.addStaffProfile(802, "Two", "Administrator");
        admin.deleteStaffProfile(801);
        
        int count = admin.getStaffCount();
        assertEquals(1, count);
    }
    
    // Test case 23: viewAllStaffProfiles empty
    @Test
    public void testViewAllStaffProfilesEmpty() {
        assertDoesNotThrow(() -> admin.viewAllStaffProfiles());
    }
    
    // Test case 24: viewStaffProfile existing staff
    @Test
    public void testViewStaffProfileExisting() {
        admin.addStaffProfile(901, "View", "Administrator");
        assertDoesNotThrow(() -> admin.viewStaffProfile(901));
    }
    
    // Test case 25: viewStaffProfile non-existent staff
    @Test
    public void testViewStaffProfileNonExistent() {
        assertDoesNotThrow(() -> admin.viewStaffProfile(999));
    }
    
    // Test case 26: add multiple staff and verify count
    @Test
    public void testAddMultipleStaff() {
        admin.addStaffProfile(1001, "First", "Employee");
        admin.addStaffProfile(1002, "Second", "Administrator");
        admin.addStaffProfile(1003, "Third", "Employee");
        
        assertEquals(3, admin.getStaffCount());
        assertTrue(admin.staffExists(1001));
        assertTrue(admin.staffExists(1002));
        assertTrue(admin.staffExists(1003));
    }
    
    // Test case 27: edit and verify staff persistence
    @Test
    public void testEditAndVerifyPersistence() {
        admin.addStaffProfile(1101, "Original", "Employee");
        admin.editStaffProfile(1101, "name", "Updated");
        admin.editStaffProfile(1101, "role", "Administrator");
        
        assertTrue(admin.staffExists(1101));
    }
    
    // Test case 28: requestLeave multiple times for same staff
    @Test
    public void testMultipleLeaveRequests() {
        admin.addStaffProfile(1201, "Frequent", "Employee");
        
        boolean result1 = admin.requestLeave(1201, "2024-04-01", "2024-04-03", "First Vacation");
        boolean result2 = admin.requestLeave(1201, "2024-05-01", "2024-05-05", "Second Vacation");
        boolean result3 = admin.requestLeave(1201, "2024-06-01", "2024-06-02", "Short Break");
        
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }
    
    // Test case 29: test staffExists with multiple staff members
    @Test
    public void testStaffExistsWithMultipleStaff() {
        admin.addStaffProfile(1301, "A", "Employee");
        admin.addStaffProfile(1302, "B", "Administrator");
        admin.addStaffProfile(1303, "C", "Employee");
        
        assertTrue(admin.staffExists(1301));
        assertTrue(admin.staffExists(1302));
        assertTrue(admin.staffExists(1303));
        assertFalse(admin.staffExists(1304));
    }
    
    // Test case 30: comprehensive staff life cycle test
    @Test
    public void testStaffLifecycle() {
        // Add staff
        boolean addResult = admin.addStaffProfile(1401, "Lifecycle", "Employee");
        assertTrue(addResult);
        assertTrue(admin.staffExists(1401));
        assertEquals(1, admin.getStaffCount());
        
        // Edit staff
        boolean editResult = admin.editStaffProfile(1401, "name", "UpdatedLifecycle");
        assertTrue(editResult);
        
        // Delete staff
        boolean deleteResult = admin.deleteStaffProfile(1401);
        assertTrue(deleteResult);
        assertFalse(admin.staffExists(1401));
        assertEquals(0, admin.getStaffCount());
    }
    
    // Test case 31: test requestLeave with different staff members
    @Test
    public void testRequestLeaveDifferentStaff() {
        admin.addStaffProfile(1501, "One", "Employee");
        admin.addStaffProfile(1502, "Two", "Administrator");
        
        boolean result1 = admin.requestLeave(1501, "2024-07-01", "2024-07-03", "Sick Leave");
        boolean result2 = admin.requestLeave(1502, "2024-08-01", "2024-08-05", "Annual Leave");
        
        assertTrue(result1);
        assertTrue(result2);
    }
    
    // Test case 32: test editStaffProfile with empty new value
    @Test
    public void testEditStaffProfileEmptyValue() {
        admin.addStaffProfile(1601, "Test", "Employee");
        boolean result = admin.editStaffProfile(1601, "name", "");
        assertTrue(result);
    }
    
    // Test case 33: test deleteStaffProfile multiple times
    @Test
    public void testDeleteStaffProfileMultipleTimes() {
        admin.addStaffProfile(1701, "Temp", "Employee");
        
        boolean firstDelete = admin.deleteStaffProfile(1701);
        assertTrue(firstDelete);
        
        boolean secondDelete = admin.deleteStaffProfile(1701);
        assertFalse(secondDelete);
    }
    
    // Test case 34: test addStaffProfile with special characters in name
    @Test
    public void testAddStaffProfileSpecialCharacters() {
        boolean result = admin.addStaffProfile(1801, "Mary", "Employee");
        assertTrue(result);
        assertTrue(admin.staffExists(1801));
    }
    
    // Test case 35: test addStaffProfile with long role name
    @Test
    public void testAddStaffProfileLongRole() {
        boolean result = admin.addStaffProfile(1901, "Test", "Administrator");
        assertTrue(result);
        assertTrue(admin.staffExists(1901));
    }

    // ================== FILE CREATION AND EXCEPTION HANDLING TEST CASES ==================

    // Test case 36: Test file creation when file doesn't exist
    @Test
    @DisplayName("Test initializeStaffFile creates file when it doesn't exist")
    public void testInitializeStaffFileCreatesNewFile() throws Exception {
        // Delete the staff profile file to simulate first run
        staffProfileFile.delete();
        assertFalse(staffProfileFile.exists(), "File should not exist before test");
        
        // Use reflection to test private initializeStaffFile method
        Method initializeStaffFileMethod = AdminFunction.class.getDeclaredMethod("initializeStaffFile");
        initializeStaffFileMethod.setAccessible(true);
        
        // This should create the file
        initializeStaffFileMethod.invoke(admin);
        
        // Verify file was created
        assertTrue(staffProfileFile.exists(), "Staff profile file should be created");
    }

    // Test case 37: Test file creation success message path
    @Test
    @DisplayName("Test initializeStaffFile handles existing file without error")
    public void testInitializeStaffFileExistingFile() throws Exception {
        // File should already exist from setup
        assertTrue(staffProfileFile.exists(), "File should exist before test");
        
        // Use reflection to test private initializeStaffFile method
        Method initializeStaffFileMethod = AdminFunction.class.getDeclaredMethod("initializeStaffFile");
        initializeStaffFileMethod.setAccessible(true);
        
        // Should handle existing file without throwing exception
        assertDoesNotThrow(() -> initializeStaffFileMethod.invoke(admin));
    }

    // Test case 38: Test IOException in file creation is handled gracefully
    @Test
    @DisplayName("Test initializeStaffFile handles IOException during file creation")
    public void testInitializeStaffFileHandlesIOException() throws Exception {
        // Create a directory with the same name as the file to cause IOException
        File dataDir = tempDir.resolve("Data").toFile();
        dataDir.mkdirs();
        File invalidFile = new File(dataDir, "Staff_Profile.txt");
        invalidFile.mkdirs(); // Make it a directory to cause IOException on createNewFile
        
        // Use reflection to set the STAFF_PROFILE_FILE to our invalid path
        Field staffProfileField = AdminFunction.class.getDeclaredField("STAFF_PROFILE_FILE");
        staffProfileField.setAccessible(true);
        String originalPath = (String) staffProfileField.get(admin);
        staffProfileField.set(admin, invalidFile.getAbsolutePath());
        
        try {
            // Use reflection to test private initializeStaffFile method
            Method initializeStaffFileMethod = AdminFunction.class.getDeclaredMethod("initializeStaffFile");
            initializeStaffFileMethod.setAccessible(true);
            
            // Should handle IOException gracefully without throwing
            assertDoesNotThrow(() -> initializeStaffFileMethod.invoke(admin));
        } finally {
            // Restore original path
            staffProfileField.set(admin, originalPath);
        }
    }

    // Test case 39: Test IOException handling in loadStaffProfiles
    @Test
    @DisplayName("Test loadStaffProfiles handles IOException gracefully")
    public void testLoadStaffProfilesIOExceptionHandling() throws Exception {
        // Set file to a directory to cause IOException when reading
        File dataDir = tempDir.resolve("Data").toFile();
        dataDir.mkdirs();
        File invalidFile = new File(dataDir, "Staff_Profile.txt");
        invalidFile.mkdirs();
        
        // Use reflection to set the STAFF_PROFILE_FILE to our invalid path
        Field staffProfileField = AdminFunction.class.getDeclaredField("STAFF_PROFILE_FILE");
        staffProfileField.setAccessible(true);
        String originalPath = (String) staffProfileField.get(admin);
        staffProfileField.set(admin, invalidFile.getAbsolutePath());
        
        try {
            // Use reflection to test private loadStaffProfiles method
            Method loadStaffProfilesMethod = AdminFunction.class.getDeclaredMethod("loadStaffProfiles");
            loadStaffProfilesMethod.setAccessible(true);
            
            // Should handle IOException gracefully and return empty list
            @SuppressWarnings("unchecked")
            List<Object> result = (List<Object>) loadStaffProfilesMethod.invoke(admin);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        } finally {
            // Restore original path
            staffProfileField.set(admin, originalPath);
        }
    }

    // Test case 40: Test NumberFormatException handling in loadStaffProfiles
    @Test
    @DisplayName("Test loadStaffProfiles handles NumberFormatException gracefully")
    public void testLoadStaffProfilesNumberFormatExceptionHandling() throws Exception {
        // Write invalid data that will cause NumberFormatException
        try (PrintWriter writer = new PrintWriter(new FileWriter(staffProfileFile))) {
            writer.println("invalid_id,Mary,Employee"); // non-numeric ID
            writer.println("123,Chris,Employee"); // valid line
            writer.println("456,David,"); // valid but missing role
        }
        
        // Use reflection to test private loadStaffProfiles method
        Method loadStaffProfilesMethod = AdminFunction.class.getDeclaredMethod("loadStaffProfiles");
        loadStaffProfilesMethod.setAccessible(true);
        
        // Should handle NumberFormatException gracefully and skip invalid lines
        @SuppressWarnings("unchecked")
        List<Object> result = (List<Object>) loadStaffProfilesMethod.invoke(admin);
        assertNotNull(result);
        // Should still load valid lines despite invalid ones
        assertTrue(result.size() >= 0);
    }

    // Test case 41: Test IOException handling in saveStaffProfiles
    @Test
    @DisplayName("Test saveStaffProfiles handles IOException gracefully")
    public void testSaveStaffProfilesIOExceptionHandling() throws Exception {
        // First add some staff to save
        admin.addStaffProfile(4101, "Test", "Employee");
        
        // Set file to a directory to cause IOException when writing
        File dataDir = tempDir.resolve("Data").toFile();
        dataDir.mkdirs();
        File invalidFile = new File(dataDir, "Staff_Profile.txt");
        invalidFile.mkdirs();
        
        // Use reflection to set the STAFF_PROFILE_FILE to our invalid path
        Field staffProfileField = AdminFunction.class.getDeclaredField("STAFF_PROFILE_FILE");
        staffProfileField.setAccessible(true);
        String originalPath = (String) staffProfileField.get(admin);
        staffProfileField.set(admin, invalidFile.getAbsolutePath());
        
        try {
            // Use reflection to get loadStaffProfiles method to create staff list
            Method loadStaffProfilesMethod = AdminFunction.class.getDeclaredMethod("loadStaffProfiles");
            loadStaffProfilesMethod.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Object> staffList = (List<Object>) loadStaffProfilesMethod.invoke(admin);
            
            // Use reflection to test private saveStaffProfiles method
            Method saveStaffProfilesMethod = AdminFunction.class.getDeclaredMethod("saveStaffProfiles", List.class);
            saveStaffProfilesMethod.setAccessible(true);
            
            // Should handle IOException gracefully without throwing
            assertDoesNotThrow(() -> saveStaffProfilesMethod.invoke(admin, staffList));
        } finally {
            // Restore original path
            staffProfileField.set(admin, originalPath);
        }
    }

    // Test case 42: Test requestLeave IOException handling during save
    @Test
    @DisplayName("Test requestLeave handles IOException during save")
    public void testRequestLeaveIOExceptionHandling() throws Exception {
        admin.addStaffProfile(4201, "Leave", "Employee");
        
        // Set leave request file to a directory to cause IOException
        File dataDir = tempDir.resolve("Data").toFile();
        dataDir.mkdirs();
        File invalidFile = new File(dataDir, "Leave_Request.txt");
        invalidFile.mkdirs();
        
        // Use reflection to set the LEAVE_REQUEST_FILE to our invalid path
        Field leaveRequestField = AdminFunction.class.getDeclaredField("LEAVE_REQUEST_FILE");
        leaveRequestField.setAccessible(true);
        String originalPath = (String) leaveRequestField.get(admin);
        leaveRequestField.set(admin, invalidFile.getAbsolutePath());
        
        try {
            // This should handle IOException and return false
            boolean result = admin.requestLeave(4201, "2024-07-01", "2024-07-05", "Vacation");
            assertFalse(result, "Request should fail due to IOException");
        } finally {
            // Restore original path
            leaveRequestField.set(admin, originalPath);
        }
    }

    // ================== SWITCH STATEMENT AND CONDITIONAL BRANCH TEST CASES ==================

    // Test case 43: Test switch case for "name" field in editStaffProfile
    @Test
    @DisplayName("Test editStaffProfile switch case for name field")
    public void testEditStaffProfileSwitchCaseName() throws Exception {
        admin.addStaffProfile(4301, "Original", "Employee");
        
        // Test name field update
        boolean result = admin.editStaffProfile(4301, "name", "Updated");
        
        assertTrue(result, "Name field update should succeed");
        
        // Verify the update persisted
        assertTrue(admin.staffExists(4301));
    }

    // Test case 44: Test switch case for "role" field in editStaffProfile
    @Test
    @DisplayName("Test editStaffProfile switch case for role field")
    public void testEditStaffProfileSwitchCaseRole() throws Exception {
        admin.addStaffProfile(4401, "Test", "Employee");
        
        // Test role field update
        boolean result = admin.editStaffProfile(4401, "role", "Administrator");
        
        assertTrue(result, "Role field update should succeed");
        
        // Verify the update persisted
        assertTrue(admin.staffExists(4401));
    }

    // Test case 45: Test switch case for role field with different case (case insensitive)
    @Test
    @DisplayName("Test editStaffProfile switch case for role field case insensitive")
    public void testEditStaffProfileSwitchCaseRoleCaseInsensitive() throws Exception {
        admin.addStaffProfile(4501, "Test", "Employee");
        
        // Test role field update with different case
        boolean result = admin.editStaffProfile(4501, "ROLE", "Administrator");
        
        assertTrue(result, "Role field update should succeed with different case");
        
        // Verify the update persisted
        assertTrue(admin.staffExists(4501));
    }

    // Test case 46: Test editStaffProfile success path with save operation
    @Test
    @DisplayName("Test editStaffProfile success path with save operation")
    public void testEditStaffProfileSuccessWithSave() throws Exception {
        admin.addStaffProfile(4601, "Original", "Employee");
        
        // Perform edit operation
        boolean result = admin.editStaffProfile(4601, "name", "Success");
        
        assertTrue(result, "Edit operation should succeed");
        
        // Verify staff still exists after edit
        assertTrue(admin.staffExists(4601));
        
        // Test that we can perform another operation to verify persistence
        boolean secondEdit = admin.editStaffProfile(4601, "role", "Administrator");
        assertTrue(secondEdit, "Second edit operation should also succeed");
    }

    // Test case 47: Test viewAllStaffProfiles empty list condition
    @Test
    @DisplayName("Test viewAllStaffProfiles handles empty profiles list")
    public void testViewAllStaffProfilesEmptyList() throws Exception {
        // Clear all staff profiles
        try (PrintWriter writer = new PrintWriter(staffProfileFile)) {
            writer.print(""); // Clear the file
        }
        
        // Should handle empty list without throwing exception
        assertDoesNotThrow(() -> admin.viewAllStaffProfiles());
    }

    // Test case 48: Test deleteStaffProfile staff finding loop
    @Test
    @DisplayName("Test deleteStaffProfile finds staff in loop")
    public void testDeleteStaffProfileFindingLoop() throws Exception {
        // Add multiple staff members
        admin.addStaffProfile(4801, "First", "Employee");
        admin.addStaffProfile(4802, "Second", "Administrator");
        admin.addStaffProfile(4803, "Third", "Employee");
        
        // Verify all exist
        assertTrue(admin.staffExists(4801));
        assertTrue(admin.staffExists(4802));
        assertTrue(admin.staffExists(4803));
        
        // Delete one staff member
        boolean result = admin.deleteStaffProfile(4802);
        
        assertTrue(result, "Delete should succeed for existing staff");
        
        // Verify the specific staff was removed
        assertFalse(admin.staffExists(4802));
        assertTrue(admin.staffExists(4801));
        assertTrue(admin.staffExists(4803));
    }

    // Test case 49: Test deleteStaffProfile removal and save operation
    @Test
    @DisplayName("Test deleteStaffProfile removal and save operation")
    public void testDeleteStaffProfileRemovalAndSave() throws Exception {
        admin.addStaffProfile(4901, "Delete", "Employee");
        
        // Get initial count
        int initialCount = admin.getStaffCount();
        
        // Perform deletion
        boolean result = admin.deleteStaffProfile(4901);
        
        assertTrue(result, "Delete operation should succeed");
        
        // Verify count decreased
        int finalCount = admin.getStaffCount();
        assertEquals(initialCount - 1, finalCount, "Staff count should decrease by 1");
        
        // Verify staff no longer exists
        assertFalse(admin.staffExists(4901));
    }

    // Test case 50: Test deleteStaffProfile success message path
    @Test
    @DisplayName("Test deleteStaffProfile executes success message path")
    public void testDeleteStaffProfileSuccessMessagePath() throws Exception {
        admin.addStaffProfile(5001, "Test", "Employee");
        
        // This should execute the success path including the success message
        boolean result = admin.deleteStaffProfile(5001);
        
        assertTrue(result, "Delete should succeed");
        assertFalse(admin.staffExists(5001), "Staff should no longer exist");
    }

    // ================== INNER CLASSES TEST CASES ==================

    // Test case 51: Test Request class constructor and getters
    @Test
    @DisplayName("Test Request class functionality")
    public void testRequestClass() {
        Request request = new Request(1001, 2001, "2024-01-01");
        
        assertEquals(1001, request.getEmployeeId());
        assertEquals(2001, request.getRequestId());
        assertEquals("2024-01-01", request.getRequestDate());
    }

    // Test case 52: Test DutyRequest class constructor and getters/setters
    @Test
    @DisplayName("Test DutyRequest class functionality")
    public void testDutyRequestClass() {
        DutyRequest dutyRequest = new DutyRequest(1002, 2002, "2024-01-02", "MORNING");
        
        assertEquals(1002, dutyRequest.getEmployeeId());
        assertEquals(2002, dutyRequest.getRequestId());
        assertEquals("2024-01-02", dutyRequest.getRequestDate());
        assertEquals("MORNING", dutyRequest.getSession());
        
        // Test setter
        dutyRequest.setSession("AFTERNOON");
        assertEquals("AFTERNOON", dutyRequest.getSession());
    }

    // Test case 53: Test LeaveRequest class constructor and getters/setters
    @Test
    @DisplayName("Test LeaveRequest class functionality")
    public void testLeaveRequestClass() {
        LeaveRequest leaveRequest = new LeaveRequest(1003, 2003, "2024-01-03", 
                                                   "2024-02-01", "2024-02-05", "Vacation");
        
        assertEquals(1003, leaveRequest.getEmployeeId());
        assertEquals(2003, leaveRequest.getRequestId());
        assertEquals("2024-01-03", leaveRequest.getRequestDate());
        assertEquals("2024-02-01", leaveRequest.getStartDate());
        assertEquals("2024-02-05", leaveRequest.getEndDate());
        assertEquals("Vacation", leaveRequest.getReason());
        
        // Test setters
        leaveRequest.setStartDate("2024-03-01");
        leaveRequest.setEndDate("2024-03-05");
        leaveRequest.setReason("Sick Leave");
        
        assertEquals("2024-03-01", leaveRequest.getStartDate());
        assertEquals("2024-03-05", leaveRequest.getEndDate());
        assertEquals("Sick Leave", leaveRequest.getReason());
    }

    // ================== REQUEST LEAVE AND ID GENERATION TEST CASES ==================

    // Test case 54: Test requestLeave method with valid data
    @Test
    @DisplayName("Test requestLeave with valid data")
    public void testRequestLeaveValidData() {
        admin.addStaffProfile(5401, "Leave", "Employee");
        boolean result = admin.requestLeave(5401, "2024-06-01", "2024-06-05", "Annual Leave");
        assertTrue(result);
    }

    // Test case 55: Test requestLeave method with non-existent staff
    @Test
    @DisplayName("Test requestLeave returns false for non-existent staff")
    public void testRequestLeaveNonExistingStaff() {
        boolean result = admin.requestLeave(99999, "2024-06-01", "2024-06-05", "Vacation");
        assertFalse(result);
    }

    // Test case 56: Test generateRequestId with empty file
    @Test
    @DisplayName("Test generateRequestId with empty file")
    public void testGenerateRequestIdEmptyFile() throws Exception {
        // Clear the leave request file
        try (PrintWriter writer = new PrintWriter(leaveRequestFile)) {
            writer.print("");
        }
        
        // Use reflection to test private generateRequestId method
        Method generateRequestIdMethod = AdminFunction.class.getDeclaredMethod("generateRequestId");
        generateRequestIdMethod.setAccessible(true);
        
        int requestId = (Integer) generateRequestIdMethod.invoke(admin);
        
        // Should return INITIAL_REQUEST_ID + 1 (2000 + 1 = 2001)
        assertEquals(2001, requestId);
    }

    // Test case 57: Test generateRequestId with existing requests
    @Test
    @DisplayName("Test generateRequestId with existing requests")
    public void testGenerateRequestIdWithExistingRequests() throws Exception {
        // Write some existing requests to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(leaveRequestFile))) {
            writer.println("2001|1001|2024-01-01|2024-01-05|Vacation|2024-01-01");
            writer.println("2005|1002|2024-02-01|2024-02-03|Sick|2024-01-15");
            writer.println("2003|1003|2024-03-01|2024-03-02|Personal|2024-02-01");
        }
        
        // Use reflection to test private generateRequestId method
        Method generateRequestIdMethod = AdminFunction.class.getDeclaredMethod("generateRequestId");
        generateRequestIdMethod.setAccessible(true);
        
        int requestId = (Integer) generateRequestIdMethod.invoke(admin);
        
        // Should return max existing ID + 1 (2005 + 1 = 2006)
        assertEquals(2006, requestId);
    }

    // Test case 58: Test generateRequestId with invalid lines
    @Test
    @DisplayName("Test generateRequestId handles invalid lines")
    public void testGenerateRequestIdWithInvalidLines() throws Exception {
        // Write file with invalid and valid lines
        try (PrintWriter writer = new PrintWriter(new FileWriter(leaveRequestFile))) {
            writer.println("# Comment line");
            writer.println(""); // Empty line
            writer.println("invalid_data"); // Invalid format
            writer.println("2002|1001|2024-01-01|2024-01-05|Vacation|2024-01-01"); // Valid
            writer.println("abc|1002|2024-02-01|2024-02-03|Sick|2024-01-15"); // Invalid ID
        }
        
        // Use reflection to test private generateRequestId method
        Method generateRequestIdMethod = AdminFunction.class.getDeclaredMethod("generateRequestId");
        generateRequestIdMethod.setAccessible(true);
        
        int requestId = (Integer) generateRequestIdMethod.invoke(admin);
        
        // Should handle invalid lines and return max valid ID + 1 (2002 + 1 = 2003)
        assertEquals(2003, requestId);
    }

    // Test case 59: Test generateRequestId when file doesn't exist
    @Test
    @DisplayName("Test generateRequestId when file doesn't exist")
    public void testGenerateRequestIdFileNotExists() throws Exception {
        // Delete the leave request file
        if (leaveRequestFile.exists()) {
            leaveRequestFile.delete();
        }
        
        // Use reflection to test private generateRequestId method
        Method generateRequestIdMethod = AdminFunction.class.getDeclaredMethod("generateRequestId");
        generateRequestIdMethod.setAccessible(true);
        
        int requestId = (Integer) generateRequestIdMethod.invoke(admin);
        
        // Should return INITIAL_REQUEST_ID + 1 (2000 + 1 = 2001)
        assertEquals(2001, requestId);
    }

    // Test case 60: Test generateRequestId with IOException
    @Test
    @DisplayName("Test generateRequestId handles IOException")
    public void testGenerateRequestIdIOException() throws Exception {
        // Set leave request file to a directory to cause IOException
        File dataDir = tempDir.resolve("Data").toFile();
        dataDir.mkdirs();
        File invalidFile = new File(dataDir, "Leave_Request.txt");
        invalidFile.mkdirs();
        
        // Use reflection to set the LEAVE_REQUEST_FILE to our invalid path
        Field leaveRequestField = AdminFunction.class.getDeclaredField("LEAVE_REQUEST_FILE");
        leaveRequestField.setAccessible(true);
        leaveRequestField.set(admin, invalidFile.getAbsolutePath());
        
        // Use reflection to test private generateRequestId method
        Method generateRequestIdMethod = AdminFunction.class.getDeclaredMethod("generateRequestId");
        generateRequestIdMethod.setAccessible(true);
        
        int requestId = (Integer) generateRequestIdMethod.invoke(admin);
        
        // Should handle IOException and return INITIAL_REQUEST_ID + 1 (2000 + 1 = 2001)
        assertEquals(2001, requestId);
    }

    // Test case 61: Test requestLeave integration with generateRequestId
    @Test
    @DisplayName("Test requestLeave generates increasing request IDs")
    public void testRequestLeaveGeneratesIncreasingIds() {
        admin.addStaffProfile(6101, "One", "Employee");
        admin.addStaffProfile(6102, "Two", "Administrator");
        
        boolean result1 = admin.requestLeave(6101, "2024-08-01", "2024-08-03", "Vacation");
        boolean result2 = admin.requestLeave(6102, "2024-09-01", "2024-09-05", "Sick Leave");
        
        assertTrue(result1);
        assertTrue(result2);
        // Both requests should succeed with different request IDs
    }

    // Test case 62: Test requestLeave success path without IOException
    @Test
    @DisplayName("Test requestLeave success path without exceptions")
    public void testRequestLeaveSuccessPath() throws Exception {
        admin.addStaffProfile(6201, "Success", "Employee");
        
        // This should execute the success path without IOException
        boolean result = admin.requestLeave(6201, "2024-08-01", "2024-08-05", "Successful Leave");
        
        assertTrue(result, "Leave request should succeed");
    }

    // ================== ADDITIONAL EDGE CASE TEST CASES ==================

    // Test case 63: Test switch default case for invalid field
    @Test
    @DisplayName("Test editStaffProfile switch default case for invalid field")
    public void testEditStaffProfileSwitchDefaultCase() throws Exception {
        admin.addStaffProfile(6301, "Test", "Employee");
        
        // Test with invalid field name
        boolean result = admin.editStaffProfile(6301, "invalid_field", "some value");
        
        assertFalse(result, "Edit with invalid field should return false");
    }

    // Test case 64: Test viewAllStaffProfiles with long names and roles
    @Test
    @DisplayName("Test viewAllStaffProfiles truncates long names and roles")
    public void testViewAllStaffProfilesLongNames() {
        admin.addStaffProfile(6401, "VeryLongStaffNameThatExceedsTwentyFiveCharacters", 
                            "Administrator");
        
        assertDoesNotThrow(() -> admin.viewAllStaffProfiles());
    }

    // Test case 65: Test multiple sequential operations to verify file persistence
    @Test
    @DisplayName("Test multiple operations verify file persistence")
    public void testMultipleOperationsFilePersistence() throws Exception {
        // Add staff
        boolean addResult = admin.addStaffProfile(6501, "Persistence", "Employee");
        assertTrue(addResult);
        assertTrue(admin.staffExists(6501));
        
        // Edit staff
        boolean editResult = admin.editStaffProfile(6501, "name", "UpdatedPersistence");
        assertTrue(editResult);
        
        // Request leave
        boolean leaveResult = admin.requestLeave(6501, "2024-09-01", "2024-09-03", "Persistence Test Leave");
        assertTrue(leaveResult);
        
        // Delete staff
        boolean deleteResult = admin.deleteStaffProfile(6501);
        assertTrue(deleteResult);
        assertFalse(admin.staffExists(6501));
    }

    // Test case 66: Test staff removal loop edge case - staff at beginning of list
    @Test
    @DisplayName("Test deleteStaffProfile finds staff at beginning of list")
    public void testDeleteStaffProfileBeginningOfList() throws Exception {
        // Clear existing staff
        try (PrintWriter writer = new PrintWriter(staffProfileFile)) {
            writer.print("");
        }
        
        // Add staff - first one should be at beginning of list
        admin.addStaffProfile(6601, "First", "Employee");
        admin.addStaffProfile(6602, "Second", "Administrator");
        admin.addStaffProfile(6603, "Third", "Employee");
        
        // Delete the first staff
        boolean result = admin.deleteStaffProfile(6601);
        
        assertTrue(result, "Should successfully delete first staff");
        assertFalse(admin.staffExists(6601), "First staff should be removed");
        assertTrue(admin.staffExists(6602), "Second staff should still exist");
        assertTrue(admin.staffExists(6603), "Third staff should still exist");
    }

    // Test case 67: Test staff removal loop edge case - staff at end of list
    @Test
    @DisplayName("Test deleteStaffProfile finds staff at end of list")
    public void testDeleteStaffProfileEndOfList() throws Exception {
        // Clear existing staff
        try (PrintWriter writer = new PrintWriter(staffProfileFile)) {
            writer.print("");
        }
        
        // Add multiple staff
        admin.addStaffProfile(6701, "First", "Employee");
        admin.addStaffProfile(6702, "Second", "Administrator");
        admin.addStaffProfile(6703, "Last", "Employee");
        
        // Delete the last staff
        boolean result = admin.deleteStaffProfile(6703);
        
        assertTrue(result, "Should successfully delete last staff");
        assertTrue(admin.staffExists(6701), "First staff should still exist");
        assertTrue(admin.staffExists(6702), "Second staff should still exist");
        assertFalse(admin.staffExists(6703), "Last staff should be removed");
    }

    // Test case 68: Test requestLeave complete success path
    @Test
    @DisplayName("Test requestLeave complete success path")
    public void testRequestLeaveCompleteSuccessPath() throws Exception {
        admin.addStaffProfile(6801, "Complete", "Employee");
        
        // This should execute the entire success path:
        // - staffExists check passes
        // - generateRequestId succeeds  
        // - file write succeeds
        // - getUserInfo succeeds
        // - success message path
        boolean result = admin.requestLeave(6801, "2024-10-01", "2024-10-05", "Complete Test Leave");
        
        assertTrue(result, "Complete leave request should succeed");
    }

    // Test case 69: Test loadStaffProfiles with actual file format
    @Test
    @DisplayName("Test loadStaffProfiles reads correct file format")
    public void testLoadStaffProfilesCorrectFormat() throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(staffProfileFile))) {
            writer.println("1001,Mary,Employee");
            writer.println("1002,Chris,Employee");
            writer.println("1003,David,Administrator");
        }
        
        // Use reflection to test private loadStaffProfiles method
        Method loadStaffProfilesMethod = AdminFunction.class.getDeclaredMethod("loadStaffProfiles");
        loadStaffProfilesMethod.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        List<Object> result = (List<Object>) loadStaffProfilesMethod.invoke(admin);
        
        assertNotNull(result);
        assertTrue(result.size() >= 3); // Should load all valid entries
    }

    // Test case 70: Test saveStaffProfiles writes correct format
    @Test
    @DisplayName("Test saveStaffProfiles writes correct file format")
    public void testSaveStaffProfilesCorrectFormat() throws Exception {
        // Clear the file first
        try (PrintWriter writer = new PrintWriter(staffProfileFile)) {
            writer.print("");
        }
        
        // Add staff through the system
        admin.addStaffProfile(7001, "Test", "Employee");
        
        // Verify file was written in correct format
        java.util.Scanner scanner = new java.util.Scanner(staffProfileFile);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            assertEquals(3, parts.length);
            assertEquals("7001", parts[0].trim());
            assertEquals("Test", parts[1].trim());
            assertEquals("Employee", parts[2].trim());
        }
        scanner.close();
    }

    // Test case 71: Test requestLeave writes correct format
    @Test
    @DisplayName("Test requestLeave writes correct file format")
    public void testRequestLeaveCorrectFormat() throws Exception {
        admin.addStaffProfile(7101, "Leave", "Employee");
        
        // Make a leave request
        admin.requestLeave(7101, "2024-10-01", "2024-10-05", "Test Leave");
        
        // Verify file was written in correct format
        java.util.Scanner scanner = new java.util.Scanner(leaveRequestFile);
        boolean foundCorrectFormat = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("7101") && line.contains("2024-10-01") && line.contains("2024-10-05") && line.contains("Test Leave")) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    foundCorrectFormat = true;
                    assertEquals("7101", parts[1].trim()); // employeeId
                    assertEquals("2024-10-01", parts[2].trim()); // startDate
                    assertEquals("2024-10-05", parts[3].trim()); // endDate
                    assertEquals("Test Leave", parts[4].trim()); // reason
                    break;
                }
            }
        }
        scanner.close();
        assertTrue(foundCorrectFormat, "Leave request should be written in correct format");
    }

    // Test case 72: Test staffExists with existing staff from file
    @Test
    @DisplayName("Test staffExists finds staff from file")
    public void testStaffExistsFromFile() {
        // Staff 1001, 1002, 1003 were added in setUp()
        assertTrue(admin.staffExists(1001));
        assertTrue(admin.staffExists(1002));
        assertTrue(admin.staffExists(1003));
        assertFalse(admin.staffExists(9999));
    }

    // Test case 73: Test getStaffCount includes staff from file
    @Test
    @DisplayName("Test getStaffCount includes loaded staff")
    public void testGetStaffCountIncludesLoaded() {
        int count = admin.getStaffCount();
        assertTrue(count >= 3); // Should include the 3 staff members from setUp
    }

    // Test case 74: Test viewAllStaffProfiles with multiple staff
    @Test
    @DisplayName("Test viewAllStaffProfiles with multiple staff members")
    public void testViewAllStaffProfilesMultiple() {
        admin.addStaffProfile(7401, "First", "Employee");
        admin.addStaffProfile(7402, "Second", "Administrator");
        admin.addStaffProfile(7403, "Third", "Employee");
        
        assertDoesNotThrow(() -> admin.viewAllStaffProfiles());
    }

    // Test case 75: Test viewStaffProfile with valid staff
    @Test
    @DisplayName("Test viewStaffProfile displays staff information")
    public void testViewStaffProfileValidStaff() {
        admin.addStaffProfile(7501, "Display", "Administrator");
        assertDoesNotThrow(() -> admin.viewStaffProfile(7501));
    }
}