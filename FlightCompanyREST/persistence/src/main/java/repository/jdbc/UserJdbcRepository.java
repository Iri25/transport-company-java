package repository.jdbc;

import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserJdbcRepository implements UserRepository {

    private final JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();

    public UserJdbcRepository(Properties properties) {

        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public User findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User user = new User(username, password);
                    user.setId(id);

                    return user;
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
    public User findUsernamePassword(String username, String password) {
        logger.traceEntry("Finding task with username and password {}, {} ", username, password);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    username = resultSet.getString("username");
                    password = resultSet.getString("password");
                    Integer id = resultSet.getInt("id");

                    User user = new User(username, password);
                    user.setId(id);

                    return user;
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with username {}, ", username);
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User user = new User(username, password);
                    user.setId(id);

                    users.add(user);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id){
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id = ?")){
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
    public void update(User entity){
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE INTO Users VALUES (?, ?)")) {
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
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