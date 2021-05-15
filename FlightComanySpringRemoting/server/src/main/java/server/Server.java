package server;

import domain.Flight;
import domain.Ticket;
import domain.User;
import repository.FlightRepository;
import repository.TicketRepository;
import repository.UserRepository;
import repository.jdbc.FlightJdbcRepository;
import repository.jdbc.TicketJdbcRepository;
import repository.jdbc.UserJdbcRepository;
import service.IObserver;
import service.IServices;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IServices {

    private final UserJdbcRepository userRepository;
    private final FlightJdbcRepository flightRepository;
    private final TicketJdbcRepository ticketRepository;

    private final Integer defaultThreadsCount = 5;
    private final Map<Integer, IObserver> loggedClients;


    public Server(UserJdbcRepository userRepository, FlightJdbcRepository flightRepository, TicketJdbcRepository ticketRepository) {
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        loggedClients = new HashMap<>();
    }

    @Override
    public synchronized List<User> login(User user, IObserver client) throws Exception {

        ArrayList<User> login = new ArrayList<>();

        User userLogin = userRepository.findUsernamePassword(user.getUsername(), user.getPassword());
        if(userLogin != null) {
            System.out.println(userLogin.getId());
            if (loggedClients.get(userLogin.getId()) != null)
                throw new Exception("User already logged in!");
            loggedClients.put(userLogin.getId(), client);
            login.add(userLogin);
        }
        else
            throw new Exception("Authentication failed.");
        return login;

    }

    @Override
    public synchronized void logout(User user, IObserver client) throws Exception {

        if (!loggedClients.containsKey(user.getId()))
            throw new Exception("User already logout!");

        User userLogin = userRepository.findOne(user.getId());
        IObserver localClient = loggedClients.remove(user.getId());
        if (localClient == null)
            throw new Exception("User " + user.getUsername() + " is not logged in.");
    }

    @Override
    public synchronized Iterable<Flight> getAllFlights() {

        return flightRepository.findAll();
    }

    @Override
    public synchronized List<Flight> searchFlights(String destination, String date) {
        List<Flight> flights = new ArrayList<>();
        for (Flight flight : flightRepository.findAll()) {
            if (flight.getDestination().equals(destination) && flight.getDepartureDate().equals(date))
                    flights.add(flight);
        }
        return flights;
    }

    @Override
    public synchronized void buyTicket(Ticket ticket) {
        Integer freeId = 0;
        for(Ticket ticketId: ticketRepository.findAll()) {
            freeId++;
            if (!freeId.equals(ticketId.getId())) {
                break;
            }
        }
        freeId++;

        Iterable<Ticket> empty = new ArrayList<>();
        if(ticketRepository.findAll().equals(empty))
            freeId++;

        Ticket ticketBuy = new Ticket(ticket.getClientName(), ticket.getTouristsName(), ticket.getClientAddress(), ticket.getNumberOfSeats(), ticket.getIdFlight());
        ticketBuy.setId(freeId);
        ticketRepository.save(ticketBuy);
        notifyBuyTicket(ticketBuy);
    }

    @Override
    public synchronized int getSeatsAvailable(Flight flight) {

        Iterable<Ticket> tickets = ticketRepository.findAll();
        int seatUnavailable = 0;
        for (Ticket ticket:tickets) {
            if(ticket.getIdFlight() == flight.getId())
                seatUnavailable = seatUnavailable + ticket.getNumberOfSeats();
        }
        flight.setNumberOfSeatsAvailable(flight.getNumberOfSeatsAvailable() - seatUnavailable);
        return flight.getNumberOfSeatsAvailable();
    }

    @Override
    public synchronized int getSeatsAvailableUpdate(Flight flight) {
        int seatUnavailable = 0;
        Iterable<Ticket> tickets = ticketRepository.findAll();
        for (Ticket ticket:tickets) {
            if(ticket.getIdFlight() == flight.getId())

                seatUnavailable = seatUnavailable + ticket.getNumberOfSeats();
        }
        flight.setNumberOfSeatsAvailable(flight.getNumberOfSeatsAvailable() - seatUnavailable);
        return flight.getNumberOfSeatsAvailable();
    }

    private void notifyBuyTicket(Ticket ticket) {

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsCount);
        for (User user : userRepository.findAll()){
            IObserver observer = loggedClients.get(user.getId());
            if(observer != null)
                executor.execute(() -> {
                    try {
                        observer.buyTicket(ticket);
                    }
                    catch (Exception exception) {
                        try {
                            throw new Exception("Unable to notify tickets sold ", exception);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        }
        executor.shutdown();
    }
}
