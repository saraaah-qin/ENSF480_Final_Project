
import java.sql.*;
import java.util.*;
import java.time.*;

/**
 * Database Management System, handles any interaction between the program and
 * the database
 *
 * DBMS instance - The global instance for our database manager (See Singleton
 * Design Pattern)
 * Connection dbConnect - SQL connection object for initializing connection with
 * database
 * ResultSet results - The results of queries will be in this object
 *
 */
public class DBMS {

    private static DBMS instance;
    private Connection dbConnect;
    private ResultSet results;

    /**
     * DBMS Constructor
     *
     * Called only when an instance does not yet exist, creates onnection to local
     * database
     *
     */
    private DBMS() throws SQLException {
        // the connection info here will need to be changed depending on the user
        dbConnect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ENSF480", "root", "password");
    }

    /**
     * DBMS Constructor
     *
     * Ensures only one instance of database manager exists at once, returns DBMS
     * object
     *
     */
    public static DBMS getDBMS() throws SQLException {
        if (instance == null) {
            System.out.println("Database instance created");
            instance = new DBMS();
        }
        return instance;
    }

    /**
     * closeConnection
     *
     * Closes database connection, use at end of program
     *
     */
    public void closeConnection() throws SQLException {
        dbConnect.close();
        results.close();

    }

    /*
     * getAircraft list from database
     */
    public ArrayList<Aircraft> getAircrafts() throws SQLException {
        ArrayList<Aircraft> aircrafts = new ArrayList<Aircraft>();
        Statement myStmt = dbConnect.createStatement();
        results = myStmt.executeQuery("SELECT * FROM Aircrafts");
        while (results.next()) {
            int aircraftID = results.getInt("AircraftID");
            String aircraftModel = results.getString("Model");
            int numEconomySeats = results.getInt("Ordinary");
            int numComfortSeats = results.getInt("Comfort");
            int numBusinessSeats = results.getInt("Business");
            double economyPrice = results.getDouble("EconomyPrice");
            double businessPrice = results.getDouble("BusinessPrice");

            Aircraft aircraft = new Aircraft(aircraftID, aircraftModel, numEconomySeats, numComfortSeats,
                    numBusinessSeats, economyPrice, businessPrice);
            aircrafts.add(aircraft);
        }
        results.close();
        return aircrafts;
    }

    /*
     * add aircraft to database
     */

    public void addAircraft(String aircraftModel, int numEconomySeats, int numComfortSeats,
            int numBusinessSeats, double economyPrice, double businessPrice) throws SQLException {
        // Statement myStmt = dbConnect.createStatement();

        // Use PreparedStatement to avoid SQL injection
        String sql = "INSERT INTO aircrafts (Model, Ordinary, Comfort, Business, EconomyPrice, BusinessPrice) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbConnect.prepareStatement(sql)) {
            pstmt.setString(1, aircraftModel);
            pstmt.setInt(2, numEconomySeats);
            pstmt.setInt(3, numComfortSeats);
            pstmt.setInt(4, numBusinessSeats);
            pstmt.setDouble(5, economyPrice);
            pstmt.setDouble(6, businessPrice);

            pstmt.executeUpdate();
        }
    }

    /*
     * remove aircraft from database
     */

    public void removeAircraft(int aircraftID) throws SQLException {
        Statement myStmt = dbConnect.createStatement();
        String sql = "DELETE FROM aircrafts WHERE AircraftID = ?";
        try (PreparedStatement pstmt = dbConnect.prepareStatement(sql)) {
            pstmt.setInt(1, aircraftID);
            pstmt.executeUpdate();
        }
    }

    /*
     * getFlight list based on destination and origin from database
     */
    public ArrayList<Flight> getFlights(String origin, String destination) throws SQLException {
        ArrayList<Flight> flights = new ArrayList<Flight>();
        ArrayList<Aircraft> aircrafts = getAircrafts();
        Statement myStmt = dbConnect.createStatement();
        String query = "SELECT * FROM Flights WHERE Origin = ? AND Destination = ?";
        PreparedStatement pstmt = dbConnect.prepareStatement(query);
        pstmt.setString(1, origin);
        pstmt.setString(2, destination);
        ResultSet results = pstmt.executeQuery();
        while (results.next()) {
            LocalDateTime departureDateTime = results.getTimestamp("DepartureDateTime").toLocalDateTime();
            LocalDateTime arrivalDateTime = results.getTimestamp("ArrivalDateTime").toLocalDateTime();
            int aircraftID = results.getInt("AircraftID");
            Aircraft aircraft = null;
            for (Aircraft a : aircrafts) {
                if (a.getAircraftID() == aircraftID) {
                    aircraft = a;
                    break; // 找到匹配的飞机后即可退出循环
                }
            }
            Flight flight = new Flight(aircraft, results.getInt("FlightID"), results.getString("Origin"),
                    results.getString("Destination"), departureDateTime.toLocalDate(),
                    departureDateTime.toLocalTime(), arrivalDateTime.toLocalDate(), arrivalDateTime.toLocalTime());
            flights.add(flight);
        }
        results.close();
        return flights;
    }

    public ArrayList<String> getOrigins() throws SQLException {
        ArrayList<String> origins = new ArrayList<>();
        Statement statement = dbConnect.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT Origin FROM Flights");
        while (resultSet.next()) {
            origins.add(resultSet.getString("Origin"));
        }
        resultSet.close();
        statement.close();
        return origins;
    }

    public ArrayList<String> getDestinations() throws SQLException {
        ArrayList<String> destinations = new ArrayList<>();
        Statement statement = dbConnect.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT Destination FROM Flights");
        while (resultSet.next()) {
            destinations.add(resultSet.getString("Destination"));
        }
        resultSet.close();
        statement.close();
        return destinations;
    }

    /*
     * get ALL flights from database
     */

    public ArrayList<Flight> getFlights(LocalDate selectedDate) throws SQLException {
        ArrayList<Flight> flights = new ArrayList<Flight>();
        ArrayList<Aircraft> aircrafts = getAircrafts();
        Statement myStmt = dbConnect.createStatement();
        results = myStmt.executeQuery("SELECT * FROM Flights");
        while (results.next()) {
            LocalDateTime departureDateTime = results.getTimestamp("DepartureDateTime").toLocalDateTime();
            LocalDateTime arrivalDateTime = results.getTimestamp("ArrivalDateTime").toLocalDateTime();
            int aircraftID = results.getInt("AircraftID");
            Aircraft aircraft = null;
            for (Aircraft a : aircrafts) {
                if (a.getAircraftID() == aircraftID) {
                    aircraft = a;
                }
            }

            Flight flight = new Flight(aircraft, results.getInt("FlightID"), results.getString("Origin"),
                    results.getString("Destination"), departureDateTime.toLocalDate(),

                    departureDateTime.toLocalTime(), arrivalDateTime.toLocalDate(), arrivalDateTime.toLocalTime());
            flights.add(flight);
        }
        ArrayList<Flight> flightsOnDate = new ArrayList<Flight>();
        for (Flight f : flights) {
            if (f.getDepartureDate().equals(selectedDate)) {
                flightsOnDate.add(f);
            }
        }
        return flightsOnDate;
    }

    /*
     * add flight to database
     */
    public void addFlight(Aircraft aircraft, String origin, String destination, LocalDate departureDate,
            LocalTime departureTime, LocalDate arrivalDate, LocalTime arrivalTime) throws SQLException {
        String sql = "INSERT INTO Flights (AircraftID, Origin, Destination, DepartureDateTime, ArrivalDateTime) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbConnect.prepareStatement(sql)) {
            pstmt.setInt(1, aircraft.getAircraftID());
            pstmt.setString(2, origin);
            pstmt.setString(3, destination);
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.of(departureDate, departureTime)));
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.of(arrivalDate, arrivalTime)));

            pstmt.executeUpdate();
        }
    }

    /*
     * remove flight from database
     */
    public void removeFlight(int flightID) throws SQLException {
        Statement myStmt = dbConnect.createStatement();
        String sql = "DELETE FROM Flights WHERE FlightID = ?";
        try (PreparedStatement pstmt = dbConnect.prepareStatement(sql)) {
            pstmt.setInt(1, flightID);
            pstmt.executeUpdate();
        }
    }

    /*
     * edit flight in database
     */

    // public void editFlight(int flightID, String origin, String destination,
    // LocalDate departureDate,
    // LocalTime departureTime, LocalDate arrivalDate, LocalTime arrivalTime) throws
    // SQLException {
    // String sql = "UPDATE Flights SET Origin = ?, Destination = ?,
    // DepartureDateTime = ?, ArrivalDateTime = ? WHERE FlightID = ?";

    // try (PreparedStatement pstmt = dbConnect.prepareStatement(sql)) {
    // pstmt.setString(1, origin);
    // pstmt.setString(2, destination);
    // pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(departureDate,
    // departureTime)));
    // pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.of(arrivalDate,
    // arrivalTime)));
    // pstmt.setInt(5, flightID);

    // pstmt.executeUpdate();
    // }
    // }

    /*
     * TODO: bookFlight - THIS DOESNT WORK YET
     * Books a flight and insert into database
     */

    public void bookFlight(int userID, int flightID, int seatID, LocalDateTime DateTime) throws SQLException {
        Statement myStmt = dbConnect.createStatement();
        String sql = "INSERT INTO Bookings (UserID, FlightID, SeatID, CancellationInsurance, BookingDateTime) VALUES ("
                + userID + flightID + seatID + "0" + DateTime + ")";
        myStmt.executeUpdate(sql);
    }

    /*
     * getUser List from database
     */

    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        Statement myStmt = dbConnect.createStatement();
        results = myStmt.executeQuery("SELECT * FROM Users");
        while (results.next()) {
            int userID = results.getInt("UserID");
            String username = results.getString("Name");
            String address = results.getString("Address");
            String email = results.getString("Email");
            String userType = results.getString("UserType");
            boolean isMember = results.getBoolean("MembershipStatus");
            String creditCard = results.getString("CreditCardInfo");
            User user = new User(userID, username, email, address, creditCard, userType);
            user.setIsMember(isMember);
            users.add(user);
        }
        results.close();
        return users;
    }

    /*
     * add user to database
     */
    public void addUser(User user) throws SQLException {
        // SQL query for insertion using a prepared statement
        String insertQuery = "INSERT INTO Users (Name, Address, Email, UserType, MembershipStatus, CreditCardInfo) VALUES (?, ?, ?, ?, ?, ?)";

        // Create a prepared statement
        try (PreparedStatement preparedStatement = dbConnect.prepareStatement(insertQuery)) {
            // Set values for the placeholders in the query
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getAddress());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserType());
            preparedStatement.setBoolean(5, user.getIsMember());
            preparedStatement.setString(6, user.getCreditCard());

            // Execute the insertion
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User inserted successfully!");
            } else {
                System.out.println("Failed to insert user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getEconomyPrice(int aircraftID) throws SQLException {
        String query = "SELECT EconomyPrice FROM Aircrafts WHERE AircraftID = ?";
        try (PreparedStatement stmt = dbConnect.prepareStatement(query)) {
            stmt.setInt(1, aircraftID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("EconomyPrice");
            }
        }
        return -1; // Or throw an exception
    }

    public double getBusinessPrice(int aircraftID) throws SQLException {
        String query = "SELECT BusinessPrice FROM Aircrafts WHERE AircraftID = ?";
        try (PreparedStatement stmt = dbConnect.prepareStatement(query)) {
            stmt.setInt(1, aircraftID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("BusinessPrice");
            }
        }
        return -1; // Or throw an exception
    }

    /*
     * getCrew list from database
     */

    public ArrayList<CrewMember> getCrewMembers(int flight) throws SQLException {
        ArrayList<CrewMember> crewMembers = new ArrayList<>();

        String sql = "SELECT * FROM crews WHERE FlightID = ?";

        try (PreparedStatement pstmt = dbConnect.prepareStatement(sql)) {
            pstmt.setInt(1, flight);

            try (ResultSet results = pstmt.executeQuery()) {
                while (results.next()) {
                    int crewID = results.getInt("CrewID");
                    String crewName = results.getString("Name");
                    String position = results.getString("Position");

                    CrewMember crewMember = new CrewMember(crewID, crewName, position);
                    crewMembers.add(crewMember);
                }
            }
        }

        return crewMembers;
    }

    /*
     * add crew to flight - changes flightID in database - 0 is no flights assigned
     */
    public void updateCrew(int crewID, int flight) throws SQLException {
        String sql = "UPDATE crews SET FlightID = ? WHERE CrewID = ?";

        try (PreparedStatement pstmt = dbConnect.prepareStatement(sql)) {
            pstmt.setInt(1, flight);
            pstmt.setInt(2, crewID);
            pstmt.executeUpdate();
        }
    }

    /*
     * getBooking list from database
     */

    // public ArrayList<Booking> getBookings() throws SQLException {
    // ArrayList<Booking> bookings = new ArrayList<Booking>();
    // ArrayList<User> users = getUsers();
    // ArrayList<Flight> flights = getFlights();
    // Statement myStmt = dbConnect.createStatement();
    // results = myStmt.executeQuery("SELECT * FROM Bookings");
    // while (results.next()) {
    // int bookingID = results.getInt("BookingID");
    // int userID = results.getInt("UserID");
    // User user = null;
    // for (User u : users) {
    // if (u.getUserID() == userID) {
    // user = u;
    // }
    // }
    // int flightID = results.getInt("FlightID");
    // Flight flight = null;
    // for (Flight f : flights) {
    // if (f.getFlightID() == flightID) {
    // flight = f;
    // }
    // }

    public static void main(String args[]) throws SQLException {

        DBMS connect = getDBMS();

        // This is a list of all flight information in the database
        // - can use all flight getter methods for flight info
        // ArrayList<Flight> flightList = connect.getFlights();
        // This is a list of all user information in the database
        // - can use all user getter methods for user info
        ArrayList<User> userList = connect.getUsers();

        // This is a list of all aircraft information in the database
        ArrayList<Aircraft> aircraftList = connect.getAircrafts();
        // for (Aircraft a : aircraftList) {
        // System.out.print(a.getAircraftID() + " ");
        // System.out.println(a.getAircraftModel());
        // }

        // test add aircraft
        // connect.removeAircraft(13);

        ArrayList<CrewMember> crewList = connect.getCrewMembers(1);
        // for (CrewMember c : crewList) {
        // System.out.println(+c.getCrewID() + " " + c.getCrewName() + " " +
        // c.getCrewPos());
        // }

        // connect.updateCrew(1, 0);
        // crewList = connect.getCrewMembers(1);
        // for (CrewMember c : crewList) {
        // System.out.println(+c.getCrewID() + " " + c.getCrewName() + " " +
        // c.getCrewPos());
        // }

        // connect.updateCrew(1, 1);
        // crewList = connect.getCrewMembers(1);
        // for (CrewMember c : crewList) {
        // System.out.println(+c.getCrewID() + " " + c.getCrewName() + " " +
        // c.getCrewPos());
        // }

        // test date
        // String dateString = "2024-10-11";
        // LocalDate date = LocalDate.parse(dateString);
        // for (Flight f : flightList) {
        // if (f.getDepartureDate().equals(date)) {
        // System.out.println(
        // "Flights Departing on this date: " + f.getFlightID() + " " +
        // f.getDepartureLocation() + " to " +
        // f.getArrivalLocation());
        // }
        // if (f.getArrivalDate().equals(date)) {
        // System.out.println(
        // "Flights Arriving on this date: " + f.getFlightID() + " " +
        // f.getDepartureLocation() + " to " +
        // f.getArrivalLocation());
        // }

        // }

        connect.closeConnection();
    }
}
