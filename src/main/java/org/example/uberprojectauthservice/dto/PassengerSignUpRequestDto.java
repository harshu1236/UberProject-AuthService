package org.example.uberprojectauthservice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerSignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
}
