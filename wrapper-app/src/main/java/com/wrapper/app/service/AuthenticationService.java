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

    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, TokenUtils tokenUtils,
                                 UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userService = userService;
    }

    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        return getAuthentication(user);
    }

    public UserTokenState refreshToken(String token) {
        String username = tokenUtils.getUsernameFromToken(token.split(" ")[1]);
        User user = (User) userService.loadUserByUsername(username);
        return getAuthentication(user);
    }

    public UserTokenState getAuthentication(User user) {
        return new UserTokenState(getToken(user), getRefreshToken(user));
    }

    public String getToken(User user) {
        return tokenUtils.generateToken(user.getUsername(), false);
    }

    private String getRefreshToken(User user) {
        return tokenUtils.generateToken(user.getUsername(), true);
    }
}
