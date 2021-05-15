package domain.validators;

import domain.Ticket;

public class TicketValidator implements Validator<Ticket>{

    /**
     * validate the ticket
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Ticket entity) throws ValidationException {
        String clientName = entity.getClientName();
        String touristsName = entity.getTouristsName();
        String clientAddress = entity.getClientAddress();
        int numberOfSeats = entity.getNumberOfSeats();
        Integer id = entity.getId();

        String errors = "";

        if (clientName.equals(""))
            errors += "Client name not valid! ";
        if (clientName.length() < 4)
            errors += "Client name  should have at least four characters! ";
        char[] charsClientName = clientName.toCharArray();
        for(char characterClientName : charsClientName){
            if(Character.isDigit(characterClientName)){
                errors += "Client name must not contain digits! ";
                break;
            }
        }

        if (touristsName.equals(""))
            errors += "Tourists name  not valid! ";
        if (touristsName.length() < 4)
            errors += "Tourists name have at least four characters! ";
        char[] charsTouristsName = touristsName.toCharArray();
        for(char characterTouristsName : charsTouristsName){
            if(Character.isDigit(characterTouristsName)){
                errors += "Tourists name must not contain digits! ";
                break;
            }
        }

        if (clientAddress.equals(""))
            errors += "Client address  not valid! ";

        if(numberOfSeats < 0)
            errors += "Number of seats not valid! ";

        if(id < 0)
            errors += "Id not valid! ";

        if(errors.length() > 0)
            throw new ValidationException(errors);

    }
}
