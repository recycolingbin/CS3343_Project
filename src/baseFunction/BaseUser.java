package baseFunction;

public class BaseUser {
    protected int userId;
    protected String username;
    protected String password;
    protected String userType;

    public BaseUser(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = "EMPLOYEE";
    }

    public BaseUser(int userId, String username, String password, String userType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void viewFunction() {
        System.out.println("Viewing available functions for user: " + username);
    }
    
    public void Login_page(String userId) {
        System.out.println("Login page for user: " + userId);
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public static boolean login(BaseUser user, String username, String password) {
        if (user != null) {
            return user.login(username, password);
        }
        return false;
    }
    
    public void viewRoster() {
        System.out.println("Viewing roster for user: " + username + " (ID: " + userId + ")");
    }
}