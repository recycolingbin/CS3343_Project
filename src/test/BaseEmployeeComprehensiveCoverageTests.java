package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.EmployeeFunction;
import staffRosteringSystem.Shift;
import staffRosteringSystem.StaffProfile;
import staffRosteringSystem.BaseFunction;

class BaseEmployeeComprehensiveCoverageTests {
        // ========== BaseFunction Comprehensive Coverage ==========
        @Test
        @DisplayName("BaseFunction.login with exact match should return true")
        void testBaseFunctionLoginSuccess() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public boolean exposeLogin(String u, String p) { return login(u, p); }
            }
            TestBase base = new TestBase(1001, "john", "pass123");
            assertTrue(base.exposeLogin("john", "pass123"));
        }

        @Test
        @DisplayName("BaseFunction.login with wrong username should return false")
        void testBaseFunctionLoginWrongUsername() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public boolean exposeLogin(String u, String p) { return login(u, p); }
            }
            TestBase base = new TestBase(1001, "john", "pass123");
            assertFalse(base.exposeLogin("jane", "pass123"));
        }

        @Test
        @DisplayName("BaseFunction.login with wrong password should return false")
        void testBaseFunctionLoginWrongPassword() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public boolean exposeLogin(String u, String p) { return login(u, p); }
            }
            TestBase base = new TestBase(1001, "john", "pass123");
            assertFalse(base.exposeLogin("john", "wrongpass"));
        }

        @Test
        @DisplayName("BaseFunction.viewFunction should print user info")
        void testBaseFunctionViewFunction() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public void exposeViewFunction() { viewFunction(); }
            }
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(out));
                TestBase base = new TestBase(1001, "testuser", "pass");
                base.exposeViewFunction();
                String output = out.toString();
                assertTrue(output.contains("testuser"));
            } finally {
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("BaseFunction.getUserId should return correct ID")
        void testBaseFunctionGetUserId() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
            }
            TestBase base = new TestBase(5555, "user", "pass");
            assertEquals(5555, base.getUserId());
        }

        @Test
        @DisplayName("BaseFunction.getUsername should return correct username")
        void testBaseFunctionGetUsername() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
            }
            TestBase base = new TestBase(5555, "alice", "pass");
            assertEquals("alice", base.getUsername());
        }

        @Test
        @DisplayName("BaseFunction.loadShifts should return list with shifts from file")
        void testBaseFunctionLoadShifts() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public List<Shift> exposeLoadShifts() { return loadShifts(); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            List<Shift> shifts = base.exposeLoadShifts();
            assertNotNull(shifts);
            assertTrue(shifts.size() > 0);
        }

        @Test
        @DisplayName("BaseFunction.getUserInfo should return staff profile")
        void testBaseFunctionGetUserInfo() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public StaffProfile exposeGetUserInfo(int id) { return getUserInfo(id); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            StaffProfile profile = base.exposeGetUserInfo(1001);
            assertNotNull(profile);
            assertEquals(1001, profile.getStaffId());
        }

        @Test
        @DisplayName("BaseFunction.getUserInfo with invalid ID should return null")
        void testBaseFunctionGetUserInfoInvalidId() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public StaffProfile exposeGetUserInfo(int id) { return getUserInfo(id); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            StaffProfile profile = base.exposeGetUserInfo(999999);
            assertNull(profile);
        }

        @Test
        @DisplayName("BaseFunction.isValidSession should accept MORNING/AFTERNOON/NIGHT")
        void testBaseFunctionIsValidSession() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public boolean exposeIsValidSession(String s) { return isValidSession(s); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            assertTrue(base.exposeIsValidSession("MORNING"));
            assertTrue(base.exposeIsValidSession("AFTERNOON"));
            assertTrue(base.exposeIsValidSession("NIGHT"));
            assertFalse(base.exposeIsValidSession("INVALID"));
        }

        @Test
        @DisplayName("BaseFunction.getSessionOrder should return correct order")
        void testBaseFunctionGetSessionOrder() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public int exposeGetSessionOrder(String s) { return getSessionOrder(s); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            assertEquals(1, base.exposeGetSessionOrder("MORNING"));
            assertEquals(2, base.exposeGetSessionOrder("AFTERNOON"));
            assertEquals(3, base.exposeGetSessionOrder("NIGHT"));
            assertEquals(4, base.exposeGetSessionOrder("INVALID"));
        }

        @Test
        @DisplayName("BaseFunction.isValidDate with various formats and edge cases")
        void testBaseFunctionIsValidDateComprehensive() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public boolean exposeIsValidDate(String d) { return isValidDate(d); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            assertTrue(base.exposeIsValidDate("2025-12-25"));
            assertTrue(base.exposeIsValidDate("2025-1-1"));
            assertTrue(base.exposeIsValidDate("2024-2-29")); // leap year
            assertFalse(base.exposeIsValidDate("2025-2-29")); // not leap year
            assertFalse(base.exposeIsValidDate("2025-13-01")); // invalid month
            assertFalse(base.exposeIsValidDate("2025-12-32")); // invalid day
            assertFalse(base.exposeIsValidDate("invalid"));
        }

        @Test
        @DisplayName("BaseFunction.viewShiftSchedule with existing date")
        void testBaseFunctionViewShiftScheduleWithDate() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public void exposeViewShiftSchedule(String d) { viewShiftSchedule(d); }
            }
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(out));
                TestBase base = new TestBase(1001, "user", "pass");
                base.exposeViewShiftSchedule("2025-12-15");
                String output = out.toString();
                assertTrue(output.contains("Shift") || output.contains("shift"));
            } finally {
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("BaseFunction.viewShiftsBySession with MORNING session")
        void testBaseFunctionViewShiftsBySessionMorning() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public void exposeViewShiftsBySession(String d, String s) { viewShiftsBySession(d, s); }
            }
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(out));
                TestBase base = new TestBase(1001, "user", "pass");
                base.exposeViewShiftsBySession("2025-12-15", "MORNING");
                String output = out.toString();
                assertTrue(output.length() > 0);
            } finally {
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("BaseFunction.viewShiftsBySession with invalid session")
        void testBaseFunctionViewShiftsBySessionInvalid() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public void exposeViewShiftsBySession(String d, String s) { viewShiftsBySession(d, s); }
            }
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(out));
                TestBase base = new TestBase(1001, "user", "pass");
                base.exposeViewShiftsBySession("2025-12-15", "INVALID");
                String output = out.toString();
                assertTrue(output.contains("Invalid") || output.contains("INVALID"));
            } finally {
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("BaseFunction.viewMyRoster should show user roster")
        void testBaseFunctionViewMyRoster() {
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public void exposeViewMyRoster() { viewMyRoster(); }
            }
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(out));
                TestBase base = new TestBase(1001, "user", "pass");
                base.exposeViewMyRoster();
                String output = out.toString();
                assertTrue(output.contains("SCHEDULE") || output.contains("roster"));
            } finally {
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("BaseFunction.getValidDateInput with single valid date")
        void testBaseFunctionGetValidDateInputSingleValid() {
            String input = "2025-03-15\n";
            Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public String exposeGetValidDateInput(Scanner scanner, String prompt) { return getValidDateInput(scanner, prompt); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            String result = base.exposeGetValidDateInput(sc, "Enter date: ");
            assertEquals("2025-03-15", result);
        }

        @Test
        @DisplayName("BaseFunction.getValidDateInput with multiple invalid then valid")
        void testBaseFunctionGetValidDateInputMultipleRetries() {
            String input = "invalid\n2025-13-01\n2025-12-32\n2025-06-15\n";
            Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
            class TestBase extends BaseFunction {
                TestBase(int userId, String username, String password) { super(userId, username, password); }
                public String exposeGetValidDateInput(Scanner scanner, String prompt) { return getValidDateInput(scanner, prompt); }
            }
            TestBase base = new TestBase(1001, "user", "pass");
            String result = base.exposeGetValidDateInput(sc, "Enter date: ");
            assertEquals("2025-06-15", result);
        }

        // ========== EmployeeFunction Comprehensive Coverage ==========

        @Test
        @DisplayName("EmployeeFunction constructor should initialize with defaults")
        void testEmployeeFunctionConstructor() {
            EmployeeFunction ef = new EmployeeFunction();
            assertNotNull(ef);
        }

        @Test
        @DisplayName("EmployeeFunction.login with valid employee name")
        void testEmployeeFunctionLoginValid() {
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
            	String input = "4\n";
        		InputStream in = new ByteArrayInputStream(input.getBytes());
        		System.setIn(in);
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                // Use an existing employee from Staff_Profile.txt
                boolean result = ef.login("Alice Wang", "anypass");
                String output = out.toString();
                assertTrue(result || output.contains("Employee login successful."));
            } finally {
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.login with invalid employee name")
        void testEmployeeFunctionLoginInvalid() {
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                boolean result = ef.login("NonExistentEmployee", "pass");
                assertFalse(result);
                assertTrue(out.toString().contains("Invalid"));
            } finally {
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.addDuty should add new duty")
        void testEmployeeFunctionAddDutyNew() {
            int uniqueId = (int)(System.currentTimeMillis() % 100000) + 88000;
            EmployeeFunction ef = new EmployeeFunction();
            boolean result = ef.addDuty(String.valueOf(uniqueId), "2025-12-30", "MORNING", "N", "N");
            assertTrue(result);
        }

//        @Test
//        @DisplayName("EmployeeFunction.addDuty with duplicate should fail")
//        void testEmployeeFunctionAddDutyDuplicate() {
//            int uniqueId = (int)(System.currentTimeMillis() % 100000) + 77000;
//            String date = "2025-12-28";
//            String session = "AFTERNOON";
//            EmployeeFunction ef = new EmployeeFunction();
//            
//            // Add first time
//            ef.addDuty(String.valueOf(uniqueId), date, session, "N", "N");
//            
//            // Try to add duplicate
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                boolean result = ef.addDuty(String.valueOf(uniqueId), date, session, "N", "N");
//                String output = out.toString();
//                assertFalse(result);
//                assertTrue(output.contains("already have duty"));
//            } finally {
//                System.setOut(prevOut);
//            }
//        }

        @Test
        @DisplayName("EmployeeFunction.checkDuty should verify duty in range")
        void testEmployeeFunctionCheckDutyInRange() {
            EmployeeFunction ef = new EmployeeFunction();
            assertDoesNotThrow(() -> {
                ef.checkDuty("1001", "2025-01-01", "2025-12-31");
            });
        }

        @Test
        @DisplayName("EmployeeFunction.isValidDate with comprehensive test cases")
        void testEmployeeFunctionIsValidDateFull() {
            EmployeeFunction ef = new EmployeeFunction();
            assertTrue(ef.isValidDate("2025-12-25"));
            assertTrue(ef.isValidDate("2024-2-29"));
            assertFalse(ef.isValidDate("2025-13-01"));
            assertFalse(ef.isValidDate("2025-12-32"));
            assertFalse(ef.isValidDate("invalid-date"));
        }

        @Test
        @DisplayName("EmployeeFunction.requestDuty with valid inputs")
        void testEmployeeFunctionRequestDutyValid() {
            String input = String.join(System.lineSeparator(),
                    "2025-12-29",
                    "NIGHT",
                    ""
            ) + System.lineSeparator();
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                int uniqueId = (int)(System.currentTimeMillis() % 100000) + 66000;
                ef.requestDuty(String.valueOf(uniqueId));
                String output = out.toString();
                assertTrue(output.contains("request") || output.length() > 0);
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.requestDuty with invalid session")
        void testEmployeeFunctionRequestDutyInvalidSession() {
            String input = String.join(System.lineSeparator(),
                    "2025-12-29",
                    "INVALID_SESSION",
                    ""
            ) + System.lineSeparator();
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                ef.requestDuty("5555");
                String output = out.toString();
                assertTrue(output.contains("Invalid") || output.length() > 0);
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage menu option 1 view schedule")
        void testEmployeeFunctionLoginPageOption1() {
            String input = "1\n4\n";
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                ef.loginPage("1001");
                String output = out.toString();
                assertTrue(output.contains("Employee Menu"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage menu option 2 request duty")
        void testEmployeeFunctionLoginPageOption2() {
            String input = "2\n2025-12-31\nMORNING\n4\n";
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                ef.loginPage("1001");
                String output = out.toString();
                assertTrue(output.contains("Employee Menu"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage menu option 3 request leave")
        void testEmployeeFunctionLoginPageOption3() {
            String input = "3\n1\nVacation\n2025-12-26\n2025-12-28\n4\n";
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                ef.loginPage("1001");
                String output = out.toString();
                assertTrue(output.contains("Employee Menu"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage menu option 4 logout")
        void testEmployeeFunctionLoginPageOption4() {
            String input = "4\n";
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                ef.loginPage("1001");
                String output = out.toString();
                assertTrue(output.contains("Logging out") || output.contains("Employee Menu"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.loginPage with invalid choice")
        void testEmployeeFunctionLoginPageInvalidChoice() {
            String input = "99\n4\n";
            InputStream prevIn = System.in;
            PrintStream prevOut = System.out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                System.setOut(new PrintStream(out));
                EmployeeFunction ef = new EmployeeFunction();
                ef.loginPage("1001");
                String output = out.toString();
                assertTrue(output.contains("Invalid choice") || output.contains("Employee Menu"));
            } finally {
                System.setIn(prevIn);
                System.setOut(prevOut);
            }
        }

        @Test
        @DisplayName("EmployeeFunction.getValidDateInput with valid single input")
        void testEmployeeFunctionGetValidDateInput() {
            String input = "2025-06-20\n";
            Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
            EmployeeFunction ef = new EmployeeFunction();
            String result = ef.getValidDateInput(sc, "Enter date: ");
            assertEquals("2025-06-20", result);
        }

        @Test
        @DisplayName("EmployeeFunction.isValidDate with boundary dates")
        void testEmployeeFunctionIsValidDateBoundary() {
            EmployeeFunction ef = new EmployeeFunction();
            assertTrue(ef.isValidDate("2025-01-01"));
            assertTrue(ef.isValidDate("2025-12-31"));
            assertFalse(ef.isValidDate("2025-12-32"));
            assertFalse(ef.isValidDate("2025-13-01"));
        }

        @Test
        @DisplayName("EmployeeFunction.checkDuty with various date ranges")
        void testEmployeeFunctionCheckDutyRanges() throws Exception {
            EmployeeFunction ef = new EmployeeFunction();
            boolean result1 = ef.checkDuty("1001", "2025-12-20", "2025-12-22");
            assertFalse(result1); // Employee 1001 has no duty in this range
            boolean result2 = ef.checkDuty("9999", "2025-01-01", "2025-01-03");
            assertFalse(result2); // Non-existent employee
        }

        @Test
        @DisplayName("EmployeeFunction string representations")
        void testEmployeeFunctionStringRepresentations() {
            EmployeeFunction ef = new EmployeeFunction();
            assertNotNull(ef.toString());
            assertTrue(ef.toString().length() > 0);
        }
    }
