package com.codingshuttle.SecurityApp.SecurityApplication.controllers;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.SignUpDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.UserDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.services.AuthService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDto){
        UserDTO userDto=userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDto, HttpServletRequest request,
                                        HttpServletResponse response){
        String token=authService.login(loginDto);

        // we can pass the token in the cookies as well
        Cookie cookie = new Cookie("token",token);
        cookie.setHttpOnly(true);// Sets the flag that controls if this cookie will be hidden from scripts on the client
        // side mean no javascript can access this token
        //cookie.setSecure(true); //Indicates to the browser whether the cookie should only be sent using a secure protocol,
        // such as HTTPS or SSL and if true, sends the cookie from the browser to the server only when using a secure protocol;
        // if false, sent on any protocol
        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }
}
