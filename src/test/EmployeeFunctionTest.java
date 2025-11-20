package test;

import org.junit.jupiter.api.*;
import staffRosteringSystem.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bottom-up comprehensive tests for BaseFunction, EmployeeFunction, and ShiftManager
 * Tests isolated methods first, then builds to integration tests
 */
class ComprehensiveRosteringSystemTest {

    private static Path testDataDir;
    private static String shiftFile;
    private static String staffProfileFile;
    private static String dutyRequestFile;
    private static String leaveRequestFile;

    private ShiftManager shiftManager;
    private StaffManager staffManager;
    private EmployeeFunction employeeFunction;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeAll
    static void setupTestEnvironment() throws IOException {
        // Create test Data directory
        testDataDir = Paths.get("Data");
        Files.createDirectories(testDataDir);
        
        shiftFile = testDataDir.resolve("Shift.txt").toString();
        staffProfileFile = testDataDir.resolve("Staff_Profile.txt").toString();
        dutyRequestFile = testDataDir.resolve("Duty_Request.txt").toString();
        leaveRequestFile = testDataDir.resolve("Leave_Request.txt").toString();
    }

    @BeforeEach
    void setUp() throws IOException {
        // Clean and create fresh files
        createFreshFiles();
        
        // Initialize managers
        staffManager = new StaffManager();
        staffManager.addStaffProfile(101, "John Doe", "Employee");
        staffManager.addStaffProfile(102, "Jane Smith", "Manager");
        staffManager.addStaffProfile(103, "Bob Wilson", "Employee");
        
        shiftManager = new ShiftManager(staffManager);
        
        // Create test instances
        employeeFunction = new EmployeeFunction(101, "John Doe", "password123", 
                                                shiftManager, staffManager);
        
        // Setup I/O redirection
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originalIn = System.in;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        outputStream.reset();
    }

    @AfterAll
    static void cleanup() throws IOException {
        // Clean up test files
        Files.deleteIfExists(Paths.get(shiftFile));
        Files.deleteIfExists(Paths.get(staffProfileFile));
        Files.deleteIfExists(Paths.get(dutyRequestFile));
        Files.deleteIfExists(Paths.get(leaveRequestFile));
    }

    private void createFreshFiles() throws IOException {
        // Staff Profile
        try (PrintWriter writer = new PrintWriter(staffProfileFile)) {
            writer.println("101,John Doe,Employee");
            writer.println("102,Jane Smith,Manager");
            writer.println("103,Bob Wilson,Employee");
        }
        
        // Empty shift, duty, and leave files
        new PrintWriter(shiftFile).close();
        new PrintWriter(dutyRequestFile).close();
        new PrintWriter(leaveRequestFile).close();
    }

    private void provideInput(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        System.setIn(inputStream);
    }

    private String getOutput() {
        return outputStream.toString();
    }

    /* ================================================================
       EmployeeFunction - addDuty Method (3)
       ================================================================ */

    @Test
    void testAddDuty_Success() throws IOException, FileNotFoundException {
        boolean result = employeeFunction.addDutyRequest("101", "2025-12-25", "MORNING");
        assertTrue(result);
    }

    @Test
    void testAddDuty_VerifyFileContent() throws IOException,FileNotFoundException {
        employeeFunction.addDutyRequest("101", "2025-12-25", "MORNING");
        
        List<String> lines = Files.readAllLines(Paths.get(dutyRequestFile));
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("101"));
        assertTrue(lines.get(0).contains("2025-12-25"));
        assertTrue(lines.get(0).contains("MORNING"));
    }

    @Test
    void testAddDuty_Duplicate() throws IOException, FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(dutyRequestFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        boolean result = employeeFunction.addDutyRequest("101", "2025-12-25", "MORNING");
        
        assertFalse(result);
        String output = getOutput();
        assertTrue(output.contains("already have duty"));
    }


    /* ================================================================
       EmployeeFunction - checkDuty Method (3)
       ================================================================ */

    @Test
    void testEmployeeFunction_CheckDuty_DutyExists() throws Exception {
        try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        boolean result = employeeFunction.checkDuty("101", "2025-12-20", "2025-12-30");
        assertTrue(result);
    }

    @Test
    void testEmployeeFunction_CheckDuty_NoDuty() throws Exception {
        boolean result = employeeFunction.checkDuty("101", "2025-12-20", "2025-12-30");
        
        assertFalse(result);
        String output = getOutput();
        assertTrue(output.contains("do not have a duty"));
    }

    @Test
    void testEmployeeFunction_CheckDuty_FileNotFound() throws Exception {
        Files.delete(Paths.get(shiftFile));
        
        boolean result = employeeFunction.checkDuty("101", "2025-12-20", "2025-12-30");
        
        assertFalse(result);
        String output = getOutput();
        assertTrue(output.contains("No duty requests found"));
    }
    
    /* ================================================================
    EmployeeFunction - requestDuty Method (5)
    ================================================================ */
    @Test
    void testRequestDuty_Success() {
        provideInput("2025-12-25\nMORNING\n");
        employeeFunction.requestDuty("101");
        
        assertTrue(getOutput().contains("Duty request submitted successfully"));
    }

    @Test
    void testRequestDuty_InvalidDate() {
        provideInput("invalid-date\n");
        employeeFunction.requestDuty("101");
        
        assertFalse(getOutput().contains("Duty request submitted successfully"));
    }
    
    @Test
    void testRequestDuty_InvalidSession() {
        provideInput("2025-12-25\nINVALID\n");
        employeeFunction.requestDuty("101");
        
        String output = getOutput();
        assertTrue(output.contains("Invalid session"));
        assertFalse(output.contains("Duty request submitted successfully"));
    }

    @Test
    void testRequestDuty_ValidSessionUppercase() {
        provideInput("2025-12-25\nAFTERNOON\n");
        employeeFunction.requestDuty("101");
        
        assertTrue(getOutput().contains("Duty request submitted successfully"));
    }

    @Test
    void testRequestDuty_ValidSessionLowercase() {
        provideInput("2025-12-25\nnight\n");
        employeeFunction.requestDuty("101");
        
        assertTrue(getOutput().contains("Duty request submitted successfully"));
    }

    /* ================================================================
    EmployeeFunction - requestLeave Method (6)
    ================================================================ */
    @Test
    void testRequestLeave_Success() throws IOException {
    	try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        provideInput("2025-12-20\n2025-12-30\nAnnual\nVacation\n");
        employeeFunction.requestLeave("101");
        
        String output = getOutput();
        assertTrue(output.contains("Leave request submitted successfully"));
    }
    
    @Test
    void testRequestLeave_InvalidStartDate() throws IOException {
    	try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        provideInput("invalid-date\n2025-12-30\nAnnual\nVacation\n");
        employeeFunction.requestLeave("101");
        
        String output = getOutput();
        assertFalse(output.contains("Leave request submitted successfully"));
    }
    
    @Test
    void testRequestLeave_InvalidEndDate() throws IOException {
    	try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        // Provide valid start date, then invalid end date, then valid retry
        provideInput("2025-12-20\ninvalid-date\n2025-12-30\nAnnual\nVacation\n");
        employeeFunction.requestLeave("101");
        
        String output = getOutput();
        assertTrue(output.contains("Leave request submitted successfully"));
    }
    
    @Test
    void testRequestLeave_EndDateBeforeStartDate() throws IOException {
    	try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        provideInput("2025-12-30\n2025-12-20\n2025-12-31\nAnnual\nVacation\n");
        employeeFunction.requestLeave("101");
        
        String output = getOutput();
        assertTrue(output.contains("End date cannot be earlier than start date"));
    }
    
    @Test
    void testRequestLeave_EmptyLeaveType() throws IOException {
    	try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        // Empty leave type, then valid one
        provideInput("2025-12-20\n2025-12-30\n\nSick\nFlu\n");
        employeeFunction.requestLeave("101");
        
        String output = getOutput();
        assertTrue(output.contains("Input cannot be empty"));
        assertTrue(output.contains("Leave request submitted successfully"));
    }
    
    @Test
    void testRequestLeave_EmptyReason() throws IOException {
    	try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        // Empty reason, then valid one
        provideInput("2025-12-20\n2025-12-30\nAnnual\n\nVacation\n");
        employeeFunction.requestLeave("101");
        
        String output = getOutput();
        assertTrue(output.contains("Input cannot be empty"));
        assertTrue(output.contains("Leave request submitted successfully"));
    }
    
    
    /* ================================================================
       EmployeeFunction - Login Method (4)
       ================================================================ */

    @Test
    void testEmployeeFunction_Login_Success() {
        provideInput("4\n"); // Logout immediately
        
        boolean result = employeeFunction.login("John Doe", "anyPassword");
        
        assertTrue(result);
        String output = getOutput();
        assertTrue(output.contains("Employee login successful"));
    }

    @Test
    void testEmployeeFunction_Login_NonEmployee() {
        boolean result = employeeFunction.login("Jane Smith", "password");
        
        assertFalse(result);
        String output = getOutput();
        assertTrue(output.contains("Invalid. Please try again"));
    }

    @Test
    void testEmployeeFunction_Login_InvalidUser() {
        boolean result = employeeFunction.login("NonExistent", "password");
        
        assertFalse(result);
    }
    
    @Test
    void testLogin_IOExceptionHandling() throws IOException {
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errorStream));
        
        // Delete the staff profile file to cause IOException
        Files.delete(Paths.get(staffProfileFile));
        
        boolean result = employeeFunction.login("John Doe", "password");
        
        assertFalse(result);
        assertTrue(errorStream.toString().contains("Error reading staff profiles"));
        
        System.setErr(originalErr);
        createFreshFiles(); // Restore files
    }

    /* ================================================================
    EmployeeFunction - LoginPage Method ()4
    ================================================================ */
    @Test
    void testLoginPage_ViewShiftSchedule() throws IOException {
        String today = java.time.LocalDate.now().toString();
        try (PrintWriter writer = new PrintWriter(shiftFile)) {
            writer.println("1,101," + today + ",MORNING,08:00,16:00,Regular");
        }
        
        provideInput("1\n4\n"); // Option 1, then logout
        employeeFunction.loginPage("101");
        
        String output = getOutput();
        assertTrue(output.contains("SHIFT SCHEDULE FOR " + today));
        assertTrue(output.contains("Logging out"));
    }
    
    @Test
    void testLoginPage_RequestDuty() {
        provideInput("2\n2025-12-25\nMORNING\n4\n"); // Option 2, then logout
        employeeFunction.loginPage("101");
        String output = getOutput();
        assertTrue(output.contains("Logging out..."));
    }
    
    @Test
    void testLoginPage_RequestLeave() throws IOException {
        try (PrintWriter writer = new PrintWriter(dutyRequestFile)) {
            writer.println("1,101,2025-12-25,MORNING");
        }
        
        provideInput("3\n2025-12-20\n2025-12-30\nAnnual\nVacation\n4\n"); // Option 3, then logout
        employeeFunction.loginPage("101");
        String output = getOutput();
        assertTrue(output.contains("Logging out..."));
    }
    
    @Test
    void testLoginPage_InvalidChoice() {
        provideInput("9\n4\n"); // Invalid option, then logout
        employeeFunction.loginPage("101");
        
        String output = getOutput();
        assertTrue(output.contains("Invalid choice. Please try again"));
    }
    

}