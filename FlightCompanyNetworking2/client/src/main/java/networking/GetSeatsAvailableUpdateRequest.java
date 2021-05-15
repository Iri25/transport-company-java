package networking;

import domain.Flight;

import java.io.Serializable;

public class GetSeatsAvailableUpdateRequest implements Request, Serializable {
    String type;
    Object data;

    GetSeatsAvailableUpdateRequest(Flight flight) {

        this.type = "GetSeatsAvailableUpdateRequest";
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
