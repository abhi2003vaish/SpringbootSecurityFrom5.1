package com.codingshuttle.SecurityApp.SecurityApplication;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.AuthService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthService authService;

	@Test
	void contextLoads() {

//		User user=new User(4L,"abhi@gmail.com","123456");
//		String token=jwtService.generateAccessToken(user);
//		System.out.println(token);
//		Long id=jwtService.getUserIdFromToken(token);
//		System.out.println(id);
	}

	@Test
	void test(){
		System.out.println(authService.login(new LoginDTO("abhivaish200@gmail.com","abhis%^hek1@#$23")));
	}

}
