package org.example.uberprojectauthservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.Passenger;


import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponseDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Date createdAt;

    public static PassengerResponseDto fromPassenger(Passenger passenger) {
        PassengerResponseDto passengerResponseDto = PassengerResponseDto.builder()
                .id(passenger.getId())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .password(passenger.getPassword())
                .phone(passenger.getPhone())
                .createdAt(passenger.getCreatedAt())
                .build();
        return passengerResponseDto;
    }
}
