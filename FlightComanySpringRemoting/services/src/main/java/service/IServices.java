package service;

import domain.Flight;
import domain.Ticket;
import domain.User;

import java.util.List;

public interface IServices {

    List<User> login(User user, IObserver client) throws Exception;

    void logout(User user, IObserver client) throws Exception;

    Iterable<Flight> getAllFlights() throws Exception;

    List<Flight> searchFlights(String destination, String date) throws Exception;

    int getSeatsAvailable(Flight flight);

    int getSeatsAvailableUpdate(Flight flight);

    void buyTicket(Ticket ticket) throws Exception;
}
