package network.protobuffprotocol;

import domain.Flight;
import domain.Ticket;
import domain.User;
import service.IObserver;
import service.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements IServices {

    private String host;
    private int port;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket connection;
    private BlockingQueue<Protobufs.FlightCompanyResponse> responseBlockingQueue;

    private volatile boolean finished;

    private IObserver client;
    private IServices server;

    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responseBlockingQueue = new LinkedBlockingQueue<Protobufs.FlightCompanyResponse>();
    }

    private void startReader(){
        Thread thread = new Thread(new ReaderThread());
        thread.start();
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleUpdate(Protobufs.FlightCompanyResponse updateResponse){

        switch (updateResponse.getType()) {
            case LOGIN: {
                User user = ProtoUtils.getUser(updateResponse);
                System.out.println("Client logged in " + user);
                try {
                    client.userLoggedIn(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case LOGOUT: {
                User user = ProtoUtils.getUser(updateResponse);
                System.out.println("User logged out: " + user);
                try {
                    client.userLoggedOut(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case SEARCH: {
                Flight flight = ProtoUtils.getFlight(updateResponse);
                try {
                    client.searchFlights(flight.getDestination(), flight.getDepartureDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case BUY: {
                Ticket ticket = ProtoUtils.getTicket(updateResponse);
                try {
                    client.buyTicket(ticket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private boolean isUpdate(Protobufs.FlightCompanyResponse.Type type) {
        switch (type) {
            case LOGIN: return true;
            case LOGOUT: return true;
            case SEARCH: return true;
            case BUY: return true;
        }
        return  false;
    }

    private void sendRequest(Protobufs.FlightCompanyRequest request) throws Exception {
        try {
            request.writeDelimitedTo(output);
            output.flush();
        }
        catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }
    }

    private Protobufs.FlightCompanyResponse readResponse() {
        Protobufs.FlightCompanyResponse response = null;
        try{
            response = responseBlockingQueue.take();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<User> login(User user, IObserver client) throws Exception {
        initializeConnection();
        sendRequest(ProtoUtils.createLoginRequest(user));
        Protobufs.FlightCompanyResponse response = readResponse();
        if (response.getType() == Protobufs.FlightCompanyResponse.Type.OK){
            this.client = client;
        }
        if (response.getType() == Protobufs.FlightCompanyResponse.Type.ERROR){
            String error = ProtoUtils.getError(response);
            closeConnection();
            throw new Exception(error);
        }

        List<User> userLogin = ProtoUtils.getUsers(response);
        return userLogin;
    }

    @Override
    public void logout(User user, IObserver client) throws Exception {
        sendRequest(ProtoUtils.createLogoutRequest(user));
        Protobufs.FlightCompanyResponse response = readResponse();
        closeConnection();
        if (response.getType() == Protobufs.FlightCompanyResponse.Type.ERROR){
            String error = ProtoUtils.getError(response);
            throw new Exception(error);
        }
    }

    @Override
    public Iterable<Flight> getAllFlights() throws Exception {
        return server.getAllFlights();
    }

    @Override
    public List<Flight> searchFlights(String destination, String date) throws Exception {
        initializeConnection();
        Flight flight = new Flight(destination, date, "", "", 0);
        sendRequest(ProtoUtils.createSearchFlightsRequest(flight));
        Protobufs.FlightCompanyResponse response = readResponse();
        if (response.getType() == Protobufs.FlightCompanyResponse.Type.OK){
            this.client = client;
        }
        if (response.getType() == Protobufs.FlightCompanyResponse.Type.ERROR){
            String error = ProtoUtils.getError(response);
            throw new Exception(error);
        }

        List<Flight> flightSearch = ProtoUtils.getFlights(response);
        return flightSearch;
    }

    @Override
    public int getSeatsAvailable(Flight flight) {
        return server.getSeatsAvailable(flight);
    }

    @Override
    public int getSeatsAvailableUpdate(Flight flight) {
        return server.getSeatsAvailableUpdate(flight);
    }

    @Override
    public void buyTicket(Ticket ticket) throws Exception {
        initializeConnection();
        sendRequest(ProtoUtils.createBuyTicketRequest(ticket));
        Protobufs.FlightCompanyResponse response = readResponse();
        if (response.getType() == Protobufs.FlightCompanyResponse.Type.OK){
            this.client = client;
        }
        if (response.getType()== Protobufs.FlightCompanyResponse.Type.ERROR){
            String error = ProtoUtils.getError(response);
            throw new Exception(error);
        }

        List<Ticket> ticketBuy = ProtoUtils.geTickets(response);
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while  (!finished){
                try {
                    Protobufs.FlightCompanyResponse response = Protobufs.FlightCompanyResponse.parseDelimitedFrom(input);
                    if (isUpdate(response.getType())) {
                        handleUpdate(response);
                    }
                    else {
                            try {
                                responseBlockingQueue.put(response);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                }
                catch (IOException e){
                        System.out.println("Reading error " + e);
                }
            }
        }
    }
}
