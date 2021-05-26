package client;

import client.restclient.Client;
import domain.Flight;

public class MainRestClient {

    public static void main(String[] args) throws Exception {
        Client client = new Client();

        System.out.println();
        System.out.println("Caut zborul cu ID-ul 1");
        Flight flightById = client.findOne(1);
        System.out.println("Am gasit! ");
        System.out.println("Zborul este: " + flightById.getId() + " " + flightById.getDestination() + " " +
                flightById.getDepartureDate() + " " + flightById.getDepartureTime() + " " + flightById.getAirport() +
                        " " + flightById.getNumberOfSeats());
        System.out.println();

        System.out.println("Afisez toate zborurile");
        for (Flight flight : client.findAll()) {
            System.out.println(flight.getId() + " " + flight.getDestination() + " " + flight.getDepartureDate() + " " +
                    flight.getDepartureTime() + " " + flight.getAirport() + " " + flight.getNumberOfSeats());
        }
        System.out.println();

        System.out.println("Adaug un zbor");
        Flight flightAdd = client.save(new Flight(8, "Timisoara", "2021-05-19", "21:00",
                "International", 40));
        System.out.println("Am adaugat un zbor cu ID-ul 8! ");
        System.out.println("Zborul este: " +
                flightAdd.getId() + " " + flightAdd.getDestination() + " " + flightAdd.getDepartureDate() + " " +
                flightAdd.getDepartureTime() + " " + flightAdd.getAirport() + " " + flightAdd.getNumberOfSeats());
        System.out.println();

        System.out.println("Afisez toate zborurile");
        for (Flight flight : client.findAll()) {
            System.out.println(flight.getId() + " " + flight.getDestination() + " " + flight.getDepartureDate() + " " +
                    flight.getDepartureTime() + " " + flight.getAirport() + " " + flight.getNumberOfSeats());
        }
        System.out.println();

        System.out.println("Actualizez un zbor");
        Flight flightUpdate = client.update(new Flight(6, "Timisoara", "2021-05-18", "11:00",
                "International", 50));
        System.out.println("Am actualizat zborul cu ID-ul 6! ");
        System.out.println("Zborul este: " + flightUpdate.getId() + " " + flightUpdate.getDestination() + " " +
                flightUpdate.getDepartureDate() + " " + flightUpdate.getDepartureTime() + " " + flightUpdate.getAirport() + " " +
                flightUpdate.getNumberOfSeats());
        System.out.println();

        System.out.println("Afisez toate zborurile");
        for (Flight flight : client.findAll()) {
            System.out.println(flight.getId() + " " + flight.getDestination() + " " + flight.getDepartureDate() + " " +
                    flight.getDepartureTime() + " " + flight.getAirport() + " " + flight.getNumberOfSeats());
        }
        System.out.println();

        System.out.println("Sterg un zbor");
        client.delete(8);
        System.out.println("Am sters zborul cu ID-ul 8!");
        System.out.println();

        System.out.println("Afisez toate zborurile");
        for (Flight flight : client.findAll()) {
            System.out.println(flight.getId() + " " + flight.getDestination() + " " + flight.getDepartureDate() + " " +
                    flight.getDepartureTime() + " " + flight.getAirport() + " " + flight.getNumberOfSeats());
        }
        System.out.println();
    }
}
