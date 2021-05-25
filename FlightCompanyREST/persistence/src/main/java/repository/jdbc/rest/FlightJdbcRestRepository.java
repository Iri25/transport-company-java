package repository.jdbc.rest;

import domain.Flight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import repository.FlightRepository;
import repository.jdbc.JdbcUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@org.springframework.stereotype.Repository
public class FlightJdbcRestRepository implements FlightRepository {

    private final JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();

    public FlightJdbcRestRepository(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("C:\\Users\\HP\\Desktop\\FlightCompanyREST\\app.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Flights VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getDestination());
            preparedStatement.setString(3, entity.getDepartureDate());
            preparedStatement.setString(4, entity.getDepartureTime());
            preparedStatement.setString(5, entity.getAirport());
            preparedStatement.setInt(6, entity.getNumberOfSeats());
            preparedStatement.executeUpdate();
            preparedStatement.close();
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
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException sqlException){
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Flight entity) {

        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Flights SET destination = ?, departureDate = ?, departureTime = ?, airport = ?, numberOfSeatsAvailable = ? WHERE id = ?")) {
            preparedStatement.setString(1, entity.getDestination());
            preparedStatement.setString(2, entity.getDepartureDate());
            preparedStatement.setString(3, entity.getDepartureTime());
            preparedStatement.setString(4, entity.getAirport());
            preparedStatement.setInt(5, entity.getNumberOfSeats());
            preparedStatement.setInt(6, entity.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }
}