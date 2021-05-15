package repository.jdbc;

import domain.Flight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.FlightRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlightJdbcRepository implements FlightRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();

    public FlightJdbcRepository(){}

    public FlightJdbcRepository(Properties properties) {

        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Flight findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Flights WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idNew = resultSet.getInt("id");
                    String destination = resultSet.getString("destination");
                    String departureDate = resultSet.getString("departureDate");
                    String departureTime = resultSet.getString("departureTime");
                    String airport = resultSet.getString("airport");
                    int numberOfSeatsAvailable = resultSet.getInt("numberOfSeatsAvailable");

                    Flight flight = new Flight(destination, departureDate, departureTime, airport, numberOfSeatsAvailable);
                    flight.setId(id);

                    return flight;
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with id {}", id);
        return null;
    }

    @Override
    public Iterable<Flight> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Flight> flights = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Flights")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String destination = resultSet.getString("destination");
                    String departureDate = resultSet.getString("departureDate");
                    String departureTime = resultSet.getString("departureTime");
                    String airport = resultSet.getString("airport");
                    int numberOfSeatsAvailable = resultSet.getInt("numberOfSeatsAvailable");
                    Flight flight = new Flight(destination, departureDate, departureTime, airport, numberOfSeatsAvailable);
                    flight.setId(id);

                    flights.add(flight);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(flights);
        return flights;
    }

    @Override
    public void save(Flight entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO flight VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getDestination());
            preparedStatement.setString(3, entity.getDepartureDate());
            preparedStatement.setString(4, entity.getDepartureTime());
            preparedStatement.setString(5, entity.getAirport());
            preparedStatement.setInt(6, entity.getNumberOfSeats());
            preparedStatement.executeUpdate();
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id){
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Flights WHERE id = ?")){
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Flight entity){

        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE INTO Flights VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(2, entity.getDestination());
            preparedStatement.setString(3, entity.getDepartureDate());
            preparedStatement.setString(4, entity.getDepartureTime());
            preparedStatement.setString(5, entity.getAirport());
            preparedStatement.setInt(6, entity.getNumberOfSeats());
            //preparedStatement.setInt(1, entity.setId());
            int result = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", result);
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }
}