package networking;

import java.io.Serializable;

public class GetAllFlightsRequest implements Request, Serializable {
    String type;
    Object data;

    GetAllFlightsRequest() {
        this.type = "GetAllFlightsRequest";
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
