package src;

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
            Aircraft aircraft = new Aircraft(aircraftID, aircraftModel, numEconomySeats, numComfortSeats,
                    numBusinessSeats);
            aircrafts.add(aircraft);
        }
        results.close();
        return aircrafts;
    }

    /*
     * getFlight list from database
     */
    public ArrayList<Flight> getFlights() throws SQLException {
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
        results.close();
        return flights;
    }

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
            String username = results.getString("Name");
            String address = results.getString("Address");
            String email = results.getString("Email");
            String userType = results.getString("UserType");
            boolean isMember = results.getBoolean("MembershipStatus");
            String creditCard = results.getString("CreditCardInfo");
            User user = new User(username, email, address, creditCard, userType);
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

    /*
     * getSeating list
     */
    // public ArrayList<Seat> getSeats() throws SQLException {
    // ArrayList<Seat> seats = new ArrayList<Seat>();
    // Statement myStmt = dbConnect.createStatement();
    // results = myStmt.executeQuery("SELECT * FROM Seats");
    // while (results.next()) {
    // String seatID = results.getString("SeatID");
    // String seatType = results.getString("SeatType");
    // int seatPrice = results.getInt("SeatPrice");
    // Seat seat = new Seat(seatID, seatType, seatPrice);
    // seats.add(seat);
    // }
    // return seats;
    // }

    public static void main(String args[]) throws SQLException {

        DBMS connect = getDBMS();

        // This is a list of all flight information in the database
        // - can use all flight getter methods for flight info
        ArrayList<Flight> flightList = connect.getFlights();
        // This is a list of all user information in the database
        // - can use all user getter methods for user info
        ArrayList<User> userList = connect.getUsers();

        // for (Flight flight : flightList) {
        // System.out.println(flight.getAircraft().getAircraftModel());
        // System.out.println(flight.getAircraft().getAircraftID());
        // }

        /*
         * TODO: with no login, customer can browse flights and book a flight
         * can search for flights by date, origin, destination
         * - use flight class getter methods to get info from flightList
         */

        // selected a flight
        Flight testFlight = flightList.get(0);

        /*
         * TODO: prompt user to login or create an account
         * this is a test object
         */

        // User testObject = new User("test", "test st", "test@email", "test",
        // "passenger");

        User testObject = userList.get(0);

        // connect.addUser(testObject);

        // Upon selecting a flight, display seat availability

        // create a new booking
        // testFlight.bookSeat(new Seat("1", "economy", 80), testObject);
        // System.out.println("Available seats: ");
        // for (Seat seat : testFlight.getSeats().values()) {
        // if (seat.getIsAvailable()) {
        // System.out.println(seat.getSeatNumber());
        // }
        // }
        // connect.bookFlight(testFlight.getFlightID(), testObject);

        /*
         * TODO: with login, customer can view booked flights and cancel a flight
         * do they need to login? or they can view by bookingID?
         */

        connect.closeConnection();
    }
}