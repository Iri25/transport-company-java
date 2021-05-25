package service.restservices;

import domain.Flight;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.exceptions.RepositoryException;
import repository.jdbc.rest.FlightJdbcRestRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightJdbcRestRepository flightRepository = new FlightJdbcRestRepository();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findOne(@PathVariable Integer id) {
        Flight flight = flightRepository.findOne(id);
        if (flight == null)
            return new ResponseEntity<String>("Flight not found!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Flight>(flight, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Flight[] findAll() {
        int size = (int) StreamSupport.stream(flightRepository.findAll().spliterator(), false).count();
        Flight[] flights = new Flight[size];
        flights = ((List<Flight>) flightRepository.findAll()).toArray(flights);
        return flights;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Flight save(@RequestBody Flight flight) {
        flightRepository.save(flight);
        return flight;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            flightRepository.delete(id);
            return new ResponseEntity<Flight>(HttpStatus.OK);
        }
        catch(RepositoryException repositoryException){
            return new ResponseEntity<String>(repositoryException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Flight update(@RequestBody Flight flight) {
        flightRepository.update(flight);
        return flight;
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
