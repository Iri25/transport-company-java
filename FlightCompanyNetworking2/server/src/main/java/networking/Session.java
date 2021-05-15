package networking;

import domain.Flight;
import domain.SearchFlightsDTO;
import domain.Ticket;
import domain.User;
import service.Service;
import utils.events.Event;
import utils.observer.Observer;

import javax.print.attribute.standard.RequestingUserName;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Session implements Observer, Runnable {

    private Server server;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Service service;

    public Session(Server server, Socket socket, Service service) throws IOException{
        this.server = server;
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        this.service = service;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = (Request) inputStream.readObject();

                if (request.getType().equals("login")) {
                    LoginRequest loginRequest = (LoginRequest) request;
                    User user = (User) loginRequest.data;
                    outputStream.writeObject(service.Login(user));
                    outputStream.flush();
                }

                if (request.getType().equals("GetAllFlightsRequest")) {
                    outputStream.writeObject(service.getAllFlights());
                    outputStream.flush();
                }

                if (request.getType().equals("SearchFlightsRequest")) {
                    SearchFlightsDTO searchFlights = (SearchFlightsDTO) request;
                    outputStream.writeObject(service.searchFlights(searchFlights));
                    outputStream.flush();
                }

                if (request.getType().equals("GetSeatsAvailableRequest")) {
                    Flight flight = (Flight) request;
                    outputStream.writeObject(service.getSeatsAvailable(flight));
                    outputStream.flush();
                }

                if (request.getType().equals("GetSeatsAvailableUpdateRequest")) {
                    Flight flight = (Flight) request;
                    outputStream.writeObject(service.getSeatsAvailableUpdate(flight));
                    outputStream.flush();
                }

                if (request.getType().equals("BuyTicketRequest")) {
                    Ticket ticket = (Ticket) request;
                    outputStream.writeObject(service.buyTicket(ticket));
                    outputStream.flush();
                }

                if (request.getType().equals("notifyServer")) {
                    server.notifyObservers();
                }
            }
        }
        catch (IOException e) {
        e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        try {
            Response response = new Response("update", service.getAllFlights());
            outputStream.writeObject(response);
            outputStream.flush();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
