package client;

import client.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.rpcprotocol.ServicesRpcProxy;
import service.IServices;

import java.io.IOException;
import java.util.Properties;

public class MainFXClient extends Application {

    private IServices server;
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {

        System.out.println("Start clients");
        Properties properties = new Properties();
        try {
            properties.load(MainFXClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
        }
        catch (IOException ioException) {
            System.err.println("Cannot find client.properties " + ioException);
            return;
        }

        String serverIP = properties.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(properties.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        server = new ServicesRpcProxy(serverIP, serverPort);

        Stage stage = new Stage();
        try {
            initWindow(stage);
            stage.setTitle("Login");
            stage.show();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void initWindow(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/LoginView.fxml"));
        AnchorPane layout = loader.load();
        primaryStage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setServer(server);
    }

    public static void main(String[] args) {
        launch(args);
    }
}



