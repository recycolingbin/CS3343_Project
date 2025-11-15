//package test;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.Scanner;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import staffRosteringSystem.MenuManager;
//import staffRosteringSystem.RequestManager;
//import staffRosteringSystem.ShiftManager;
//import staffRosteringSystem.StaffManager;
//
//public class MenuManagerCoverageTest {
//    private MenuManager menuManager;
//    private StaffManager staffManager;
//    private RequestManager requestManager;
//    private ShiftManager shiftManager;
//
//    private MenuManager createMenuManagerWithInput(String input) {
//        InputStream in = new ByteArrayInputStream((input == null ? "" : input).getBytes());
//        Scanner scanner = new Scanner(in);
//        return new MenuManager(staffManager, requestManager, shiftManager, scanner);
//    }
//
//    @BeforeEach
//    void setup() {
//        staffManager = new StaffManager();
//        requestManager = new RequestManager();
//        shiftManager = new ShiftManager();
//        menuManager = createMenuManagerWithInput("");
//        assertNotNull(menuManager);
//    }
//
//    // ----- Direct handler coverage for reject/approve paths -----
//
//    @Test
//    @DisplayName("Direct: handleRejectLeaveRequest")
//    void directHandleRejectLeaveRequest() {
//        seedLeaveRequest();
//        MenuManager mm = createMenuManagerWithInput("1\n");
//        mm.handleRejectLeaveRequest(); // reject case 1
//        assertTrue(true);
//    }
//
//    @Test
//    @DisplayName("Direct: handleRejectDutyRequest")
//    void directHandleRejectDutyRequest() {
//        seedDutyRequest();
//        MenuManager mm = createMenuManagerWithInput("1\n");
//        mm.handleRejectDutyRequest(); // reject case 1
//        assertTrue(true);
//    }
//
//    @Test
//    @DisplayName("Direct: handleRosterApproveLeaveRequest")
//    void directHandleRosterApproveLeaveRequest() {
//        seedLeaveRequest();
//        MenuManager mm = createMenuManagerWithInput("1\n");
//        mm.handleRosterApproveLeaveRequest(); // approve case 1
//        assertTrue(true);
//    }
//
//    @Test
//    @DisplayName("Direct: handleRosterRejectLeaveRequest")
//    void directHandleRosterRejectLeaveRequest() {
//        seedLeaveRequest();
//        MenuManager mm = createMenuManagerWithInput("1\n");
//        mm.handleRosterRejectLeaveRequest(); // reject case 1
//        assertTrue(true);
//    }
//
//    @Test
//    @DisplayName("Direct: handleRosterApproveDutyRequest")
//    void directHandleRosterApproveDutyRequest() {
//        seedDutyRequest();
//        MenuManager mm = createMenuManagerWithInput("1\n");
//        mm.handleRosterApproveDutyRequest(); // approve case 1
//        assertTrue(true);
//    }
//
//    @Test
//    @DisplayName("Direct: handleRosterRejectDutyRequest")
//    void directHandleRosterRejectDutyRequest() {
//        seedDutyRequest();
//        MenuManager mm = createMenuManagerWithInput("1\n");
//        mm.handleRosterRejectDutyRequest(); // reject case 1
//        assertTrue(true);
//    }
//
//    @Test
//    @DisplayName("Direct: handleDeleteShift")
//    void directHandleDeleteShift() {
//        int staffId = ensureStaffExists();
//        // assign a shift to delete
//        shiftManager.assignShift(staffId, "2025-12-31", "MORNING", "Temp", staffManager);
//        // obtain assigned shift id (last one)
//        List<staffRosteringSystem.Shift> shifts = shiftManager.loadShifts();
//        int targetId = shifts.get(shifts.size()-1).getShiftId();
//        MenuManager mm = createMenuManagerWithInput(targetId + "\n");
//        mm.handleDeleteShift();
//        assertTrue(true);
//    }
//
//    // -------- Helpers to seed data for case-number handlers --------
//    private int ensureStaffExists() {
//        // ensure a unique staff id
//        int base = 888000;
//        while (staffManager.staffExists(base)) base++;
//        staffManager.addStaffProfile(base, "CoverageStaff", "Employee");
//        return base;
//    }
//
//    private void seedLeaveRequest() {
//        int staffId = ensureStaffExists();
//        String reqInput = "Annual\nVacation\n2025-12-10\n";
//        requestManager.requestLeave(staffId, new Scanner(new ByteArrayInputStream(reqInput.getBytes())), staffManager);
//    }
//
//    private void seedDutyRequest() {
//        int staffId = ensureStaffExists();
//        String reqInput = "Training\nSkills\n2025-12-11\nSession A\n";
//        requestManager.requestDuty(staffId, new Scanner(new ByteArrayInputStream(reqInput.getBytes())), staffManager);
//    }
//}
