package com.test.auth.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandling extends ResponseEntityExceptionHandler {


    public static ResponseVO<String> buildErrorResponse(Throwable ex) {
        log.error("buildErrorResponse", ex);
        String message = "Error occurred. Contact support";
        List<ErrorVO> detailErrors = Collections.singletonList(new ErrorVO(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Internal Server Error",
                ex.getMessage()));
        ResponseVO<String> ret = new ResponseVO<>();
        ret.setError(true);
        ret.setErrors(detailErrors);
        ret.setMessage(message);

        return ret;

    }


    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseVO<String>> handleCustomException(SecurityException ex){
        return ResponseEntity.badRequest().body(buildErrorResponse(ex));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseVO<String>> handleAllException(Exception ex){
        return ResponseEntity.badRequest().body(buildErrorResponse(ex));
    }
}
