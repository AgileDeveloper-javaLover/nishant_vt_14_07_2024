package com.test.auth.controller;


import com.test.auth.aop.Validate;
import com.test.auth.enums.LogicEnum;
import com.test.auth.enums.PermissionsEnum;
import com.test.auth.exceptions.ResponseVO;
import com.test.auth.modal.Users;
import com.test.auth.payload.UpdateUserRequest;
import com.test.auth.payload.UserLoginRequest;
import com.test.auth.payload.UserLoginResponse;
import com.test.auth.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class ApiController {


    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    @Validate(permissions = {PermissionsEnum.AllowRead,PermissionsEnum.AllowWrite,PermissionsEnum.AllowUpdate},type = LogicEnum.Any)
    public ResponseEntity<ResponseVO<List<Users>>> getAllUsers(HttpServletRequest request) {
        ResponseVO<List<Users>> responseVO = new ResponseVO<>();
        responseVO.setError(false);
        responseVO.setMessage("Success");
        responseVO.setResponse(userService.getAllUsers());
        return ResponseEntity.ok(responseVO);
    }



    @PutMapping("/updateUserById/{id}")
    @Validate(permissions = {PermissionsEnum.AllowUpdate},type = LogicEnum.All)
    public ResponseEntity<ResponseVO<Users>> updateUserById(HttpServletRequest request, @PathVariable("id")Long userId, @RequestBody UpdateUserRequest updateUserRequest) {
        ResponseVO<Users> responseVO = new ResponseVO<>();
        responseVO.setError(false);
        responseVO.setMessage("Success");
        responseVO.setResponse(userService.updateUserById(userId,updateUserRequest));
        return ResponseEntity.ok(responseVO);
    }


    @PostMapping("/generateJwtToken")
    public ResponseEntity<ResponseVO<UserLoginResponse>> generateJwtToken(HttpServletRequest request, @RequestBody UserLoginRequest userLogin) {
        ResponseVO<UserLoginResponse> responseVO = new ResponseVO<>();
        responseVO.setError(false);
        responseVO.setMessage("Success");
        responseVO.setResponse(userService.generateJwtToken(userLogin));
        return ResponseEntity.ok(responseVO);
    }

}
