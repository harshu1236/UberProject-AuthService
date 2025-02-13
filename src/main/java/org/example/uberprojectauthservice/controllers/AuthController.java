package org.example.uberprojectauthservice.controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.dto.AuthRequestDto;
import org.example.uberprojectauthservice.dto.PassengerResponseDto;
import org.example.uberprojectauthservice.dto.PassengerSignUpRequestDto;
import org.example.uberprojectauthservice.models.Passenger;
import org.example.uberprojectauthservice.repositories.PassengerRepository;
import org.example.uberprojectauthservice.services.AuthService;
import org.example.uberprojectauthservice.services.JWTService;
import org.example.uberprojectauthservice.utils.SuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.example.uberprojectauthservice.utils.ErrorResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController  {


    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PassengerRepository passengerRepository;

    @Value("${cookie.expiry}")
    private int cookieExpiry;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JWTService jwtService, PassengerRepository passengerRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passengerRepository = passengerRepository;
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

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signInPassenger(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(),authRequestDto.getPassword()));
        if(authentication.isAuthenticated()){
            Passenger passenger = passengerRepository.findPassengerByEmail(authRequestDto.getEmail());

            Map<String, Object> payLoad = new HashMap<>();

            payLoad.put("name",passenger.getName());
            payLoad.put("email", passenger.getEmail());
            payLoad.put("phone",passenger.getPhone());

            String jwtToken = jwtService.generateToken(payLoad,passenger.getName());

            ResponseCookie cookie = ResponseCookie.from("jwtToken",jwtToken)
                    .httpOnly(true)   // httpOnly cookie can not be accessed by javaScript in the web Browser.
                    .path("/")       //   This ensures that the cookie is sent with requests to all endpoints under /.
                    .secure(false)  //  false for http request, true for https request
                    .maxAge(cookieExpiry)
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE,cookie.toString());

            return new ResponseEntity<>(jwtToken,HttpStatus.OK);
        }
        return new ResponseEntity<>("Wrong credentials",HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request){
        for(Cookie cookie : request.getCookies()){
            System.out.println(cookie.getName() + " " + cookie.getValue());
        }
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }
}
