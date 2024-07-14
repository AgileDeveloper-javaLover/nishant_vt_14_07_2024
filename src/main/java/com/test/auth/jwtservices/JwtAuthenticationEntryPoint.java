package com.test.auth.jwtservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.auth.exceptions.SecurityException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        System.out.println("In JWT Authentication filter ============");
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

        final String requestTokenHeader = request.getHeader("Authorization");

        JwtErrorResponse jwtErrorResponse=new JwtErrorResponse();
        jwtErrorResponse.setTimestamp(new Date().getTime());

        if(requestTokenHeader == null) {
            jwtErrorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            jwtErrorResponse.setError("JWT Token is not present");
        }
        else if(!requestTokenHeader.startsWith("Bearer")) {
            jwtErrorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            jwtErrorResponse.setError("JWT Token is not starting with Bearer ");

        }


        else {
            String jwtToken="";
            try{
                jwtToken = requestTokenHeader.substring(7);

                try {


                    if(!jwtTokenUtil.isValidToken(jwtToken))
                        throw new SecurityException("Not valid token!!");
                        
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    jwtErrorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    jwtErrorResponse.setError( "Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    e.printStackTrace();
                    jwtErrorResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
                    response.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
                    jwtErrorResponse.setError( "JWT Token has been expired");
                }
            }
            catch (SecurityException e){
                e.printStackTrace();
                jwtErrorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                jwtErrorResponse.setError(e.getMessage());
            }
            catch (Exception e){
                e.printStackTrace();
                jwtErrorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                jwtErrorResponse.setError( "Unable to get JWT Token");
            }


        }


        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = Obj.writeValueAsString(jwtErrorResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonStr);
        out.flush();

    }
}
