package Model;

public class User {
    private String username;
    private String password;
    private int accessControl;
    //super admin, normal atc, manager


    public User(String username, String password, int accessControl) {
        this.username = username;
        this.password = password;
        this.accessControl = accessControl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAccessControl() {
        return accessControl;
    }
}
