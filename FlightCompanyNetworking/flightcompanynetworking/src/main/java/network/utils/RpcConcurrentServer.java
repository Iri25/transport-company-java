package network.utils;

import network.rpcprotocol.ClientRpcReflectionWorker;
import services.IServices;

import java.net.Socket;

public class RpcConcurrentServer extends AbsConcurrentServer {
    private IServices chatServer;

    public RpcConcurrentServer(int port, IServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ClientRpcWorker worker = new ChatClientRpcWorker(chatServer, client);
        ClientRpcReflectionWorker worker = new ClientRpcReflectionWorker(chatServer, client);

        Thread thread = new Thread(worker);
        return thread;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
