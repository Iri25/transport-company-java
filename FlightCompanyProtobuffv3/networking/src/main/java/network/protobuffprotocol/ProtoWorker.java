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

public class ProtoWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private volatile boolean connected;

    public ProtoWorker(IServices server, Socket connection) {
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

    private Protobufs.FlightCompanyResponse handleRequest(Protobufs.FlightCompanyRequest request) {
        Protobufs.FlightCompanyResponse response = null;
        switch (request.getType()) {
            case LOGGED_IN: {
                System.out.println("Login request ...");
                User user = ProtoUtils.getUsers(request);
                try {
                    server.login(user, this);
                }
                catch (Exception e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case LOGGED_OUT: {
                System.out.println("Logout request ...");
                User user = ProtoUtils.getUsers(request);
                try {
                    server.logout(user, this);
                    connected = false;
                    return ProtoUtils.createOkResponse();
                }
                catch (Exception e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case SEARCH: {
                System.out.println("Search request ...");
                Flight flight = ProtoUtils.getFlights(request);
                try {
                    server.searchFlights(flight.getDestination(), flight.getDepartureDate());
                } catch (Exception e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case BUY: {
                System.out.println("Buy request ...");
                Ticket ticket = ProtoUtils.getTickets(request);
                try {
                    server.buyTicket(ticket);
                } catch (Exception e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
        }
        return response;
    }


    private void sendResponse(Protobufs.FlightCompanyResponse response) throws IOException{
        System.out.println("Sending response " + response);
        response.writeDelimitedTo(output);
        output.flush();
    }

    @Override
    public void run() {

        while(connected) {
            try {
                System.out.println("Waiting requests ...");
                Protobufs.FlightCompanyRequest request = Protobufs.FlightCompanyRequest.parseDelimitedFrom(input);
                System.out.println("Request received: " + request);
                Protobufs.FlightCompanyResponse response = handleRequest(request);
                if (response != null){
                    sendResponse(response);
                }
            }
            catch (IOException e) {
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
        System.out.println("User logged in " + user);
        try {
            sendResponse(ProtoUtils.createLoggedInResponse(user));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userLoggedOut(User user) {
        System.out.println("User logged out " + user);
        try {
            sendResponse(ProtoUtils.createLoggedOutResponse(user));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchFlights(String destination, String date) {
        Flight flight = new Flight(destination, date, "", "", 0);
        System.out.println("Search flight" + flight);
        try {
            sendResponse(ProtoUtils.createSearchFlightsResponse(flight));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void buyTicket(Ticket ticket) {
        System.out.println("User logged out " + ticket);
        try {
            sendResponse(ProtoUtils.createBuyTicketResponse(ticket));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
