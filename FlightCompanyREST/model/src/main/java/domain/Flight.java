package domain;

import java.util.Objects;

public class Flight extends Entity<Integer> {

    private String destination;
    private String departureDate;
    private String departureTime;
    private String airport;
    private int numberOfSeats;
    private int numberOfSeatsAvailable;

    public Flight() {
    }

    public Flight(String destination, String departureDate, String departureTime, String airport, int numberOfSeats){
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.airport = airport;
        this.numberOfSeats = numberOfSeats;
        this.numberOfSeatsAvailable = numberOfSeats;
    }

    public Flight(Integer id, String destination, String departureDate, String departureTime, String airport, int numberOfSeats){
        super(id);
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
    public int getNumberOfSeatsAvailable(){ return numberOfSeatsAvailable; }

    public void setDestination(String destination) {

        this.destination = destination;
    }

    public void setDepartureDate(String departureDate) {

        this.departureDate = departureDate;
    }

    public void setDepartureTime(String departureTime) {

        this.departureTime = departureTime;
    }

    public void setAirport(String airport) {

        this.airport = airport;
    }

    public void setNumberOfSeats(int numberOfSeats) {

        this.numberOfSeats = numberOfSeats;
    }

    public void setNumberOfSeatsAvailable(int numberOfSeats) {

        this.numberOfSeatsAvailable = numberOfSeats;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + getId() +
                ", destination='" + destination + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", airport='" + airport + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return numberOfSeats == flight.numberOfSeats && Objects.equals(getId(), flight.getId()) && Objects.equals(destination, flight.destination) && Objects.equals(departureDate, flight.departureDate) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(airport, flight.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), destination, departureDate, departureTime, airport, numberOfSeats);
    }

    public String getIdString() {
        return super.getId().toString(); }

    public String getDestinationString() {
        return "" + destination; }

    public String getDepartureDateString() {
        return "" + departureDate; }

    public String getDepartureTimeString() {
        return "" + departureTime; }

    public String getAirportString() {
        return "" + airport; }

    public String getNumberOfSeatsString() {
        return"" + numberOfSeats; }

    public String getNumberOfSeatsAvailableString() {
        return"" + numberOfSeatsAvailable; }
}
