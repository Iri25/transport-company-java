package controller;

import domain.Flight;
import domain.SearchFlightsDTO;
import domain.Ticket;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import networking.Proxy;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuController {

    private Proxy proxy;
    User user;
    public void setService(Proxy proxy, User user) throws IOException, InterruptedException {
        this.proxy = proxy;
        this.user = user;

        initDestinationModel();
    }

    ObservableList<Flight> modelFlight = FXCollections.observableArrayList();
    ObservableList<String> modelFrom = FXCollections.observableArrayList();

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

    @FXML
    public void initialize() {
        initializeDestinationComboBox();
    }

    private void initializeDestinationComboBox() {
        destinationComboBox.setItems(modelFrom);
    }

    private void initDestinationModel() throws IOException, InterruptedException{
        List<String> destination = new ArrayList<String>();
        for (Flight flight : proxy.getAllFlights()) {
            destination.add(flight.getDestination());
        }
        modelFrom.setAll(destination);
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

    private void initFlightModel(String destination, String date) throws IOException, InterruptedException{
        if (date.isEmpty() || destination.isEmpty())
            MessageAlert.showErrorMessage(null, "Empty field!\n");
        else {
            SearchFlightsDTO searchFlightsDTO = new SearchFlightsDTO(destination, date);
            List<Flight> flights = proxy.searchFlights(searchFlightsDTO);
            List<Flight> flightsUpdate = new ArrayList<>();
            for (Flight flight: flights) {
                if(flight.getNumberOfSeats() == flight.getNumberOfSeatsAvailable())
                    flight.setNumberOfSeatsAvailable(proxy.getSeatsAvailable(flight));
                else
                    flight.setNumberOfSeatsAvailable(proxy.getSeatsAvailableUpdate(flight));
                flightsUpdate.add(flight);
            }
            modelFlight.setAll(flightsUpdate);
        }
    }

    @FXML
    public void handleSearch(ActionEvent actionEvent) throws IOException, InterruptedException {
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
    public void handleBuy(ActionEvent actionEvent) throws IOException, InterruptedException {
        String clientName = clientNameTextField.getText();
        String touristsName = touristsNameTextField.getText();
        String clientAddress = clientAddressTextField.getText();
        String number = numberOfSeatsTextField.getText();
        Integer numberOfSeats = Integer.valueOf(number);


        int idFlight = handleSelect();
        Ticket ticket = new Ticket(clientName, touristsName, clientAddress, numberOfSeats, idFlight);
        proxy.buyTicket(ticket);

        LocalDate localDate= dayDatePicker.getValue();
        String date = new String(String.valueOf(localDate));
        String destination = destinationComboBox.getValue();

        initFlightModel(destination, date);
        initializeFlightTable();
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) throws IOException, InterruptedException {
        System.out.println("aici");
        Stage stage = new Stage();
        stage.setTitle("Login");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LogoutView.fxml"));

        AnchorPane layout = loader.load();
        stage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setProxy(proxy);

        stage.show();
    }
}