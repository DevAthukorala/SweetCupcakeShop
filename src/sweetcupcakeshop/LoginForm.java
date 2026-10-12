
package sweetcupcakeshop;


import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblMessage;
    private UserManager userManager = new UserManager("data/users.csv");

    public LoginForm() {
        setTitle("Sweet Cupcake Shop - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        initComponents();
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(240, 200, 255));   // soft lavender
        btn.setForeground(new Color(230, 80, 160));    // cupcake pink
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 240, 245));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Sweet Cupcake Shop Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(102, 0, 102));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(lblTitle, c);

        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("Username:"), c);
        txtUsername = new JTextField();
        c.gridx = 1;
        panel.add(txtUsername, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Password:"), c);
        txtPassword = new JPasswordField();
        c.gridx = 1;
        panel.add(txtPassword, c);

        c.gridx = 0; c.gridy++;
        JButton btnLogin = new JButton("Login");
        styleButton(btnLogin);
        btnLogin.addActionListener(e -> loginAction());
        c.gridwidth = 2;
        panel.add(btnLogin, c);

        c.gridy++;
        lblMessage = new JLabel("", SwingConstants.CENTER);
        lblMessage.setForeground(Color.RED);
        panel.add(lblMessage, c);

        add(panel);
    }

    private void loginAction() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        User user = userManager.login(username, password);
        if (user == null) {
            lblMessage.setText("Invalid credentials!");
            return;
        }

        lblMessage.setText("");
        if (user.getRole().equalsIgnoreCase("MANAGER")) {
            new ManagerDashboard(user).setVisible(true);
        } else {
            new CashierDashboard(user).setVisible(true);
        }
        this.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
