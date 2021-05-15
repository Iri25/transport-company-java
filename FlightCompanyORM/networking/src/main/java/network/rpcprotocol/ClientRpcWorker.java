package network.rpcprotocol;

import domain.Flight;
import domain.Ticket;
import domain.User;
import domain.dto.FlightDTO;
import domain.dto.TicketDTO;
import domain.dto.UserDTO;
import domain.dto.UtilsDTO;
import service.IObserver;
import service.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientRpcWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    public ClientRpcWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.type() == RequestType.LOGIN) {
            System.out.println("Login request ..." + request.type());
            UserDTO userDTO = (UserDTO) request.data();
            User user = UtilsDTO.getUser(userDTO);
            try {
                server.login(user, this);
                return okResponse;
            }
            catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            System.out.println("Logout request ");
            //LogoutRequest logReq = (LogoutRequest)request;
            UserDTO userDTO = (UserDTO) request.data();
            User user = UtilsDTO.getUser(userDTO);
            try {
                server.logout(user, this);
                connected = false;
                return okResponse;

            }
            catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.SEARCH){
            System.out.println("Search request ");
            FlightDTO flightDTO = (FlightDTO) request.data();
            Flight flight = UtilsDTO.getFlight(flightDTO);
            try {
                server.searchFlights(flight.getDestination(), flight.getDepartureDate());
                return okResponse;
            }
            catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.BUY){
            System.out.println("Buy Request ");
            TicketDTO ticketDTO = (TicketDTO) request.data();
            Ticket ticket = UtilsDTO.getTicket(ticketDTO);
            try {
                server.buyTicket(ticket);
                return okResponse;
            }
            catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("Sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void run() {

        while(connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request)request);
                if (response != null){
                    sendResponse(response);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        }
        catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public void userLoggedIn(User user) {
        UserDTO userDTO = UtilsDTO.getUserDTO(user);
        Response response = new Response.Builder().type(ResponseType.LOGGED_IN).data(userDTO).build();
        System.out.println("User logged in " + user);
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userLoggedOut(User user) {
        UserDTO userDTO = UtilsDTO.getUserDTO(user);
        Response response = new Response.Builder().type(ResponseType.LOGGED_OUT).data(userDTO).build();
        System.out.println("User logged out " + user);
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchFlights(String destination, String date) {
        Flight flight = new Flight(destination, date, "", "", 0);
        FlightDTO flightDTO = UtilsDTO.getFlightDTO(flight);
        Response response = new Response.Builder().type(ResponseType.SEARCH).data(flightDTO).build();
        System.out.println("Search flight" + flight);
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void buyTicket(Ticket ticket) {
        TicketDTO ticketDTO = UtilsDTO.getTicketDTO(ticket);
        Response response = new Response.Builder().type(ResponseType.BUY).data(ticketDTO).build();
        System.out.println("User logged out " + ticket);
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
