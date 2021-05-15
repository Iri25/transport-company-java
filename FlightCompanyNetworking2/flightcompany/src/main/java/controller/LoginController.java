package controller;

import domain.User;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import service.Service;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import utils.events.Event;
import utils.observer.Observer;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;


public class LoginController implements Observer {


    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @Override
    public void update(Event e) {
    }

    Service service;

    public void setService(Service service) {
        this.service = service;

        service.addObserver(this);
    }


    @FXML
    public void handleLogin(ActionEvent actionEvent) throws IOException {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        User userLogin = new User(username, password);

        if ((username.isEmpty()) || password.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty filed!\n");
        else {
            List<User> login = service.Login(userLogin);
            if (login.isEmpty())
                MessageAlert.showErrorMessage(null, "Username or Password invalid!\n");
            else {
                Stage stage = new Stage();
                stage.setTitle(userLogin.getUsername());

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/MenuView.fxml"));
                AnchorPane layout = loader.load();
                stage.setScene(new Scene(layout));

                MenuController menuController = loader.getController();
                menuController.setService(service, userLogin);
                stage.show();
            }
        }
    }
}
