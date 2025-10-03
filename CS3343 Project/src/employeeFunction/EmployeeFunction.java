package employeeFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EmployeeFunction extends BaseFunction{
	private EmployeeFunction instance;
	
	public EmployeeFunction getInstance() {
		if (instance == null) {
			instance = new EmployeeFunction();
		}
		return instance;
	}
	
	public void Request_duty(String userid) {
		System.out.println("Please enter the day you want to request a duty:");
		Scanner scanner = new Scanner(System.in);
		String day = scanner.nextLine();
		
		System.out.println("Please enter the section you want to request on " + day + ":");
		String section = scanner.nextLine();
		
		File shift = new File("Shift.txt");
		try (Scanner fileScanner = new Scanner(shift)) {
            boolean found = false;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.equals(userid)) {
                    line = fileScanner.nextLine();
                    if (line.equals(day)) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                System.out.println("You already have duty on " + day + " at section " + section + ". Cannot request another duty.");
                return;
            }
		}catch (Exception e) {
            e.printStackTrace();
		}
		
		try (FileWriter writer = new FileWriter("Duty_Request.txt", true)) {
			writer.write(userid + "\n" + day + "\n" + section);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		scanner.close();
	}
	
	public void Request_leave(String userid) {
		System.out.println("Please enter the day you want to request a leave (You can only request the day you have duty on):");
		Scanner scanner = new Scanner(System.in);
		String day = scanner.nextLine();
		
		File shift = new File("Shift.txt");
		try (Scanner fileScanner = new Scanner(shift)) {
			boolean found = false;
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				if (line.equals(userid)) {
					line = fileScanner.nextLine();
					if (line.equals(day)) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				System.out.println("You do not have a duty on " + day + ". Cannot request leave.");
				return;
			}
			
		}catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println("Please enter the section you want to request on " + day + ":");
		String section = scanner.nextLine();
		
		try (FileWriter writer = new FileWriter("Leave_Request.txt", true)) {
			writer.write(userid + "\n" + day + "\n" + section);
		} catch (IOException e) {
			e.printStackTrace();
		}

		scanner.close();
	}
	
	public void Login_page(String userid) {
		while (true) {
			super.View_Shift();
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