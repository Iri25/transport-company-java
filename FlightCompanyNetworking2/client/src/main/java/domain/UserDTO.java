package domain;

import java.io.Serializable;
import java.util.Objects;

public class UserDTO implements Serializable {
    private String username;
    private String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }
}
