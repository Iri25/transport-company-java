package client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import domain.Flight;
import domain.Ticket;
import domain.User;
import service.IObserver;
import service.IServices;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable, IObserver {

    @FXML
    private DatePicker dayDatePicker;

    @FXML
    private ComboBox<String> destinationComboBox;

    @FXML
    private TableView<Flight> flightTableView;
    @FXML
    private TableColumn<Flight, String> id;
    @FXML
    private TableColumn<Flight, String> destination;
    @FXML
    private TableColumn<Flight, String> departureDate;
    @FXML
    private TableColumn<Flight, String> departureTime;
    @FXML
    private TableColumn<Flight, String> airport;
    @FXML
    private TableColumn<Flight, String> numberOfSeats;
    @FXML
    private TableColumn<Flight, String> numberOfSeatsAvailable;

    @FXML
    private Label clientNameLabel;

    @FXML
    private TextField clientNameTextField;

    @FXML
    private Label touristsNameLabel;

    @FXML
    private TextField touristsNameTextField;

    @FXML
    private Label clientAddressLabel;

    @FXML
    private TextField clientAddressTextField;

    @FXML
    private Label numberOfSeatsLabel;

    @FXML
    private TextField numberOfSeatsTextField;

    User user;
    IServices server;
    ObservableList<Flight> modelFlight = FXCollections.observableArrayList();
    ObservableList<String> modelDestination = FXCollections.observableArrayList();


    public void setServer(IServices server) throws Exception {
        this.server = server;

        //initDestinationModel();
        init();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        initializeDestinationComboBox();
    }

    private void initializeDestinationComboBox() {
        destinationComboBox.setItems(modelDestination);
    }

    private void initDestinationModel() throws Exception {
        List<String> destination = new ArrayList<String>();
        for (Flight flight : server.getAllFlights()) {
            destination.add(flight.getDestination());
        }
        modelDestination.setAll(destination);
    }

    private void init(){
        List<String> destination = new ArrayList<String>();
        destination.add("Iasi");
        destination.add("Bucuresti");
        destination.add("Cluj-Napoca");
        destination.add("Suceava");
        modelDestination.setAll(destination);
    }

    private void initializeFlightTable() {
        id.setCellValueFactory(new PropertyValueFactory<Flight, String>("IdString"));
        destination.setCellValueFactory(new PropertyValueFactory<Flight, String>("DestinationString"));
        departureDate.setCellValueFactory(new PropertyValueFactory<Flight, String>("DepartureDateString"));
        departureTime.setCellValueFactory(new PropertyValueFactory<Flight, String>("DepartureTimeString"));
        airport.setCellValueFactory(new PropertyValueFactory<Flight, String>("AirportString"));
        numberOfSeats.setCellValueFactory(new PropertyValueFactory<Flight, String>("NumberOfSeatsString"));
        numberOfSeatsAvailable.setCellValueFactory(new PropertyValueFactory<Flight, String>("NumberOfSeatsAvailableString"));

        flightTableView.setItems(modelFlight);
    }

    private void initFlightModel(String destination, String date) throws Exception {
        if (date.isEmpty() || destination.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty field!\n");
        else {
            List<Flight> flights = server.searchFlights(destination, date);
            List<Flight> flightsUpdate = new ArrayList<>();
            for (Flight flight: flights) {
                if(flight.getNumberOfSeats() == flight.getNumberOfSeatsAvailable())
                    flight.setNumberOfSeatsAvailable(server.getSeatsAvailable(flight));
                else
                    flight.setNumberOfSeatsAvailable(server.getSeatsAvailableUpdate(flight));
                flightsUpdate.add(flight);
            }
            modelFlight.setAll(flightsUpdate);
        }
    }

    @FXML
    public void handleSearch(ActionEvent actionEvent) throws Exception {
        LocalDate localDate= dayDatePicker.getValue();
        String date = new String(String.valueOf(localDate));
        String destination = destinationComboBox.getValue();

        initFlightModel(destination, date);
        initializeFlightTable();
    }

    @FXML
    public int handleSelect() {
        Flight flightSelected = flightTableView.getSelectionModel().getSelectedItem();
        return flightSelected.getId();
    }

    @FXML
    public void handleBuy(ActionEvent actionEvent) throws Exception {
        String clientName = clientNameTextField.getText();
        String touristsName = touristsNameTextField.getText();
        String clientAddress = clientAddressTextField.getText();
        String number = numberOfSeatsTextField.getText();
        Integer numberOfSeats = Integer.valueOf(number);


        int idFlight = handleSelect();
        Ticket ticket = new Ticket(clientName, touristsName, clientAddress, numberOfSeats, idFlight);
        server.buyTicket(ticket);

        LocalDate localDate= dayDatePicker.getValue();
        String date = new String(String.valueOf(localDate));
        String destination = destinationComboBox.getValue();

        initFlightModel(destination, date);
        initializeFlightTable();
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Login");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LogoutView.fxml"));

        AnchorPane layout = loader.load();
        stage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setServer(server);

        stage.show();

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void userLoggedIn(User user) throws Exception {

    }

    @Override
    public void userLoggedOut(User user) {

    }

    @Override
    public void searchFlights(String destination, String departureDate) {

    }

    @Override
    public void buyTicket(Ticket ticket) {

    }
}