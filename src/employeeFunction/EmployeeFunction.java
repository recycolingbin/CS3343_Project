package employeeFunction;

import java.io.*;
import java.util.*;
import baseFunction.BaseFunction;

<<<<<<< Updated upstream
import baseFunction.BaseFunction;
=======
public class EmployeeFunction extends BaseFunction {
    private static final String DUTY_REQUEST_FILE = "Data/Duty_Request.txt";
    private static final String LEAVE_REQUEST_FILE = "Data/Leave_Request.txt";
    private static final String SHIFT_FILE = "Data/Shift.txt";
    private static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";
>>>>>>> Stashed changes

    public EmployeeFunction() {
        super(0, "", ""); // Default constructor with dummy values
    }

<<<<<<< Updated upstream
public class EmployeeFunction extends BaseFunction {
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
=======
    public boolean login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String staffName = parts[1].trim();
                    String role = parts[2].trim();
                    if (staffName.equals(username) && role.equals("Employee")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading staff profiles: " + e.getMessage());
        }
        return false;
    }

    public void Login_page(String userid) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=============== Employee Menu ===============");
            System.out.println("1. View Shift Schedule");
            System.out.println("2. Request Duty");
            System.out.println("3. Request Leave");
            System.out.println("4. Logout");
            System.out.print("Please select an option (1-4): ");
>>>>>>> Stashed changes

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    viewShiftSchedule();
                    break;
                case 2:
                    requestDuty(userid);
                    break;
                case 3:
                    requestLeave(userid);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void requestDuty(String userid) {
        Scanner scanner = new Scanner(System.in);
        String date = getValidDateInput(scanner, "Please enter the date you want to request a duty (YYYY-MM-DD): ");

        System.out.println("Please enter the session (MORNING/AFTERNOON/NIGHT):");
        String session = scanner.nextLine().trim().toUpperCase();

        if (!session.equals("MORNING") && !session.equals("AFTERNOON") && !session.equals("NIGHT")) {
            System.out.println("Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
            return;
        }

        try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE, true)) {
            writer.write(userid + "," + date + "," + session + ",PENDING\n");
            System.out.println("Duty request submitted successfully.");
        } catch (IOException e) {
            System.err.println("Error writing duty request: " + e.getMessage());
        }
    }

    public void requestLeave(String userid) {
        Scanner scanner = new Scanner(System.in);
        String startDate = getValidDateInput(scanner, "Please enter the start date for leave (YYYY-MM-DD): ");
        String endDate;
        
        // Validate that end date is not earlier than start date
        while (true) {
            endDate = getValidDateInput(scanner, "Please enter the end date for leave (YYYY-MM-DD): ");
            if (isDateAfterOrEqual(endDate, startDate)) {
                break;
            } else {
                System.out.println("Error: End date cannot be earlier than start date! Please try again.");
            }
        }

        System.out.println("Please enter the reason for leave:");
        String reason = scanner.nextLine().trim();

        try (FileWriter writer = new FileWriter(LEAVE_REQUEST_FILE, true)) {
            writer.write(userid + "," + startDate + "," + endDate + "," + reason + ",PENDING\n");
            System.out.println("Leave request submitted successfully.");
        } catch (IOException e) {
            System.err.println("Error writing leave request: " + e.getMessage());
        }
    }

<<<<<<< Updated upstream
		try (FileWriter writer = new FileWriter("Leave_Request.txt", true)) {
			int lines = getfilelinesnum("Leave_Request.txt") + 1000;
			writer.write(lines + "|" + userid + "|" + sday + "|" + eday + "|" + reason + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		scanner.close();
	}

	public void Login_page(String userid, String name, String pw) {
		BaseFunction base = new BaseFunction(useid, name, pw);
		while (true) {
			base.viewFunction();
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
=======
    private void viewShiftSchedule() {
        System.out.println("\n=============== Shift Schedule ===============");
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFT_FILE))) {
            String line;
            boolean hasShifts = false;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 8) {
                        System.out.println("Shift ID: " + parts[0] + ", Staff ID: " + parts[1] + ", Date: " + parts[2] + 
                                         ", Session: " + parts[3] + ", Notes: " + parts[7]);
                        hasShifts = true;
                    }
                }
            }
            if (!hasShifts) {
                System.out.println("No shifts scheduled.");
            }
        } catch (IOException e) {
            System.err.println("Error reading shift schedule: " + e.getMessage());
        }
    }
    
    // ================== DATE VALIDATION ==================
    
    public boolean isValidDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }
        
        try {
            String[] parts = dateString.split("-");
            if (parts.length != 3) {
                return false;
            }
            
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            
            // Basic validation
            if (year < 2020 || year > 2030) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            
            // Days in month validation
            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            
            // Check for leap year
            if (month == 2 && isLeapYear(year)) {
                daysInMonth[1] = 29;
            }
            
            if (day > daysInMonth[month - 1]) {
                return false;
            }
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    // Helper method to check if date1 is after or equal to date2
    private boolean isDateAfterOrEqual(String date1, String date2) {
        try {
            String[] parts1 = date1.split("-");
            String[] parts2 = date2.split("-");
            
            int year1 = Integer.parseInt(parts1[0]);
            int month1 = Integer.parseInt(parts1[1]);
            int day1 = Integer.parseInt(parts1[2]);
            
            int year2 = Integer.parseInt(parts2[0]);
            int month2 = Integer.parseInt(parts2[1]);
            int day2 = Integer.parseInt(parts2[2]);
            
            // Compare year first
            if (year1 > year2) return true;
            if (year1 < year2) return false;
            
            // Same year, compare month
            if (month1 > month2) return true;
            if (month1 < month2) return false;
            
            // Same year and month, compare day
            return day1 >= day2;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public String getValidDateInput(Scanner scanner, String prompt) {
        String date;
        while (true) {
            System.out.print(prompt);
            date = scanner.nextLine().trim();
            if (isValidDate(date)) {
                return date;
            } else {
                System.out.println("Invalid date format! Please enter date in YYYY-MM-DD format (e.g., 2025-10-25)");
            }
        }
    }
}
>>>>>>> Stashed changes
