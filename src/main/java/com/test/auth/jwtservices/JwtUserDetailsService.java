package com.test.auth.jwtservices;

import com.test.auth.modal.Users;
import com.test.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<Users> user = usersRepository.findByEmail(username);
        if (!user.isPresent()) {
            throw new SecurityException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(),
                new ArrayList<>());
    }

}
