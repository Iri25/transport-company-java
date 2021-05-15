package service;

import domain.Flight;
import domain.Ticket;
import domain.User;
import repository.jdbc.FlightJdbcRepository;
import repository.jdbc.TicketJdbcRepository;
import repository.jdbc.UserJdbcRepository;
import repository.jdbc.hbm.UserJdbcHbmRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service {

    private final UserJdbcHbmRepository userRepository;
    private final FlightJdbcRepository flightRepository;
    private final TicketJdbcRepository ticketRepository;


    public Service(UserJdbcHbmRepository userRepository, FlightJdbcRepository flightRepository, TicketJdbcRepository ticketRepository) {
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Iterable<Flight> getAllFlights(){
        return flightRepository.findAll();
    }

    public Iterable<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    public List<User> Login(User user) {
        ArrayList<User> login = new ArrayList<>();

        for (User userLogin : getAllUsers()) {
            if(userLogin.getUsername().equals(user.getUsername()) && userLogin.getPassword().equals(user.getPassword()))
                login.add(userLogin);
        }
        return login;
    }

    public List<Flight> searchFlights(String destination, String date) {
        ArrayList<Flight> flights = new ArrayList<>();

        for (Flight flight : getAllFlights()) {
            if(flight.getDestination().equals(destination) && flight.getDepartureDate().equals(date))
                flights.add(flight);
        }
        return flights;
    }

    public void buyTicket(Ticket ticket){
        Integer freeId = 0;
        for(Ticket ticketId: getAllTickets()) {
            freeId++;
            if (!freeId.equals(ticketId.getId())) {
                break;
            }
        }
        freeId++;

        Iterable<Ticket> empty = new ArrayList<>();
        if(getAllTickets() == empty)
            freeId++;

        Ticket ticketBuy = new Ticket(ticket.getClientName(), ticket.getTouristsName(), ticket.getClientAddress(), ticket.getNumberOfSeats(), ticket.getIdFlight());
        ticketBuy.setId(freeId);
        ticketRepository.save(ticketBuy);

    }

    public Integer getSeatsAvailable(Flight flight) {
        Iterable<Ticket> tickets = getAllTickets();
        int seatUnavailable = 0;
        for (Ticket ticket:tickets) {
            if(ticket.getIdFlight() == flight.getId())
                seatUnavailable ++;
        }
        flight.setNumberOfSeatsAvailable(flight.getNumberOfSeatsAvailable() - seatUnavailable);
        return flight.getNumberOfSeatsAvailable();
    }

    public Integer getSeatsAvailableUpdate(Flight flight) {
        flight.setNumberOfSeatsAvailable(flight.getNumberOfSeatsAvailable() - 1);
        return flight.getNumberOfSeatsAvailable();
    }


}
