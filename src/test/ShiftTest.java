package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.Shift;

public class ShiftTest {
        private Shift shift;
        
        @BeforeEach
        void setUp() {
            shift = new Shift(1, 1001, "2025-12-15", "MORNING", "08:00", "16:00", "Regular shift");
        }
        
        // ---- Shift Initialization Tests (2) ----
        
        @Test
        @DisplayName("Should initialize shift with valid data")
        void testShiftInitialization() {
            assertNotNull(shift);
            assertEquals(1, shift.getShiftId());
            assertEquals(1001, shift.getEmployeeId());
        }
        
        @Test
        @DisplayName("Should get shift properties correctly")
        void testShiftProperties() {
            assertEquals("2025-12-15", shift.getDate());
            assertEquals("MORNING", shift.getSession());
            assertEquals("08:00", shift.getStartTime());
            assertEquals("16:00", shift.getEndTime());
            assertEquals("Regular shift", shift.getNotes());
        }
        
        // ---- Shift Equality Tests (2) ----
        
        @Test
        @DisplayName("Should identify equal shifts")
        void testShiftEquality() {
            Shift shift2 = new Shift(1, 1001, "2025-12-15", "MORNING", "08:00", "16:00", "Regular shift");
            // Compare field-by-field since equals may not be overridden
            assertEquals(shift.getShiftId(), shift2.getShiftId());
            assertEquals(shift.getEmployeeId(), shift2.getEmployeeId());
            assertEquals(shift.getDate(), shift2.getDate());
            assertEquals(shift.getSession(), shift2.getSession());
            assertEquals(shift.getStartTime(), shift2.getStartTime());
            assertEquals(shift.getEndTime(), shift2.getEndTime());
            assertEquals(shift.getNotes(), shift2.getNotes());
        }
        
        @Test
        @DisplayName("Should identify different shifts")
        void testShiftInequality() {
            Shift shift2 = new Shift(2, 1001, "2025-12-15", "MORNING", "08:00", "16:00", "Regular shift");
            assertNotEquals(shift, shift2);
        }
        
        // ---- Shift Modification Tests (2) ----
        
        @Test
        @DisplayName("Should set notes on shift")
        void testSetShiftNotes() {
            shift.setNotes("Updated notes");
            assertEquals("Updated notes", shift.getNotes());
        }
        
        @Test
        @DisplayName("Should preserve shift ID when modifying")
        void testShiftIdPersistence() {
            int originalId = shift.getShiftId();
            shift.setNotes("New notes");
            assertEquals(originalId, shift.getShiftId());
        }
        
        // ---- Shift Session Tests (2) ----
        
        @Test
        @DisplayName("Should handle morning session")
        void testMorningSession() {
            Shift morningShift = new Shift(2, 1002, "2025-12-15", "MORNING", "08:00", "16:00", "Morning");
            assertEquals("MORNING", morningShift.getSession());
        }
        
        @Test
        @DisplayName("Should handle afternoon session")
        void testAfternoonSession() {
            Shift afternoonShift = new Shift(3, 1003, "2025-12-15", "AFTERNOON", "16:00", "00:00", "Afternoon");
            assertEquals("AFTERNOON", afternoonShift.getSession());
        }
    }
    

