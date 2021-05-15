package networking;

import java.io.Serializable;

public class Response implements Serializable {
    public String type;
    public Object data;

    Response(String type, Object data) {
        this.type = type;
        this.data = data;
    }
}
