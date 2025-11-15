package test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import staffRosteringSystem.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestManagerTest {

    private StaffManager staffManager;
    private FileOperations fileOps;
    private RequestManager requestManager;

    private String leaveFilePath;
    private String dutyFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        // Create isolated file paths
        leaveFilePath = tempDir.resolve("Leave_Request.txt").toString();
        dutyFilePath  = tempDir.resolve("Duty_Request.txt").toString();

        staffManager = new StaffManager();
        fileOps = new FileOperations();

        // Inject dependencies
        requestManager = new RequestManager(leaveFilePath, dutyFilePath, staffManager, fileOps);

        // Add test staff
        staffManager.addStaffProfile(101, "John Doe", "Engineer");
        staffManager.addStaffProfile(102, "Jane Smith", "Nurse");
    }

    /* ==============================================================
       1. submitLeaveRequest()
       ============================================================== */
    @Test
    void testSubmitLeaveRequest_Success() {
        boolean result = requestManager.submitLeaveRequest(101, "Sick", "Flu", "2025-01-15");

        assertTrue(result);
        assertEquals(1, requestManager.getLeaveRequestCount());

        LeaveRequest req = requestManager.findLeaveRequestById(1001);
        assertNotNull(req);
        assertEquals("Sick", req.getLeaveType());
        assertEquals("Flu", req.getReason());
        assertEquals("2025-01-15", req.getRequestDate());
    }

    @Test
    void testSubmitLeaveRequest_EmployeeNotFound() {
        boolean result = requestManager.submitLeaveRequest(999, "Annual", "Vacation", "2025-02-01");

        assertFalse(result);
        assertEquals(0, requestManager.getLeaveRequestCount());
    }

    /* ==============================================================
       2. viewLeaveRequestsWithCaseNumbers()
       ============================================================== */
    @Test
    void testViewLeaveRequests_PrintsCorrectly() {
        requestManager.submitLeaveRequest(101, "Sick", "Headache", "2025-01-20");
        requestManager.submitLeaveRequest(102, "Annual", "Trip", "2025-02-01");

        // Capture console output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
		AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", staffManager, requestManager,
				new ShiftManager(), new Scanner(System.in));
		adminFunction.viewLeaveRequests();

        String output = out.toString();
        assertTrue(output.contains("Case"));
        assertTrue(output.contains("Sick"));
        assertTrue(output.contains("Annual"));
        assertTrue(output.contains("1"));
        assertTrue(output.contains("2"));

        System.setOut(System.out); // restore
    }

    @Test
    void testViewLeaveRequests_EmptyList() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        requestManager.viewLeaveRequestsWithCaseNumbers();

        assertTrue(out.toString().contains("No pending leave requests found."));
        System.setOut(System.out);
    }

    /* ==============================================================
       3. approveLeaveRequestByCaseNumber()
       ============================================================== */
    @Test
    void testApproveLeaveRequest_Success() {
        requestManager.submitLeaveRequest(101, "Sick", "Flu", "2025-01-15");
        assertEquals(1, requestManager.getLeaveRequestCount());
        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", staffManager, requestManager,
    			new ShiftManager(), new Scanner(System.in));
        boolean result = adminFunction.approveLeaveRequest(1);
        assertTrue(result);
        assertEquals(0, requestManager.getLeaveRequestCount());
    }

    @Test
    void testApproveLeaveRequest_InvalidCaseNumber() {
        boolean result = requestManager.approveLeaveRequestByCaseNumber(5);
        assertFalse(result);
        assertEquals(0, requestManager.getLeaveRequestCount());
    }

    /* ==============================================================
       4. rejectLeaveRequestByCaseNumber()
       ============================================================== */
    @Test
    void testRejectLeaveRequest_Success() {
        requestManager.submitLeaveRequest(101, "Sick", "Cold", "2025-01-10");
        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", staffManager, requestManager,
    			new ShiftManager(), new Scanner(System.in));
        boolean result = adminFunction.rejectLeaveRequest(1);

        assertTrue(result);
        assertEquals(0, requestManager.getLeaveRequestCount());
    }
    
    @Test
	void testRejectLeaveRequest_InvalidCaseNumber() {
		boolean result = requestManager.rejectLeaveRequestByCaseNumber(3);
		assertFalse(result);
		assertEquals(0, requestManager.getLeaveRequestCount());
	}

    /* ==============================================================
       5. findLeaveRequestById()
       ============================================================== */
    @Test
    void testFindLeaveRequestById_Found() {
        requestManager.submitLeaveRequest(101, "Annual", "Trip", "2025-03-01");
        LeaveRequest req = requestManager.findLeaveRequestById(1001);

        assertNotNull(req);
        assertEquals(101, req.getEmployeeId());
    }

    @Test
    void testFindLeaveRequestById_NotFound() {
        assertNull(requestManager.findLeaveRequestById(9999));
    }

    /* ==============================================================
       6. submitDutyRequest()
       ============================================================== */
    @Test
    void testSubmitDutyRequest_Success() {
        boolean result = requestManager.submitDutyRequest(102, "Night Shift", "2025-01-16");

        assertTrue(result);
        assertEquals(1, requestManager.getDutyRequestCount());

        DutyRequest req = requestManager.findDutyRequestById(2001);
        assertNotNull(req);
        assertEquals("Night Shift", req.getSection());
    }

    @Test
    void testSubmitDutyRequest_EmployeeNotFound() {
        boolean result = requestManager.submitDutyRequest(999, "Overtime", "2025-01-17");
        assertFalse(result);
        assertEquals(0, requestManager.getDutyRequestCount());
    }

    /* ==============================================================
       7. viewDutyRequestsWithCaseNumbers()
       ============================================================== */
    @Test
    void testViewDutyRequests_PrintsCorrectly() {
        requestManager.submitDutyRequest(101, "Training", "2025-02-10");
        requestManager.submitDutyRequest(102, "Meeting", "2025-02-11");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", staffManager, requestManager,
				new ShiftManager(), new Scanner(System.in));
        adminFunction.viewDutyRequests();

        String output = out.toString();
        assertTrue(output.contains("Training"));
        assertTrue(output.contains("Meeting"));
        assertTrue(output.contains("1"));
        assertTrue(output.contains("2"));

        System.setOut(System.out);
    }
    
    @Test
	void testViewDutyRequests_EmptyList() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		requestManager.viewDutyRequestsWithCaseNumbers();

		assertTrue(out.toString().contains("No pending duty requests found."));
		System.setOut(System.out);
	}
    
    /* ==============================================================
       8. approveDutyRequestByCaseNumber()
       ============================================================== */
    @Test
    void testApproveDutyRequest_Success() {
        requestManager.submitDutyRequest(101, "Audit", "2025-03-01");
        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", staffManager, requestManager,
    			new ShiftManager(), new Scanner(System.in));
        boolean result = adminFunction.approveDutyRequest(1);

        assertTrue(result);
        assertEquals(0, requestManager.getDutyRequestCount());
    }
    
    @Test
	void testApproveDutyRequest_InvalidCaseNumber() {
		boolean result = requestManager.approveDutyRequestByCaseNumber(4);
		assertFalse(result);
		assertEquals(0, requestManager.getDutyRequestCount());
	}

    /* ==============================================================
       9. rejectDutyRequestByCaseNumber()
       ============================================================== */
    @Test
    void testRejectDutyRequest_Success() {
        requestManager.submitDutyRequest(102, "Backup", "2025-03-02");
        AdminFunction adminFunction = new AdminFunction(2001, "admin", "admin123", staffManager, requestManager,
    			new ShiftManager(), new Scanner(System.in));
        boolean result = adminFunction.rejectDutyRequest(1);

        assertTrue(result);
        assertEquals(0, requestManager.getDutyRequestCount());
    }
    
    @Test
	void tesRejecttRejectDutyRequest_InvalidCaseNumber() {
		boolean result = requestManager.rejectDutyRequestByCaseNumber(2);
		assertFalse(result);
		assertEquals(0, requestManager.getDutyRequestCount());
	}

    /* ==============================================================
       10. findDutyRequestById()
       ============================================================== */
    @Test
    void testFindDutyRequestById_Found() {
        requestManager.submitDutyRequest(101, "On-call", "2025-04-01");
        DutyRequest req = requestManager.findDutyRequestById(2001);
        assertNotNull(req);
    }

    @Test
    void testFindDutyRequestById_NotFound() {
        assertNull(requestManager.findDutyRequestById(9999));
    }

    /* ==============================================================
       11. getLeaveRequestCount() / getDutyRequestCount()
       ============================================================== */
    @Test
    void testRequestCounts() {
        requestManager.submitLeaveRequest(101, "Sick", "Fever", "2025-05-01");
        requestManager.submitDutyRequest(102, "Training", "2025-05-02");

        assertEquals(1, requestManager.getLeaveRequestCount());
        assertEquals(1, requestManager.getDutyRequestCount());
    }

    /* ==============================================================
       12. getEmployeeLeaveRequests() / getEmployeeDutyRequests()
       ============================================================== */
    @Test
    void testGetEmployeeLeaveRequests() {
        requestManager.submitLeaveRequest(101, "Annual", "Trip", "2025-06-01");
        requestManager.submitLeaveRequest(101, "Sick", "Cold", "2025-06-02");
        requestManager.submitLeaveRequest(102, "Unpaid", "Personal", "2025-06-03");

        List<LeaveRequest> johns = requestManager.getEmployeeLeaveRequests(101);
        List<LeaveRequest> janes = requestManager.getEmployeeLeaveRequests(102);

        assertEquals(2, johns.size());
        assertEquals(1, janes.size());
    }
    
    @Test
    void testGetEmployeeDutyRequests() {
        RequestManager requestManager = new RequestManager(leaveFilePath, dutyFilePath, staffManager, fileOps);
        requestManager.submitDutyRequest(101, "MORNING", "2025-06-10");
        requestManager.submitDutyRequest(101, "EVENING", "2025-06-11");
        requestManager.submitDutyRequest(102, "NIGHT", "2025-06-12");
        
        List<DutyRequest> johns = requestManager.getEmployeeDutyRequests(101);
        List<DutyRequest> janes = requestManager.getEmployeeDutyRequests(102);
        
        assertEquals(2, johns.size());
        assertEquals(1, janes.size());
    }

    /* ==============================================================
       13. getAllLeaveRequests() / getAllDutyRequests() → defensive copy
       ============================================================== */
    @Test
    void testGetAllLeaveRequests() {
        requestManager.submitLeaveRequest(101, "Sick", "Flu", "2025-07-01");

        List<LeaveRequest> copy = requestManager.getAllLeaveRequests();
        copy.clear(); 

        assertEquals(1, requestManager.getLeaveRequestCount()); 
    }
    
    @Test
	void testGetAllDutyRequests() {
		requestManager.submitDutyRequest(102, "Overtime", "2025-07-02");

		List<DutyRequest> copy = requestManager.getAllDutyRequests();
		copy.clear();

		assertEquals(1, requestManager.getDutyRequestCount());
	}

    /* ==============================================================
       14. reloadFromFiles() — ensures file persistence
       ============================================================== */
    @Test
    void testReloadFromFiles_PersistsData() throws Exception {
        requestManager.submitLeaveRequest(101, "Annual", "Vacation", "2025-08-01");

        // Create new manager with same files
        RequestManager newMgr = new RequestManager(leaveFilePath, dutyFilePath, staffManager, fileOps);

        assertEquals(1, newMgr.getLeaveRequestCount());
        LeaveRequest req = newMgr.findLeaveRequestById(1001);
        assertNotNull(req);
        assertEquals("Vacation", req.getReason());
    }

    /* ==============================================================
    15. Constructor Testing
    ============================================================== */
 @Test
 void testDefaultConstructor_CreatesValidInstance() {
     // Use default constructor
     RequestManager rm = new RequestManager();
     
     // Verify instance is created and functional
     assertNotNull(rm);
 }

 @Test
 void testStaffManagerConstructor_CreatesValidInstance() {
     // Create a StaffManager with test data
     StaffManager customStaffManager = new StaffManager();
     customStaffManager.addStaffProfile(500, "Alice", "Developer");
     
     RequestManager rm = new RequestManager(customStaffManager);
     
     assertNotNull(rm);

 }

}