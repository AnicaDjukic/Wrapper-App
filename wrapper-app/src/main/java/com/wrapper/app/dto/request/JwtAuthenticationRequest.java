package com.wrapper.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JwtAuthenticationRequest {

    private String username;

    private String password;
}
