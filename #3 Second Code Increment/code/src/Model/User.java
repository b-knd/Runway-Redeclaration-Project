package Model;

public class User {
    private String username;
    private String password;
    private int role;
    //super admin, normal atc, manager


    public User(String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAccessControl() {
        return role;
    }
}
