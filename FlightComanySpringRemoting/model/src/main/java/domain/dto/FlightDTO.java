package domain.dto;

import java.io.Serializable;

public class FlightDTO implements Serializable {

    private String destination;
    private String departureDate;
    private String departureTime;
    private String airport;
    private int numberOfSeats;
    private int numberOfSeatsAvailable;

    public  FlightDTO (String destination, String departureDate, String departureTime, String airport, int numberOfSeats){
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.airport = airport;
        this.numberOfSeats = numberOfSeats;
        this.numberOfSeatsAvailable = numberOfSeats;
    }

    public String getDestination() {

        return destination;
    }

    public String getDepartureDate() {

        return departureDate;
    }

    public String getDepartureTime() {

        return departureTime;
    }

    public String getAirport() {

        return airport;
    }

    public int getNumberOfSeats() {

        return numberOfSeats;
    }
    public int getNumberOfSeatsAvailable(){
        return numberOfSeatsAvailable;
    }

    public void setNumberOfSeats(int numberOfSeats) {

        this.numberOfSeats = numberOfSeats;
    }

    public void setNumberOfSeatsAvailable(int numberOfSeats) {

        this.numberOfSeatsAvailable = numberOfSeats;
    }

}
