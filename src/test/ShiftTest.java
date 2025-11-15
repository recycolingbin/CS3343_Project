package test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import staffRosteringSystem.FileOperations;
import staffRosteringSystem.Shift;
import staffRosteringSystem.ShiftManager;
import staffRosteringSystem.ShiftSession;
import staffRosteringSystem.StaffManager;
import staffRosteringSystem.StaffProfile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {
    
    @Test
    void testMorningShiftProperties() {
        assertEquals("06:00", ShiftSession.MORNING.getStartTime());
        assertEquals("14:00", ShiftSession.MORNING.getEndTime());
        assertEquals("MORNING", ShiftSession.MORNING.toString());
    }
    
    @Test
    void testAfternoonShiftProperties() {
        assertEquals("14:00", ShiftSession.AFTERNOON.getStartTime());
        assertEquals("22:00", ShiftSession.AFTERNOON.getEndTime());
        assertEquals("AFTERNOON", ShiftSession.AFTERNOON.toString());
    }
    
    @Test
    void testNightShiftProperties() {
        assertEquals("22:00", ShiftSession.NIGHT.getStartTime());
        assertEquals("06:00", ShiftSession.NIGHT.getEndTime());
        assertEquals("NIGHT", ShiftSession.NIGHT.toString());
    }
    
    @Test
    void testFromStringValidUpperCase() {
        assertEquals(ShiftSession.MORNING, ShiftSession.fromString("MORNING"));
        assertEquals(ShiftSession.AFTERNOON, ShiftSession.fromString("AFTERNOON"));
        assertEquals(ShiftSession.NIGHT, ShiftSession.fromString("NIGHT"));
    }
    
    @Test
    void testFromStringValidLowerCase() {
        assertEquals(ShiftSession.MORNING, ShiftSession.fromString("morning"));
        assertEquals(ShiftSession.AFTERNOON, ShiftSession.fromString("afternoon"));
        assertEquals(ShiftSession.NIGHT, ShiftSession.fromString("night"));
    }
    
    @Test
    void testFromStringValidMixedCase() {
        assertEquals(ShiftSession.MORNING, ShiftSession.fromString("MoRnInG"));
        assertEquals(ShiftSession.AFTERNOON, ShiftSession.fromString("AfTeRnOoN"));
    }
    
    @Test
    void testFromStringWithWhitespace() {
        assertEquals(ShiftSession.MORNING, ShiftSession.fromString("  MORNING  "));
        assertEquals(ShiftSession.AFTERNOON, ShiftSession.fromString(" afternoon "));
    }
    
    @Test
    void testFromStringNull() {
        assertNull(ShiftSession.fromString(null));
    }
    
    @Test
    void testFromStringEmpty() {
        assertNull(ShiftSession.fromString(""));
        assertNull(ShiftSession.fromString("   "));
    }
    
    @Test
    void testFromStringInvalid() {
        assertNull(ShiftSession.fromString("INVALID"));
        assertNull(ShiftSession.fromString("DAWN"));
        assertNull(ShiftSession.fromString("123"));
    }
    
    @Test
    void testToString() {
        assertEquals("MORNING", ShiftSession.MORNING.name());
        assertEquals("AFTERNOON", ShiftSession.AFTERNOON.name());
        assertEquals("NIGHT", ShiftSession.NIGHT.name());
    }
}
