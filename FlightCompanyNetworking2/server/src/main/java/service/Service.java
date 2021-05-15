package service;

import domain.Flight;
import domain.SearchFlightsDTO;
import domain.Ticket;
import domain.User;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {

    private final Repository<Integer, User> userRepository;
    private final Repository<Integer, Flight> flightRepository;
    private final Repository<Integer, Ticket> ticketRepository;

    public Service(Repository<Integer, User> userRepository, Repository<Integer, Flight> flightRepository, Repository<Integer, Ticket> ticketRepository){
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
        return StreamSupport.stream(getAllUsers().spliterator(), false)
                .filter(x -> ((x.getUsername().equals(user.getUsername())) && (x.getPassword().equals(user.getPassword()))))
                .collect(Collectors.toList());
    }

    public List<Flight> searchFlights(SearchFlightsDTO searchFlightsDTO) {
        return StreamSupport.stream(getAllFlights().spliterator(), false)
                .filter(x -> x.getDestination().equals(searchFlightsDTO.getDestination()))
                .filter(x -> x.getDepartureDate().equals(searchFlightsDTO.getDate()))
                .collect(Collectors.toList());
    }

    public Object buyTicket(Ticket ticket){
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

        return null;
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
