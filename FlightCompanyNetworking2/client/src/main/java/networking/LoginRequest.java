package networking;

import domain.User;

import java.io.Serializable;

public class LoginRequest implements Request, Serializable {
    String type;
    Object data;

    LoginRequest(String username, String password) {
        this.type = "login";
        this.data = new User(username, password);
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
