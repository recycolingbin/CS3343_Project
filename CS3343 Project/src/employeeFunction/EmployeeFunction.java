package employeeFunction;

import java.util.Scanner;

public class EmployeeFunction extends BaseFunction{
	private EmployeeFunction instance;
	
	public EmployeeFunction getInstance() {
		if (instance == null) {
			instance = new EmployeeFunction();
		}
		return instance;
	}
	
	public void Request_duty() {
		System.out.println("Please enter the day you want to request a duty:");
		Scanner scanner = new Scanner(System.in);
		String day = scanner.nextLine();
		
		System.out.println("Please enter the section you want to request on " + day + ":");
		
		scanner.close();
	}
	
	public void Request_leave() {
		System.out.println("Please enter the day you want to request a leave:");
		Scanner scanner = new Scanner(System.in);
		String day = scanner.nextLine();

		scanner.close();
	}
	
	public void Login_page() {
		while (true) {
			super.View_Shift();
			System.out.println("Please input the action you want to do: \n 1.Request Duty \n 2.Request Leave \n 3. Logout");
			Scanner scanner = new Scanner(System.in);
			int action = scanner.nextInt();
			switch (action) {
			case 1:
				Request_duty();
				break;
			case 2:
				Request_leave();
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
