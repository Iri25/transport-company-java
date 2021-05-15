package networking;


import java.io.Serializable;

public class LogoutRequest implements Serializable, Request {
    String type;
    Object data;

    public LogoutRequest() {
        this.type = "logout";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getData() {
        return null;
    }
}
