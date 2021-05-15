package domain.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String username;
    private String password;

    public UserDTO(String username, String password){
        this.username = username;
        this.password = password;
    }

    public UserDTO(String username) {
        this(username, "");
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

