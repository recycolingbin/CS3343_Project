package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import staffRosteringSystem.MenuManager;
import staffRosteringSystem.RequestManager;
import staffRosteringSystem.ShiftManager;
import staffRosteringSystem.StaffManager;


public class MenuManagerTest {
    private MenuManager menuManager;
    private StaffManager staffManager;
    private RequestManager requestManager;
    private ShiftManager shiftManager;
    
	private static int uniqueStaffId(StaffManager sm) {
        List<staffRosteringSystem.StaffProfile> profiles = sm.loadStaffProfiles();
        Set<Integer> taken = new HashSet<>();
        for (staffRosteringSystem.StaffProfile p : profiles) taken.add(p.getStaffId());
        int id = 900000; // start high to avoid collisions with seeded data
        while (taken.contains(id)) id++;
        return id;
    }
    
    @BeforeEach
    void setUp() {
        staffManager = new StaffManager();
        requestManager = new RequestManager();
        shiftManager = new ShiftManager();
        
        // Provide exit input (option 6)
        String input = "6\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        menuManager = new MenuManager(staffManager, requestManager, shiftManager, scanner);
    }
    
    // ---- MenuManager Initialization Tests (1) ----
    
//    @Test
//    void testMenuManagerInitialization() {
//        assertNotNull(menuManager);
//    }
    
    // ---- Menu State Management Tests (3) ----
    
    @Test
    void testStaffManagerReference() {
        staffManager.addStaffProfile(94001, "John", "Employee");
        staffManager.addStaffProfile(94002, "Jane", "Manager");
        int count = staffManager.getStaffCount();
        assertTrue(count >= 2);
    }
    
    @Test
    void testRequestManagerReference() {
        assertNotNull(requestManager);
    }
    
    @Test
    void testShiftManagerReference() {
        assertNotNull(shiftManager);
    }
    
    // ---- Menu Integration Tests (3) ----
    
    @Test
    void testMenuSequentialOperations() {
        assertNotNull(staffManager);
        assertNotNull(requestManager);
        assertNotNull(shiftManager);
        assertNotNull(menuManager);
    }
    
    @Test
    void testManagerIndependence() {
        StaffManager manager1 = new StaffManager();
        StaffManager manager2 = new StaffManager();
        
        int id1 = uniqueStaffId(manager1);
        manager1.addStaffProfile(id1, "John", "Employee");
        int id2 = uniqueStaffId(manager2); // recompute after file updated
        boolean result2 = manager2.addStaffProfile(id2, "Jane", "Manager");
        
        assertTrue(result2);
    }
    
}

