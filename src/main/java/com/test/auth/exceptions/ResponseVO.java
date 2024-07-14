package com.test.auth.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVO<T> implements Serializable {
    private boolean isError;

    private String message;

    private List<ErrorVO> errors;

    private T response;

}
