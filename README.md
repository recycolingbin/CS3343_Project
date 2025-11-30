# CS3343 Roster Managing system

# Description
The roster management system is a software design for small-medium retail to have a simple, affordable and command-line based management system. The program deliver 3 kinds of functions, which is staff management, shift management and request management.

# Installation and Execution
Windows:
1, Download the .jar file
2. Navigate to the destination of downloaded .jar file in command prompt, using the following command:
     cd <file path>
3. Execute the program using the following command line:
    java -jar StaffRosteringSystem.jar

MacOS:
1, Download the .jar file
2. Navigate to the destination of downloaded .jar file in Terminal, using the following command:
     cd <file path>
3. Execute the program using the following command line:
    java -jar StaffRosteringSystem.jar

# Instruction
The rostering system requires the user to log in before entering, so that the system can ensure the identity of user. Since all datafile will be initialized, please use the following accounts to log in as staff or administrator. Note that both username and password are case sensitive.

- Staff
Username: John Doe
Password: Abcd1234

- Admin
Username: admin
Password: pw1234
(Choose the corresponding identity in main menu before entering username and password. 1 for Employee and 2 for admin.)

# Detail User Guideline
[Employee]

1. View Shift Schedule (Option 1)
- Shows all shifts of the employee for today.

2. Request Duty (Option 2)
- Apply for duty
- Fill in the date and session wanted, then the request will be added to Duty_Request.txt
- Only success if the employee doesn't have duty on that time

3. Request Leave (Option 3)
- Apply for leave
- Fill in the start date, end date, type of leave and reason, then the request will be added to Leave_Request.txt 
- Only success if there is shift during the period


[Administrator]

1. Staff Management (Admin → Option 1)

1.1  Add Staff Profile (Option 1)
- Add new staff profile into data file
- Fill in the staff ID, name and role, then the request will be added to Staff_Profile.txt 
- Only success if the added staff doesn't exist in the file

1.2  Edit Staff Profile (Option 2)
-Edit staff profile in data file
- Enter the staff ID of target staff and the field wanted to change, then the new data will be edit and save to Staff_Profile.txt 
- Only success if the target staff exists in the file

1.3  Delete Staff Profile (Option 3)
- Delete staff profile in data file
- Enter the staff ID, then the profile will be delete from Staff_Profile.txt 
- Only success if the target staff exists in the file

1.4  View Staff Profile (Option 4)
- View the target staff profile in data file.txt
- Enter the staff ID, then the profile will be displayed
- Only success if the target staff exists in the file

1.5  View All Staff Profile (Option 5)
- View all staff profile in Staff_Profile.txt

===

2. Shift Management (Admin → Option 2)

2.1  View Available Sessions (Option 1)
- Display the 3 session choices and their time period: Morning, Afternoon, Night 

2.2  View All Shifts (Option 2)
- View all shifts of all staff in Shift.txt

2.3  Assign Shift (Option 3)
- Assign shift to staff
- Enter the staff ID, date, session and notes(optional), then the shift will be added to Staff_Profile.txt 
- Only success if the target staff exists in the file and duplication won't happened

2.4  Delete Shift (Option 4)
- Delete the shift of staff
- Enter the staff ID, then the profile will be deleted from Staff_Profile.txt
- Only success if the target staff exists in the file



===

3. Duty Request Management (Admin → Option 3)

3.1  View Duty Requests (Option 1)
- Display all pending request from Duty_Request.txt

3.2  Approve Duty Request (Option 2)
- Approve a pending request
- Enter the case number, then the approved shift will be added to Shift.txt and it will be removed from the data file

3.3  Reject Duty Request (Option 3)
- Reject a pending request
- Enter the case number, then the rejected shift will be removed from the data file

===

4. Leave Request Management (Admin → Option 3)

4.1  View Leave Requests (Option 1)
- Display all pending request from Duty_Request.txt

4.2  Approve Leave Request (Option 2)
- Approve a pending request
- Enter the case number, then the approved request will be removed from the data file

4.3  Reject Leave Request (Option 3)
- Reject a pending request
- Enter the case number, then the rejected request will be removed from the data file
