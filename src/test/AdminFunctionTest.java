package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.AdminFunction;
import staffRosteringSystem.DutyRequest;
import staffRosteringSystem.LeaveRequest;
import staffRosteringSystem.MenuManager;
import staffRosteringSystem.RequestManager;
import staffRosteringSystem.Shift;
import staffRosteringSystem.ShiftManager;
import staffRosteringSystem.StaffManager;
import staffRosteringSystem.StaffProfile;

public class AdminFunctionTest {
        
        private AdminFunction adminFunction;
        
        private static int uniqueStaffId(StaffManager sm) {
            List<staffRosteringSystem.StaffProfile> profiles = sm.loadStaffProfiles();
            Set<Integer> taken = new HashSet<>();
            for (staffRosteringSystem.StaffProfile p : profiles) taken.add(p.getStaffId());
            int id = 900000; // start high to avoid collisions with seeded data
            while (taken.contains(id)) id++;
            return id;
        }
        
        @BeforeEach
        void setUp() {
            adminFunction = new AdminFunction(1001, "admin", "admin123");
        }
        
        // ---- AdminFunction Initialization Tests (1) ----
        @Test
        @DisplayName("Should initialize AdminFunction without error")
        void testAdminFunctionInitialization() {
            assertNotNull(adminFunction);
        }
        
        // ---- Manager Getter Tests (4) ----
        
        @Test
        @DisplayName("Should return non-null StaffManager")
        void testGetStaffManager() {
            StaffManager staffManager = adminFunction.getStaffManager();
            assertNotNull(staffManager);
        }
        
        @Test
        @DisplayName("Should return non-null RequestManager")
        void testGetRequestManager() {
            RequestManager requestManager = adminFunction.getRequestManager();
            assertNotNull(requestManager);
        }
        
        @Test
        @DisplayName("Should return non-null ShiftManager")
        void testGetShiftManager() {
            ShiftManager shiftManager = adminFunction.getShiftManager();
            assertNotNull(shiftManager);
        }
        
        @Test
        @DisplayName("Should return non-null MenuManager")
        void testGetMenuManager() {
            MenuManager menuManager = adminFunction.getMenuManager();
            assertNotNull(menuManager);
        }
        
        // ---- Manager Coordination Tests (2) ----
        
        @Test
        @DisplayName("Should coordinate staff operations")
        void testStaffCoordination() {
            StaffManager staffManager = adminFunction.getStaffManager();
            int id = uniqueStaffId(staffManager);
            boolean result = staffManager.addStaffProfile(id, "John", "Employee");
            assertTrue(result);
        }
        
        @Test
        @DisplayName("Should maintain manager consistency")
        void testManagerConsistency() {
            StaffManager manager1 = adminFunction.getStaffManager();
            StaffManager manager2 = adminFunction.getStaffManager();
            assertSame(manager1, manager2);
        }
        
        // ---- Coordinator Pattern Tests (2) ----
        
        @Test
        @DisplayName("Should provide unified access to all managers")
        void testUnifiedAccess() {
            StaffManager staffManager = adminFunction.getStaffManager();
            RequestManager requestManager = adminFunction.getRequestManager();
            ShiftManager shiftManager = adminFunction.getShiftManager();
            MenuManager menuManager = adminFunction.getMenuManager();
            
            assertNotNull(staffManager);
            assertNotNull(requestManager);
            assertNotNull(shiftManager);
            assertNotNull(menuManager);
        }
        
        @Test
        @DisplayName("Should handle multiple coordinator instances independently")
        void testMultipleInstances() {
            AdminFunction admin1 = new AdminFunction(1001, "admin1", "pass1");
            AdminFunction admin2 = new AdminFunction(1002, "admin2", "pass2");
            
            StaffManager manager1 = admin1.getStaffManager();
            StaffManager manager2 = admin2.getStaffManager();
            
            assertNotNull(manager1);
            assertNotNull(manager2);
        }
        
        // ---- Facade Pattern Implementation Tests (1) ----
        
        @Test
        @DisplayName("Should act as facade to complex subsystem")
        void testFacadeImplementation() {
            StaffManager staffManager = adminFunction.getStaffManager();
            int before = staffManager.getStaffCount();
            int id = uniqueStaffId(staffManager);
            staffManager.addStaffProfile(id, "Test User", "Employee");
            int after = staffManager.getStaffCount();
            assertTrue(after >= before + 1);
        }
        
        // ---- Integration Tests (2) ----
        
        @Test
        @DisplayName("Should support staff management workflow")
        void testStaffManagementWorkflow() {
            StaffManager staffManager = adminFunction.getStaffManager();
            int id = uniqueStaffId(staffManager);
            boolean added = staffManager.addStaffProfile(id, "Alice", "Manager");
            assertTrue(added);
            boolean exists = staffManager.staffExists(id);
            assertTrue(exists);
            StaffProfile info = staffManager.getStaffInfo(id);
            assertNotNull(info);
        }
        
        @Test
        @DisplayName("Should support request management workflow")
        void testRequestManagementWorkflow() {
            RequestManager requestManager = adminFunction.getRequestManager();
            
            List<LeaveRequest> leaveRequests = requestManager.loadLeaveRequests();
            assertNotNull(leaveRequests);
            
            List<DutyRequest> dutyRequests = requestManager.loadDutyRequests();
            assertNotNull(dutyRequests);
        }
        
        @Test
        @DisplayName("Should support shift management workflow")
        void testShiftManagementWorkflow() {
            ShiftManager shiftManager = adminFunction.getShiftManager();
            List<Shift> shifts = shiftManager.loadShifts();
            assertNotNull(shifts);
        }
        
        
    }
    

