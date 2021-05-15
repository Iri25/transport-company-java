package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import domain.User;
import service.IServices;

import java.io.IOException;
import java.util.List;


public class LoginController {

    private User user;
    private IServices server;
    private MenuController menuController;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    public void setServer(IServices server) {
        this.server = server;

    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) throws Exception, IOException {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        user = new User(username, password);

        if ((username.isEmpty()) || password.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty filed!\n");
//        else {
//            List<User> login = server.login(user, menuController);
//            if (login != null)
//                MessageAlert.showErrorMessage(null, "Username or Password invalid!\n");
//            else {
                Stage stage = new Stage();
                stage.setTitle(user.getUsername());

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/MenuView.fxml"));
                AnchorPane layout = loader.load();
                stage.setScene(new Scene(layout));

                menuController = loader.getController();
                menuController.setServer(server);
                menuController.setUser(user);
                stage.show();
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
//           }
//        }
    }
}
