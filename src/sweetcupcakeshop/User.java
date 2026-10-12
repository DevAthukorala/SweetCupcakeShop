
package sweetcupcakeshop;



public abstract class User {
    protected String username;
    protected String role; // "MANAGER" or "CASHIER"

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }

    // abstract method demonstrating polymorphism
    public abstract void showMenu();
}
