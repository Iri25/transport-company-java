package networking;

import service.Service;
import utils.events.Event;
import utils.observer.Observable;
import utils.observer.Observer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Observable {

    private String localhost;
    private int port;
    private ServerSocket serverSocket = null;
    private Service service;
    public List<Observer> observerList = new ArrayList<Observer>();


    public Server(String localhost, int port, Service service) {
        this.localhost = localhost;
        this.port = port;
        this.service = service;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started! Awaiting incoming connections...");

        while(true){
            Socket socket = serverSocket.accept();
            Session session = new Session(this, socket, service);
            addObserver(session);
            Thread thread = new Thread(session);
            thread.start();
        }
    }

    @Override
    public void addObserver(Observer e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observerList.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        for (Observer observer : observerList) {
            observer.update();
        }
    }

    public void notifyObservers() {
    }
}
