package com.wrapper.app.service;

import com.wrapper.app.domain.User;
import com.wrapper.app.dto.JwtAuthenticationRequest;
import com.wrapper.app.dto.UserTokenState;
import com.wrapper.app.security.util.TokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;


    public AuthenticationService(AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }


    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        return getAuthentication(user);
    }

    public UserTokenState getAuthentication(User user) {
        return new UserTokenState(getToken(user));
    }

    public String getToken(User user) {
        return tokenUtils.generateToken(user.getUsername());
    }
}
