package main_employeefunction_testcase;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.Test;
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

}
