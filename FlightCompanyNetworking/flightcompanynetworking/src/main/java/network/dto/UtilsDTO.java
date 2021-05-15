package network.dto;

import model.Flight;
import model.Ticket;
import model.User;

public class UtilsDTO {

    public static User getUser(UserDTO user){
        String username = user.getUsername();
        String password = user.getPassword();
        return new User(username, password);
    }

    public static UserDTO getUserDTO(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        return new UserDTO(username, password);
    }

    public static Flight getFlight(FlightDTO flight){
        String destination = flight.getDestination();
        String departureDate = flight.getDepartureDate();
        String departureTime = flight.getDepartureTime();
        String airport = flight.getAirport();
        int numberOfSeats = flight.getNumberOfSeats();
        return new Flight(destination, departureDate, departureTime, airport, numberOfSeats);
    }

    public static FlightDTO getFlightDTO(Flight flight){
        String destination = flight.getDestination();
        String departureDate = flight.getDepartureDate();
        String departureTime = flight.getDepartureTime();
        String airport = flight.getAirport();
        int numberOfSeats = flight.getNumberOfSeats();
        return new FlightDTO(destination, departureDate, departureTime, airport, numberOfSeats);
    }

    public static Ticket getTicket(TicketDTO ticket){
        String clientName = ticket.getClientName();
        String clientTourists = ticket.getTouristsName();
        String clientAddress = ticket.getClientAddress();
        int numberOfSeats = ticket.getNumberOfSeats();
        int idFlight = ticket.getIdFlight();
        return new Ticket(clientName, clientTourists, clientAddress, numberOfSeats, idFlight);
    }

    public static TicketDTO getTicketDTO(Ticket ticket){
        String clientName = ticket.getClientName();
        String clientTourists = ticket.getTouristsName();
        String clientAddress = ticket.getClientAddress();
        int numberOfSeats = ticket.getNumberOfSeats();
        int idFlight = ticket.getIdFlight();
        return new TicketDTO(clientName, clientTourists, clientAddress, numberOfSeats, idFlight);
    }
}
