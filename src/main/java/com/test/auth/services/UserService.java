package com.test.auth.services;

import com.test.auth.exceptions.UserNotFoundException;
import com.test.auth.jwtservices.JwtTokenUtil;
import com.test.auth.modal.Users;
import com.test.auth.payload.UpdateUserRequest;
import com.test.auth.payload.UserLoginRequest;
import com.test.auth.payload.UserLoginResponse;
import com.test.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    public Users updateUserById(Long id, UpdateUserRequest updateUserRequest){

        Users users=userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found"));

        users.setName(updateUserRequest.getName());
        users.setEmail(updateUserRequest.getEmail());
        users.setPassword(updateUserRequest.getPassword());
        userRepository.save(users);

        return users;
    }
    public UserLoginResponse generateJwtToken(UserLoginRequest userLogin) throws UserNotFoundException {

        //Authenticating the user
        Users users=userRepository.findByEmailAndPassword(userLogin.getEmail(),userLogin.getPassword()).orElseThrow(()->new UserNotFoundException("User not found"));

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(users.getEmail());
        UserLoginResponse userLoginResponse=new UserLoginResponse();
        userLoginResponse.setName(users.getName());
        userLoginResponse.setName(users.getEmail());
        userLoginResponse.setJwtToken(jwtTokenUtil.generateJwtToken(userDetails));
        return userLoginResponse;
    }
}
