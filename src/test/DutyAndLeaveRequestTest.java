package test;

import org.junit.jupiter.api.*;

import staffRosteringSystem.DutyRequest;
import staffRosteringSystem.LeaveRequest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class DutyAndLeaveRequestTest {
	
	@Test
    void testFromFileFormat1() {
        assertNull(DutyRequest.fromFileFormat(null));
    }
	
	@Test
	void testFromFileFormat2() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        assertNull(DutyRequest.fromFileFormat("2001,101,2025-02-01"));
        assertNull(DutyRequest.fromFileFormat("2001,101"));

        String output = out.toString();
        assertTrue(output.contains("Warning: Invalid duty request format"));
        System.setOut(System.out);
	}

    @Test
    void testFromFileFormat3() {
        assertNull(LeaveRequest.fromFileFormat(null));
    }
    
    @Test
    void testFromFileFormatValid() {
		DutyRequest request = DutyRequest.fromFileFormat("5,105,2025-11-10,Operations");
		
		assertNotNull(request);
		assertEquals(5, request.getRequestId());
		assertEquals(105, request.getEmployeeId());
		assertEquals("2025-11-10", request.getRequestDate());
		assertEquals("Operations", request.getSection());
	}
    
    @Test
    void testFromFileFormat4() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        assertNull(LeaveRequest.fromFileFormat("101|1001|2025-01-01|Annual"));
        assertNull(LeaveRequest.fromFileFormat("101|1001|2025-01-01"));

        String output = out.toString();
        assertTrue(output.contains("Warning: Invalid leave request format"));
        System.setOut(System.out);
    }

}