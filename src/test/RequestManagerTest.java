package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.DutyRequest;
import staffRosteringSystem.LeaveRequest;
import staffRosteringSystem.RequestManager;
import staffRosteringSystem.StaffManager;

public class RequestManagerTest {
        
        private RequestManager requestManager;
        private StaffManager staffManager;
        
        @BeforeEach
        void setUp() {
            requestManager = new RequestManager();
            staffManager = new StaffManager();
        }
        
        // ---- Leave Request Loading Tests (1) ----
        
        @Test
        void testLoadLeaveRequests() {
            List<LeaveRequest> leaveRequests = requestManager.loadLeaveRequests();
            assertNotNull(leaveRequests);
        }
        
        // ---- Duty Request Loading Tests (1) ----
        
        @Test
        void testLoadDutyRequests() {
            List<DutyRequest> dutyRequests = requestManager.loadDutyRequests();
            assertNotNull(dutyRequests);
        }
        
        // ---- View Request Tests (2) ----
        
        @Test
        void testViewLeaveRequestsWithCaseNumbers() {
            assertDoesNotThrow(() -> {
                requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
            });
        }
        
        @Test
        void testViewDutyRequestsWithCaseNumbers() {
            assertDoesNotThrow(() -> {
                requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
            });
        }
        
        // ---- Leave Request Approval Tests (2) ----
        
        @Test
        void testApproveLeaveRequest() {
            boolean result = requestManager.approveLeaveRequestByCaseNumber(999);
            assertFalse(result);
        }
        
        @Test
        void testRejectLeaveRequest() {
            boolean result = requestManager.rejectLeaveRequestByCaseNumber(999);
            assertFalse(result);
        }
        
        // ---- Duty Request Approval Tests (2) ----
        
        @Test
        void testApproveDutyRequest() {
            boolean result = requestManager.approveDutyRequestByCaseNumber(999);
            assertFalse(result);
        }
        
        @Test
        void testRejectDutyRequest() {
            boolean result = requestManager.rejectDutyRequestByCaseNumber(999);
            assertFalse(result);
        }
        
        // ---- Request File Initialization Tests (1) ----
        
        @Test
        void testInitializeRequestFiles() {
            assertDoesNotThrow(() -> {
                requestManager.initializeRequestFiles(null);
            });
        }
        
        // ---- Additional Request Tests (3) ----
        
        @Test
        void testViewRequestsMultipleScenarios() {
            assertDoesNotThrow(() -> {
                requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
                requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
            });
        }
        
        @Test
        void testRequestStateConsistency() {
            List<LeaveRequest> leave1 = requestManager.loadLeaveRequests();
            List<LeaveRequest> leave2 = requestManager.loadLeaveRequests();
            assertNotNull(leave1);
            assertNotNull(leave2);
        }
        
        @Test
        void testMixedRequestOperations() {
            requestManager.loadLeaveRequests();
            requestManager.loadDutyRequests();
            assertDoesNotThrow(() -> {
                requestManager.viewLeaveRequestsWithCaseNumbers(staffManager);
                requestManager.viewDutyRequestsWithCaseNumbers(staffManager);
            });
        }
    }
