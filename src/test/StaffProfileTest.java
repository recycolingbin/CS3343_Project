package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import staffRosteringSystem.StaffProfile;

public class StaffProfileTest {
    @Test
    void testStaffProfileConstructor() {
        StaffProfile sp = new StaffProfile(5555, "Test User", "Employee");
        assertEquals(5555, sp.getStaffId());
        assertEquals("Test User", sp.getName());
        assertEquals("Employee", sp.getRole());
    }

    @Test
    void testSetName() {
        StaffProfile sp = new StaffProfile(1, "Old", "Role");
        sp.setName("New");
        assertEquals("New", sp.getName());
    }

    @Test
    void testSetRole() {
        StaffProfile sp = new StaffProfile(1, "Name", "OldRole");
        sp.setRole("NewRole");
        assertEquals("NewRole", sp.getRole());
    }

    @Test
    void testLongFields() {
        String longName = "A".repeat(200);
        String longRole = "R".repeat(200);
        StaffProfile sp = new StaffProfile(9, longName, longRole);
        assertEquals(longName, sp.getName());
        assertEquals(longRole, sp.getRole());
    }

    @Test
    void testEmptyRole() {
        StaffProfile sp = new StaffProfile(2, "Name", "");
        assertEquals("", sp.getRole());
    }

    @Test
    void testEmptyName() {
        StaffProfile sp = new StaffProfile(3, "", "Employee");
        assertEquals("", sp.getName());
    }
}
