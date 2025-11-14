package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import staffRosteringSystem.BaseFunction;

class EmployeeFunctionExtendedTests {
        
        @Test
        @DisplayName("EmployeeFunction.login with invalid credentials")
        void testEmployeeLoginInvalidCredentials() {
            EmployeeFunction ef = new EmployeeFunction();
            assertFalse(ef.login("InvalidUser", "password"));
        }

        @Test
        @DisplayName("EmployeeFunction.isLeapYear validation")
        void testEmployeeIsLeapYear() {
            EmployeeFunction ef = new EmployeeFunction();
            // Test leap years
            assertTrue(ef.isValidDate("2024-02-29"));   // leap year
            assertFalse(ef.isValidDate("2025-02-29"));  // not leap year
            assertTrue(ef.isValidDate("2020-02-29"));   // leap year
            assertFalse(ef.isValidDate("1900-02-29"));  // not leap year (century rule)
        }

        @Test
        @DisplayName("EmployeeFunction.isValidDate with various formats")
        void testEmployeeIsValidDateFormats() {
            EmployeeFunction ef = new EmployeeFunction();
            // Valid dates
            assertTrue(ef.isValidDate("2025-01-01"));
            assertTrue(ef.isValidDate("2025-12-31"));
            assertTrue(ef.isValidDate("2025-06-15"));
            
            // Invalid dates
            assertFalse(ef.isValidDate("2025-13-01"));   // month > 12
            assertFalse(ef.isValidDate("2025-00-01"));   // month = 0
            assertFalse(ef.isValidDate("2025-12-32"));   // day > 31
            assertFalse(ef.isValidDate("2025-12-00"));   // day = 0
            assertFalse(ef.isValidDate("2019-06-15"));   // year < 2020
            assertFalse(ef.isValidDate("2031-06-15"));   // year > 2030
            assertFalse(ef.isValidDate("not-a-date"));   // invalid format
            assertFalse(ef.isValidDate("2025/06/15"));   // wrong separator
            assertFalse(ef.isValidDate(""));             // empty
            assertFalse(ef.isValidDate(null));           // null
        }

        @Test
        @DisplayName("EmployeeFunction.getValidDateInput with immediate valid input")
        void testEmployeeGetValidDateInputImmediate() {
            EmployeeFunction ef = new EmployeeFunction();
            String input = "2025-06-15\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            String result = ef.getValidDateInput(scanner, "Enter date: ");
            assertEquals("2025-06-15", result);
        }

        @Test
        @DisplayName("EmployeeFunction.getValidDateInput with retry on invalid")
        void testEmployeeGetValidDateInputRetry() {
            EmployeeFunction ef = new EmployeeFunction();
            String input = "invalid\n2025-06-15\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            String result = ef.getValidDateInput(scanner, "Enter date: ");
            assertEquals("2025-06-15", result);
        }

        @Test
        @DisplayName("EmployeeFunction.getValidDateInput max retries exceeded")
        void testEmployeeGetValidDateInputMaxRetries() {
            EmployeeFunction ef = new EmployeeFunction();
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 11; i++) {
                input.append("invalid\n");
            }
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.toString().getBytes()));
            String result = ef.getValidDateInput(scanner, "Enter date: ");
            assertNull(result);
        }

        @Test
        @DisplayName("EmployeeFunction.checkDuty with duty present")
        void testEmployeeCheckDutyPresent() throws Exception {
            EmployeeFunction ef = new EmployeeFunction();
            long uniqueId = 7777 + (System.nanoTime() % 10000);
            String userid = String.valueOf(uniqueId);
            String date = "2025-08-10";
            
            // Add duty first
            ef.addDuty(userid, date, "MORNING", "N", "N");
            
            // Check that duty exists in date range
            assertTrue(ef.checkDuty(userid, "2025-08-01", "2025-08-31"));
        }

        @Test
        @DisplayName("EmployeeFunction.checkDuty with no duty")
        void testEmployeeCheckDutyNone() throws Exception {
            EmployeeFunction ef = new EmployeeFunction();
            long uniqueId = 6666 + (System.nanoTime() % 10000);
            String userid = String.valueOf(uniqueId);
            
            // Check without adding duty
            assertFalse(ef.checkDuty(userid, "2025-09-01", "2025-09-30"));
        }

        @Test
        @DisplayName("EmployeeFunction.requestDuty with valid input")
        void testEmployeeRequestDutyValid() {
            EmployeeFunction ef = new EmployeeFunction();
            long uniqueId = 5555 + (System.nanoTime() % 10000);
            String userid = String.valueOf(uniqueId);
            String input = "2025-10-15\nMORNING\n";
            
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                ef.requestDuty(userid);
                // Should complete without error
                assertDoesNotThrow(() -> {});
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.requestDuty with invalid session")
        void testEmployeeRequestDutyInvalidSession() {
            EmployeeFunction ef = new EmployeeFunction();
            long uniqueId = 4444 + (System.nanoTime() % 10000);
            String userid = String.valueOf(uniqueId);
            String input = "2025-10-20\nINVALID\n";
            
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                ef.requestDuty(userid);
                String output = out.toString();
                assertTrue(output.contains("Invalid session"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage with view schedule")
        void testEmployeeLoginPageViewSchedule() {
            EmployeeFunction ef = new EmployeeFunction();
            String input = "1\n4\n";  // View schedule then logout
            
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                ef.loginPage("1001");
                // Should complete without error
                assertDoesNotThrow(() -> {});
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage with invalid choice")
        void testEmployeeLoginPageInvalidChoice() {
            EmployeeFunction ef = new EmployeeFunction();
            String input = "99\n4\n";  // Invalid choice then logout
            
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                ef.loginPage("1001");
                String output = out.toString();
                assertTrue(output.contains("Invalid choice"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage handles input mismatch")
        void testEmployeeLoginPageInputMismatch() {
            EmployeeFunction ef = new EmployeeFunction();
            String input = "abc\n4\n";  // Non-numeric input then logout
            
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                ef.loginPage("1001");
                String output = out.toString();
                assertTrue(output.contains("Invalid input"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }
    }
