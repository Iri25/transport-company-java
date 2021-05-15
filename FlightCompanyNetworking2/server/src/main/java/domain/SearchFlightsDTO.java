package domain;

public class SearchFlightsDTO {
    
    private String destination;
    private String date;
    
    public SearchFlightsDTO(String destination, String date){
        this.destination = destination;
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }
}
