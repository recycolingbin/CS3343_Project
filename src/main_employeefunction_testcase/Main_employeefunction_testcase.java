package main_employeefunction_testcase;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.Test;

import employeeFunction.EmployeeFunction;
import main.Main;
import adminFunction.AdminFunction;

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
	void addDutyTest() {
		EmployeeFunction e = new EmployeeFunction();
		boolean result = e.addDuty("1002", "2024-12-25", "Morning");
		assertEquals(true, result);		
	}
	
	@Test
	void removeDutyTest() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		AdminFunction admin = new AdminFunction(2001, "admin", "adminpass");
		admin.removeDutyRequest(1002, "2024-12-25", "Morning");		
		
		System.setOut(new PrintStream(out));
		String output = out.toString();
		assertTrue(output.contains("Duty request removed for employee 2001 session Morning"));
	}
}
