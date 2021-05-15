import controller.LoginController;

import repository.jdbc.FlightJdbcRepository;
import repository.jdbc.TicketJdbcRepository;
import repository.jdbc.UserJdbcRepository;
import service.Service;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainFX extends Application {

    Service service;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties properties = new Properties();
        try {
            properties.load(new FileReader("app.config"));
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

        UserJdbcRepository userJdbcRepository = new UserJdbcRepository(properties);
        FlightJdbcRepository flightJdbcRepository = new FlightJdbcRepository(properties);
        TicketJdbcRepository ticketJdbcRepository = new TicketJdbcRepository(properties);

        service = new Service(userJdbcRepository, flightJdbcRepository, ticketJdbcRepository);

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
        loader.setLocation(getClass().getResource("views/LoginView.fxml"));
        AnchorPane layout = loader.load();
        primaryStage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setService(service);
    }

    public static void main(String[] args) {
        launch(args);
    }
}



