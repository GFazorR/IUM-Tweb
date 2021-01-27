package Model;

public class User {
    private int id;
    private String username;
    private boolean isAdmin;
    private String token;

    public User(int id, String username, boolean isAdmin, String token) {
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
        this.token = token;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public boolean isAdmin() { return isAdmin; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
