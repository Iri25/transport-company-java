package network.utils;

import network.protobuffprotocol.ProtoWorker;
import service.IServices;

import java.net.Socket;

public class ProtobuffConcurrentServer extends AbsConcurrentServer {
    private IServices server;

    public ProtobuffConcurrentServer(int port, IServices server) {
        super(port);
        this.server = server;
        System.out.println("ProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoWorker worker = new ProtoWorker(server, client);

        Thread thread = new Thread(worker);
        return thread;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
