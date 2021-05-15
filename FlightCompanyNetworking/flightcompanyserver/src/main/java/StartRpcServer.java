import network.utils.AbstractServer;
import network.utils.RpcConcurrentServer;
import network.utils.ServerException;
import persistence.FlightRepository;
import persistence.TicketRepository;
import persistence.UserRepository;
import persistence.jdbc.FlightJdbcRepository;
import persistence.jdbc.TicketJdbcRepository;
import persistence.jdbc.UserJdbcRepository;
import server.Services;
import services.IServices;

import java.io.IOException;
import java.util.Properties;

class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {

        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("server.properties"));
            System.out.println("Server properties set: ");
            serverProps.list(System.out);
        }
        catch (IOException e) {
            System.err.println("Cannot find server.properties! " + e);
            return;
        }

        UserRepository userRepository = new UserJdbcRepository(serverProps);
        FlightRepository flightRepository = new FlightJdbcRepository(serverProps);
        TicketRepository ticketRepository = new TicketJdbcRepository(serverProps);

        IServices iServices = new Services(userRepository, flightRepository, ticketRepository);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
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
