import network.utils.AbstractServer;
import network.utils.RpcConcurrentServer;
import network.utils.ServerException;
import repository.jdbc.FlightJdbcRepository;
import repository.jdbc.TicketJdbcRepository;
import repository.jdbc.UserJdbcRepository;
import repository.jdbc.hbm.UserJdbcHbmRepository;
import server.Server;
import service.IServices;

import java.io.IOException;
import java.util.Properties;

public class MainServer {

    private static int defaultPort = 55555;

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        try {
            properties.load(MainServer.class.getResourceAsStream("server.properties"));
            System.out.println("server.Server properties set: ");
            properties.list(System.out);
        }
        catch (IOException e) {
            System.err.println("Cannot find server.properties! " + e);
            return;
        }

        //UserJdbcHbmRepository userRepository = new UserJdbcHbmRepository(properties);
        UserJdbcRepository userRepository = new UserJdbcRepository(properties);
        FlightJdbcRepository flightRepository = new FlightJdbcRepository(properties);
        TicketJdbcRepository ticketRepository = new TicketJdbcRepository(properties);

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
        AbstractServer server = new RpcConcurrentServer(serverPort, iServices);
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
