package network.rpcprotocol;

import model.Flight;
import model.Ticket;
import model.User;
import network.dto.FlightDTO;
import network.dto.TicketDTO;
import network.dto.UserDTO;
import network.dto.UtilsDTO;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IServices {

    private String host;
    private int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> responseBlockingQueue;
    private volatile boolean finished;

    private IObserver client;
    private IServices server;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responseBlockingQueue = new LinkedBlockingQueue<Response>();
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

    private void handleUpdate(Response response){
        if (response.type() == ResponseType.LOGGED_IN){

            User user = UtilsDTO.getUser((UserDTO)response.data());
            System.out.println("Client logged in " + user);
            try {
                client.userLoggedIn(user);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (response.type() == ResponseType.LOGGED_OUT){
            User user = UtilsDTO.getUser((UserDTO)response.data());
            System.out.println("User logged out: " + user);
            try {
                client.userLoggedOut(user);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (response.type()== ResponseType.SEARCH){
            Flight flight = UtilsDTO.getFlight((FlightDTO)response.data());
            try {
                client.searchFlights(flight.getDestination(), flight.getDepartureDate());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (response.type()== ResponseType.BUY){
            Ticket ticket = UtilsDTO.getTicket((TicketDTO) response.data());
            try {
                client.buyTicket(ticket);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.LOGGED_IN || response.type()== ResponseType.LOGGED_OUT ||
                response.type() == ResponseType.SEARCH || response.type()== ResponseType.BUY;
    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        }
        catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }

    }

    private Response readResponse() throws Exception {
        Response response = null;
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
        UserDTO userDTO = UtilsDTO.getUserDTO(user);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
        //sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK){
            this.client = client;
        }
        if (response.type() == ResponseType.ERROR){
            String error = response.data().toString();
            closeConnection();
            throw new Exception(error);
        }

        List<UserDTO> userLogin = (List<UserDTO>) response.data();
        List<User> users = (List<User>) UtilsDTO.getUserDTO((User) userLogin);
        System.out.println(users);
        return users;
    }

    @Override
    public void logout(User user, IObserver client) throws Exception {
        UserDTO userDTO = UtilsDTO.getUserDTO(user);
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(userDTO).build();
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR){
            String error = response.data().toString();
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
        FlightDTO flightDTO = UtilsDTO.getFlightDTO(flight);
        Request request = new Request.Builder().type(RequestType.SEARCH).data(flightDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK){
            this.client = client;
        }
        if (response.type()== ResponseType.ERROR){
            String error = response.data().toString();
            closeConnection();
            throw new Exception(error);
        }

        List<FlightDTO> flightSearch = (List<FlightDTO>) response.data();
        List<Flight> flights = (List<Flight>) UtilsDTO.getFlight((FlightDTO) flightSearch);
        System.out.println(flights);
        return flights;
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
        TicketDTO ticketDTO = UtilsDTO.getTicketDTO(ticket);
        Request request = new Request.Builder().type(RequestType.BUY).data(ticketDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK){
            this.client = client;
        }
        if (response.type()== ResponseType.ERROR){
            String error = response.data().toString();
            closeConnection();
            throw new Exception(error);
        }

        List<TicketDTO> ticketBuy = (List<TicketDTO>) response.data();
        List<Ticket> tickets = (List<Ticket>) UtilsDTO.getTicketDTO((Ticket) ticketBuy);
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while  (!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }
                    else{

                        try {
                            responseBlockingQueue.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (IOException e) {
                    System.out.println("Reading error "+ e);
                }
                catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+ e);
                }
            }
        }
    }
}
