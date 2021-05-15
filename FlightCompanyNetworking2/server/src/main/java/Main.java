import networking.Server;
import repository.jdbc.FlightJdbcRepository;
import repository.jdbc.TicketJdbcRepository;
import repository.jdbc.UserJdbcRepository;
import service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        try {
            properties.load(new FileReader("app.config"));
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

        UserJdbcRepository userJdbcRepository = new UserJdbcRepository(properties);
        FlightJdbcRepository flightJdbcRepository = new FlightJdbcRepository(properties);
        TicketJdbcRepository ticketJdbcRepository = new TicketJdbcRepository(properties);

        Service service = new Service(userJdbcRepository, flightJdbcRepository, ticketJdbcRepository);

        Server server = new Server("localhost", 55555, service);
        server.start();

    }
}
