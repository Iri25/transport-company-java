package networking;

import domain.Flight;

import java.io.Serializable;

public class GetSeatsAvailableRequest implements Request, Serializable {
    String type;
    Object data;

    GetSeatsAvailableRequest(Flight flight) {

        this.type = "GetSeatsAvailableRequest";
        this.data = flight;
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
