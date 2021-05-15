package networking;

import domain.Ticket;

import java.io.Serializable;

public class BuyTicketRequest implements Request, Serializable {
    String type;
    Object data;

    BuyTicketRequest(Ticket ticket) {

        this.type = "BuyTicketRequest";
        this.data = ticket;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getData() {
        return data;
    }
}
