package org.example.uberprojectauthservice.services;


import org.example.uberprojectauthservice.dto.PassengerResponseDto;
import org.example.uberprojectauthservice.dto.PassengerSignUpRequestDto;
import org.example.uberprojectauthservice.models.Passenger;
import org.example.uberprojectauthservice.repositories.PassengerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PassengerRepository passengerRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public AuthService(PassengerRepository passengerRepository,PasswordEncoder bCryptPasswordEncoder) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public PassengerResponseDto signUpPassenger(PassengerSignUpRequestDto passengerSignUpRequestDto){
        Passenger passenger = Passenger.builder()
                .email(passengerSignUpRequestDto.getEmail())
                .name(passengerSignUpRequestDto.getName())
                .phone((passengerSignUpRequestDto.getPhone()))
                .password(bCryptPasswordEncoder.encode(passengerSignUpRequestDto.getPassword()))   // Todo : encrypt the password
                .build();

        Passenger newPassenger =  passengerRepository.save(passenger);

        return PassengerResponseDto.fromPassenger(newPassenger);
    }
}
