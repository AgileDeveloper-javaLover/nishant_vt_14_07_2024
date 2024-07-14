package com.test.auth.payload;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String email;
    private String name;
    private String jwtToken;
}
