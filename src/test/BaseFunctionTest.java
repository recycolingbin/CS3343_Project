package test;

import org.junit.*;

import staffRosteringSystem.BaseFunction;
import staffRosteringSystem.FileOperations;
import staffRosteringSystem.ShiftManager;
import staffRosteringSystem.ShiftSession;
import staffRosteringSystem.StaffManager;
import staffRosteringSystem.StaffProfile;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BaseFunctionTest {
    private static final String TEST_SHIFT_FILE = "test_data/Shift.txt";
    private static final String TEST_STAFF_FILE = "test_data/Staff_Profile.txt";
    
    private TestableBaseFunction baseFunction;
    private ShiftManager shiftManager;
    private StaffManager staffManager;
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        // Create test data directory
        Files.createDirectories(Paths.get("test_data"));
    }
    
    @Before
    public void setUp() throws IOException {
        // Create test files with sample data
        createTestShiftFile();
        createTestStaffFile();
        
        // Initialize managers with test file paths
        FileOperations fileOps = new FileOperations();
        staffManager = new StaffManager(TEST_STAFF_FILE, fileOps);
        shiftManager = new ShiftManager(TEST_SHIFT_FILE, staffManager, fileOps);
        
        // Initialize base function with both managers
        baseFunction = new TestableBaseFunction(1, "testuser", "password123", shiftManager, staffManager);
    }
    
    @After
    public void tearDown() throws IOException {
        // Clean up test files
        Files.deleteIfExists(Paths.get(TEST_SHIFT_FILE));
        Files.deleteIfExists(Paths.get(TEST_STAFF_FILE));
    }
    
    // ========== UTILITY METHOD TESTS ==========
    
    @Test
    public void testIsValidSession_Valid() {
        assertTrue(baseFunction.isValidSession("MORNING"));
        assertTrue(baseFunction.isValidSession("AFTERNOON"));
        assertTrue(baseFunction.isValidSession("NIGHT"));
    }
   
    @Test
    public void testIsValidSession_CaseInsensitive() {
        assertTrue(baseFunction.isValidSession("morning"));
        assertTrue(baseFunction.isValidSession("afternoon"));
        assertTrue(baseFunction.isValidSession("night"));
    }
    
    @Test
    public void testIsValidSession_Invalid() {
        assertFalse(baseFunction.isValidSession("INVALID"));
        assertFalse(baseFunction.isValidSession(""));
        assertFalse(baseFunction.isValidSession(null));
    }
    
    @Test
    public void testGetSessionOrder() {
        assertEquals(1, baseFunction.getSessionOrder(ShiftSession.MORNING));
        assertEquals(2, baseFunction.getSessionOrder(ShiftSession.AFTERNOON));
        assertEquals(3, baseFunction.getSessionOrder(ShiftSession.NIGHT));
    }

    
    // ========== DATE VALIDATION TESTS ==========
    
    @Test
    public void testIsValidDate_Valid() {
        assertTrue(baseFunction.isValidDate("2025-11-15"));
        assertTrue(baseFunction.isValidDate("2025-01-01"));
        assertTrue(baseFunction.isValidDate("2025-12-31"));
    }
    
    @Test
    public void testIsValidDate_LeapYear() {
        assertTrue(baseFunction.isValidDate("2024-02-29"));
        assertFalse(baseFunction.isValidDate("2025-02-29"));
    }
    
    @Test
    public void testIsValidDate_InvalidFormat() {
        assertFalse(baseFunction.isValidDate("15-11-2025"));
        assertFalse(baseFunction.isValidDate("2025/11/15"));
        assertFalse(baseFunction.isValidDate("2025-11"));
    }
    
    @Test
    public void testIsValidDate_InvalidMonth() {
        assertFalse(baseFunction.isValidDate("2025-13-01"));
        assertFalse(baseFunction.isValidDate("2025-00-01"));
    }
    
    @Test
    public void testIsValidDate_InvalidDay() {
        assertFalse(baseFunction.isValidDate("2025-11-32"));
        assertFalse(baseFunction.isValidDate("2025-04-31"));
        assertFalse(baseFunction.isValidDate("2025-02-30"));
    }
    
    @Test
    public void testIsValidDate_InvalidYear() {
        assertFalse(baseFunction.isValidDate("2019-11-15"));
        assertFalse(baseFunction.isValidDate("2031-11-15"));
    }
    
    @Test
    public void testIsValidDate_NullOrEmpty() {
        assertFalse(baseFunction.isValidDate(null));
        assertFalse(baseFunction.isValidDate(""));
        assertFalse(baseFunction.isValidDate("   "));
    }
    
    @Test
    public void testIsValidDate_NonNumeric() {
        assertFalse(baseFunction.isValidDate("abcd-ef-gh"));
        assertFalse(baseFunction.isValidDate("2025-ab-15"));
    }
    
    // ========== LOGIN TESTS ==========
    
    @Test
    public void testLogin_Success() {
        assertTrue(baseFunction.login("testuser", "password123"));
    }
    
    @Test
    public void testLogin_WrongPassword() {
        assertFalse(baseFunction.login("testuser", "123"));
    }
    
    @Test
    public void testLogin_WrongUsernameAndPassword() {
        assertFalse(baseFunction.login("wronguser", "wrongpassword"));
    }
    
    // ========== GETTER TESTS ==========
    
    @Test
    public void testGetUserId() {
        assertEquals(1, baseFunction.getUserId());
    }
    
    @Test
    public void testGetUsername() {
        assertEquals("testuser", baseFunction.getUsername());
    }
    

    // ========== VIEW SHIFT SCHEDULE TESTS ==========
    
    @Test
    public void testViewShiftSchedule_WithShifts() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        baseFunction.viewShiftSchedule("2025-11-15");
        
        String output = outputStream.toString();
        assertTrue(output.contains("SHIFT SCHEDULE FOR 2025-11-15"));        
        System.setOut(System.out);
    }
    
    @Test
    public void testViewShiftSchedule_NoShifts() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        baseFunction.viewShiftSchedule("2025-12-25");
        
        String output = outputStream.toString();
        assertTrue(output.contains("No shifts scheduled for 2025-12-25"));
        
        System.setOut(System.out);
    }
    
    // ========== HELPER METHODS ==========
    
    private void createTestShiftFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_SHIFT_FILE))) {
            writer.println("1,1,2025-11-15,MORNING,08:00,16:00,Regular shift");
            writer.println("2,2,2025-11-15,AFTERNOON,14:00,22:00,Regular shift");
            writer.println("3,1,2025-11-16,MORNING,08:00,16:00,Regular shift");
            writer.println("4,1,2025-11-16,AFTERNOON,14:00,22:00,Regular shift");
        }
    }
    
    private void createTestStaffFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_STAFF_FILE))) {
            writer.println("1,John Doe,Employee");
            writer.println("2,Jane Smith,Manager");
            writer.println("3,Bob Johnson,Employee");
        }
    }
    
    // ========== TESTABLE CONCRETE CLASS ==========
    
    private static class TestableBaseFunction extends BaseFunction {
        public TestableBaseFunction(int userId, String username, String password, 
                                   ShiftManager shiftManager, StaffManager staffManager) {
            super(userId, username, password, shiftManager, staffManager);
        }
    }
}