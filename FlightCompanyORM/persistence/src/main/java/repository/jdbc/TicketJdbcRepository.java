package repository.jdbc;

import domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.TicketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketJdbcRepository implements TicketRepository {

    private final JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();

    public TicketJdbcRepository(Properties properties) {

        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Ticket findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tickets WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String clientName = resultSet.getString("clientName");
                    String touristsName = resultSet.getString("touristsName");
                    String clientAddress = resultSet.getString("clientAddress");
                    int numberOfSeats = resultSet.getInt("numberOfSeats");
                    int idFlight = resultSet.getInt("idFlight");

                    Ticket ticket = new Ticket(clientName, touristsName, clientAddress, numberOfSeats, idFlight);
                    ticket.setId(id);

                    return ticket;
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
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tickets")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String clientName = resultSet.getString("clientName");
                    String touristsName = resultSet.getString("touristsName");
                    String clientAddress = resultSet.getString("clientAddress");
                    int numberOfSeats = resultSet.getInt("numberOfSeats");
                    int idFlight = resultSet.getInt("idFlight");

                    Ticket ticket = new Ticket(clientName, touristsName, clientAddress, numberOfSeats, idFlight);
                    ticket.setId(id);

                    tickets.add(ticket);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public void save(Ticket entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Tickets VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getClientName());
            preparedStatement.setString(3, entity.getTouristsName());
            preparedStatement.setString(4, entity.getClientAddress());
            preparedStatement.setInt(5, entity.getNumberOfSeats());
            preparedStatement.setInt(6, entity.getIdFlight());
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id){
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Tickets WHERE id = ?")){
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
    public void update(Ticket entity){
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE INTO Tickets VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(2, entity.getClientName());
            preparedStatement.setString(3, entity.getTouristsName());
            preparedStatement.setString(4, entity.getClientAddress());
            preparedStatement.setInt(5, entity.getNumberOfSeats());
            preparedStatement.setInt(6, entity.getIdFlight());
            // preparedStatement.setInt(1, entity.setId());
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