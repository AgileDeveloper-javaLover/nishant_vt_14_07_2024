package com.test.auth.jwtservices;

import lombok.Data;

@Data
public class JwtErrorResponse {
    long timestamp;
    int status;
    String error;
}
