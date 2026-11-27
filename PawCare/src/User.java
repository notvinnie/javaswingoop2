public class User {
    public final String username;
    public String password;
    public final Role role;
    public String fullName;
    public String phone = "";

    public User(String username, String password, Role role, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }
}