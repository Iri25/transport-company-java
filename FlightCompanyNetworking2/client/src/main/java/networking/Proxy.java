package networking;

import controller.MenuController;
import domain.Flight;
import domain.SearchFlightsDTO;
import domain.Ticket;
import domain.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Proxy {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private BlockingQueue<Object> objects = new LinkedBlockingQueue<>();
    private volatile boolean finished;
    private MenuController menuController;

    public Proxy(String localhost, int port) throws IOException {
        socket = new Socket(localhost, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        Thread thread = new Thread(new Reader());
        thread.start();
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    public User login(User user) throws IOException, InterruptedException {
        Request request = new LoginRequest(user.getUsername(), user.getPassword());
        outputStream.writeObject(request);
        outputStream.flush();
        return (User) objects.take();
    }

    public Iterable<Flight> getAllFlights() throws IOException, InterruptedException {
        Request request = new GetAllFlightsRequest();
        outputStream.writeObject(request);
        outputStream.flush();
        return (Iterable<Flight>) objects.take();
    }

    public List<Flight> searchFlights(SearchFlightsDTO searchFlightsDTO) throws IOException, InterruptedException {
        Request request = new SearchFlightsRequest(searchFlightsDTO);
        outputStream.writeObject(request);
        outputStream.flush();
        return (List<Flight>) objects.take();
    }

    public int getSeatsAvailable(Flight flight) throws IOException, InterruptedException {
        Request request = new GetSeatsAvailableRequest(flight);
        outputStream.writeObject(request);
        outputStream.flush();
        return (int) objects.take();
    }

    public int getSeatsAvailableUpdate(Flight flight) throws IOException, InterruptedException {
        Request request = new GetSeatsAvailableUpdateRequest(flight);
        outputStream.writeObject(request);
        outputStream.flush();
        return (int) objects.take();
    }

    public void buyTicket(Ticket ticket) throws IOException, InterruptedException {
        Request request = new BuyTicketRequest(ticket);
        outputStream.writeObject(request);
        outputStream.flush();
    }

    private class Reader implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    Object object = inputStream.readObject();
                    if (object instanceof Response && ((Response) object).type.equals("update")) {
                       // menuController.handleLogout(((Response) object).data);
                    }
                    else objects.put(object);
                } catch (IOException | InterruptedException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
