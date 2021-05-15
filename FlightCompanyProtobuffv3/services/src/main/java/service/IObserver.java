package service;

import domain.Ticket;
import domain.User;

public interface IObserver {

    void userLoggedIn(User user) throws Exception;

    void userLoggedOut(User user);

    void searchFlights(String destination, String departureDate);

    void buyTicket(Ticket ticket);
}
