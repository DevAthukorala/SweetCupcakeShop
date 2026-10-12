
package sweetcupcakeshop;



public class Manager extends User {
    public Manager(String username) {
        super(username, "MANAGER");
    }

    @Override
    public void showMenu() {
        System.out.println("Manager menu for: " + username);
    }
}
