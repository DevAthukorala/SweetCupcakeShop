
package sweetcupcakeshop;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManagerDashboard extends JFrame {
    private final User manager;
    private final Inventory inventory = new Inventory("data/cupcakes.csv");
    private final UserManager userManager = new UserManager("data/users.csv");

    private JTable tblView, tblSearch;
    private JTextField txtName, txtPrice, txtSearch, txtNewUser, txtNewPass;
    private JComboBox<String> cmbCategory;

    public ManagerDashboard(User manager) {
        this.manager = manager;
        setTitle("Manager Dashboard - Sweet Cupcake Shop");
        setSize(780, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        initComponents();
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(240, 200, 255));
        btn.setForeground(new Color(230, 80, 160));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(250, 30));
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return lbl;
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 153, 255));
        JLabel lblWelcome = new JLabel("  Welcome, " + manager.getUsername() + " (" + manager.getRole() + ")");
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(lblWelcome, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // View
        JPanel viewPanel = new JPanel(new BorderLayout());
        tblView = new JTable(new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Price"}, 0));
        styleTable(tblView);
        viewPanel.add(new JScrollPane(tblView), BorderLayout.CENTER);
        JButton btnRefresh = new JButton("Refresh");
        styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadCupcakes(tblView, inventory.getAll()));
        viewPanel.add(btnRefresh, BorderLayout.SOUTH);
        tabs.add("View Cupcakes", viewPanel);

        // Add cupcake
        JPanel addPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        addPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        txtName = new JTextField(); styleField(txtName);
        txtPrice = new JTextField(); styleField(txtPrice);
        cmbCategory = new JComboBox<>(new String[]{"Classic","Seasonal","Custom Order","Gluten-Free","Special Collection"});
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        addPanel.add(makeLabel("Name:")); addPanel.add(txtName);
        addPanel.add(makeLabel("Category:")); addPanel.add(cmbCategory);
        addPanel.add(makeLabel("Price:")); addPanel.add(txtPrice);
        JButton btnAdd = new JButton("Add Cupcake");
        styleButton(btnAdd);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.addActionListener(e -> addCupcake());
        addPanel.add(new JLabel()); addPanel.add(btnAdd);
        tabs.add("Add Cupcake", addPanel);

        // Search
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        txtSearch = new JTextField(20);
        styleField(txtSearch);
        addPlaceholder(txtSearch, "Enter cupcake name or category...");
        JButton btnSearch = new JButton("Search");
        styleButton(btnSearch);
        btnSearch.addActionListener(e -> searchCupcake());
        top.add(makeLabel("Search:")); top.add(txtSearch); top.add(btnSearch);
        searchPanel.add(top, BorderLayout.NORTH);
        tblSearch = new JTable(new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Price"}, 0));
        styleTable(tblSearch);
        searchPanel.add(new JScrollPane(tblSearch), BorderLayout.CENTER);
        tabs.add("Search Cupcakes", searchPanel);

        // Create cashier
        JPanel cashierPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        cashierPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        txtNewUser = new JTextField(); styleField(txtNewUser);
        txtNewPass = new JTextField(); styleField(txtNewPass);
        JButton btnCreate = new JButton("Create Cashier");
        styleButton(btnCreate);
        btnCreate.addActionListener(e -> createCashier());
        cashierPanel.add(makeLabel("New Username:")); cashierPanel.add(txtNewUser);
        cashierPanel.add(makeLabel("Password:")); cashierPanel.add(txtNewPass);
        cashierPanel.add(new JLabel()); cashierPanel.add(btnCreate);
        tabs.add("Create Cashier", cashierPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private void styleTable(JTable table) {
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void addPlaceholder(JTextField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void loadCupcakes(JTable table, List<Cupcake> data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Cupcake c : data)
            model.addRow(new Object[]{c.getId(), c.getName(), c.getCategory(), c.getPrice()});
    }

    private void addCupcake() {
        try {
            String name = txtName.getText().trim();
            String category = (String) cmbCategory.getSelectedItem();
            double price = Double.parseDouble(txtPrice.getText().trim());
            inventory.addCupcake(name, category, price);
            JOptionPane.showMessageDialog(this, "Cupcake added successfully!");
            txtName.setText(""); txtPrice.setText("");
            loadCupcakes(tblView, inventory.getAll());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchCupcake() {
        String q = txtSearch.getText().trim();
        List<Cupcake> res = inventory.searchByCategory(q);
        loadCupcakes(tblSearch, res);
    }

    private void createCashier() {
        String u = txtNewUser.getText().trim();
        String p = txtNewPass.getText().trim();
        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
            return;
        }
        boolean ok = userManager.createCashier(u, p);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cashier created!");
            txtNewUser.setText(""); txtNewPass.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!");
        }
    }
}
