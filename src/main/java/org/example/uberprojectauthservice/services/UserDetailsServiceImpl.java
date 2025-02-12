package org.example.uberprojectauthservice.services;

import org.example.uberprojectauthservice.helpers.AuthPassengerDetails;
import org.example.uberprojectauthservice.models.Passenger;
import org.example.uberprojectauthservice.repositories.PassengerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/*
* This class is responsible for loading the user in the form of UserDetails object for auth.
* */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PassengerRepository passengerRepository;

    public UserDetailsServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Passenger passenger = passengerRepository.findPassengerByEmail(username);
        if(passenger == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return new AuthPassengerDetails(passenger);
    }
}
