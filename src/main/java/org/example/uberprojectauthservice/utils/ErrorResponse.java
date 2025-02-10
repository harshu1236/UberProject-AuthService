package org.example.uberprojectauthservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private int statusCode;
    private String message;
    private String details;
    private LocalDateTime timestamp;
}
