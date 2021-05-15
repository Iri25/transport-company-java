package model.validators;

import model.Flight;

public class FlightValidator implements Validator<Flight>{

    /**
     * validate the flight
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Flight entity) throws ValidationException {
        String destination = entity.getDestination();
        String airport = entity.getAirport();
        int numberOfSeatsAvailable = entity.getNumberOfSeats();
        Integer id = entity.getId();

        String errors = "";

        if (destination.equals(""))
            errors += "Destination not valid! ";
        char[] charsDestination = destination.toCharArray();
        for(char characterDestination : charsDestination){
            if(Character.isDigit(characterDestination)){
                errors += "Destination must not contain digits! ";
                break;
            }
        }

        if (airport.equals(""))
            errors += "Airport not valid! ";
        char[] charsAirport = airport.toCharArray();
        for(char characterAirport : charsAirport){
            if(Character.isDigit(characterAirport)){
                errors += "Airport must not contain digits! ";
                break;
            }
        }

        if(numberOfSeatsAvailable < 0)
            errors += "Number of seats available not valid! ";

        if(id < 0)
            errors += "Id not valid! ";

        if(errors.length() > 0)
            throw new ValidationException(errors);

    }
}
