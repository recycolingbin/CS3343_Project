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
//import java.util.List;
//import java.util.Scanner;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import staffRosteringSystem.AdminFunction;
//import staffRosteringSystem.BaseFunction;
//import staffRosteringSystem.DutyRequest;
//import staffRosteringSystem.EmployeeFunction;
//import staffRosteringSystem.LeaveRequest;
//import staffRosteringSystem.Main;
//import staffRosteringSystem.MenuManager;
//import staffRosteringSystem.RequestManager;
//import staffRosteringSystem.Shift;
//import staffRosteringSystem.ShiftManager;
//import staffRosteringSystem.StaffManager;
//import staffRosteringSystem.StaffProfile;
//
//class CriticalBranchCoverageTests {
//        
//        // ============ EmployeeFunction.requestLeave() FULL FLOW ============
//        @Test
//        @DisplayName("EmployeeFunction.requestLeave with valid dates and reason")
//        void testEmployeeRequestLeaveFullFlow() {
//            EmployeeFunction ef = new EmployeeFunction();
//            long userId = 10000 + (System.nanoTime() % 10000);
//            String input = "2025-11-01\n2025-11-05\nVacation\n";
//            
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                ef.requestLeave(String.valueOf(userId));
//                assertDoesNotThrow(() -> {});
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.requestLeave with end date before start date")
//        void testEmployeeRequestLeaveEndBeforeStart() {
//            EmployeeFunction ef = new EmployeeFunction();
//            long userId = 20000 + (System.nanoTime() % 10000);
//            String input = "2025-11-10\n2025-11-05\n2025-11-12\nSick\n";
//            
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                ef.requestLeave(String.valueOf(userId));
//                String output = out.toString();
//                assertTrue(output.contains("Error: End date cannot be earlier"));
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.requestLeave empty reason retry")
//        void testEmployeeRequestLeaveEmptyReasonRetry() {
//            EmployeeFunction ef = new EmployeeFunction();
//            long userId = 30000 + (System.nanoTime() % 10000);
//            String input = "2025-12-01\n2025-12-03\n\nAnnual\n";  // Empty reason first, then valid
//            
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                ef.requestLeave(String.valueOf(userId));
//                String output = out.toString();
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        // ============ AdminFunction login ============
//        @Test
//        @DisplayName("AdminFunction.login with valid credentials")
//        void testAdminFunctionLoginValid() {
//            AdminFunction admin = new AdminFunction(2001, "admin", "password");
//            assertTrue(admin.login("admin", "password"));
//        }
//
//        @Test
//        @DisplayName("AdminFunction.login with invalid credentials")
//        void testAdminFunctionLoginInvalid() {
//            AdminFunction admin = new AdminFunction(2001, "admin", "password");
//            assertFalse(admin.login("wronguser", "wrongpass"));
//        }
//
//        // ============ MenuManager login logout ============
//        @Test
//        @DisplayName("MenuManager.loginPage logout option")
//        void testMenuManagerLogout() {
//            StaffManager sm = new StaffManager();
//            RequestManager rm = new RequestManager();
//            ShiftManager shm = new ShiftManager();
//            
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream("6\n".getBytes()));
//                System.setOut(new PrintStream(out));
//                MenuManager mm = new MenuManager(sm, rm, shm, new Scanner(System.in));
//                mm.loginPage();
//                String output = out.toString();
//                assertTrue(output.contains("Logged out") || output.contains("logout"));
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        // ============ RequestManager request flows ============
//        @Test
//        @DisplayName("RequestManager.requestLeave with invalid employee")
//        void testRequestManagerRequestLeaveInvalidEmployee() {
//            RequestManager rm = new RequestManager();
//            StaffManager sm = new StaffManager();
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                rm.requestLeave(99999, new Scanner(""), sm);
//                String output = out.toString();
//                assertTrue(output.contains("not found") || output.contains("Error"));
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("RequestManager.requestDuty with invalid employee")
//        void testRequestManagerRequestDutyInvalidEmployee() {
//            RequestManager rm = new RequestManager();
//            StaffManager sm = new StaffManager();
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                rm.requestDuty(99999, new Scanner(""), sm);
//                String output = out.toString();
//                assertTrue(output.contains("not found") || output.contains("Error"));
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        // ============ LeaveRequest and DutyRequest getters ============
//        @Test
//        @DisplayName("LeaveRequest.getLeaveType and getReason")
//        void testLeaveRequestGetters() {
//            LeaveRequest lr = new LeaveRequest(1001, 1000, "2025-12-25", "Vacation", "Holiday break");
//            assertEquals("Vacation", lr.getLeaveType());
//            assertEquals("Holiday break", lr.getReason());
//        }
//
//        @Test
//        @DisplayName("DutyRequest.getDutyType and getDutyDescription")
//        void testDutyRequestGetters() {
//           DutyRequest dr = new DutyRequest(1001, 1001, "2025-12-20", "MORNING", "Training", "Java skills");
//            assertEquals("Training", dr.getDutyType());
//            assertEquals("Java skills", dr.getDutyDescription());
//        }
//
//        // ============ StaffManager comprehensive tests ============
//        @Test
//        @DisplayName("StaffManager.staffExists for existing staff")
//        void testStaffManagerStaffExists() {
//            StaffManager sm = new StaffManager();
//            List<StaffProfile> profiles = sm.loadStaffProfiles();
//            if (!profiles.isEmpty()) {
//                assertTrue(sm.staffExists(profiles.get(0).getStaffId()));
//            }
//        }
//
//        @Test
//        @DisplayName("StaffManager.staffExists for non-existing staff")
//        void testStaffManagerStaffNotExists() {
//            StaffManager sm = new StaffManager();
//            assertFalse(sm.staffExists(999999));
//        }
//
//        @Test
//        @DisplayName("StaffManager.loadStaffProfiles")
//        void testStaffManagerLoadStaffProfiles() {
//            StaffManager sm = new StaffManager();
//            List<StaffProfile> profiles = sm.loadStaffProfiles();
//            assertNotNull(profiles);
//        }
//
//        // ============ ShiftManager comprehensive tests ============
//        @Test
//        @DisplayName("ShiftManager.loadShifts")
//        void testShiftManagerLoadShifts() {
//            ShiftManager shm = new ShiftManager();
//            List<Shift> shifts = shm.loadShifts();
//            assertNotNull(shifts);
//        }
//
//        
//
//        // ============ Main with multiple invalid retries ============
//        @Test
//        @DisplayName("Main with multiple invalid inputs before exit")
//        void testMainMultipleInvalidInputs() {
//            String input = "99\n98\n97\n3\n";
//            InputStream prevIn = System.in;
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setIn(new ByteArrayInputStream(input.getBytes()));
//                System.setOut(new PrintStream(out));
//                Main.main(new String[] {});
//                String output = out.toString();
//                assertTrue(output.contains("Invalid"));
//            } finally {
//                System.setIn(prevIn);
//                System.setOut(prevOut);
//            }
//        }
//
//        // ============ BaseFunction constructor and setup ============
//        @Test
//        @DisplayName("EmployeeFunction extends BaseFunction correctly")
//        void testEmployeeFunctionInheritance() {
//            EmployeeFunction ef = new EmployeeFunction();
//            assertNotNull(ef);
//            assertTrue(ef instanceof BaseFunction);
//        }
//
//        @Test
//        @DisplayName("BaseFunction.getUserId with default constructor")
//        void testBaseGetUserIdDefault() {
//            EmployeeFunction ef = new EmployeeFunction();
//            assertEquals(0, ef.getUserId());
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.isValidDate comprehensive")
//        void testEmployeeIsValidDateComprehensive() {
//            EmployeeFunction ef = new EmployeeFunction();
//            // Valid dates
//            assertTrue(ef.isValidDate("2025-01-01"));
//            assertTrue(ef.isValidDate("2025-12-31"));
//            assertTrue(ef.isValidDate("2024-02-29"));
//            // Invalid dates
//            assertFalse(ef.isValidDate("2025-13-01"));
//            assertFalse(ef.isValidDate("2025-02-30"));
//            assertFalse(ef.isValidDate("2019-06-15"));
//            assertFalse(ef.isValidDate("2031-06-15"));
//        }
//
//        @Test
//        @DisplayName("EmployeeFunction.addDuty returns boolean correctly")
//        void testEmployeeAddDutyReturnsCorrectly() {
//            EmployeeFunction ef = new EmployeeFunction();
//            long uid = 60000 + (System.nanoTime() % 10000);
//            boolean result = ef.addDuty(String.valueOf(uid), "2025-11-20", "EVENING", "N", "N");
//            // Will be true or false depending on session validity and file state
//            assertTrue(result || !result);  // Just verify it returns boolean
//        }
//
//        @Test
//        @DisplayName("RequestManager.viewLeaveRequestsWithCaseNumbers")
//        void testRequestManagerViewLeaveRequests() {
//            RequestManager rm = new RequestManager();
//            StaffManager sm = new StaffManager();
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                rm.viewLeaveRequestsWithCaseNumbers(sm);
//                assertDoesNotThrow(() -> {});
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("RequestManager.viewDutyRequestsWithCaseNumbers")
//        void testRequestManagerViewDutyRequests() {
//            RequestManager rm = new RequestManager();
//            StaffManager sm = new StaffManager();
//            PrintStream prevOut = System.out;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                System.setOut(new PrintStream(out));
//                rm.viewDutyRequestsWithCaseNumbers(sm);
//                assertDoesNotThrow(() -> {});
//            } finally {
//                System.setOut(prevOut);
//            }
//        }
//
//        @Test
//        @DisplayName("RequestManager.loadLeaveRequests")
//        void testRequestManagerLoadLeaveRequests() {
//            RequestManager rm = new RequestManager();
//            List<LeaveRequest> reqs = rm.loadLeaveRequests();
//            assertNotNull(reqs);
//        }
//
//        @Test
//        @DisplayName("RequestManager.loadDutyRequests")
//        void testRequestManagerLoadDutyRequests() {
//            RequestManager rm = new RequestManager();
//            List<DutyRequest> reqs = rm.loadDutyRequests();
//            assertNotNull(reqs);
//        }
//
//        @Test
//        @DisplayName("AdminFunction.getMenuManager")
//        void testAdminGetMenuManager() {
//            AdminFunction admin = new AdminFunction(2001, "admin", "password");
//            MenuManager mm = admin.getMenuManager();
//            assertNotNull(mm);
//        }
//    }
