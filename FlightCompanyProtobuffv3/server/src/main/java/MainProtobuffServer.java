import network.utils.AbstractServer;
import network.utils.ProtobuffConcurrentServer;
import network.utils.ServerException;
import repository.FlightRepository;
import repository.TicketRepository;
import repository.UserRepository;
import repository.jdbc.FlightJdbcRepository;
import repository.jdbc.TicketJdbcRepository;
import repository.jdbc.UserJdbcRepository;
import server.Server;
import service.IServices;

import java.io.IOException;
import java.util.Properties;

class MainProtobuffServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {

        Properties properties = new Properties();
        try {
            properties.load(MainProtobuffServer.class.getResourceAsStream("server.properties"));
            System.out.println("Server properties set: ");
            properties.list(System.out);
        }
        catch (IOException e) {
            System.err.println("Cannot find server.properties! " + e);
            return;
        }

        UserRepository userRepository = new UserJdbcRepository(properties);
        FlightRepository flightRepository = new FlightJdbcRepository(properties);
        TicketRepository ticketRepository = new TicketJdbcRepository(properties);

        IServices iServices = new Server(userRepository, flightRepository, ticketRepository);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(properties.getProperty("server.port"));
        }
        catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port: " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ProtobuffConcurrentServer(serverPort, iServices);
        try {
            server.start();
        }
        catch (ServerException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
        finally {
            try {
                server.stop();
            }
            catch(ServerException e){
                System.err.println("Error stopping server: " + e.getMessage());
            }
        }
    }
}
