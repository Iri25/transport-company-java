package network.dto;

import model.Ticket;

import java.io.Serializable;
import java.util.Objects;

public class TicketDTO implements Serializable {
    private String clientName;
    private String touristsName;
    private String clientAddress;
    private int numberOfSeats;
    private int idFlight;

    public TicketDTO(String clientName, String touristsName, String clientAddress, int numberOfSeats, int idFlight){
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

}
