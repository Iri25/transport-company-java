package model;

import java.util.Objects;

public class Ticket extends Entity<Integer> {

    private String clientName;
    private String touristsName;
    private String clientAddress;
    private int numberOfSeats;
    private int idFlight;

    public Ticket(String clientName, String touristsName, String clientAddress, int numberOfSeats, int idFlight){
        this.clientName = clientName;
        this.touristsName = touristsName;
        this.clientAddress = clientAddress;
        this.numberOfSeats = numberOfSeats;
        this.idFlight = idFlight;
    }

    public String getClientName() {

        return clientName;
    }

    public String getTouristsName() {

        return touristsName;
    }

    public String getClientAddress() {

        return clientAddress;
    }

    public int getNumberOfSeats() {

        return numberOfSeats;
    }

    public int getIdFlight() {

        return idFlight;
    }

    public void setClientName(String clientName) {

        this.clientName = clientName;
    }

    public void setTouristsName(String touristsName) {

        this.touristsName = touristsName;
    }

    public void setClientAddress(String clientAddress) {

        this.clientAddress = clientAddress;
    }

    public void setNumberOfSeats(int numberOfSeats) {

        this.numberOfSeats = numberOfSeats;
    }

    public void setFlight(int idFlight) {

        this.idFlight = idFlight;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + getId() +
                ", clientName='" + clientName + '\'' +
                ", touristsName='" + touristsName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                ", idFlight=" + idFlight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return numberOfSeats == ticket.numberOfSeats && Objects.equals(getId(), ticket.getId()) && Objects.equals(clientName, ticket.clientName) && Objects.equals(touristsName, ticket.touristsName) && Objects.equals(clientAddress, ticket.clientAddress) && Objects.equals(idFlight, ticket.idFlight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), clientName, touristsName, clientAddress, numberOfSeats, idFlight);
    }
}
