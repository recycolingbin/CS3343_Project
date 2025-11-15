package test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import staffRosteringSystem.AdminFunction;
import staffRosteringSystem.FileOperations;
import staffRosteringSystem.Shift;
import staffRosteringSystem.ShiftManager;
import staffRosteringSystem.ShiftSession;
import staffRosteringSystem.StaffManager;
import staffRosteringSystem.StaffProfile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

// ==================== ShiftManager Tests ====================
class ShiftManagerTest {
    
    @TempDir
    Path tempDir;
    
    private StaffManager staffManager;
    private FileOperations fileOps;
    private String testShiftFile;
    
    @BeforeEach
    void setUp() throws IOException {
        // Create a test shift file
        testShiftFile = tempDir.resolve("TestShift.txt").toString();
        
        // Create a real FileOperations instance
        fileOps = new FileOperations();
        
        // Create a simple StaffManager implementation
        staffManager = new TestStaffManager();
    }
    
    @Test
    void testConstructorWithParameters() {
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        assertNotNull(manager);
    }
    
    @Test
    void testConstructorWithStaffManagerOnly() {
        ShiftManager manager = new ShiftManager(staffManager);
        assertNotNull(manager);
    }
    
    //loadShifts tests
    @Test
    void testLoadShiftsEmptyFile() throws IOException {
        Files.createFile(Paths.get(testShiftFile));
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        List<Shift> shifts = manager.loadShifts();
        
        assertTrue(shifts.isEmpty());
    }
    
    @Test
    void testLoadShiftsWithData() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,Test note\n" +
                        "3002,1002,2025-01-16,AFTERNOON,14:00,22:00,Another note\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        List<Shift> shifts = manager.loadShifts();
        
        assertEquals(2, shifts.size());
        assertEquals(3001, shifts.get(0).getShiftId());
        assertEquals(3002, shifts.get(1).getShiftId());
    }
    
    @Test
    void testSaveShiftsSuccess() {
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        List<Shift> shifts = new ArrayList<>();
        shifts.add(Shift.create(3001, 1001, "2025-01-15", ShiftSession.MORNING, "Note"));
        
        assertTrue(manager.saveShifts(shifts));
    }
    
    @Test
    void testSaveShiftsNull() {
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        assertFalse(manager.saveShifts(null));
    }
    
    //AssignShifts tests
    @Test
    void testAssignMultipleEmployeesSameDateSameSession() throws IOException {
        Files.createFile(Paths.get(testShiftFile));
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", 
        		staffManager, null, manager, new Scanner(""));
        boolean result1 = adminFunction.assignShift(1001, "2025-01-15", "MORNING", "Employee 1");
        boolean result2 = adminFunction.assignShift(1002, "2025-01-15", "MORNING", "Employee 2");
        boolean result3 = adminFunction.assignShift(1003, "2025-01-15", "MORNING", "Employee 3");
        
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        
        List<Shift> shifts = manager.loadShifts();
        assertEquals(3, shifts.size());
    }
    
    @Test
    void testAssignShiftEmployeeNotFound() throws IOException {
        Files.createFile(Paths.get(testShiftFile));
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        boolean result = manager.assignShift(9999, "2025-01-15", "MORNING", "Test note");
        
        assertFalse(result);
    }
    
    @Test
    void testAssignShiftInvalidSession() throws IOException {
        Files.createFile(Paths.get(testShiftFile));
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        boolean result = manager.assignShift(1001, "2025-01-15", "INVALID", "Test note");
        
        assertFalse(result);
    }
    
    @Test
    void testAssignShiftConflict() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,Existing shift\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        boolean result = manager.assignShift(1001, "2025-01-15", "MORNING", "Duplicate");
        
        assertFalse(result);
    }
    
    @Test
    void testAssignShiftDifferentSessionSameDate() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,Morning shift\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        boolean result = manager.assignShift(1001, "2025-01-15", "AFTERNOON", "Afternoon shift");
        
        assertTrue(result);
    }
    
    @Test
    void testAssignShiftWithEmptyNotes() throws IOException {
        Files.createFile(Paths.get(testShiftFile));
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        boolean result = manager.assignShift(1001, "2025-01-15", "MORNING", "");
        
        assertTrue(result);
    }
    
    @Test
    void testAssignMultipleShifts() throws IOException {
        Files.createFile(Paths.get(testShiftFile));
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        manager.assignShift(1001, "2025-01-15", "MORNING", "Shift 1");
        manager.assignShift(1002, "2025-01-16", "AFTERNOON", "Shift 2");
        manager.assignShift(1003, "2025-01-17", "NIGHT", "Shift 3");
        
        List<Shift> shifts = manager.loadShifts();
        assertEquals(3, shifts.size());
        assertEquals(3000, shifts.get(0).getShiftId());
        assertEquals(3001, shifts.get(1).getShiftId());
        assertEquals(3002, shifts.get(2).getShiftId());
    }
    
    //DeleteShifts tests  
    @Test
    void testDeleteShiftSuccess() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,Test note\n" +
                        "3002,1002,2025-01-16,AFTERNOON,14:00,22:00,Another note\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", 
        		staffManager, null, manager, new Scanner(""));
        
        boolean result = adminFunction.deleteShift(3001);
        
        assertTrue(result);
        List<Shift> shifts = manager.loadShifts();
        assertEquals(1, shifts.size());
        assertEquals(3002, shifts.get(0).getShiftId());
    }
    
    @Test
    void testDeleteShiftNotFound() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,Test note\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        boolean result = manager.deleteShift(9999);
        
        assertFalse(result);
    }
    
    @Test
    void testDeleteShiftWithNullStaffProfile() throws IOException {
        String content = "3001,9999,2025-01-15,MORNING,06:00,14:00,Test note\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        boolean result = manager.deleteShift(3001);
        
        assertTrue(result);
    }
    
    @Test
    void testDeleteAllShifts() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,Note 1\n" +
                        "3002,1002,2025-01-16,AFTERNOON,14:00,22:00,Note 2\n" +
                        "3003,1003,2025-01-17,NIGHT,22:00,06:00,Note 3\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        manager.deleteShift(3001);
        manager.deleteShift(3002);
        manager.deleteShift(3003);
        
        List<Shift> shifts = manager.loadShifts();
        assertTrue(shifts.isEmpty());
    }
    
    @Test
    void testViewAllShiftSchedulesEmpty() throws IOException {
        Files.createFile(Paths.get(testShiftFile));
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        manager.viewAllShiftSchedules();
        // Just verify it doesn't throw an exception
    }
    
    @Test
    void testViewAllShiftSchedulesWithData() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,Test note\n" +
                        "3002,1002,2025-01-16,AFTERNOON,14:00,22:00,Another note\n" +
                        "3003,1003,2025-01-17,NIGHT,22:00,06:00,Night shift\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", 
        		staffManager, null, manager, new Scanner(""));
        adminFunction.viewAllShiftSchedules();
        // Just verify it doesn't throw an exception
    }
        
    @Test
    void testViewAllShiftSchedulesWithLongNotes() throws IOException {
        String content = "3001,1001,2025-01-15,MORNING,06:00,14:00,This is a very long note that exceeds twenty characters\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        manager.viewAllShiftSchedules();
        // Just verify it doesn't throw an exception
    }
    
    @Test
    void testViewAllShiftSchedulesWithNullStaffProfile() throws IOException {
        String content = "3001,9999,2025-01-15,MORNING,06:00,14:00,Test note\n";
        Files.write(Paths.get(testShiftFile), content.getBytes());
        
        ShiftManager manager = new ShiftManager(testShiftFile, staffManager, fileOps);
        
        manager.viewAllShiftSchedules();
        // Just verify it doesn't throw an exception
    }
    
    // Helper class - Simple StaffManager implementation
    
    private static class TestStaffManager extends StaffManager {
        private Map<Integer, StaffProfile> staffMap = new HashMap<>();
        
        public TestStaffManager() {
            super();
            // Add test staff data
            staffMap.put(1001, new StaffProfile(1001, "Alice Smith", "Employee"));
            staffMap.put(1002, new StaffProfile(1002, "Bob Jones", "Employee"));
            staffMap.put(1003, new StaffProfile(1003, "Carol White", "Manager"));
        }
        
        @Override
        public boolean staffExists(int staffId) {
            return staffMap.containsKey(staffId);
        }
        
        @Override
        public StaffProfile getStaffInfo(int staffId) {
            return staffMap.get(staffId);
        }
    }
    
}