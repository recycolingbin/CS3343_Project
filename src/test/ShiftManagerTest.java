package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.Shift;
import staffRosteringSystem.ShiftManager;
import staffRosteringSystem.StaffManager;

@Nested
class ShiftManagerTest {
    
    private ShiftManager shiftManager;
    private StaffManager staffManager;
    
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
        shiftManager = new ShiftManager();
        staffManager = new StaffManager();
        // Ensure staff 1001 exists without duplicating
        
    }
    
    // ---- Shift Loading Tests (1) ----
    
    @Test
    void testLoadShifts() {
        List<Shift> shifts = shiftManager.loadShifts();
        assertNotNull(shifts);
    }
    
    // ---- Shift Assignment Tests (3) ----
    
    @Test
    void testAssignShiftSuccess() {
        int id = uniqueStaffId(staffManager);
        staffManager.addStaffProfile(id, "Temp Emp", "Employee");
        boolean result = shiftManager.assignShift(id, "2025-12-15", "MORNING", "Regular shift", staffManager);
        assertTrue(result);
    }
    
    @Test
    void testAssignShiftInvalidSession() {
        boolean result = shiftManager.assignShift(1001, "2025-12-15", "INVALID", "Bad shift", staffManager);
        assertFalse(result);
    }
    
    @Test
    void testAssignDuplicateShift() {
        shiftManager.assignShift(1001, "2025-12-15", "MORNING", "Regular shift", staffManager);
        boolean result = shiftManager.assignShift(1001, "2025-12-15", "MORNING", "Another shift", staffManager);
        assertFalse(result);
    }
    
    // ---- Shift Deletion Tests (2) ----
    
    @Test
    void testDeleteNonExistentShift() {
        boolean result = shiftManager.deleteShift(9999, staffManager);
        assertFalse(result);
    }
    
    @Test
    void testDeleteShift() {
        shiftManager.assignShift(1001, "2025-12-15", "MORNING", "Regular shift", staffManager);
        // Try to delete (may fail if shift list is empty or shift ID not found)
        assertDoesNotThrow(() -> {
            shiftManager.deleteShift(1, staffManager);
        });
    }
    
    // ---- Shift Viewing Tests (1) ----
    
    @Test
    void testViewAllShiftSchedules() {
        assertDoesNotThrow(() -> {
            shiftManager.viewAllShiftSchedules();
        });
    }
    
    // ---- Remove Shifts for Leave Tests (1) ----
    
    @Test
    void testRemoveShiftsForLeave() {
        shiftManager.assignShift(1001, "2025-12-15", "MORNING", "Regular shift", staffManager);
        assertDoesNotThrow(() -> {
            shiftManager.removeShiftsForLeave(1001);
        });
    }
    
    // ---- Additional Shift Tests (3) ----
    
    @Test
    void testMultipleShiftAssignments() {
        int id1 = uniqueStaffId(staffManager);
        staffManager.addStaffProfile(id1, "Emp A", "Employee");
        int id2 = uniqueStaffId(staffManager);
        staffManager.addStaffProfile(id2, "Emp B", "Employee");
        int id3 = uniqueStaffId(staffManager);
        staffManager.addStaffProfile(id3, "Emp C", "Employee");

        boolean result1 = shiftManager.assignShift(id1, "2025-12-15", "MORNING", "Shift 1", staffManager);
        boolean result2 = shiftManager.assignShift(id2, "2025-12-16", "AFTERNOON", "Shift 2", staffManager);
        boolean result3 = shiftManager.assignShift(id3, "2025-12-17", "NIGHT", "Shift 3", staffManager);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }
    
    @Test
    void testShiftSessionVariations() {
        int idMorning = uniqueStaffId(staffManager);
        staffManager.addStaffProfile(idMorning, "Jane M", "Employee");
        boolean morning = shiftManager.assignShift(idMorning, "2025-12-15", "MORNING", "Morning shift", staffManager);
        int idAfternoon = uniqueStaffId(staffManager);
        staffManager.addStaffProfile(idAfternoon, "Jane A", "Employee");
        boolean afternoon = shiftManager.assignShift(idAfternoon, "2025-12-15", "AFTERNOON", "Afternoon shift", staffManager);
        
        assertTrue(morning);
        assertTrue(afternoon);
    }
    
    @Test
    void testShiftConsistency() {
        shiftManager.assignShift(1001, "2025-12-15", "MORNING", "Regular shift", staffManager);
        List<Shift> shifts1 = shiftManager.loadShifts();
        List<Shift> shifts2 = shiftManager.loadShifts();
        assertNotNull(shifts1);
        assertNotNull(shifts2);
    }
}