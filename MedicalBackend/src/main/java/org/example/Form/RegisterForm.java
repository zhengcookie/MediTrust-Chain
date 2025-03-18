package org.example.Form;

import io.swagger.models.auth.In;
import lombok.Data;

@Data

public class RegisterForm {
    private String username;
    private String gender;
    private Integer age;
    private String accountAddress;
    private String password;
}
