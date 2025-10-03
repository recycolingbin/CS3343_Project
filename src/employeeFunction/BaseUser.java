package employeeFunction;

public class BaseUser {
    protected int userId;
    protected String username;
    protected String password;

    public BaseUser(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        // Simple login logic for demonstration
        return this.username.equals(username) && this.password.equals(password);
    }

    public void viewFunction() {
        System.out.println("Viewing available functions for user: " + username);
    }
}
