package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserModel {
    private final Integer id;
    private final String username;
    private final String password;
    private final RoleEnum role;

    public UserModel( String username, String password, RoleEnum role) {
        this.id = -1;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
