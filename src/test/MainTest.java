package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import org.junit.jupiter.api.*;

import staffRosteringSystem.Main;

public class MainTest {
    
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;
    
    @BeforeEach
    void setUp() {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originalIn = System.in;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
    
    @AfterAll
    static void cleanup() {
        // Clean up test data files
        try {
            deleteDirectory(new File("Data"));
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }
    
    // ==================== Exit Option Tests ====================
    
    @Test
    void testMainExit() throws Exception {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to the Roster Management System"));
        assertTrue(output.contains("See you next time :)"));
    }
    
    // ==================== Employee Login Tests ====================
    
    @Test
    void testEmployeeLoginSuccess() throws Exception {
        // First, ensure data files exist with test data
        setupTestDataFiles();
        
        // Input: 1 (Employee login), username, password, 4 (logout from employee menu), 3 (exit)
        String input = "1\nJohn Doe\npassword123\n4\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter Employee Username:"));
        assertTrue(output.contains("Enter Password:"));
        // The output depends on EmployeeFunction implementation
    }
    
    @Test
    void testEmployeeLoginFailure() throws Exception {
        setupTestDataFiles();
        
        // Input: 1 (Employee login), invalid username, invalid password, 3 (exit)
        String input = "1\nInvalidUser\nwrongpassword\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter Employee Username:"));
        assertTrue(output.contains("Enter Password:"));
        assertTrue(output.contains("Invalid credentials. Please try again."));
    }
    
    // ==================== Administrator Login Tests ====================
    
    @Test
    void testAdminLoginSuccess() throws Exception {
        setupTestDataFiles();
        
        // Input: 2 (Admin login), admin, admin123, 5 (logout from admin menu), 3 (exit)
        String input = "2\nadmin\nadmin123\n5\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter Administrator Username:"));
        assertTrue(output.contains("Enter Password:"));
        assertTrue(output.contains("Administrator login successful."));
    }
    
    @Test
    void testAdminLoginInvalidUsername() throws Exception {
        setupTestDataFiles();
        
        // Input: 2 (Admin login), wrong username, password, 3 (exit)
        String input = "2\nwrongadmin\nadmin123\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter Administrator Username:"));
        assertTrue(output.contains("Invalid username or password. Please try again :("));
    }
    
    @Test
    void testAdminLoginInvalidPassword() throws Exception {
        setupTestDataFiles();
        
        // Input: 2 (Admin login), correct username, wrong password, 3 (exit)
        String input = "2\nadmin\nwrongpassword\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter Administrator Username:"));
        assertTrue(output.contains("Invalid username or password. Please try again :("));
    }
    
    // ==================== Invalid Input Tests ====================
    
    @Test
    void testInvalidMenuChoice() throws Exception {
        // Input: 99 (invalid choice), 3 (exit)
        String input = "99\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid choice. Please try again :("));
    }
    
    @Test
    void testInvalidMenuChoiceZero() throws Exception {
        // Input: 0 (invalid choice), 3 (exit)
        String input = "0\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid choice. Please try again :("));
    }
    
    @Test
    void testInvalidMenuChoiceNegative() throws Exception {
        // Input: -1 (invalid choice), 3 (exit)
        String input = "-1\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid choice. Please try again :("));
    }
    
    @Test
    void testInvalidInputString() throws Exception {
        // Input: abc (invalid input), 3 (exit)
        String input = "abc\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input! Please enter a number between 1-3."));
    }
    
    @Test
    void testMultipleInvalidInputs() throws Exception {
        // Input: abc, xyz, 999, then 3 (exit)
        String input = "abc\nxyz\n999\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input! Please enter a number between 1-3."));
        assertTrue(output.contains("Invalid choice. Please try again :("));
        assertTrue(output.contains("See you next time :)"));
    }
    
    // ==================== Data File Initialization Tests ====================
    
    @Test
    void testDataDirectoryCreation() throws Exception {
        // Delete Data directory if it exists
        deleteDirectory(new File("Data"));
        
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        File dataDir = new File("Data");
        assertTrue(dataDir.exists(), "Data directory should be created");
        assertTrue(dataDir.isDirectory(), "Data should be a directory");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Created Data directory."));
    }
    
    @Test
    void testDataFilesCreation() throws Exception {
        // Delete Data directory if it exists
        deleteDirectory(new File("Data"));
        
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Created Data/Staff_Profile.txt"));
        assertTrue(output.contains("Created Data/Duty_Request.txt"));
        assertTrue(output.contains("Created Data/Leave_Request.txt"));
        assertTrue(output.contains("Created Data/Shift.txt"));
        
        // Verify files exist
        assertTrue(new File("Data/Staff_Profile.txt").exists());
        assertTrue(new File("Data/Duty_Request.txt").exists());
        assertTrue(new File("Data/Leave_Request.txt").exists());
        assertTrue(new File("Data/Shift.txt").exists());
    }
    
    @Test
    void testStaffProfileInitialData() throws Exception {
        // Delete Data directory if it exists
        deleteDirectory(new File("Data"));
        
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        // Read Staff_Profile.txt and verify initial data
        File staffFile = new File("Data/Staff_Profile.txt");
        String content = new String(Files.readAllBytes(staffFile.toPath()));
        
        assertTrue(content.contains("1001,John Doe,Employee,IT,50000.0"));
        assertTrue(content.contains("2001,admin,Administrator,Management,80000.0"));
    }
    
    @Test
    void testDataFilesAlreadyExist() throws Exception {
        // First run - create files
        setupTestDataFiles();
        
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Main.main(new String[] {});
        
        String output = outputStream.toString();
        // Should NOT contain "Created Data/..." messages since files already exist
        assertFalse(output.contains("Created Data/Staff_Profile.txt"));
    }
    
    // ==================== Helper Methods ====================
    
    private void setupTestDataFiles() throws IOException {
        File dataDir = new File("Data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Create Staff_Profile.txt with test data
        File staffFile = new File("Data/Staff_Profile.txt");
        try (FileWriter writer = new FileWriter(staffFile)) {
            writer.write("1001,John Doe,Employee,IT,50000.0\n");
            writer.write("1002,Jane Smith,Employee,HR,55000.0\n");
            writer.write("2001,admin,Administrator,Management,80000.0\n");
        }
        
        // Create other files
        new File("Data/Duty_Request.txt").createNewFile();
        new File("Data/Leave_Request.txt").createNewFile();
        new File("Data/Shift.txt").createNewFile();
    }
    
    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}

