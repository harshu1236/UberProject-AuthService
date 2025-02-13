package org.example.uberprojectauthservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.services.JWTService;
import org.example.uberprojectauthservice.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private final JWTService jwtService;

    public JwtAuthFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("jwtToken")) {
                    token = cookie.getValue();
                }
            }
        }

        if(token == null){
            // User not has provided any JWT Token ,then request shold not go forward.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = (String) jwtService.extractPayLoad(token,"email");

        if(email != null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if(jwtService.validateToken(token,email)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null);  // Creates an authentication token containing the user's details.
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // Associates the authentication token with additional request details (IP, session ID).
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);  // Stores the authenticated user in the SecurityContext, allowing Spring Security to recognize the user for the rest of the request.
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/api/v1/auth/validate");
    }

}



/*
* 1) -> Extracts JWT token from cookies.
  2) -> If no token is found, return 401 Unauthorized.
  3) -> Extracts email from JWT token.
  4) -> Loads user details from the database.
  5) -> Validates JWT token.
  6) -> If valid, authenticate the user and store authentication in SecurityContextHolder.
  Passes the request to the next filter.
 *
 * */
