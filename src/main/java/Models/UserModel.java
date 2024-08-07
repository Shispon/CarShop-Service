package Models;

public class UserModel {
    private Integer addId = -1;
    private String id;
    private String username;
    private String password;
    private String role;

    public UserModel( String username, String password, String role) {
        this.addId = addId + 1;
        this.id = addId.toString();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserModel () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
