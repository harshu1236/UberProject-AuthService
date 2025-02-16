package org.example.uberprojectauthservice.repositories;

import org.example.uberprojectentityservice.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    Passenger findPassengerByEmail(String username);
}
