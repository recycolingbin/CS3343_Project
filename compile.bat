@echo off
echo ===============================================
echo   Compiling Roster Management System
echo ===============================================
echo.
cd /d "%~dp0"
javac -d bin src/main/Main.java src/administrator/Administrator.java src/employeeFunction/EmployeeFunction.java src/staffProfile/StaffProfile.java src/baseFunction/BaseFunction.java src/baseFunction/Shift.java
if %errorlevel% == 0 (
    echo.
    echo ✅ Compilation successful!
    echo Class files are in the 'bin' directory
) else (
    echo.
    echo ❌ Compilation failed!
)
pause
