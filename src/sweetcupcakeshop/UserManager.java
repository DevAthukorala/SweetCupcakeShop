
package sweetcupcakeshop;



import java.io.*;
import java.util.*;

public class UserManager {
    private final File file;

    public UserManager(String filePath) {
        this.file = new File(filePath);
        ensureFile();
    }

    private void ensureFile() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                    pw.println("username,password,role");
                    // create default manager account
                    pw.println("admin,admin123,MANAGER");
                }
            } catch (IOException e) {
                System.err.println("Error creating users file: " + e.getMessage());
            }
        }
    }

    public boolean createCashier(String username, String password) {
        Map<String, String[]> users = loadAll();
        if (users.containsKey(username)) return false;
        // append
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.println(username + "," + password + ",CASHIER");
            return true;
        } catch (IOException e) {
            System.err.println("Error writing users file: " + e.getMessage());
            return false;
        }
    }

    private Map<String, String[]> loadAll() {
        Map<String, String[]> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length < 3) continue;
                map.put(parts[0], parts);
            }
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return map;
    }

    public User login(String username, String password) {
        Map<String, String[]> users = loadAll();
        if (!users.containsKey(username)) return null;
        String[] parts = users.get(username);
        String storedPassword = parts[1];
        String role = parts[2];
        if (storedPassword.equals(password)) {
            if ("MANAGER".equalsIgnoreCase(role)) return new Manager(username);
            else return new Cashier(username);
        } else return null;
    }
}
