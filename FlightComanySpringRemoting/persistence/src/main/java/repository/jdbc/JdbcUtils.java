package repository.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private final Properties properties;
    private static final Logger logger = LogManager.getLogger();
    private Connection instance = null;

    public JdbcUtils(Properties properties) {
        this.properties = properties;
    }

    private Connection getNewConnection() {
        logger.traceEntry();

        // String driver = properties.getProperty("jdbc.driver");
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        String password = properties.getProperty("jdbc.password");

        logger.info("Trying to connect to database {}", url);
        logger.info("User: {}", user);
        logger.info("Password: {}", password);

        Connection connection = null;
        try {
           // Class.forName(driver);
           // logger.info("Loaded driver {}", driver);
            if (user != null && password != null)
                connection = DriverManager.getConnection(url, user, password);
            else
                connection = DriverManager.getConnection(url);
            logger.info("Connected successfully to {}", url);
        }
        // catch (ClassNotFoundException classNotFoundException) {
        //    logger.error(classNotFoundException);
        //    System.out.println("Error loading driver " + classNotFoundException);
        // }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            System.out.println("Error getting connection " + sqlException);
        }
        return connection;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        }
            catch (SQLException sqlException) {
            logger.error(sqlException);
            System.out.println("Error connecting to database " + sqlException);
        }
        logger.traceExit(instance);
        return instance;
    }
}