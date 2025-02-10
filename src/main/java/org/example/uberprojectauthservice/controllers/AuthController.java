package org.example.uberprojectauthservice.controllers;


import org.example.uberprojectauthservice.dto.PassengerResponseDto;
import org.example.uberprojectauthservice.dto.PassengerSignUpRequestDto;
import org.example.uberprojectauthservice.models.Passenger;
import org.example.uberprojectauthservice.services.AuthService;
import org.example.uberprojectauthservice.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.example.uberprojectauthservice.utils.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController  {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<?> signUpPassenger(@RequestBody PassengerSignUpRequestDto passengerSignUpRequestDto){
        try{
            PassengerResponseDto response =  authService.signUpPassenger(passengerSignUpRequestDto);
            SuccessResponse successResponse = new SuccessResponse(true,HttpStatus.CREATED.value(), "Successfully created a passenger",response,LocalDateTime.now());
            return new ResponseEntity<>(successResponse,HttpStatus.CREATED);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error in database","Error while creating the review", LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/signin/passenger")
    public ResponseEntity<?> signInPassenger(){
        return new ResponseEntity<>("sign in work",HttpStatus.NOT_IMPLEMENTED);
    }
}
