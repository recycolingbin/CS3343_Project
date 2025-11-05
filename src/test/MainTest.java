package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import staffRosteringSystem.EmployeeFunction;
import staffRosteringSystem.Main;

public class MainTest {
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
	
}
