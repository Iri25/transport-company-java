package network.protobuffprotocol;

import domain.Flight;
import domain.Ticket;
import domain.User;

import java.util.List;

public class ProtoUtils {

    public static Protobufs.FlightCompanyRequest createLoginRequest(User user) {
        Protobufs.User userDTO = Protobufs.User.newBuilder().setUsername(user.getUsername()).setPassword(user.getPassword()).build();
        Protobufs.FlightCompanyRequest request = Protobufs.FlightCompanyRequest.newBuilder().setType(Protobufs.FlightCompanyRequest.Type.LOGGED_IN)
                .setUser(userDTO).build();
        return request;
    }

    public static Protobufs.FlightCompanyRequest createLogoutRequest(User user) {
        Protobufs.User userDTO = Protobufs.User.newBuilder().setUsername(user.getUsername()).setPassword(user.getPassword()).build();
        Protobufs.FlightCompanyRequest request = Protobufs.FlightCompanyRequest.newBuilder().setType(Protobufs.FlightCompanyRequest.Type.LOGGED_OUT)
                .setUser(userDTO).build();
        return request;
    }

    public static Protobufs.FlightCompanyRequest createSearchFlightsRequest(Flight flight) {
        return null;
    }
    public static Protobufs.FlightCompanyRequest createBuyTicketRequest(Ticket ticket) {
        return null;
    }

    public static Protobufs.FlightCompanyResponse createOkResponse() {
        Protobufs.FlightCompanyResponse response = Protobufs.FlightCompanyResponse.newBuilder()
                .setType(Protobufs.FlightCompanyResponse.Type.OK).build();
        return response;
    }

    public static Protobufs.FlightCompanyResponse createErrorResponse(String message) {
        Protobufs.FlightCompanyResponse response = Protobufs.FlightCompanyResponse.newBuilder()
                .setType(Protobufs.FlightCompanyResponse.Type.ERROR).build();
        return response;
    }

    public static Protobufs.FlightCompanyResponse createLoggedInResponse(User user) {
        Protobufs.User userDTO = Protobufs.User.newBuilder().setUsername(user.getUsername()).build();

        Protobufs.FlightCompanyResponse response = Protobufs.FlightCompanyResponse.newBuilder()
                .setType(Protobufs.FlightCompanyResponse.Type.LOGIN)
                .setUser(userDTO).build();
        return response;
    }

    public static Protobufs.FlightCompanyResponse createLoggedOutResponse(User user) {
        Protobufs.User userDTO=Protobufs.User.newBuilder().setUsername(user.getUsername()).build();

        Protobufs.FlightCompanyResponse response = Protobufs.FlightCompanyResponse.newBuilder()
                .setType(Protobufs.FlightCompanyResponse.Type.LOGOUT)
                .setUser(userDTO).build();
        return response;
    }

    public static Protobufs.FlightCompanyResponse createSearchFlightsResponse(Flight flight) {
        return null;
    }

    public static Protobufs.FlightCompanyResponse createBuyTicketResponse(Ticket ticket) {
        return null;
    }

    public static String getError(Protobufs.FlightCompanyResponse response) {
        String errorMessage = null;
        return errorMessage;
    }

    public static User getUser(Protobufs.FlightCompanyResponse request) {
        User user = new User();
        user.setUsername(request.getUser().getUsername());
        user.setPassword(request.getUser().getPassword());
        return user;
    }

    public static Flight getFlight(Protobufs.FlightCompanyResponse request) {
        return null;
    }

    public static Ticket getTicket(Protobufs.FlightCompanyResponse request) {
        return null;
    }

    public static User getUsers(Protobufs.FlightCompanyRequest request) {
        return null;
    }

    public static Flight getFlights(Protobufs.FlightCompanyRequest request) {
        return null;
    }

    public static Ticket getTickets(Protobufs.FlightCompanyRequest request) {
        return null;
    }

    public static List<User> getUsers(Protobufs.FlightCompanyResponse response) {
        return null;
    }

    public static List<Flight> getFlights(Protobufs.FlightCompanyResponse response) {
        return null;
    }

    public static List<Ticket> geTickets(Protobufs.FlightCompanyResponse response) {
        return null;
    }

}
