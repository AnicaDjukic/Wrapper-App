package com.wrapper.app.controller;

import com.wrapper.app.dto.JwtAuthenticationRequest;
import com.wrapper.app.dto.UserTokenState;
import com.wrapper.app.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserTokenState createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        return service.login(authenticationRequest);
    }

    @GetMapping("/refresh")
    public ResponseEntity<UserTokenState> refreshToken(@RequestHeader("Authorization") String token) {
        UserTokenState authentication = service.refreshToken(token);
        return ResponseEntity.ok(authentication);
    }
}
