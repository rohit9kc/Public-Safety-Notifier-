import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Custom panel with gradient and diagonal line pattern
class PatternedPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient background (light gray to white)
        GradientPaint gradient = new GradientPaint(0, 0, new Color(220, 220, 220), getWidth(), getHeight(), Color.WHITE);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Diagonal lines pattern
        g2d.setColor(new Color(200, 200, 200, 100));
        for (int i = -getHeight(); i < getWidth(); i += 20) {
            g2d.drawLine(i, getHeight(), i + getHeight(), 0);
        }
    }
}

// Utility class for database connection management
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/psn_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "blueberry"; // Replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Main GUI class
public class PSNGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel statusLabel;
    private final ReportService reportService;
    private final UserService userService;
    private final Admin admin;
    private final Authority authority;

    public PSNGUI() {
        reportService = new ReportService();
        userService = new UserService();
        admin = new Admin("admin@psn.com", "admin123");
        authority = new Authority("auth@psn.com", "auth123");

        setTitle("Public Safety Notifier");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new PatternedPanel();
        mainPanel.setLayout(cardLayout);

        statusLabel = new JLabel("Welcome to Public Safety Notifier", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(createRolePanel(), "role");
        mainPanel.add(createAdminLoginPanel(), "adminLogin");
        mainPanel.add(createAuthorityLoginPanel(), "authLogin");
        mainPanel.add(createCitizenRegisterPanel(), "citizenRegister");

        add(mainPanel);
        cardLayout.show(mainPanel, "role");
    }

    private JPanel createRolePanel() {
        JPanel panel = new PatternedPanel();
        panel.setLayout(new GridBagLayout());
        ThemeUtil.applyTheme(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = ThemeUtil.createTitleLabel("Public Safety Notifier");
        JButton adminButton = ThemeUtil.createStyledButton("Admin");
        JButton authButton = ThemeUtil.createStyledButton("Authority");
        JButton citizenButton = ThemeUtil.createStyledButton("Citizen");
        JButton exitButton = ThemeUtil.createStyledButton("Exit");

        adminButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "adminLogin");
            statusLabel.setText("Admin Login");
        });
        authButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "authLogin");
            statusLabel.setText("Authority Login");
        });
        citizenButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "citizenRegister");
            statusLabel.setText("Citizen Register/Login");
        });
        exitButton.addActionListener(e -> System.exit(0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);
        gbc.gridy = 1;
        panel.add(adminButton, gbc);
        gbc.gridy = 2;
        panel.add(authButton, gbc);
        gbc.gridy = 3;
        panel.add(citizenButton, gbc);
        gbc.gridy = 4;
        panel.add(exitButton, gbc);

        return panel;
    }

    private JPanel createAdminLoginPanel() {
        JPanel panel = new PatternedPanel();
        panel.setLayout(new GridBagLayout());
        ThemeUtil.applyTheme(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Admin Email:");
        JTextField emailField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton loginButton = ThemeUtil.createStyledButton("Login");
        JButton backButton = ThemeUtil.createStyledButton("Back");

        loginButton.addActionListener(e -> {
            try {
                if (admin.login(emailField.getText(), new String(passField.getPassword()))) {
                    showAdminMenu();
                    statusLabel.setText("Logged in as Admin");
                }
            } catch (AuthenticationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Login failed");
            }
        });

        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "role");
            statusLabel.setText("Select Role");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        gbc.gridy = 3;
        panel.add(backButton, gbc);

        return panel;
    }

    private JPanel createAuthorityLoginPanel() {
        JPanel panel = new PatternedPanel();
        panel.setLayout(new GridBagLayout());
        ThemeUtil.applyTheme(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Authority Email:");
        JTextField emailField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton loginButton = ThemeUtil.createStyledButton("Login");
        JButton backButton = ThemeUtil.createStyledButton("Back");

        loginButton.addActionListener(e -> {
            try {
                if (authority.login(emailField.getText(), new String(passField.getPassword()))) {
                    showAuthorityMenu();
                    statusLabel.setText("Logged in as Authority");
                }
            } catch (AuthenticationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Login failed");
            }
        });

        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "role");
            statusLabel.setText("Select Role");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        gbc.gridy = 3;
        panel.add(backButton, gbc);

        return panel;
    }

    private JPanel createCitizenRegisterPanel() {
        JPanel panel = new PatternedPanel();
        panel.setLayout(new GridBagLayout());
        ThemeUtil.applyTheme(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton registerButton = ThemeUtil.createStyledButton("Register");
        JButton loginButton = ThemeUtil.createStyledButton("Login");
        JButton backButton = ThemeUtil.createStyledButton("Back");

        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Registration failed");
                return;
            }
            try {
                if (userService.getCitizens().stream().anyMatch(u -> u.getUsername().equals(username))) {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    statusLabel.setText("Registration failed");
                    return;
                }
                Citizen citizen = new Citizen(username, password);
                userService.addUser(citizen);
                showCitizenMenu(citizen);
                statusLabel.setText("Registered and logged in as " + username);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Registration failed");
            }
        });

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            try {
                Citizen citizen = (Citizen) userService.getCitizens().stream()
                        .filter(u -> u.getUsername().equals(username))
                        .findFirst()
                        .orElseThrow(() -> new AuthenticationException("User not found!"));
                citizen.login(username, password);
                showCitizenMenu(citizen);
                statusLabel.setText("Logged in as " + username);
            } catch (AuthenticationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Login failed");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Login failed");
            }
        });

        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "role");
            statusLabel.setText("Select Role");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(userField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(registerButton, gbc);
        gbc.gridx = 1;
        panel.add(loginButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(backButton, gbc);

        return panel;
    }

    private void showAdminMenu() {
        JPanel panel = new PatternedPanel();
        panel.setLayout(new BorderLayout(15, 15));
        ThemeUtil.applyTheme(panel);

        JButton viewButton = ThemeUtil.createStyledButton("View All Reports/Alerts");
        JButton approveButton = ThemeUtil.createStyledButton("Approve/Reject Reports");
        JButton manageButton = ThemeUtil.createStyledButton("Manage Users");
        JButton logoutButton = ThemeUtil.createStyledButton("Logout");

        viewButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "All Reports and Alerts", true);
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JPanel contentPanel = new PatternedPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel pendingPanel = new JPanel(new BorderLayout());
            pendingPanel.setBorder(BorderFactory.createTitledBorder("Pending Reports"));
            JTable pendingTable = new JTable(new DefaultTableModel(new String[]{"ID", "Description", "Timestamp"}, 0));
            DefaultTableModel pendingModel = (DefaultTableModel) pendingTable.getModel();
            try {
                for (IncidentReport report : reportService.getPendingReports()) {
                    pendingModel.addRow(new Object[]{report.getId(), report.getDescription(), report.getTimestamp()});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            pendingPanel.add(new JScrollPane(pendingTable), BorderLayout.CENTER);

            JPanel approvedPanel = new JPanel(new BorderLayout());
            approvedPanel.setBorder(BorderFactory.createTitledBorder("Approved Reports"));
            JTable approvedTable = new JTable(new DefaultTableModel(new String[]{"ID", "Description", "Timestamp"}, 0));
            DefaultTableModel approvedModel = (DefaultTableModel) approvedTable.getModel();
            try {
                for (IncidentReport report : reportService.getApprovedReports()) {
                    approvedModel.addRow(new Object[]{report.getId(), report.getDescription(), report.getTimestamp()});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            approvedPanel.add(new JScrollPane(approvedTable), BorderLayout.CENTER);

            JPanel alertsPanel = new JPanel(new BorderLayout());
            alertsPanel.setBorder(BorderFactory.createTitledBorder("Official Alerts"));
            JTable alertsTable = new JTable(new DefaultTableModel(new String[]{"ID", "Description", "Timestamp"}, 0));
            DefaultTableModel alertsModel = (DefaultTableModel) alertsTable.getModel();
            try {
                for (Alert alert : reportService.getAlerts()) {
                    alertsModel.addRow(new Object[]{alert.getId(), alert.getDescription(), alert.getTimestamp()});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            alertsPanel.add(new JScrollPane(alertsTable), BorderLayout.CENTER);

            contentPanel.add(pendingPanel);
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(approvedPanel);
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(alertsPanel);

            dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            JButton closeButton = ThemeUtil.createStyledButton("Close");
            closeButton.addActionListener(e2 -> dialog.dispose());
            dialog.add(closeButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            statusLabel.setText("Viewing all reports and alerts");
        });

        approveButton.addActionListener(e -> {
            List<IncidentReport> reports;
            try {
                reports = reportService.getPendingReports();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("No pending reports");
                return;
            }
            if (reports.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No pending reports.", "Info", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("No pending reports");
                return;
            }

            JDialog dialog = new JDialog(this, "Approve/Reject Reports", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JTable table = new JTable(new DefaultTableModel(new String[]{"ID", "Description", "Timestamp"}, 0));
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (IncidentReport report : reports) {
                model.addRow(new Object[]{report.getId(), report.getDescription(), report.getTimestamp()});
            }
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton approveButton2 = ThemeUtil.createStyledButton("Approve");
            JButton rejectButton = ThemeUtil.createStyledButton("Reject");
            JButton closeButton = ThemeUtil.createStyledButton("Close");

            approveButton2.addActionListener(e2 -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int reportId = (int) model.getValueAt(selectedRow, 0);
                    try {
                        reportService.approveReport(reportId);
                        model.removeRow(selectedRow);
                        statusLabel.setText("Report approved");
                    } catch (SQLException | ReportValidationException ex) {
                        JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("Action failed");
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Select a report.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            rejectButton.addActionListener(e2 -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int reportId = (int) model.getValueAt(selectedRow, 0);
                    try {
                        reportService.rejectReport(reportId);
                        model.removeRow(selectedRow);
                        statusLabel.setText("Report rejected");
                    } catch (SQLException | ReportValidationException ex) {
                        JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("Action failed");
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Select a report.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            closeButton.addActionListener(e2 -> dialog.dispose());

            buttonPanel.add(approveButton2);
            buttonPanel.add(rejectButton);
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        });

        manageButton.addActionListener(e -> {
            List<User> citizens;
            try {
                citizens = userService.getCitizens();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("No citizens to manage");
                return;
            }
            if (citizens.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No citizens registered.", "Info", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("No citizens to manage");
                return;
            }

            JDialog dialog = new JDialog(this, "Manage Users", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            JTable table = new JTable(new DefaultTableModel(new String[]{"Username", "Status"}, 0));
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (User user : citizens) {
                model.addRow(new Object[]{user.getUsername(), user.isBlocked() ? "Blocked" : "Unblocked"});
            }
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton blockButton = ThemeUtil.createStyledButton("Block");
            JButton unblockButton = ThemeUtil.createStyledButton("Unblock");
            JButton deleteButton = ThemeUtil.createStyledButton("Delete");
            JButton closeButton = ThemeUtil.createStyledButton("Close");

            blockButton.addActionListener(e2 -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String username = (String) model.getValueAt(selectedRow, 0);
                    try {
                        userService.blockUser(username);
                        model.setValueAt("Blocked", selectedRow, 1);
                        statusLabel.setText("User " + username + " blocked");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            unblockButton.addActionListener(e2 -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String username = (String) model.getValueAt(selectedRow, 0);
                    try {
                        userService.unblockUser(username);
                        model.setValueAt("Unblocked", selectedRow, 1);
                        statusLabel.setText("User " + username + " unblocked");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            deleteButton.addActionListener(e2 -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String username = (String) model.getValueAt(selectedRow, 0);
                    try {
                        userService.deleteUser(username);
                        model.removeRow(selectedRow);
                        statusLabel.setText("User " + username + " deleted");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            closeButton.addActionListener(e2 -> dialog.dispose());

            buttonPanel.add(blockButton);
            buttonPanel.add(unblockButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "role");
            statusLabel.setText("Logged out");
        });

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buttonPanel.add(viewButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(manageButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(panel, "adminMenu");
        cardLayout.show(mainPanel, "adminMenu");
    }

    private void showAuthorityMenu() {
        JPanel panel = new PatternedPanel();
        panel.setLayout(new BorderLayout(15, 15));
        ThemeUtil.applyTheme(panel);

        JButton postButton = ThemeUtil.createStyledButton("Post Alert");
        JButton viewButton = ThemeUtil.createStyledButton("View Alerts");
        JButton viewReportsButton = ThemeUtil.createStyledButton("View Approved Reports");
        JButton postReportAsAlertButton = ThemeUtil.createStyledButton("Post Report as Alert");
        JButton logoutButton = ThemeUtil.createStyledButton("Logout");

        postButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Post Alert", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel label = new JLabel("Alert Message:");
            JTextArea textArea = new JTextArea(5, 20);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JButton submitButton = ThemeUtil.createStyledButton("Submit");
            JButton cancelButton = ThemeUtil.createStyledButton("Cancel");

            submitButton.addActionListener(e2 -> {
                String alert = textArea.getText().trim();
                try {
                    reportService.addAlert(alert);
                    JOptionPane.showMessageDialog(dialog, "Alert posted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("Alert posted");
                    dialog.dispose();
                } catch (SQLException | ReportValidationException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    statusLabel.setText("Failed to post alert");
                }
            });

            cancelButton.addActionListener(e2 -> dialog.dispose());

            gbc.gridx = 0;
            gbc.gridy = 0;
            dialog.add(label, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            dialog.add(scrollPane, gbc);
            gbc.gridy = 2;
            dialog.add(submitButton, gbc);
            gbc.gridx = 1;
            dialog.add(cancelButton, gbc);

            dialog.setVisible(true);
        });

        viewButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "View Alerts", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JTable table = new JTable(new DefaultTableModel(new String[]{"ID", "Description", "Timestamp"}, 0));
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            try {
                for (Alert alert : reportService.getAlerts()) {
                    model.addRow(new Object[]{alert.getId(), alert.getDescription(), alert.getTimestamp()});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = ThemeUtil.createStyledButton("Close");
            closeButton.addActionListener(e2 -> dialog.dispose());
            dialog.add(closeButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            statusLabel.setText("Viewing alerts");
        });

        viewReportsButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "View Approved Reports", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JTable table = new JTable(new DefaultTableModel(new String[]{"ID", "Description", "Timestamp"}, 0));
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            try {
                for (IncidentReport report : reportService.getApprovedReports()) {
                    model.addRow(new Object[]{report.getId(), report.getDescription(), report.getTimestamp()});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = ThemeUtil.createStyledButton("Close");
            closeButton.addActionListener(e2 -> dialog.dispose());
            dialog.add(closeButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            statusLabel.setText("Viewing approved reports");
        });

        postReportAsAlertButton.addActionListener(e -> {
            List<IncidentReport> reports;
            try {
                reports = reportService.getApprovedReports();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("No approved reports");
                return;
            }
            if (reports.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No approved reports available.", "Info", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("No approved reports");
                return;
            }

            JDialog dialog = new JDialog(this, "Post Report as Alert", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JTable table = new JTable(new DefaultTableModel(new String[]{"ID", "Description", "Timestamp"}, 0));
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (IncidentReport report : reports) {
                model.addRow(new Object[]{report.getId(), report.getDescription(), report.getTimestamp()});
            }
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton postButton2 = ThemeUtil.createStyledButton("Post as Alert");
            JButton closeButton = ThemeUtil.createStyledButton("Close");

            postButton2.addActionListener(e2 -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String description = (String) model.getValueAt(selectedRow, 1);
                    try {
                        reportService.addAlert(description);
                        JOptionPane.showMessageDialog(dialog, "Report posted as alert!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        statusLabel.setText("Report posted as alert");
                        dialog.dispose();
                    } catch (SQLException | ReportValidationException ex) {
                        JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("Failed to post alert");
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Select a report.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            closeButton.addActionListener(e2 -> dialog.dispose());

            buttonPanel.add(postButton2);
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "role");
            statusLabel.setText("Logged out");
        });

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buttonPanel.add(postButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(viewReportsButton);
        buttonPanel.add(postReportAsAlertButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(panel, "authMenu");
        cardLayout.show(mainPanel, "authMenu");
    }

    private void showCitizenMenu(Citizen citizen) {
        JPanel panel = new PatternedPanel();
        panel.setLayout(new BorderLayout(15, 15));
        ThemeUtil.applyTheme(panel);

        JButton reportButton = ThemeUtil.createStyledButton("Report Incident");
        JButton viewButton = ThemeUtil.createStyledButton("View Alerts/Reports");
        JButton logoutButton = ThemeUtil.createStyledButton("Logout");

        reportButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Report Incident", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel label = new JLabel("Incident Description:");
            JTextArea textArea = new JTextArea(5, 20);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JButton submitButton = ThemeUtil.createStyledButton("Submit");
            JButton cancelButton = ThemeUtil.createStyledButton("Cancel");

            submitButton.addActionListener(e2 -> {
                String report = textArea.getText().trim();
                try {
                    reportService.addReport(report);
                    JOptionPane.showMessageDialog(dialog, "Report submitted for review.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("Report submitted");
                    dialog.dispose();
                } catch (SQLException | ReportValidationException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    statusLabel.setText("Failed to submit report");
                }
            });

            cancelButton.addActionListener(e2 -> dialog.dispose());

            gbc.gridx = 0;
            gbc.gridy = 0;
            dialog.add(label, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            dialog.add(scrollPane, gbc);
            gbc.gridy = 2;
            dialog.add(submitButton, gbc);
            gbc.gridx = 1;
            dialog.add(cancelButton, gbc);

            dialog.setVisible(true);
        });

        viewButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "View Alerts and Reports", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JTable table = new JTable(new DefaultTableModel(new String[]{"Type", "ID", "Description", "Timestamp"}, 0));
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            try {
                for (IncidentReport report : reportService.getApprovedReports()) {
                    model.addRow(new Object[]{"Report", report.getId(), report.getDescription(), report.getTimestamp()});
                }
                for (Alert alert : reportService.getAlerts()) {
                    model.addRow(new Object[]{"Alert", alert.getId(), alert.getDescription(), alert.getTimestamp()});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = ThemeUtil.createStyledButton("Close");
            closeButton.addActionListener(e2 -> dialog.dispose());
            dialog.add(closeButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            statusLabel.setText("Viewing alerts and approved reports");
        });

        logoutButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "role");
            statusLabel.setText("Logged out");
        });

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buttonPanel.add(reportButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(panel, "citMenu");
        cardLayout.show(mainPanel, "citMenu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PSNGUI().setVisible(true));
    }
}

class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}

class ReportValidationException extends Exception {
    public ReportValidationException(String message) {
        super(message);
    }
}

interface Reportable {
    int getId();
    String getDescription();
    String getStatus();
    LocalDateTime getTimestamp();
}

class IncidentReport implements Reportable {
    private int id;
    private String description;
    private String status;
    private LocalDateTime timestamp;

    public IncidentReport(int id, String description, String status, LocalDateTime timestamp) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

class Alert implements Reportable {
    private int id;
    private String description;
    private String status;
    private LocalDateTime timestamp;

    public Alert(int id, String description, String status, LocalDateTime timestamp) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

abstract class User {
    protected String username;
    protected String passwordHash;
    protected boolean isBlocked;

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.isBlocked = false;
    }

    private String hashPassword(String password) {
        return password; // Placeholder: Use BCrypt.hashpw(password, BCrypt.gensalt())
    }

    public abstract String getRole();

    public boolean login(String username, String password) throws AuthenticationException {
        if (isBlocked) {
            throw new AuthenticationException("User is blocked!");
        }
        if (!isValidCredentials(username, password)) {
            throw new AuthenticationException("Invalid credentials!");
        }
        return true;
    }

    private boolean isValidCredentials(String username, String password) {
        return this.username.equals(username) && this.passwordHash.equals(hashPassword(password));
    }

    public String getUsername() {
        return username;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }
}

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}

class Authority extends User {
    public Authority(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Authority";
    }
}

class Citizen extends User {
    public Citizen(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Citizen";
    }
}

class ReportService {
    public void addReport(String description) throws SQLException, ReportValidationException {
        if (description == null || description.trim().isEmpty()) {
            throw new ReportValidationException("Report description cannot be empty.");
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO pending_reports (description, status, timestamp) VALUES (?, ?, ?)")) {
            stmt.setString(1, description);
            stmt.setString(2, "Pending");
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }
    }

    public void addAlert(String description) throws SQLException, ReportValidationException {
        if (description == null || description.trim().isEmpty()) {
            throw new ReportValidationException("Alert description cannot be empty.");
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO alerts (description, status, timestamp) VALUES (?, ?, ?)")) {
            stmt.setString(1, description);
            stmt.setString(2, "Active");
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }
    }

    public void approveReport(int id) throws SQLException, ReportValidationException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Fetch the pending report
            PreparedStatement selectStmt = conn.prepareStatement(
                    "SELECT description, timestamp FROM pending_reports WHERE id = ? AND status = ?");
            selectStmt.setInt(1, id);
            selectStmt.setString(2, "Pending");
            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) {
                throw new ReportValidationException("Invalid report ID or report not pending.");
            }
            String description = rs.getString("description");
            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

            // Insert into reports table
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO reports (description, status, timestamp) VALUES (?, ?, ?)");
            insertStmt.setString(1, description);
            insertStmt.setString(2, "Approved");
            insertStmt.setTimestamp(3, Timestamp.valueOf(timestamp));
            insertStmt.executeUpdate();

            // Delete from pending_reports
            PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM pending_reports WHERE id = ?");
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
        }
    }

    public void rejectReport(int id) throws SQLException, ReportValidationException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM pending_reports WHERE id = ? AND status = ?")) {
            stmt.setInt(1, id);
            stmt.setString(2, "Pending");
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ReportValidationException("Invalid report ID or report not pending.");
            }
        }
    }

    public List<IncidentReport> getPendingReports() throws SQLException {
        List<IncidentReport> reports = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, description, status, timestamp FROM pending_reports WHERE status = ?")) {
            stmt.setString(1, "Pending");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reports.add(new IncidentReport(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return reports;
    }

    public List<IncidentReport> getApprovedReports() throws SQLException {
        List<IncidentReport> reports = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, description, status, timestamp FROM reports WHERE status = ?")) {
            stmt.setString(1, "Approved");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reports.add(new IncidentReport(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return reports;
    }

    public List<Alert> getAlerts() throws SQLException {
        List<Alert> alerts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, description, status, timestamp FROM alerts")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                alerts.add(new Alert(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return alerts;
    }
}

class UserService {
    public void addUser(User user) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password_hash, role, is_blocked) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user instanceof Citizen ? user.passwordHash : user.getUsername());
            stmt.setString(3, user.getRole());
            stmt.setBoolean(4, user.isBlocked());
            stmt.executeUpdate();
        }
    }

    public void blockUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET is_blocked = ? WHERE username = ?")) {
            stmt.setBoolean(1, true);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void unblockUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET is_blocked = ? WHERE username = ?")) {
            stmt.setBoolean(1, false);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void deleteUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }

    public List<User> getCitizens() throws SQLException {
        List<User> citizens = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT username, password_hash, role, is_blocked FROM users WHERE role = ?")) {
            stmt.setString(1, "Citizen");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Citizen citizen = new Citizen(rs.getString("username"), rs.getString("password_hash"));
                citizen.setBlocked(rs.getBoolean("is_blocked"));
                citizens.add(citizen);
            }
        }
        return citizens;
    }
}

class ThemeUtil {
    public static void applyTheme(JPanel panel) {
        panel.setOpaque(false);
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0, 153, 204));
                } else {
                    g2.setColor(new Color(0, 204, 255));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(text, x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(new Color(0, 204, 255));
        return label;
    }
}