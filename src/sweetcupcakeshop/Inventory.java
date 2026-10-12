
package sweetcupcakeshop;



import java.io.*;
import java.util.*;

public class Inventory {
    private List<Cupcake> cupcakes = new ArrayList<>();
    private final File file;

    public Inventory(String filePath) {
        this.file = new File(filePath);
        loadFromFile();
    }

    private void loadFromFile() {
        cupcakes.clear();
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // write header
                try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                    pw.println("id,name,category,price");
                }
            } catch (IOException e) {
                System.err.println("Error creating cupcakes file: " + e.getMessage());
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                if (line.trim().isEmpty()) continue;
                // simple CSV split - handle escaped commas
                String[] parts = parseCSVLine(line, 4);
                if (parts.length < 4) continue;
                int id = Integer.parseInt(parts[0]);
                String name = parts[1].replace("\\,", ",");
                String category = parts[2].replace("\\,", ",");
                double price = Double.parseDouble(parts[3]);
                cupcakes.add(new Cupcake(id, name, category, price));
            }
        } catch (IOException e) {
            System.err.println("Error reading cupcakes file: " + e.getMessage());
        }
    }

    private String[] parseCSVLine(String line, int expected) {
        List<String> cols = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escape) {
                sb.append(c);
                escape = false;
            } else {
                if (c == '\\') {
                    escape = true;
                } else if (c == ',') {
                    cols.add(sb.toString());
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
        }
        cols.add(sb.toString());
        return cols.toArray(new String[0]);
    }

    public List<Cupcake> getAll() { return new ArrayList<>(cupcakes); }

    public void addCupcake(String name, String category, double price) {
        int nextId = cupcakes.stream().mapToInt(Cupcake::getId).max().orElse(0) + 1;
        Cupcake c = new Cupcake(nextId, name, category, price);
        cupcakes.add(c);
        saveToFile();
    }

    public List<Cupcake> searchByCategory(String categoryQuery) {
        List<Cupcake> results = new ArrayList<>();
        for (Cupcake c : cupcakes) {
            if (c.getCategory().equalsIgnoreCase(categoryQuery) ||
                c.getCategory().toLowerCase().contains(categoryQuery.toLowerCase()) ||
                c.getName().toLowerCase().contains(categoryQuery.toLowerCase())) {
                results.add(c);
            }
        }
        return results;
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("id,name,category,price");
            for (Cupcake c : cupcakes) {
                pw.println(c.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error saving cupcakes: " + e.getMessage());
        }
    }
}
