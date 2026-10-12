
package sweetcupcakeshop;



public class Cashier extends User {
    public Cashier(String username) {
        super(username, "CASHIER");
    }

    @Override
    public void showMenu() {
        System.out.println("Cashier menu for: " + username);
    }
}
