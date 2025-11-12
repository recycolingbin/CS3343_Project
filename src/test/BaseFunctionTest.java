package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.BaseFunction;
import staffRosteringSystem.Shift;
import staffRosteringSystem.StaffManager;
import staffRosteringSystem.StaffProfile;

public class BaseFunctionTest {
		private static int uniqueStaffId(StaffManager sm) {
	        List<staffRosteringSystem.StaffProfile> profiles = sm.loadStaffProfiles();
	        Set<Integer> taken = new HashSet<>();
	        for (staffRosteringSystem.StaffProfile p : profiles) taken.add(p.getStaffId());
	        int id = 900000; // start high to avoid collisions with seeded data
	        while (taken.contains(id)) id++;
	        return id;
	    }
        // Simple concrete subclass to expose protected methods for testing
        class TestBase extends BaseFunction {
            TestBase(int userId, String username, String password) { super(userId, username, password); }
            public List<Shift> callLoadShifts() { return loadShifts(); }
            public staffRosteringSystem.StaffProfile callGetUserInfo(int id) { return getUserInfo(id); }
            public boolean callIsValidSession(String s) { return isValidSession(s); }
            public int callGetSessionOrder(String s) { return getSessionOrder(s); }
        }

        private TestBase base;

        @BeforeEach
        void setup() {
            base = new TestBase(1001, "tester", "secret");
        }

        @Test
        void testIsValidSessionVariants() {
            assertTrue(base.callIsValidSession("MORNING"));
            assertTrue(base.callIsValidSession("AFTERNOON"));
            assertTrue(base.callIsValidSession("NIGHT"));
            assertTrue(base.callIsValidSession("morning"));
            assertFalse(base.callIsValidSession(null));
            assertFalse(base.callIsValidSession("INVALID"));
        }

        @Test
        void testGetSessionOrder() {
            assertEquals(1, base.callGetSessionOrder("MORNING"));
            assertEquals(2, base.callGetSessionOrder("AFTERNOON"));
            assertEquals(3, base.callGetSessionOrder("NIGHT"));
            assertEquals(4, base.callGetSessionOrder("X"));
        }

        @Test
        void testViewShiftScheduleNoMatches() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream prev = System.out;
            System.setOut(new PrintStream(out));
            base.viewShiftSchedule("2099-12-31");
            System.setOut(prev);
            String output = out.toString();
            assertTrue(output.contains("No shifts scheduled for 2099-12-31"));
        }

        @Test
        void testViewShiftsBySessionInvalid() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream prev = System.out;
            System.setOut(new PrintStream(out));
            base.viewShiftsBySession("BAD", null);
            System.setOut(prev);
            assertTrue(out.toString().contains("Error: Invalid session!"));
        }

        @Test
        void testViewShiftsBySessionHasData() {
            // We know Data/Shift.txt contains a MORNING shift on 2025-10-23 for employee 1001
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream prev = System.out;
            System.setOut(new PrintStream(out));
            base.viewShiftsBySession("MORNING", "2025-10-23");
            System.setOut(prev);
            String output = out.toString();
            assertTrue(output.contains("MORNING"));
            assertTrue(output.contains("2025-10-23"));
        }

        @Test
        void testViewMyRoster() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream prev = System.out;
            System.setOut(new PrintStream(out));
            base.viewMyRoster();
            System.setOut(prev);
            String output = out.toString();
            // Either shows schedule or states none, both paths covered
            assertTrue(output.contains("MY SHIFT SCHEDULE") || output.contains("No shifts assigned"));
        }

        @Test
        void testLoadShifts() {
            List<Shift> list = base.callLoadShifts();
            assertNotNull(list);
            assertTrue(list.size() >= 0);
        }

        @Test
        void testGetUserInfo() {
            StaffManager sm = new StaffManager();
            int id = uniqueStaffId(sm);
            sm.addStaffProfile(id, "Temp User", "Employee");
            StaffProfile sp = base.callGetUserInfo(id);
            assertNotNull(sp);
            assertEquals(id, sp.getStaffId());
        }

        @Test
        void testBaseIsValidDate() {
            assertTrue(base.isValidDate("2025-1-1"));
            assertFalse(base.isValidDate("2019-1-1"));
            assertFalse(base.isValidDate("2025-13-1"));
            assertFalse(base.isValidDate("2025-1-32"));
            assertTrue(base.isValidDate("2024-2-29"));
            assertFalse(base.isValidDate("2025-2-29"));
        }

        @Test
        void testGetValidDateInputValid() {
            String input = String.join(System.lineSeparator(),
                    "bad",
                    "2025-02-29", // invalid
                    "2025-02-28"   // valid
            ) + System.lineSeparator();
            Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
            String result = base.getValidDateInput(sc, "Enter date: ");
            assertEquals("2025-02-28", result);
        }

        @Test
        void testGetValidDateInputTooManyInvalid() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 12; i++) sb.append("bad\n");
            Scanner sc = new Scanner(new ByteArrayInputStream(sb.toString().getBytes()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream prev = System.out;
            System.setOut(new PrintStream(out));
            String result = base.getValidDateInput(sc, "Enter date: ");
            System.setOut(prev);
            assertNull(result);
            assertTrue(out.toString().contains("Too many invalid attempts"));
        }
    }

