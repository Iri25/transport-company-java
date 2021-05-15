import controller.LoginController;
import controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.IServices;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static javafx.application.Application.launch;

public class StartClient extends Application {

    private IServices server;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            System.out.println("Start client");

            server = (IServices) factory.getBean("serv");
            System.out.println("Obtained a reference to remote flight company server");

        }
        catch (Exception exception) {
            System.err.println("Flight Company initialization  exception:" + exception);
            exception.printStackTrace();
        }

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

    private void initWindow(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/LoginView.fxml"));
        AnchorPane layout = loader.load();
        primaryStage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setServer(server);

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getClassLoader().getResource("views/MenuView.fxml"));
        AnchorPane layout2 = loader2.load();

        MenuController menuController = loader2.getController();
        menuController.setServer(server);

        loginController.setObserver(menuController);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
