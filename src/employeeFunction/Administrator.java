package employeeFunction;

public class Administrator extends BaseUser {
    public Administrator(int userId, String username, String password) {
        super(userId, username, password);
    }

    public void addStaffProfile(int staffId, String staffName, String role) {
        System.out.println("Staff profile added: " + staffId + ", " + staffName + ", " + role);
    }

    public void editStaffProfile(int staffId, String newDetails) {
        System.out.println("Staff profile " + staffId + " updated with " + newDetails);
    }

    public void viewStaffProfile(int staffId) {
        System.out.println("Viewing staff profile: " + staffId);
    }

    public void deleteStaffProfile(int staffId) {
        System.out.println("Staff profile " + staffId + " deleted");
    }
}
