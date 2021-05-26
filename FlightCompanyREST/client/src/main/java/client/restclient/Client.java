package client.restclient;

import client.restclient.exceptions.ServiceException;
import domain.Flight;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class Client {
    public static final String URL = "http://localhost:8080/flights";
    private final RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        }
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Flight findOne(Integer id) throws Exception {
        return execute(() -> restTemplate.getForObject(URL + '/' + id.toString(), Flight.class));
    }

    public Flight[] findAll() throws Exception {
        return execute(() -> restTemplate.getForObject(URL, Flight[].class));
    }

    public Flight save(Flight flight) throws Exception {
        return execute(() -> restTemplate.postForObject(URL, flight, Flight.class));
    }

    public void delete(Integer id) throws Exception {
        execute(() -> {
            restTemplate.delete(URL + '/' + id.toString());
            return null;
        });
    }

    public Flight update(Flight flight) throws Exception {
        execute(() -> {
            restTemplate.put(URL, flight);
            return null;
        });
        return findOne(flight.getId());
    }
}

