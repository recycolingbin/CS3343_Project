package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.AdminFunction;
import staffRosteringSystem.DutyRequest;
import staffRosteringSystem.EmployeeFunction;
import staffRosteringSystem.LeaveRequest;
import staffRosteringSystem.Main;
import staffRosteringSystem.RequestManager;
import staffRosteringSystem.StaffManager;

@DisplayName("Main Extended Tests")
    class MainExtendedTests {
        
        @Test
        @DisplayName("Main with employee login path")
        void testMainEmployeeLoginPath() {
            String input = "1\nJohn Doe\npassword\n4\n";  // Employee login, then logout (option 4), then exit
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
              
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("Main with admin login path")
        void testMainAdminLoginPath() {
            String input = "2\nadmin\nadmin\n5\n";  // Admin login, invalid menu choice, then exit to login screen
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("Main with direct exit")
        void testMainDirectExit() {
            String input = "3\n";  // Exit option
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                assertDoesNotThrow(() -> Main.main(new String[] {}));
                String output = out.toString();
                assertTrue(output.contains("See you next time"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("Main with invalid menu choice then valid")
        void testMainInvalidThenValidChoice() {
            String input = "99\n3\n";  // Invalid choice then exit
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                assertDoesNotThrow(() -> Main.main(new String[] {}));
                String output = out.toString();
                assertTrue(output.contains("Invalid choice"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("Main with non-numeric input then valid exit")
        void testMainNonNumericInput() {
            String input = "abc\n3\n";  // Non-numeric input then exit
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                assertDoesNotThrow(() -> Main.main(new String[] {}));
                String output = out.toString();
                assertTrue(output.contains("Invalid input"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("Main initializes data files correctly")
        void testMainInitializeDataFiles() {
            File dataDir = new File("Data");
            File[] dataFiles = {
                new File("Data/Staff_Profile.txt"),
                new File("Data/Duty_Request.txt"),
                new File("Data/Leave_Request.txt"),
                new File("Data/Shift.txt")
            };
            
            // Call main with exit option to trigger initialization
            String input = "3\n";
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                Main.main(new String[] {});
                
                // Verify data directory exists
                assertTrue(dataDir.exists() && dataDir.isDirectory());
                
                // Verify data files exist
                for (File f : dataFiles) {
                    assertTrue(f.exists(), f.getAbsolutePath() + " should exist");
                }
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("Main with invalid employee credentials")
        void testMainInvalidEmployeeCredentials() {
            String input = "1\nInvalidUser\nWrongPassword\n3\n";  // Invalid employee login then exit
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                assertDoesNotThrow(() -> Main.main(new String[] {}));
                String output = out.toString();
                assertTrue(output.contains("Invalid") || output.contains("again"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("Main with invalid admin credentials")
        void testMainInvalidAdminCredentials() {
            String input = "2\nInvalidAdmin\nWrongPassword\n3\n";  // Invalid admin login then exit
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                assertDoesNotThrow(() -> Main.main(new String[] {}));
                String output = out.toString();
               
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }
    }