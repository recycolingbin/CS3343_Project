package main_employeefunction_testcase;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.Test;

import adminFunction.AdminFunction;
import employeeFunction.EmployeeFunction;
import main.Main;

class Main_employeefunction_testcase {

	@Test
	void mainTest() throws Exception {
		String input = "3\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		
		Main.main(new String[] {});
		
		String output = out.toString();
		assertTrue(output.contains("See you next time :)"));
		
		System.setIn(System.in);
		System.setOut(System.out);
	}
	
	@Test
	void loginPageTest() {
		EmployeeFunction e = new EmployeeFunction();
		String input = "4\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		
		e.loginPage("1002");
		String output = out.toString();
		assertTrue(output.contains("Logging out..."));
		
		System.setIn(System.in);
		System.setOut(System.out);
	}
	
	@Test
	void addDutyTest1() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.addDuty("1002", "2025-12-25", "MORNING");
		assertEquals(true, result);
	}
	
	@Test
	void addDutyTest2() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.addDuty("1002", "2025-12-25", "MORNING");
		assertEquals(false, result);
	}
	
	@Test
	void checkDutyTest1() throws Exception {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.checkDuty("1003", "2025-11-11", "2025-11-13");
		assertEquals(true, result);
	}
	
	@Test
	void checkDutyTest2() throws Exception {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.checkDuty("1003", "2025-11-12", "2025-11-13");
		assertEquals(true, result);
	}
	
	@Test
	void checkDutyTest3() throws Exception {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.checkDuty("1003", "2025-11-11", "2025-11-12");
		assertEquals(true, result);
	}
	
	@Test
	void checkDutyTest4() throws Exception {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.checkDuty("1003", "2025-11-10", "2025-11-11");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest1() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("1");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest2() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2019-1-1");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest3() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2031-1-1");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest4() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2025-13-1");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest5() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2025-0-1");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest6() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2025-1-0");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest7() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2025-1-32");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest8() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2025-2-29");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest9() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2025-4-31");
		assertEquals(false, result);
	}
	
	@Test
	void isValidDateTest10() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2025-1-1");
		assertEquals(true, result);
	}
	
	@Test
	void isLeapYearTest1() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.isValidDate("2024-2-29");
		assertEquals(true, result);
	}
}
