package employeeFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;

import baseFunction.BaseUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeFunction extends BaseUser {
	private EmployeeFunction instance;

	public EmployeeFunction getInstance() {
		if (instance == null) {
			instance = new EmployeeFunction();
		}
		return instance;
	}
	
	public int getfilelinesnum(String filename) {
		int lines = 0;
		try (Scanner scanner = new Scanner(new File(filename))) {
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				lines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public void Request_duty(String userid) {
		System.out.println("Please enter the day you want to request a duty:");
		Scanner scanner = new Scanner(System.in);
		String day = scanner.nextLine();

		System.out.println("1. Morning\n2. Afternoon\n3. Night\nPlease enter the section you want to request on " + day + ":");
		String section = scanner.nextLine();

		File shift = new File("Shift.txt");
		try (Scanner fileScanner = new Scanner(shift)) {
			boolean found = false;
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				String[] info = line.split("\\|");
				if (info[0].equals(userid)) {
					if (info[1].equals(day)) {
						found = true;
						break;
					}
				}
			}
			if (found) {
				System.out.println("You already have duty on " + day + " at section " + section
						+ ". Cannot request another duty.");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (FileWriter writer = new FileWriter("Duty_Request.txt", true)) {
			writer.write(userid + "|" + day + "|" + section);
		} catch (IOException e) {
			e.printStackTrace();
		}

		scanner.close();
	}

	public void Request_leave(String userid) {
		System.out.println("Please enter the day you want to request a leave (You can only request the day you have duty on)\nPlease enter the start date (The format should be MM-DD):");
		Scanner scanner = new Scanner(System.in);
		String sday = Year.now().getValue() + "-" + scanner.nextLine();
		System.out.println("Please enter the end date");
		String eday = Year.now().getValue() + "-" + scanner.nextLine();

		File shift = new File("Shift.txt");
		try (Scanner fileScanner = new Scanner(shift)) {
			boolean found = false;
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				String[] info = line.split("\\|");
				if (info[0].equals(userid)) {
					if (info[1].compareTo(sday) >= 0 && info[1].compareTo(eday) <= 0) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				System.out.println("You do not have a duty during " + sday + " to " + eday + ". Cannot request leave.");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Please enter the reason for taking leave:");
		String reason = scanner.nextLine();

		try (FileWriter writer = new FileWriter("Leave_Request.txt", true)) {
			int lines = getfilelinesnum("Leave_Request.txt") + 1000;
			writer.write(lines + "|" + userid + "|" + sday + "|" + eday + "|" + reason + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		scanner.close();
	}

	public void Login_page(String userid) {
		while (true) {
			super.viewFunction();
			System.out.println("Please input the action you want to do: \n 1.Request Duty \n 2.Request Leave \n 3. Logout");
			Scanner scanner = new Scanner(System.in);
			int action = scanner.nextInt();
			switch (action) {
				case 1:
					Request_duty(userid);
					break;
				case 2:
					Request_leave(userid);
					break;
				case 3:
					System.out.println("Are you sure you want to logout? (Y/N)");
					String confirm = scanner.next();
					if (confirm.equalsIgnoreCase("Y")) {
						System.out.println("Logging out...");
						return;
					} else {
						break;
					}
			}
		}

	}
}