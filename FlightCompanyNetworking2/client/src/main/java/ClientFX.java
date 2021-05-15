import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import networking.Proxy;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ClientFX extends Application {

    Proxy proxy;

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {

        proxy = new Proxy("localhost", 55555);

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
        loginController.setProxy(proxy);
    }

    public static void main(String[] args) {
        launch(args);
    }
}



