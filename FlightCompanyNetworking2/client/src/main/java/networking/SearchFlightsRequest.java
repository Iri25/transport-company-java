package networking;

import domain.SearchFlightsDTO;

import java.io.Serializable;

public class SearchFlightsRequest implements Request, Serializable {
    String type;
    Object data;

    SearchFlightsRequest(SearchFlightsDTO searchFlights) {

        this.type = "SearchFlightsRequest";
        this.data = searchFlights;
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
