import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class LoginFrame extends JFrame {

    private static final Font MAIN_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font GROUP_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Color BUTTON_COLOR = new Color(100, 149, 237); // Cornflower Blue
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color INPUT_COLOR = new Color(255, 255, 255); // White

    private void createUserLoginPanel() {
        // New frame for login to match the style of RegisterFrame
        JFrame loginFrame = new JFrame("User Login");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setBackground(BACKGROUND_COLOR);

        // Set the login window to maximize
        loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(MAIN_FONT);
        titlePanel.add(titleLabel);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(INPUT_FONT);
        JTextField userTextField = new JTextField(20);
        userTextField.setFont(INPUT_FONT);
        userTextField.setBackground(INPUT_COLOR);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(INPUT_FONT);
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(INPUT_FONT);
        passField.setBackground(INPUT_COLOR);

        // Adding components to inputPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(userTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(passField, gbc);

        // Login Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.setFont(INPUT_FONT);
        loginButton.setBackground(BUTTON_COLOR); // Cornflower Blue
        loginButton.setForeground(BUTTON_TEXT_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        buttonPanel.add(loginButton);

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement your login logic here
                String username = userTextField.getText();
                String password = new String(passField.getPassword());
                if (authenticate(username, password)) {
                    // Login successful
                    SwingUtilities.invokeLater(() -> {
                        loginFrame.dispose(); // Close the login window
                        WelcomeFrame welcomeFrame = new WelcomeFrame(); // Create the welcome window
                        welcomeFrame.setVisible(true); // Show the welcome window
                    });
                } else {
                    // Login failed
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password", "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add panels to frame
        loginFrame.add(titlePanel, BorderLayout.NORTH);
        loginFrame.add(inputPanel, BorderLayout.CENTER);
        loginFrame.add(buttonPanel, BorderLayout.SOUTH);
        loginFrame.setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        // Here we should actually hash the password and compare against hashed password
        // in the database
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensf480", "root", "password");
            String sql = "SELECT * FROM users WHERE Name = ? AND PasswordHash = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password); // In real app, you should hash the password before comparing

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true; // User found with matching username and password
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + sqlException.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return false; // User not found, or db error occurred
    }

    private void createAdminLoginPanel() {
        // New frame for login to match the style of RegisterFrame
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setBackground(BACKGROUND_COLOR);

        // Set the login window to maximize
        loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(MAIN_FONT);
        titlePanel.add(titleLabel);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(INPUT_FONT);
        JTextField userTextField = new JTextField(20);
        userTextField.setFont(INPUT_FONT);
        userTextField.setBackground(INPUT_COLOR);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(INPUT_FONT);
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(INPUT_FONT);
        passField.setBackground(INPUT_COLOR);

        // Adding components to inputPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(userTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(passField, gbc);

        // Login Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.setFont(INPUT_FONT);
        loginButton.setBackground(BUTTON_COLOR); // Cornflower Blue
        loginButton.setForeground(BUTTON_TEXT_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        buttonPanel.add(loginButton);

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement your login logic here
                String username = userTextField.getText();
                String password = new String(passField.getPassword());
                if (admin(username, password)) {
                    // Login successful
                    SwingUtilities.invokeLater(() -> {
                        loginFrame.dispose(); // Close the login window
                        adminFrame adminFrame = new adminFrame(); // Create the welcome window
                        adminFrame.setVisible(true); // Show the welcome window
                    });
                } else {
                    // Login failed
                    JOptionPane.showMessageDialog(null, "You do not have admin privileges.", "Access Denied",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add panels to frame
        loginFrame.add(titlePanel, BorderLayout.NORTH);
        loginFrame.add(inputPanel, BorderLayout.CENTER);
        loginFrame.add(buttonPanel, BorderLayout.SOUTH);
        loginFrame.setVisible(true);
    }

    private boolean admin(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensf480", "root", "password");
            String sql = "SELECT * FROM users WHERE Name = ? AND PasswordHash = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password); // In a real app, you should hash the password before comparing

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Check if the user has the role of an admin
                String userType = resultSet.getString("UserType");
                if ("admin".equals(userType)) {
                    return true; // Admin found with matching username and password
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + sqlException.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return false; // User not found, or db error occurred
    }

    public class adminFrame extends JFrame {
        public adminFrame() {
            setTitle("Admin Page");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            setExtendedState(JFrame.MAXIMIZED_BOTH);

            JLabel welcomeLabel = new JLabel("Welcome to our Flight Reservation System", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(welcomeLabel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setBackground(BACKGROUND_COLOR);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.NONE;

            JButton browseFlightButton = createStyledButton("Browse Flights");
            gbc.insets = new Insets(10, 0, 10, 0);
            buttonPanel.add(browseFlightButton, gbc);

            browseFlightButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    browseFlightPanel();
                }
            });

            JButton crewListButton = createStyledButton("Browse Crew List for a Flight");
            gbc.insets = new Insets(10, 0, 10, 0);
            buttonPanel.add(crewListButton, gbc);

            crewListButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    crewListPanel();
                }
            });

            JButton aircraftListButton = createStyledButton("List All Aircrafts");
            gbc.insets = new Insets(10, 0, 10, 0);
            buttonPanel.add(aircraftListButton, gbc);

            aircraftListButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        DBMS dbms = DBMS.getDBMS(); // This will not create a new instance but will return the existing
                                                    // one.
                        ArrayList<Aircraft> aircrafts = dbms.getAircrafts();

                        AircraftInfoFrame AircraftInfo = new AircraftInfoFrame(aircrafts);
                        AircraftInfo.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error fetching flight data: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton updateCrewButton = createStyledButton("Update crew for a flight: ");
            gbc.insets = new Insets(10, 0, 10, 0);
            buttonPanel.add(updateCrewButton, gbc);

            updateCrewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateCrewPanel();
                }
            });

            JButton addAircraftButton = createStyledButton("Add an Aircraft: ");
            gbc.insets = new Insets(10, 0, 10, 0);
            buttonPanel.add(addAircraftButton, gbc);

            addAircraftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addAircraftPanel();
                }
            });

            JButton removeAircraftButton = createStyledButton("Remove an Aircraft: ");
            gbc.insets = new Insets(10, 0, 10, 0);
            buttonPanel.add(removeAircraftButton, gbc);

            removeAircraftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeAircraftPanel();
                }
            });

            add(buttonPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        private JButton createStyledButton(String text) {
            JButton button = new JButton(text);
            button.setFont(MAIN_FONT);
            button.setBackground(BUTTON_COLOR);
            button.setForeground(BUTTON_TEXT_COLOR);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            return button;
        }

        private void removeAircraftPanel() {
            JFrame browseFrame = new JFrame("Remove Aircraft");
            browseFrame.setSize(400, 300);
            browseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            browseFrame.setLayout(new GridLayout(5, 2, 10, 10));

            JLabel fromLabel = new JLabel("Enter a aircraftID: ");
            JTextField aircraftTextField = new JTextField();

            browseFrame.add(fromLabel);
            browseFrame.add(aircraftTextField);

            JButton confirmButton = new JButton("Remove Aircraft");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        int aircraftID = Integer.parseInt(aircraftTextField.getText());

                        DBMS dbms = DBMS.getDBMS(); // This will not create a new instance but will return the existing
                                                    // one.
                        dbms.removeAircraft(aircraftID);
                        ArrayList<Aircraft> aircrafts = dbms.getAircrafts();
                        AircraftInfoFrame aircraftInfo = new AircraftInfoFrame(aircrafts);
                        aircraftInfo.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(browseFrame, "Error fetching flight data: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            browseFrame.add(new JLabel());
            browseFrame.add(confirmButton);

            browseFrame.setVisible(true);
        }

        private void addAircraftPanel() {
            JFrame browseFrame = new JFrame("Add Aircraft");
            browseFrame.setSize(400, 300);
            browseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Use GridBagLayout for center alignment
            browseFrame.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

            JTextField modelTextField = new JTextField(20);
            JTextField ecoTextField = new JTextField(20);
            JTextField comfortTextField = new JTextField(20);
            JTextField businessTextField = new JTextField(20);
            JTextField ecoPriceTextField = new JTextField(20);
            JTextField businessPriceTextField = new JTextField(20);
            browseFrame.add(createLabelAndTextField("Enter Aircraft model: ", modelTextField), gbc);
            gbc.gridy++;
            browseFrame.add(createLabelAndTextField("Enter Number of Economy Seats: ", ecoTextField), gbc);
            gbc.gridy++;
            browseFrame.add(createLabelAndTextField("Enter Number of Comfort Seats: ", comfortTextField), gbc);
            gbc.gridy++;
            browseFrame.add(createLabelAndTextField("Enter Number of Business Seats: ", businessTextField), gbc);
            gbc.gridy++;
            browseFrame.add(createLabelAndTextField("Enter Economy Price: ", ecoPriceTextField), gbc);
            gbc.gridy++;
            browseFrame.add(createLabelAndTextField("Enter Business Price: ", businessPriceTextField), gbc);
            gbc.gridy++;

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JButton addButton = new JButton("Add Aircraft");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String aircraftModel = modelTextField.getText();
                        int numEconomySeats = Integer.parseInt(ecoTextField.getText());
                        int numComfortSeats = Integer.parseInt(comfortTextField.getText());
                        int numBusinessSeats = Integer.parseInt(businessTextField.getText());
                        double economyPrice = Double.parseDouble(ecoPriceTextField.getText());
                        double businessPrice = Double.parseDouble(businessPriceTextField.getText());

                        DBMS dbms = DBMS.getDBMS();
                        dbms.addAircraft(aircraftModel, numEconomySeats, numComfortSeats, numBusinessSeats,
                                economyPrice, businessPrice);

                        ArrayList<Aircraft> aircrafts = dbms.getAircrafts();
                        AircraftInfoFrame aircraftInfo = new AircraftInfoFrame(aircrafts);

                        aircraftInfo.setVisible(true);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(browseFrame, "Please enter valid numeric values.", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(browseFrame, "Error fetching flight data: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            buttonPanel.add(addButton);

            browseFrame.add(new JLabel());
            browseFrame.add(buttonPanel);

            browseFrame.setVisible(true);
        }

        private JPanel createLabelAndTextField(String labelText, JTextField textField) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JLabel label = new JLabel(labelText);

            panel.add(label);
            panel.add(textField);

            return panel;
        }

        private void updateCrewPanel() {
            JFrame browseFrame = new JFrame("Browse Flights");
            browseFrame.setSize(400, 300);
            browseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            browseFrame.setLayout(new GridLayout(5, 2, 10, 10));

            JLabel crewIDLabel = new JLabel("Enter a crewID you would like to update: ");
            JTextField crewIDTextField = new JTextField();

            JLabel flightLabel = new JLabel("Enter the flightID you would like to change to: ");
            JTextField flightTextField = new JTextField();

            browseFrame.add(crewIDLabel);
            browseFrame.add(crewIDTextField);
            browseFrame.add(flightLabel);
            browseFrame.add(flightTextField);

            JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        int crewID = Integer.parseInt(crewIDTextField.getText());
                        int flightID = Integer.parseInt(flightTextField.getText());

                        DBMS dbms = DBMS.getDBMS(); // This will not create a new instance but will return the existing
                                                    // one.
                        dbms.updateCrew(crewID, flightID);

                        ArrayList<CrewMember> crew = dbms.getCrewMembers(flightID);
                        CrewInfoFrame crewInfo = new CrewInfoFrame(crew);

                        crewInfo.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(browseFrame, "Error fetching flight data: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            browseFrame.add(new JLabel());
            browseFrame.add(confirmButton);

            browseFrame.setVisible(true);
        }

        private void browseFlightPanel() {
            JFrame browseFrame = new JFrame("Browse Flights");
            browseFrame.setSize(400, 300);
            browseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            browseFrame.setLayout(new GridLayout(5, 2, 10, 10));

            JLabel fromLabel = new JLabel("Enter a date (YYYY-MM-DD): ");
            JTextField daTextField = new JTextField();

            browseFrame.add(fromLabel);
            browseFrame.add(daTextField);

            JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        LocalDate date = LocalDate.parse(daTextField.getText());

                        DBMS dbms = DBMS.getDBMS(); // This will not create a new instance but will return the existing
                                                    // one.
                        ArrayList<Flight> flights = dbms.getFlights(date);

                        AdminFlightInfoFrame flightInfo = new AdminFlightInfoFrame(flights);
                        flightInfo.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(browseFrame, "Error fetching flight data: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            browseFrame.add(new JLabel());
            browseFrame.add(confirmButton);

            browseFrame.setVisible(true);
        }

        private void crewListPanel() {
            JFrame browseFrame = new JFrame("Browse Flights");
            browseFrame.setSize(400, 300);
            browseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            browseFrame.setLayout(new GridLayout(5, 2, 10, 10));

            JLabel fromLabel = new JLabel("Enter a flightID: ");
            JTextField flightTextField = new JTextField();

            browseFrame.add(fromLabel);
            browseFrame.add(flightTextField);

            JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        int flightID = Integer.parseInt(flightTextField.getText());

                        DBMS dbms = DBMS.getDBMS(); // This will not create a new instance but will return the existing
                                                    // one.
                        ArrayList<CrewMember> crew = dbms.getCrewMembers(flightID);

                        CrewInfoFrame crewInfo = new CrewInfoFrame(crew);
                        crewInfo.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(browseFrame, "Error fetching flight data: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            browseFrame.add(new JLabel());
            browseFrame.add(confirmButton);

            browseFrame.setVisible(true);
        }
    }

    public class AircraftInfoFrame extends JFrame {
        public AircraftInfoFrame(ArrayList<Aircraft> aircrafts) {
            setTitle("Aircraft Information");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            setSize(800, 400);

            String[] columnNames = { "Aircraft ID", "Model", "Economy Seats", "Comfort Seats", "Business Seats",
                    "Economy Price", "Business Price" };
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (Aircraft aircraft : aircrafts) {
                Object[] row = new Object[7];
                row[0] = aircraft.getAircraftID();
                row[1] = aircraft.getAircraftModel();
                row[2] = aircraft.getNumEconomySeats();
                row[3] = aircraft.getNumComfortSeats();
                row[4] = aircraft.getNumBusinessSeats();
                row[5] = aircraft.getEconomyPrice();
                row[6] = aircraft.getBusinessPrice();
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);

            // Add the JScrollPane to the frame
            add(scrollPane, BorderLayout.CENTER);

            // Make the frame visible
            setVisible(true);
        }
    }

    public class CrewInfoFrame extends JFrame {
        public CrewInfoFrame(ArrayList<CrewMember> crewList) {
            setTitle("Crew Information");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            setSize(600, 400);

            String[] columnNames = { "Crew ID", "Name", "Position" };
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (CrewMember crewMember : crewList) {
                Object[] row = new Object[3];
                row[0] = crewMember.getCrewID();
                row[1] = crewMember.getCrewName();
                row[2] = crewMember.getCrewPos();
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);

            // Add the JScrollPane to the frame
            add(scrollPane, BorderLayout.CENTER);

            // Make the frame visible
            setVisible(true);
        }
    }

    public class WelcomeFrame extends JFrame {
        public WelcomeFrame() {
            setTitle("Welcome");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            setExtendedState(JFrame.MAXIMIZED_BOTH);

            JLabel welcomeLabel = new JLabel("Welcome to our Flight Reservation System", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(welcomeLabel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setBackground(BACKGROUND_COLOR);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.NONE;

            JButton buyTicketsButton = createStyledButton("Buy Tickets");
            gbc.insets = new Insets(10, 0, 10, 0);
            buttonPanel.add(buyTicketsButton, gbc);

            buyTicketsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createTicketPurchasePanel();
                }
            });

            JButton cancelFlightButton = createStyledButton("Cancel Flight");
            gbc.insets = new Insets(0, 0, 10, 0);
            buttonPanel.add(cancelFlightButton, gbc);

            add(buttonPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        private JButton createStyledButton(String text) {
            JButton button = new JButton(text);
            button.setFont(MAIN_FONT);
            button.setBackground(BUTTON_COLOR);
            button.setForeground(BUTTON_TEXT_COLOR);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            return button;
        }
    }

    public LoginFrame() {
        setTitle("Flight Reservation System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Flight Reservation System");
        titleLabel.setFont(MAIN_FONT);
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton userLoginButton = createStyledButton("User Login");
        JButton adminLoginButton = createStyledButton("Admin Login");
        JButton guestLoginButton = createStyledButton("Guest Login");
        JButton userRegisterButton = createStyledButton("User Register");

        // Add action listeners to your buttons
        userRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hide the login frame
                LoginFrame.this.setVisible(false);
                LoginFrame.this.dispose(); // Optionally, you can dispose the LoginFrame

                // Create and show the register frame
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);
            }
        });

        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createUserLoginPanel(); // Call the method to create the login panel
            }
        });

        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAdminLoginPanel(); // Call the method to create the login panel
            }
        });

        gbc.gridy = 0;
        buttonPanel.add(userLoginButton, gbc);
        gbc.gridy = 1;
        buttonPanel.add(adminLoginButton, gbc);
        gbc.gridy = 2;
        buttonPanel.add(guestLoginButton, gbc);
        gbc.gridy = 3;
        buttonPanel.add(userRegisterButton, gbc);

        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel groupLabel = new JLabel("group25");
        groupLabel.setFont(GROUP_FONT);
        groupLabel.setForeground(Color.GRAY);
        groupPanel.add(groupLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(groupPanel, BorderLayout.SOUTH);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createTicketPurchasePanel() {

        JFrame ticketFrame = new JFrame("Purchase Tickets");
        ticketFrame.setSize(400, 300);
        ticketFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ticketFrame.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel fromLabel = new JLabel("Select Departure:");
        JComboBox<String> fromComboBox = new JComboBox<>();
        JLabel toLabel = new JLabel("Select Destination:");
        JComboBox<String> toComboBox = new JComboBox<>();

        try {
            DBMS dbms = DBMS.getDBMS();
            ArrayList<String> origins = dbms.getOrigins();
            for (String origin : origins) {
                fromComboBox.addItem(origin);
            }

            ArrayList<String> destinations = dbms.getDestinations();
            for (String destination : destinations) {
                toComboBox.addItem(destination);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ticketFrame, "Error fetching locations: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        ticketFrame.add(fromLabel);
        ticketFrame.add(fromComboBox);
        ticketFrame.add(toLabel);
        ticketFrame.add(toComboBox);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String origin = (String) fromComboBox.getSelectedItem();
                    String destination = (String) toComboBox.getSelectedItem();

                    DBMS dbms = DBMS.getDBMS(); // This will not create a new instance but will return the existing one.
                    ArrayList<Flight> flights = dbms.getFlights(origin, destination);

                    FlightInfoFrame flightInfoFrame = new FlightInfoFrame(flights);
                    flightInfoFrame.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ticketFrame, "Error fetching flight data: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ticketFrame.add(new JLabel());
        ticketFrame.add(confirmButton);

        ticketFrame.setVisible(true);
    }

    public class BookingFrame extends JFrame {
        private JRadioButton economyClassButton;
        private JRadioButton businessClassButton;
        private JCheckBox insuranceCheckbox;
        private JLabel totalPriceLabel;

        private int economySeats; // Number of economy seats
        private int businessSeats; // Number of business seats

        private double economyPrice;
        private double businessPrice;
        private double insurancePrice;

        private double totalPrice; // Class variable to store the total price

        private void updatePrice() {
            totalPrice = economyClassButton.isSelected() ? economyPrice : businessPrice;
            if (insuranceCheckbox.isSelected()) {
                totalPrice += insurancePrice;
            }
            totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
        }

        public BookingFrame(Aircraft aircraft, double economyPrice, double businessPrice, double insurancePrice) {
            // Use the Aircraft object to set the number of seats
            this.economySeats = aircraft.getNumEconomySeats();
            this.businessSeats = aircraft.getNumBusinessSeats();
            this.economyPrice = economyPrice;
            this.businessPrice = businessPrice;
            this.insurancePrice = insurancePrice;

            setTitle("Booking Options");
            setSize(300, 200); // Set size
            setLayout(new FlowLayout()); // Set layout

            economyClassButton = new JRadioButton("Economy Class", true);
            businessClassButton = new JRadioButton("Business Class");

            // Group the radio buttons.
            ButtonGroup group = new ButtonGroup();
            group.add(economyClassButton);
            group.add(businessClassButton);

            insuranceCheckbox = new JCheckBox("Cancellation Insurance");

            totalPriceLabel = new JLabel("Total Price: $" + economyPrice);

            JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SeatSelectionFrame seatSelectionFrame = new SeatSelectionFrame(
                            economyClassButton.isSelected() ? economySeats : businessSeats,
                            economyClassButton.isSelected(),
                            totalPrice); // Pass the totalPrice as a new argument
                    seatSelectionFrame.setVisible(true);
                }
            });

            add(economyClassButton);
            add(businessClassButton);
            add(insuranceCheckbox);
            add(totalPriceLabel);
            add(confirmButton);

            ActionListener priceChangeListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updatePrice();
                }
            };

            // Add the ActionListener to the radio buttons and checkbox
            economyClassButton.addActionListener(priceChangeListener);
            businessClassButton.addActionListener(priceChangeListener);
            insuranceCheckbox.addActionListener(priceChangeListener);

            // Initialize the price based on default selected values
            updatePrice();
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    public class AdminFlightInfoFrame extends JFrame {
        public AdminFlightInfoFrame(ArrayList<Flight> flights) {
            setTitle("Flight Information");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            setSize(600, 400);

            String[] columnNames = { "Flight ID", "Origin", "Destination" };
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (Flight flight : flights) {
                Object[] row = new Object[3]; // Adjusted to match the number of columns
                row[0] = flight.getFlightID();
                row[1] = flight.getDepartureLocation();
                row[2] = flight.getArrivalLocation();
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);

            // Add the JScrollPane to the frame
            add(scrollPane, BorderLayout.CENTER);

            // Make the frame visible
            setVisible(true);
        }
    }

    // DBMS dbms = DBMS.getDBMS(); // Singleton instance
    public class FlightInfoFrame extends JFrame {
        public FlightInfoFrame(ArrayList<Flight> flights) {
            setTitle("Flight Information");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            setSize(600, 400);

            String[] columnNames = { "Flight ID", "Origin", "Destination", "Departure Time", "Arrival Time" };
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (Flight flight : flights) {
                Object[] row = new Object[5];
                row[0] = flight.getFlightID();
                row[1] = flight.getDepartureLocation();
                row[2] = flight.getArrivalLocation();
                row[3] = flight.getDepartureDate().toString() + " " + flight.getDepartureTime().toString();
                row[4] = flight.getArrivalDate().toString() + " " + flight.getArrivalTime().toString();
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            JButton selectFlightButton = new JButton("Select Flight");
            selectFlightButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            selectFlightButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
            selectFlightButton.setForeground(Color.WHITE);
            selectFlightButton.setFocusPainted(false);
            selectFlightButton.setBorderPainted(false);

            JPanel selectFlightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            selectFlightPanel.add(selectFlightButton);

            add(selectFlightPanel, BorderLayout.SOUTH);

            selectFlightButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        try {
                            int flightID = (Integer) table.getValueAt(selectedRow, 0);
                            Flight selectedFlight = Flight.getFlightByID(flights, flightID);
                            Aircraft aircraft = selectedFlight.getAircraft(); // Get the Aircraft object

                            DBMS dbms = DBMS.getDBMS(); // Singleton instance
                            double economyPrice = dbms.getEconomyPrice(aircraft.getAircraftID());
                            double businessPrice = dbms.getBusinessPrice(aircraft.getAircraftID());
                            double insurancePrice = 50; // This can be a fixed value or also retrieved from the database

                            FlightInfoFrame.this.dispose(); // Close the current frame

                            BookingFrame bookingFrame = new BookingFrame(aircraft, economyPrice, businessPrice,
                                    insurancePrice);
                            bookingFrame.setVisible(true); // Display the booking frame

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(FlightInfoFrame.this,
                                    "Error accessing database: " + ex.getMessage(),
                                    "Database Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(FlightInfoFrame.this,
                                "Please select a flight to continue.",
                                "No Flight Selected",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            // 显示窗口
            setVisible(true);
        }
    }

    public class SeatSelectionFrame extends JFrame {
        private static final int ECONOMY_SEATS = 70;
        private static final int BUSINESS_SEATS = 30;
        private double totalPrice;
        private JButton confirmButton;
        private JButton selectedSeatButton;

        public SeatSelectionFrame(int totalSeats, boolean isEconomy, double totalPrice) {
            this.totalPrice = totalPrice;
            setTitle("Select Seats");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            int numRows = (int) Math.ceil((totalSeats - 5) / 5.0) + 1;
            JPanel seatPanel = new JPanel(new GridLayout(numRows, 5, 10, 10));
            seatPanel.setBorder(BorderFactory.createTitledBorder(isEconomy ? "Economy Class" : "Business Class"));

            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < 5; col++) {

                    if (row > 0 && (col == 0 || col == 4)) {
                        seatPanel.add(Box.createRigidArea(new Dimension(50, 50)));
                    } else {
                        String seatLabel = ((row == 0) ? "P" : (isEconomy ? "E" : "B")) + ((row * 5) + col);
                        JButton seatButton = createSeatButton(seatLabel);
                        seatPanel.add(seatButton);
                    }
                }
            }

            confirmButton = new JButton("Confirm Selection");
            confirmButton.setEnabled(false);
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    PaymentFrame paymentFrame = new PaymentFrame(totalPrice);
                    paymentFrame.setVisible(true);
                }
            });

            add(confirmButton, BorderLayout.SOUTH);

            JScrollPane scrollPane = new JScrollPane(seatPanel);
            add(scrollPane, BorderLayout.CENTER);

            pack();
            setVisible(true);
        }

        private JButton createSeatButton(String seatText) {
            JButton button = new JButton(seatText);
            button.setPreferredSize(new Dimension(50, 50));
            button.setBackground(Color.LIGHT_GRAY);
            button.setBorder(BorderFactory.createRaisedBevelBorder());

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedSeatButton != null) {
                        selectedSeatButton.setBackground(Color.LIGHT_GRAY);
                    }

                    selectedSeatButton = (JButton) e.getSource();
                    selectedSeatButton.setBackground(Color.RED);
                    confirmButton.setEnabled(true);
                }
            });

            return button;
        }
    }

    class PaymentFrame extends JFrame {
        private double totalPrice;

        public PaymentFrame(double totalPrice) {
            this.totalPrice = totalPrice;

            setTitle("Make Payment");
            setLayout(new BorderLayout());
            setSize(350, 200);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Panel for the total price
            JPanel pricePanel = new JPanel();
            pricePanel.add(new JLabel("Total Price: "));
            JTextField priceField = new JTextField(10);
            priceField.setText(String.format("$%.2f", totalPrice));
            priceField.setEditable(false);
            pricePanel.add(priceField);

            // Panel for card information
            JPanel cardPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            cardPanel.add(new JLabel("Card Number:"));
            JTextField cardNumberField = new JTextField();
            cardPanel.add(cardNumberField);
            cardPanel.add(new JLabel("Expiry Date (MM/YY):"));
            JTextField expiryField = new JTextField();
            cardPanel.add(expiryField);
            cardPanel.add(new JLabel("CVV:"));
            JTextField cvvField = new JTextField();
            cardPanel.add(cvvField);

            // Confirm button to submit payment
            JButton confirmButton = new JButton("Confirm Payment");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implement the payment processing logic here
                    // For example, validate the card information, process the payment, etc.
                    JOptionPane.showMessageDialog(PaymentFrame.this, "Payment submitted!", "Payment",
                            JOptionPane.INFORMATION_MESSAGE);
                    // TODO: Add actual payment processing code here
                }
            });

            // Adding components to the frame
            add(pricePanel, BorderLayout.NORTH);
            add(cardPanel, BorderLayout.CENTER);
            add(confirmButton, BorderLayout.SOUTH);

            pack();
            setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
