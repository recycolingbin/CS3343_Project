package employeeFunction;

import java.io.*;
import java.util.*;
import baseFunction.BaseFunction;

public class EmployeeFunction extends BaseFunction {
    private static final String DUTY_REQUEST_FILE = "Data/Duty_Request.txt";
    private static final String LEAVE_REQUEST_FILE = "Data/Leave_Request.txt";
    private static final String STAFF_PROFILE_FILE = "Data/Staff_Profile.txt";

    public EmployeeFunction() {
        super(0, "", ""); // Default constructor with dummy values
    }

    public boolean login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STAFF_PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String staffName = parts[1].trim();
                    String role = parts[2].trim();
                    // CRITICAL SECURITY BUG: Password is not being validated!
                    // Currently any password works for any employee
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
            try {
                System.out.println("\n=============== Employee Menu ===============");
                System.out.println("1. View Shift Schedule");
                System.out.println("2. Request Duty");
                System.out.println("3. Request Leave");
                System.out.println("4. Logout");
                System.out.print("Please select an option (1-4): ");

                int action = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (action) {
                    case 1:
                        // Use inherited viewShiftSchedule from BaseFunction for today's date
                        String todayDate = java.time.LocalDate.now().toString();
                        viewShiftSchedule(todayDate);
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
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 4.");
                scanner.nextLine(); // clear invalid input
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // clear buffer
            }
        }
    }

    public void requestDuty(String userid) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            String date = getValidDateInput(scanner, "Please enter the date you want to request a duty (YYYY-MM-DD): ");
            if (date == null) return; // user cancelled or too many attempts

            System.out.print("Please enter the session (MORNING/AFTERNOON/NIGHT): ");
            String session = scanner.nextLine().trim().toUpperCase();

            if (!session.equals("MORNING") && !session.equals("AFTERNOON") && !session.equals("NIGHT")) {
                System.out.println("Invalid session! Valid sessions: MORNING, AFTERNOON, NIGHT");
                return;
            }
            
            try (Scanner fileScanner = new Scanner(DUTY_REQUEST_FILE)) {
    			boolean found = false;
    			while (fileScanner.hasNextLine()) {
    				String line = fileScanner.nextLine();
    				String[] info = line.split("\\,");
    				if (info[0].equals(userid)) {
    					if (info[1].equals(date) && info[2].equals(session)) {
    						found = true;
    						break;
    					}
    				}
    			}
    			if (found) {
    				System.out.println("You already have duty on " + date + " at section " + session + ". Cannot request another duty.");
    				return;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

            try (FileWriter writer = new FileWriter(DUTY_REQUEST_FILE, true)) {
                writer.write(userid + "," + date + "," + session + ",PENDING\n");
                System.out.println("Duty request submitted successfully.");
            } catch (IOException e) {
                System.err.println("Error writing duty request: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error processing duty request: " + e.getMessage());
        }
    }

    public void requestLeave(String userid) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            String startDate = getValidDateInput(scanner, "Please enter the start date for leave (YYYY-MM-DD): ");
            if (startDate == null) return; // user cancelled or too many attempts
            
            String endDate;
            
            // Validate that end date is not earlier than start date
            while (true) {
                endDate = getValidDateInput(scanner, "Please enter the end date for leave (YYYY-MM-DD): ");
                if (endDate == null) return; // user cancelled or too many attempts
                
                if (isDateAfterOrEqual(endDate, startDate)) {
                    break;
                } else {
                    System.out.println("Error: End date cannot be earlier than start date! Please try again.");
                }
            }
            
            try (Scanner fileScanner = new Scanner(DUTY_REQUEST_FILE)) {
    			boolean found = false;
    			while (fileScanner.hasNextLine()) {
    				String line = fileScanner.nextLine();
    				String[] info = line.split("\\,");
    				if (info[0].equals(userid)) {
    					if (info[1].compareTo(startDate) >= 0 && info[1].compareTo(endDate) <= 0) {
    						found = true;
    						break;
    					}
    				}
    			}
    			if (!found) {
    				System.out.println("You do not have a duty during " + startDate + " to " + endDate + ". Cannot request leave.");
    				return;
    			}

    		} catch (Exception e) {
    			e.printStackTrace();
    		}

            System.out.print("Please enter the reason for leave: ");
            String reason = scanner.nextLine().trim();
            
            if (reason.isEmpty()) {
                System.out.println("Reason cannot be empty!");
                return;
            }

            try (FileWriter writer = new FileWriter(LEAVE_REQUEST_FILE, true)) {
                writer.write(userid + "," + startDate + "," + endDate + "," + reason + ",PENDING\n");
                System.out.println("Leave request submitted successfully.");
            } catch (IOException e) {
                System.err.println("Error writing leave request: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error processing leave request: " + e.getMessage());
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
        int attempts = 0;
        final int MAX_ATTEMPTS = 10; // Prevent infinite loops
        
        while (attempts < MAX_ATTEMPTS) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                System.out.println("Input stream ended. Returning to menu.");
                return null;
            }
            
            date = scanner.nextLine().trim();
            if (isValidDate(date)) {
                return date;
            } else {
                System.out.println("Invalid date format! Please enter date in YYYY-MM-DD format (e.g., 2025-10-25)");
                attempts++;
            }
        }
        
        System.out.println("Too many invalid attempts. Returning to menu.");
        return null;
    }
}