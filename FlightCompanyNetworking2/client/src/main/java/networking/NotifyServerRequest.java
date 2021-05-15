package networking;

import java.io.Serializable;

public class NotifyServerRequest implements Serializable, Request {
    String type;
    Object data;

    NotifyServerRequest() {
        this.type = "notifyServer";
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
