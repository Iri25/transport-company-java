package service;

import domain.Flight;
import domain.Ticket;
import domain.User;

import repository.Repository;
import utils.events.Event;
import utils.observer.Observable;
import utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements Observable {

    private final Repository<Integer, User> userRepository;
    private final Repository<Integer, Flight> flightRepository;
    private final Repository<Integer, Ticket> ticketRepository;

    public Service(Repository<Integer, User> userRepository, Repository<Integer, Flight> flightRepository, Repository<Integer, Ticket> ticketRepository){
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    private List<Observer> observers = new ArrayList<Observer>();

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        observers.stream().forEach(x -> x.update(t));
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

    public List<Flight> searchFlights(String destination, String date) {
        return StreamSupport.stream(getAllFlights().spliterator(), false)
                .filter(x -> x.getDestination().equals(destination))
                .filter(x -> x.getDepartureDate().equals(date))
                .collect(Collectors.toList());
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
