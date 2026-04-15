package com.codingshuttle.SecurityApp.SecurityApplication.services;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.SignUpDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.UserDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import com.codingshuttle.SecurityApp.SecurityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User with email " + username + " not found"));
    }

    public UserDTO signUp(SignUpDTO signUpDto) {
        //first check if user with email already exists
        Optional<User> user=userRepository.findByEmail(signUpDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email already exists " + signUpDto.getEmail());
        }
        User toBeCreatedUser =modelMapper.map(signUpDto,User.class);
        //encoding password
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        User savedUser=userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser,UserDTO.class);

    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id "
                + userId + " not found"));
    }


}
