package controller;

import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import networking.Proxy;
import utils.events.Event;

import java.io.IOException;



public class LoginController {

    private Proxy proxy;
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    public void handleLogin(ActionEvent actionEvent) throws IOException, InterruptedException {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        User userLogin = new User(username, password);

        if ((username.isEmpty()) || password.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty filed!\n");
        else {
            User login = proxy.login(userLogin);
            if (login == null)
                MessageAlert.showErrorMessage(null, "Username or Password invalid!\n");
            else {
                Stage stage = new Stage();
                stage.setTitle(userLogin.getUsername());

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/MenuView.fxml"));
                AnchorPane layout = loader.load();
                stage.setScene(new Scene(layout));

                MenuController menuController = loader.getController();
                menuController.setService(proxy, userLogin);
                stage.show();
            }
        }
    }

}
