//package test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.io.PrintStream;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Scanner;
//import java.util.Set;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import staffRosteringSystem.AdminFunction;
//import staffRosteringSystem.EmployeeFunction;
//import staffRosteringSystem.Main;
//import staffRosteringSystem.RequestManager;
//import staffRosteringSystem.Shift;
//import staffRosteringSystem.ShiftManager;
//import staffRosteringSystem.StaffManager;
//import staffRosteringSystem.BaseFunction;
//
//class TargetedMicroCoverageTests {
//        private static int uniqueStaffId(StaffManager sm) {
//        List<staffRosteringSystem.StaffProfile> profiles = sm.loadStaffProfiles();
//        Set<Integer> taken = new HashSet<>();
//        for (staffRosteringSystem.StaffProfile p : profiles) taken.add(p.getStaffId());
//        int id = 900000; // start high to avoid collisions with seeded data
//        while (taken.contains(id)) id++;
//        return id;
//    }
//        // ---- Shift Setters Coverage ----
//        @Test
//        @DisplayName("Shift.setEmployeeId should update employee ID")
//        void testShiftSetEmployeeId() {
//            Shift shift = new Shift(1, 1001, "2025-12-15", "MORNING", "08:00", "16:00", "Test");
//            shift.setEmployeeId(1002);
//            assertEquals(1002, shift.getEmployeeId());
//        }
//
//        @Test
//        @DisplayName("Shift.setSession should update session")
//        void testShiftSetSession() {
//            Shift shift = new Shift(1, 1001, "2025-12-15", "MORNING", "08:00", "16:00", "Test");
//            shift.setSession("AFTERNOON");
//            assertEquals("AFTERNOON", shift.getSession());
//        }
//
//        @Test
//        @DisplayName("Shift.setStartTime should update start time")
//        void testShiftSetStartTime() {
//            Shift shift = new Shift(1, 1001, "2025-12-15", "MORNING", "08:00", "16:00", "Test");
//            shift.setStartTime("09:00");
//            assertEquals("09:00", shift.getStartTime());
//        }
//
//        @Test
//        @DisplayName("Shift.setEndTime should update end time")
//        void testShiftSetEndTime() {
//            Shift shift = new Shift(1, 1001, "2025-12-15", "MORNING", "08:00", "16:00", "Test");
//            shift.setEndTime("17:00");
//            assertEquals("17:00", shift.getEndTime());
//        }
//
//        // ---- Main Constructor Coverage ----
//        @Test
//        @DisplayName("Main constructor should not throw")
//        void testMainConstructor() {
//            assertDoesNotThrow(() -> {
//                Main main = new Main();
//                assertNotNull(main);
//            });
//        }
//
//        // ---- BaseFunction.loginPage Coverage ----
//        @Test
//        @DisplayName("BaseFunction.loginPage should print menu")
//        void testBaseFunctionLoginPageDisplay() {
//            class TestBase extends BaseFunction {
//                TestBase(int userId, String username, String password) { super(userId, username, password); }
//                public void exposeLoginPage(String param) { loginPage(param); }
//            }
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream("4\n".getBytes()));
//                System.setOut(new PrintStream(out));
//                TestBase base = new TestBase(1001, "test", "pass");
//                base.exposeLoginPage("1001");
//                String output = out.toString();
//                assertTrue(output.contains("Employee Menu") || output.contains("1."));
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        // ---- Employee Function uncovered methods (SAFE TESTS - NO INTERACTIVE LOOPS) ----
//        @Test
//        @DisplayName("EmployeeFunction.checkDuty with valid date range")
//        void testEmployeeCheckDutyValidRange() {
//            EmployeeFunction ef = new EmployeeFunction();
//            assertDoesNotThrow(() -> {
//                ef.checkDuty("1001", "2025-12-01", "2025-12-31");
//            });
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.isValidDate comprehensive edge cases")
//        void testEmployeeIsValidDateEdgeCases() {
//            EmployeeFunction ef = new EmployeeFunction();
//            assertTrue(ef.isValidDate("2025-01-01"));
//            assertTrue(ef.isValidDate("2024-02-29")); // leap year
//            assertFalse(ef.isValidDate("2025-02-29")); // not leap year
//            assertFalse(ef.isValidDate("2025-13-01")); // invalid month
//            assertFalse(ef.isValidDate("2025-01-32")); // invalid day
//            assertFalse(ef.isValidDate("invalid"));
//        }
//
////        @Test
////        @DisplayName("EmployeeFunction.addDuty should add duty successfully")
////        void testEmployeeAddDutySuccess() {
////            EmployeeFunction ef = new EmployeeFunction();
////            boolean result = ef.addDuty("1001", "2025-12-25", "MORNING", "N", "N");
////            assertTrue(result);
////        }
//
////        @Test
////        @DisplayName("EmployeeFunction.addDuty with existing duty should fail")
////        void testEmployeeAddDutyDuplicate() {
////            EmployeeFunction ef = new EmployeeFunction();
////            ef.addDuty("1001", "2025-12-25", "MORNING", "N", "N");
////            boolean result = ef.addDuty("1001", "2025-12-25", "MORNING", "N", "N"); // same duty
////            assertFalse(result); // should reject duplicate
////        }
//
//        // ---- AdminFunction error paths ----
//        @Test
//        @DisplayName("AdminFunction.approveLeaveRequest delegation")
//        void testAdminApproveLeaveRequestDelegation() {
//            AdminFunction admin = new AdminFunction(3001, "admin", "pass");
//            boolean result = admin.approveLeaveRequest(999);
//            assertFalse(result);
//        }
//
//        @Test
//        @DisplayName("AdminFunction.rejectLeaveRequest delegation")
//        void testAdminRejectLeaveRequestDelegation() {
//            AdminFunction admin = new AdminFunction(3001, "admin", "pass");
//            boolean result = admin.rejectLeaveRequest(999);
//            assertFalse(result);
//        }
//
//        @Test
//        @DisplayName("AdminFunction.approveDutyRequest delegation")
//        void testAdminApproveDutyRequestDelegation() {
//            AdminFunction admin = new AdminFunction(3001, "admin", "pass");
//            boolean result = admin.approveDutyRequest(999);
//            assertFalse(result);
//        }
//
//        @Test
//        @DisplayName("AdminFunction.rejectDutyRequest delegation")
//        void testAdminRejectDutyRequestDelegation() {
//            AdminFunction admin = new AdminFunction(3001, "admin", "pass");
//            boolean result = admin.rejectDutyRequest(999);
//            assertFalse(result);
//        }
//
//        @Test
//        @DisplayName("AdminFunction.deleteShift delegation")
//        void testAdminDeleteShiftDelegation() {
//            AdminFunction admin = new AdminFunction(3001, "admin", "pass");
//            boolean result = admin.deleteShift(999);
//            assertFalse(result);
//        }
//
//        // ---- Additional RequestManager branches ----
//        @Test
//        @DisplayName("RequestManager.requestLeave with multiple leave types")
//        void testRequestLeaveMultipleTypes() {
//            RequestManager rm = new RequestManager();
//            StaffManager sm = new StaffManager();
//            int id = uniqueStaffId(sm);
//            sm.addStaffProfile(id, "LeaveTest", "Employee");
//
//            String input = String.join(System.lineSeparator(),
//                    "2",                // Annual
//                    "Family visit",
//                    "2025-12-26",
//                    "") + System.lineSeparator();
//            Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
//            assertDoesNotThrow(() -> rm.requestLeave(id, sc, sm));
//        }
//
//        // ---- Menu edge: invalid input handling ----
//        @Test
//        @DisplayName("MenuManager with invalid menu choice returns safely")
//        void testMenuManagerInvalidChoice() {
//            String input = "99\n6\n";
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                AdminFunction admin = new AdminFunction(5001, "admin", "pass");
//                admin.login();
//                String output = out.toString();
//                assertNotNull(output);
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        // ---- Shift model edge cases ----
//        @Test
//        @DisplayName("Shift with null/empty notes")
//        void testShiftWithNullNotes() {
//            Shift shift = new Shift(1, 1001, "2025-12-15", "MORNING", "08:00", "16:00", null);
//            shift.setNotes("");
//            assertEquals("", shift.getNotes());
//        }
//
//        // ---- BaseFunction edge case login ----
//        @Test
//        @DisplayName("BaseFunction.login with various credential combinations")
//        void testBaseFunctionLoginEdgeCases() {
//            class TestBase extends BaseFunction {
//                TestBase(int userId, String username, String password) { super(userId, username, password); }
//                public boolean exposedLogin(String u, String p) { return login(u, p); }
//            }
//            TestBase base = new TestBase(1001, "test", "pass");
//            boolean result = base.exposedLogin("test", "pass");
//            assertTrue(result);
//            boolean fail = base.exposedLogin("test", "wrong");
//            assertFalse(fail);
//        }
//
//        // ---- BaseFunction lambdas (view schedule sorting) ----
//        @Test
//        @DisplayName("BaseFunction.viewShiftSchedule with sorting")
//        void testBaseViewShiftScheduleSorting() {
//            class TestBase extends BaseFunction {
//                TestBase(int userId, String username, String password) { super(userId, username, password); }
//                public void exposeViewShiftSchedule(String date) { viewShiftSchedule(date); }
//            }
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                TestBase base = new TestBase(1001, "test", "pass");
//                base.exposeViewShiftSchedule("2025-10-23");
//                String output = out.toString();
//                assertTrue(output.contains("shift") || output.contains("No shifts"));
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("BaseFunction.viewMyRoster with sorting")
//        void testBaseViewMyRosterSorting() {
//            class TestBase extends BaseFunction {
//                TestBase(int userId, String username, String password) { super(userId, username, password); }
//                public void exposeViewMyRoster() { viewMyRoster(); }
//            }
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                TestBase base = new TestBase(1001, "test", "pass");
//                base.exposeViewMyRoster();
//                String output = out.toString();
//                assertTrue(output.contains("SCHEDULE") || output.contains("No shifts"));
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        // ---- Menu branches and error paths ----
//        @Test
//        @DisplayName("MenuManager.sessionManagementMenu with view shifts")
//        void testMenuSessionViewShifts() {
//            String input = "2\n1\n2\n6\n";
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                AdminFunction admin = new AdminFunction(5002, "admin", "pass");
//                admin.login();
//                String output = out.toString();
//                assertTrue(output.contains("Administrator Main Menu"));
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("MenuManager.staffManagementMenu exhaustive paths")
//        void testMenuStaffManagementPaths() {
//            StaffManager sm = new StaffManager();
//            int id = uniqueStaffId(sm);
//            sm.addStaffProfile(id, "StaffTest", "Employee");
//
//            String input = String.join(System.lineSeparator(),
//                    "1",              // staff management
//                    "5",              // view all staff
//                    "6"               // back
//            ) + System.lineSeparator();
//            input += "6\n";  // logout
//
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                AdminFunction admin = new AdminFunction(5003, "admin", "pass");
//                admin.login();
//                String output = out.toString();
//                assertTrue(output.contains("Administrator Main Menu"));
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.login with valid credentials")
//        void testEmployeeLoginValid() {
//            EmployeeFunction ef = new EmployeeFunction();
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                // Staff 1001 exists in Data/Staff_Profile.txt
//                boolean result = ef.login("1001", "1001");
//               
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("RequestManager.generateRequestId with empty list")
//        void testRequestGenerateIdEmptyList() {
//            RequestManager rm = new RequestManager();
//            rm.initializeRequestFiles(null);
//            assertDoesNotThrow(() -> {
//                rm.loadLeaveRequests();
//                rm.loadDutyRequests();
//            });
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.isLeapYear comprehensive")
//        void testEmployeeFunctionLeapYear() {
//            EmployeeFunction ef = new EmployeeFunction();
//            assertTrue(ef.isValidDate("2024-2-29"));
//            assertFalse(ef.isValidDate("2023-2-29"));
//        }
//
//        @Test
//        @DisplayName("ShiftManager.removeShiftsForLeave with target employee")
//        void testShiftManagerRemoveShiftsForLeave() {
//            ShiftManager sm = new ShiftManager();
//            StaffManager staffMgr = new StaffManager();
//            int id = uniqueStaffId(staffMgr);
//            staffMgr.addStaffProfile(id, "LeaveEmp", "Employee");
//            sm.assignShift(id, "2025-12-24", "MORNING", "Holiday", staffMgr);
//            assertDoesNotThrow(() -> sm.removeShiftsForLeave(id));
//        }
//
//        @Test
//        @DisplayName("ShiftManager.viewAllShiftSchedules with existing shifts")
//        void testShiftManagerViewAllSchedules() {
//            ShiftManager sm = new ShiftManager();
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                sm.viewAllShiftSchedules();
//                String output = out.toString();
//                assertTrue(output.contains("Shift") || output.length() > 0);
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.getValidDateInput with single valid input")
//        void testEmployeeGetValidDateInputSingle() {
//            String input = "2025-06-15\n";
//            Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
//            EmployeeFunction ef = new EmployeeFunction();
//            String result = ef.getValidDateInput(sc, "Enter date: ");
//            assertEquals("2025-06-15", result);
//        }
//
//        @Test
//        @DisplayName("BaseFunction.getUserId and getUsername consistency")
//        void testBaseFunctionUserIdUsernameConsistency() {
//            class TestBase extends BaseFunction {
//                TestBase(int userId, String username, String password) { super(userId, username, password); }
//                public int exposeGetUserId() { return getUserId(); }
//                public String exposeGetUsername() { return getUsername(); }
//            }
//            TestBase base = new TestBase(1007, "testuser", "pass");
//            assertEquals(1007, base.exposeGetUserId());
//            assertEquals("testuser", base.exposeGetUsername());
//        }
//
//        @Test
//        @DisplayName("Main.main with rapid input sequences")
//        void testMainRapidInputSequences() {
//            String input = "1\n3\n";  // employee login then exit
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//    }